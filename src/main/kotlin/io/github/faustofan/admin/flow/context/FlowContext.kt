package io.github.faustofan.admin.flow.context

/**
 * LiteFlow 通用流程上下文
 *
 * 用于在流程节点之间传递数据
 *
 * 使用方式:
 * 1. 在节点中获取上下文:
 *    val context = this.getContextBean(FlowContext::class.java)
 *
 * 2. 执行流程时传入上下文:
 *    flowExecutor.execute2Resp("chainName", null, FlowContext::class.java)
 */
class FlowContext {

    /**
     * 通用数据存储
     */
    private val dataMap: MutableMap<String, Any?> = mutableMapOf()

    /**
     * 流程执行结果
     */
    var result: Any? = null

    /**
     * 错误信息
     */
    var errorMessage: String? = null

    /**
     * 是否成功
     */
    var success: Boolean = true

    /**
     * 设置数据
     */
    fun setData(key: String, value: Any?) {
        dataMap[key] = value
    }

    /**
     * 获取数据
     */
    @Suppress("UNCHECKED_CAST")
    fun <T> getData(key: String): T? {
        return dataMap[key] as? T
    }

    /**
     * 获取数据，带默认值
     */
    @Suppress("UNCHECKED_CAST")
    fun <T> getData(key: String, defaultValue: T): T {
        return (dataMap[key] as? T) ?: defaultValue
    }

    /**
     * 检查是否包含某个键
     */
    fun hasData(key: String): Boolean {
        return dataMap.containsKey(key)
    }

    /**
     * 移除数据
     */
    fun removeData(key: String) {
        dataMap.remove(key)
    }

    /**
     * 清空所有数据
     */
    fun clear() {
        dataMap.clear()
        result = null
        errorMessage = null
        success = true
    }
}

