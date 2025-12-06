package io.github.faustofan.admin.common.unique

import java.util.*

/**
 * 请求ID生成器，用于生成唯一的请求标识符。
 */
object RequestIdGenerator {
    /**
     * 生成下一个请求ID。
     * 当前实现基于 UUID，去除所有短横线。
     * TODO: 可扩展为更复杂的生成算法。
     *
     * @return 唯一的请求ID字符串
     */
    fun next(): String {
        //TODO: 使用更复杂的算法生成请求ID
        return UUID.randomUUID().toString().replace("-".toRegex(), "")
    }
}
