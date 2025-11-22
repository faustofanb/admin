package io.github.faustofanb.admin.module.audit.aspect;

import java.time.LocalDateTime;
import java.util.Map;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.babyfish.jimmer.DraftConsumer;
import org.springframework.core.NamedThreadLocal;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import io.github.faustofanb.admin.common.util.JsonUtils;
import io.github.faustofanb.admin.module.audit.annotation.Log;
import io.github.faustofanb.admin.module.audit.domain.entity.SysOperLogDraft;
import io.github.faustofanb.admin.module.audit.service.SysOperLogService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class LogAspect {

    private final SysOperLogService sysOperLogService;

    private static final ThreadLocal<Long> TIME_THREADLOCAL = new NamedThreadLocal<>("Cost Time");

    @Before("@annotation(controllerLog)")
    public void doBefore(JoinPoint joinPoint, Log controllerLog) {
        TIME_THREADLOCAL.set(System.currentTimeMillis());
    }

    @AfterReturning(pointcut = "@annotation(controllerLog)", returning = "jsonResult")
    public void doAfterReturning(JoinPoint joinPoint, Log controllerLog, Object jsonResult) {
        handleLog(joinPoint, controllerLog, null, jsonResult);
    }

    @AfterThrowing(value = "@annotation(controllerLog)", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Log controllerLog, Exception e) {
        handleLog(joinPoint, controllerLog, e, null);
    }

    protected void handleLog(final JoinPoint joinPoint, Log controllerLog, final Exception e, Object jsonResult) {
        try {
            // Get request attributes
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
                    .getRequestAttributes();
            HttpServletRequest request = attributes != null ? attributes.getRequest() : null;

            // Create log entity
            DraftConsumer<SysOperLogDraft> producer = draft -> {
                draft.setStatus(0);
                draft.setOperTime(LocalDateTime.now());

                // Calculate cost time
                long costTime = System.currentTimeMillis() - TIME_THREADLOCAL.get();
                draft.setCostTime(costTime);

                if (request != null) {
                    draft.setOperIp(getIpAddr(request));
                    draft.setOperUrl(request.getRequestURI());
                    draft.setRequestMethod(request.getMethod());
                }

                if (e != null) {
                    draft.setStatus(1);
                    draft.setErrorMsg(e.getMessage());
                }

                String className = joinPoint.getTarget().getClass().getName();
                String methodName = joinPoint.getSignature().getName();
                draft.setMethod(className + "." + methodName + "()");

                getControllerMethodDescription(joinPoint, controllerLog, draft, jsonResult, request);
            };

            // Save log asynchronously
            saveLog(producer);

        } catch (Exception exp) {
            log.error("==LogAspect Error==", exp);
        } finally {
            TIME_THREADLOCAL.remove();
        }
    }

    @Async
    protected void saveLog(DraftConsumer<SysOperLogDraft> producer) {
        sysOperLogService.saveLog(SysOperLogDraft.$.produce(producer));
    }

    public void getControllerMethodDescription(JoinPoint joinPoint, Log log, SysOperLogDraft draft,
            Object jsonResult, HttpServletRequest request) {
        draft.setBusinessType(log.businessType().ordinal());
        draft.setTitle(log.title());

        if (log.isSaveRequestData()) {
            setRequestValue(joinPoint, draft, request);
        }

        if (log.isSaveResponseData() && jsonResult != null) {
            draft.setJsonResult(JsonUtils.toJsonString(jsonResult));
        }
    }

    private void setRequestValue(JoinPoint joinPoint, SysOperLogDraft draft, HttpServletRequest request) {
        String requestMethod = draft.requestMethod();
        if ("PUT".equals(requestMethod) || "POST".equals(requestMethod)) {
            String params = argsArrayToString(joinPoint.getArgs());
            draft.setOperParam(params);
        } else {
            if (request != null) {
                Map<?, ?> paramsMap = request.getParameterMap();
                draft.setOperParam(JsonUtils.toJsonString(paramsMap));
            }
        }
    }

    private String argsArrayToString(Object[] paramsArray) {
        StringBuilder params = new StringBuilder();
        if (paramsArray != null && paramsArray.length > 0) {
            for (Object o : paramsArray) {
                if (o != null && !isFilterObject(o)) {
                    try {
                        params.append(JsonUtils.toJsonString(o)).append(" ");
                    } catch (Exception e) {
                        // ignore
                    }
                }
            }
        }
        return params.toString().trim();
    }

    public boolean isFilterObject(final Object o) {
        Class<?> clazz = o.getClass();
        if (clazz.isArray()) {
            return clazz.getComponentType().isAssignableFrom(MultipartFile.class);
        } else if (Map.class.isAssignableFrom(clazz)) {
            Map map = (Map) o;
            for (Object value : map.values()) {
                return value instanceof MultipartFile;
            }
        }
        return o instanceof MultipartFile || o instanceof HttpServletRequest || o instanceof HttpServletResponse;
    }

    public static String getIpAddr(HttpServletRequest request) {
        if (request == null) {
            return "unknown";
        }
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return "0:0:0:0:0:0:0:1".equals(ip) ? "127.0.0.1" : ip;
    }
}
