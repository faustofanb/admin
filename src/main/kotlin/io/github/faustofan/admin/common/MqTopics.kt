package io.github.faustofan.admin.common

/**
 * MqTopics 对象定义了系统中使用的消息队列主题和标签常量，
 * 以及用于生成带标签主题的方法。
 */
object MqTopics {
    /** 用户相关事件主题 */
    const val USER_EVENTS = "USER_EVENTS"
    /** 租户相关事件主题 */
    const val TENANT_EVENTS = "TENANT_EVENTS"
    /** 资源相关事件主题 */
    const val RESOURCE_EVENTS = "RESOURCE_EVENTS"
    /** 权限相关事件主题 */
    const val PERMISSION_EVENTS = "PERMISSION_EVENTS"
    /** Excel相关事件主题 */
    const val EXCEL_EVENTS = "EXCEL_EVENTS"

    /** 标签创建事件 */
    const val TAG_CREATED = "TAG_CREATED"
    /** 标签更新事件 */
    const val TAG_UPDATED = "TAG_UPDATED"
    /** 标签删除事件 */
    const val TAG_DELETED = "TAG_DELETED"

    /**
     * 生成带标签的主题字符串，格式为 "topic:tag"。
     *
     * @param topic 主题名称
     * @param tag 标签名称
     * @return 拼接后的主题字符串
     */
    fun withTag(topic: String, tag: String): String {
        return "$topic:$tag"
    }
}