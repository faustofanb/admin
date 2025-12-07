package io.github.faustofan.admin.cache

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.reflect.MethodSignature
import org.redisson.api.RedissonClient
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.context.expression.MethodBasedEvaluationContext
import org.springframework.core.DefaultParameterNameDiscoverer
import org.springframework.core.annotation.Order
import org.springframework.expression.spel.standard.SpelExpressionParser
import org.springframework.stereotype.Component
import java.time.Duration

/**
 * 缓存切面
 *
 * 处理 @Cached, @CacheEvict, @CacheEvicts, @CachePut 注解
 */
@Aspect
@Component
@Order(1)
@ConditionalOnBean(RedissonClient::class)
class CacheAspect(
    private val cacheService: CacheService
) {
    private val log = LoggerFactory.getLogger(CacheAspect::class.java)
    private val parser = SpelExpressionParser()
    private val parameterNameDiscoverer = DefaultParameterNameDiscoverer()

    /**
     * 处理 @Cached 注解
     */
    @Around("@annotation(cached)")
    fun handleCached(joinPoint: ProceedingJoinPoint, cached: Cached): Any? {
        val cacheKey = parseSpelExpression(cached.key, joinPoint)

        // 检查条件
        if (cached.condition.isNotEmpty()) {
            val conditionResult = evaluateCondition(cached.condition, joinPoint)
            if (!conditionResult) {
                return joinPoint.proceed()
            }
        }

        // 尝试从缓存获取
        val cachedValue = cacheService.get<Any>(cacheKey)
        if (cachedValue != null) {
            log.debug("Cache HIT: key={}", cacheKey)
            // 处理 null 占位符
            if (cachedValue == NULL_PLACEHOLDER) {
                return null
            }
            return cachedValue
        }

        log.debug("Cache MISS: key={}", cacheKey)

        // 执行方法
        val result = joinPoint.proceed()

        // 检查 unless 条件
        if (cached.unless.isNotEmpty()) {
            val unlessResult = evaluateConditionWithResult(cached.unless, joinPoint, result)
            if (unlessResult) {
                return result
            }
        }

        // 计算 TTL
        val ttlDuration = Duration.of(cached.ttl, cached.unit.toChronoUnit())

        // 缓存结果
        if (result != null) {
            cacheService.set(cacheKey, result, ttlDuration)
        } else if (cached.cacheNull) {
            // 缓存 null 值防止穿透
            cacheService.set(cacheKey, NULL_PLACEHOLDER, Duration.ofSeconds(cached.nullTtlSeconds))
        }

        return result
    }

    /**
     * 处理 @CacheEvict 注解
     */
    @Around("@annotation(cacheEvict)")
    fun handleCacheEvict(joinPoint: ProceedingJoinPoint, cacheEvict: CacheEvict): Any? {
        // 方法执行前失效
        if (cacheEvict.beforeInvocation) {
            evictCache(cacheEvict, joinPoint)
        }

        val result = joinPoint.proceed()

        // 方法执行后失效（默认）
        if (!cacheEvict.beforeInvocation) {
            evictCache(cacheEvict, joinPoint)
        }

        return result
    }

    /**
     * 处理 @CacheEvicts 注解
     */
    @Around("@annotation(cacheEvicts)")
    fun handleCacheEvicts(joinPoint: ProceedingJoinPoint, cacheEvicts: CacheEvicts): Any? {
        // 方法执行前失效
        cacheEvicts.value
            .filter { it.beforeInvocation }
            .forEach { evictCache(it, joinPoint) }

        val result = joinPoint.proceed()

        // 方法执行后失效
        cacheEvicts.value
            .filter { !it.beforeInvocation }
            .forEach { evictCache(it, joinPoint) }

        return result
    }

    /**
     * 处理 @CachePut 注解
     */
    @Around("@annotation(cachePut)")
    fun handleCachePut(joinPoint: ProceedingJoinPoint, cachePut: CachePut): Any? {
        val result = joinPoint.proceed()

        // 检查条件
        if (cachePut.condition.isNotEmpty()) {
            val conditionResult = evaluateConditionWithResult(cachePut.condition, joinPoint, result)
            if (!conditionResult) {
                return result
            }
        }

        val cacheKey = parseSpelExpression(cachePut.key, joinPoint)
        val ttlDuration = Duration.of(cachePut.ttl, cachePut.unit.toChronoUnit())

        if (result != null) {
            cacheService.set(cacheKey, result, ttlDuration)
            log.debug("Cache PUT: key={}", cacheKey)
        }

        return result
    }

    /**
     * 失效缓存
     */
    private fun evictCache(cacheEvict: CacheEvict, joinPoint: ProceedingJoinPoint) {
        if (cacheEvict.key.isNotEmpty()) {
            val cacheKey = parseSpelExpression(cacheEvict.key, joinPoint)
            cacheService.delete(cacheKey)
            log.debug("Cache EVICT: key={}", cacheKey)
        }

        if (cacheEvict.pattern.isNotEmpty()) {
            val pattern = parseSpelExpression(cacheEvict.pattern, joinPoint)
            val count = cacheService.deleteByPattern(pattern)
            log.debug("Cache EVICT BY PATTERN: pattern={}, count={}", pattern, count)
        }
    }

    /**
     * 解析 SpEL 表达式
     */
    private fun parseSpelExpression(expression: String, joinPoint: ProceedingJoinPoint): String {
        // 如果不包含 SpEL 标记，直接返回
        if (!expression.contains("#") && !expression.contains("T(")) {
            return expression
        }

        val context = createEvaluationContext(joinPoint)

        return try {
            val parsed = parser.parseExpression(expression).getValue(context, String::class.java)
            parsed ?: expression
        } catch (e: Exception) {
            log.warn("Failed to parse SpEL expression: {}", expression, e)
            expression
        }
    }

    /**
     * 评估条件表达式
     */
    private fun evaluateCondition(expression: String, joinPoint: ProceedingJoinPoint): Boolean {
        val context = createEvaluationContext(joinPoint)

        return try {
            parser.parseExpression(expression).getValue(context, Boolean::class.java) ?: false
        } catch (e: Exception) {
            log.warn("Failed to evaluate condition: {}", expression, e)
            true
        }
    }

    /**
     * 评估带结果的条件表达式
     */
    private fun evaluateConditionWithResult(
        expression: String,
        joinPoint: ProceedingJoinPoint,
        result: Any?
    ): Boolean {
        val context = createEvaluationContext(joinPoint)
        context.setVariable("result", result)

        return try {
            parser.parseExpression(expression).getValue(context, Boolean::class.java) ?: false
        } catch (e: Exception) {
            log.warn("Failed to evaluate condition with result: {}", expression, e)
            false
        }
    }

    /**
     * 创建 SpEL 评估上下文
     */
    private fun createEvaluationContext(joinPoint: ProceedingJoinPoint): MethodBasedEvaluationContext {
        val signature = joinPoint.signature as MethodSignature
        val method = signature.method
        val args = joinPoint.args

        val context = MethodBasedEvaluationContext(
            joinPoint.target,
            method,
            args,
            parameterNameDiscoverer
        )

        // 添加方法参数到上下文
        val paramNames = parameterNameDiscoverer.getParameterNames(method)
        paramNames?.forEachIndexed { index, name ->
            if (index < args.size) {
                context.setVariable(name, args[index])
            }
        }

        return context
    }

    companion object {
        // Null 值占位符，用于缓存 null 防止穿透
        private const val NULL_PLACEHOLDER = "__NULL__"
    }
}

