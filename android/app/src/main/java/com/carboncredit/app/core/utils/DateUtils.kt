package com.carboncredit.app.core.utils

import java.time.Duration
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

object DateUtils {

    private val isoFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME
    private val displayFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm:ss")
    private val shortDateFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy")
    private val dateOnlyFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    fun formatDisplay(isoTimestamp: String): String {
        return try {
            val zdt = ZonedDateTime.parse(isoTimestamp, isoFormatter)
            zdt.withZoneSameInstant(ZoneId.systemDefault()).format(displayFormatter)
        } catch (e: Exception) {
            isoTimestamp
        }
    }

    fun formatShortDate(isoTimestamp: String): String {
        return try {
            val zdt = ZonedDateTime.parse(isoTimestamp, isoFormatter)
            zdt.withZoneSameInstant(ZoneId.systemDefault()).format(shortDateFormatter)
        } catch (e: Exception) {
            isoTimestamp
        }
    }

    fun formatRelativeTime(isoTimestamp: String): String {
        return try {
            val instant = ZonedDateTime.parse(isoTimestamp, isoFormatter).toInstant()
            val duration = Duration.between(instant, Instant.now())
            when {
                duration.toMinutes() < 1 -> "Just now"
                duration.toMinutes() < 60 -> "${duration.toMinutes()} min ago"
                duration.toHours() < 24 -> "${duration.toHours()} hours ago"
                duration.toDays() < 7 -> "${duration.toDays()} days ago"
                else -> formatShortDate(isoTimestamp)
            }
        } catch (e: Exception) {
            isoTimestamp
        }
    }

    fun getStartOfToday(): String {
        return LocalDate.now().atStartOfDay(ZoneId.systemDefault())
            .format(isoFormatter)
    }

    fun getDaysAgo(days: Long): String {
        return LocalDate.now().minusDays(days).atStartOfDay(ZoneId.systemDefault())
            .format(isoFormatter)
    }

    fun formatPeriodRange(start: String, end: String): String {
        val s = formatShortDate(start)
        val e = formatShortDate(end)
        return "$s – $e"
    }

    fun parseToLocalDateTime(isoTimestamp: String): LocalDateTime {
        return try {
            ZonedDateTime.parse(isoTimestamp, isoFormatter)
                .withZoneSameInstant(ZoneId.systemDefault())
                .toLocalDateTime()
        } catch (e: Exception) {
            LocalDateTime.now()
        }
    }

    fun parseToEpochMillis(isoTimestamp: String): Long {
        return try {
            ZonedDateTime.parse(isoTimestamp, isoFormatter).toInstant().toEpochMilli()
        } catch (e: Exception) {
            System.currentTimeMillis()
        }
    }
}
