package io.github.faustofan.admin.flow.service

import com.yomahub.liteflow.core.FlowExecutor
import com.yomahub.liteflow.flow.LiteflowResponse
import io.github.faustofan.admin.flow.context.FlowContext
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

/**
 * LiteFlow 流程执行服务
 *
 * 封装 FlowExecutor，提供便捷的流程调用方法
 */
@Service
class FlowService(
    private val flowExecutor: FlowExecutor
) {

    private val log = LoggerFactory.getLogger(FlowService::class.java)

    /**
     * 执行流程链
     *
     * @param chainId 流程链ID
     * @param requestData 请求数据（可选）
     * @return LiteflowResponse 流程执行响应
     */
    fun execute(chainId: String, requestData: Any? = null): LiteflowResponse {
        log.info("开始执行流程: chainId={}", chainId)
        val response = flowExecutor.execute2Resp(chainId, requestData)
        log.info("流程执行完成: chainId={}, success={}, message={}",
            chainId, response.isSuccess, response.message)
        return response
    }

    /**
     * 执行流程链（带上下文）
     *
     * @param chainId 流程链ID
     * @param requestData 请求数据（可选）
     * @return FlowContext 流程上下文
     */
    fun executeWithContext(chainId: String, requestData: Any? = null): FlowContext {
        log.info("开始执行流程(带上下文): chainId={}", chainId)
        val response = flowExecutor.execute2Resp(chainId, requestData, FlowContext::class.java)

        val context = response.getContextBean(FlowContext::class.java)
        if (!response.isSuccess) {
            context.success = false
            context.errorMessage = response.message ?: response.cause?.message
            log.error("流程执行失败: chainId={}, error={}", chainId, context.errorMessage)
        } else {
            log.info("流程执行成功: chainId={}", chainId)
        }

        return context
    }

    /**
     * 执行流程链（自定义上下文类型）
     *
     * @param chainId 流程链ID
     * @param requestData 请求数据（可选）
     * @param contextClass 上下文类型
     * @return T 流程上下文
     */
    fun <T : Any> executeWithContext(
        chainId: String,
        requestData: Any? = null,
        contextClass: Class<T>
    ): T {
        log.info("开始执行流程(自定义上下文): chainId={}, contextClass={}", chainId, contextClass.simpleName)
        val response = flowExecutor.execute2Resp(chainId, requestData, contextClass)

        if (!response.isSuccess) {
            log.error("流程执行失败: chainId={}, error={}", chainId, response.message)
            throw RuntimeException("流程执行失败: ${response.message}", response.cause)
        }

        log.info("流程执行成功: chainId={}", chainId)
        return response.getContextBean(contextClass)
    }

    /**
     * 异步执行流程链
     *
     * @param chainId 流程链ID
     * @param requestData 请求数据（可选）
     */
    fun executeAsync(chainId: String, requestData: Any? = null) {
        log.info("异步执行流程: chainId={}", chainId)
        flowExecutor.execute2Future(chainId, requestData, FlowContext::class.java)
    }

    /**
     * 重新加载规则
     */
    fun reloadRule() {
        log.info("重新加载流程规则")
        flowExecutor.reloadRule()
        log.info("流程规则重新加载完成")
    }
}

