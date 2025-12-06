package io.github.faustofan.admin.common.util

import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

/**
 * 时间工具类，提供常用的时间相关操作方法。
 */
object DateTimeUtils {

    /**
     * 获取当前 UTC 时间的 [java.time.Instant] 实例。
     *
     * @return 当前 UTC 时间
     */
    fun nowUtc(): Instant = Instant.now()

    /**
     * 获取指定时区的当前时间。
     *
     * @param zoneId 时区，默认为系统默认时区
     * @return 指定时区的当前时间 [java.time.ZonedDateTime]
     */
    fun nowAtZone(zoneId: ZoneId = ZoneId.systemDefault()): ZonedDateTime = ZonedDateTime.now(zoneId)

    /**
     * 将 [Instant] 格式化为 ISO 8601 字符串（UTC）。
     *
     * @param instant 要格式化的时间，默认为当前 UTC 时间
     * @return ISO 8601 格式的字符串
     */
    fun formatIso(instant: Instant = nowUtc()): String =
        ZonedDateTime.ofInstant(instant, ZoneId.of("UTC")).format(DateTimeFormatter.ISO_INSTANT)

    /**
     * 解析 ISO 8601 格式的字符串为 [Instant]。
     *
     * @param isoString ISO 8601 格式的时间字符串
     * @return 解析后的 [Instant]
     */
    fun parseIso(isoString: String): Instant =
        Instant.parse(isoString)

    /**
     * 将 [Instant] 转换为指定时区的 [ZonedDateTime]。
     *
     * @param instant 要转换的时间
     * @param zoneId 目标时区
     * @return 指定时区的 [ZonedDateTime]
     */
    fun toUserZone(instant: Instant, zoneId: ZoneId): ZonedDateTime =
        ZonedDateTime.ofInstant(instant, zoneId)
}