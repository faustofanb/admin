package io.github.faustofan.admin.common.context;

/**
 * 应用上下文信息，包含租户ID、用户ID、请求ID和链路追踪ID。
 *
 * @param tenantId  租户ID
 * @param userId    用户ID
 * @param requestId 请求ID
 * @param traceId   链路追踪ID
 */
public record AppContext(
        String tenantId,
        String userId,
        String requestId,
        String traceId
) {
}
