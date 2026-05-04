package com.carboncredit.app.core.utils;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000e\n\u0002\b\b\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\nJ\u0016\u0010\f\u001a\u00020\n2\u0006\u0010\r\u001a\u00020\n2\u0006\u0010\u000e\u001a\u00020\nJ\u000e\u0010\u000f\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\nJ\u000e\u0010\u0010\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\nJ\u000e\u0010\u0011\u001a\u00020\n2\u0006\u0010\u0012\u001a\u00020\u0013J\u0006\u0010\u0014\u001a\u00020\nJ\u000e\u0010\u0015\u001a\u00020\u00132\u0006\u0010\u000b\u001a\u00020\nJ\u000e\u0010\u0016\u001a\u00020\u00172\u0006\u0010\u000b\u001a\u00020\nR\u0016\u0010\u0003\u001a\n \u0005*\u0004\u0018\u00010\u00040\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0016\u0010\u0006\u001a\n \u0005*\u0004\u0018\u00010\u00040\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0016\u0010\u0007\u001a\n \u0005*\u0004\u0018\u00010\u00040\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0016\u0010\b\u001a\n \u0005*\u0004\u0018\u00010\u00040\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0018"}, d2 = {"Lcom/carboncredit/app/core/utils/DateUtils;", "", "()V", "dateOnlyFormatter", "Ljava/time/format/DateTimeFormatter;", "kotlin.jvm.PlatformType", "displayFormatter", "isoFormatter", "shortDateFormatter", "formatDisplay", "", "isoTimestamp", "formatPeriodRange", "start", "end", "formatRelativeTime", "formatShortDate", "getDaysAgo", "days", "", "getStartOfToday", "parseToEpochMillis", "parseToLocalDateTime", "Ljava/time/LocalDateTime;", "app_debug"})
public final class DateUtils {
    private static final java.time.format.DateTimeFormatter isoFormatter = null;
    private static final java.time.format.DateTimeFormatter displayFormatter = null;
    private static final java.time.format.DateTimeFormatter shortDateFormatter = null;
    private static final java.time.format.DateTimeFormatter dateOnlyFormatter = null;
    @org.jetbrains.annotations.NotNull()
    public static final com.carboncredit.app.core.utils.DateUtils INSTANCE = null;
    
    private DateUtils() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String formatDisplay(@org.jetbrains.annotations.NotNull()
    java.lang.String isoTimestamp) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String formatShortDate(@org.jetbrains.annotations.NotNull()
    java.lang.String isoTimestamp) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String formatRelativeTime(@org.jetbrains.annotations.NotNull()
    java.lang.String isoTimestamp) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getStartOfToday() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getDaysAgo(long days) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String formatPeriodRange(@org.jetbrains.annotations.NotNull()
    java.lang.String start, @org.jetbrains.annotations.NotNull()
    java.lang.String end) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.time.LocalDateTime parseToLocalDateTime(@org.jetbrains.annotations.NotNull()
    java.lang.String isoTimestamp) {
        return null;
    }
    
    public final long parseToEpochMillis(@org.jetbrains.annotations.NotNull()
    java.lang.String isoTimestamp) {
        return 0L;
    }
}