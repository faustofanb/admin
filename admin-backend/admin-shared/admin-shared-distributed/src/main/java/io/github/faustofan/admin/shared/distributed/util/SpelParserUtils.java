package io.github.faustofan.admin.shared.distributed.util;

import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.util.StringUtils;

/**
 * SpEL 表达式解析工具
 */
public class SpelParserUtils {

    private static final ExpressionParser parser = new SpelExpressionParser();
    private static final DefaultParameterNameDiscoverer nameDiscoverer = new DefaultParameterNameDiscoverer();

    public static String parseKey(String keyExpression, ProceedingJoinPoint joinPoint) {
        if (!StringUtils.hasText(keyExpression) || !keyExpression.contains("#")) {
            return keyExpression;
        }

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Object[] args = joinPoint.getArgs();
        String[] paramNames = nameDiscoverer.getParameterNames(method);

        EvaluationContext context = new StandardEvaluationContext();
        if (paramNames != null) {
            for (int i = 0; i < paramNames.length; i++) {
                context.setVariable(paramNames[i], args[i]);
            }
        }

        try {
            Object value = parser.parseExpression(keyExpression).getValue(context);
            return value != null ? value.toString() : "NULL";
        } catch (Exception e) {
            throw new IllegalArgumentException("SpEL expression parsing failed: " + keyExpression, e);
        }
    }
}