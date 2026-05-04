package com.carboncredit.app.core.utils;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0007\n\u0002\u0010\u0007\n\u0002\b\u0011\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\fX\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\fX\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0018\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0019\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001a\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001b\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001c\u001a\u00020\fX\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001d"}, d2 = {"Lcom/carboncredit/app/core/utils/Constants;", "", "()V", "ANOMALY_CONNECTIVITY", "", "ANOMALY_FROZEN", "ANOMALY_ZERO", "ANOMALY_ZSCORE", "CHANNEL_ANOMALY", "CHANNEL_CREDIT", "CHANNEL_SYSTEM", "CO2_NORMAL_MAX", "", "CO2_WARNING_MAX", "COMPLIANCE_THRESHOLD", "NOTIF_ANOMALY", "NOTIF_CREDIT", "NOTIF_SYSTEM", "PERIOD_30_DAYS", "PERIOD_7_DAYS", "PERIOD_CUSTOM", "PERIOD_TODAY", "POLYGONSCAN_BASE_URL", "ROLE_AUDITOR", "ROLE_MANAGER", "STATUS_DISPUTED", "STATUS_PENDING", "STATUS_VERIFIED", "WATCH_THRESHOLD", "app_debug"})
public final class Constants {
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String ROLE_MANAGER = "MANAGER";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String ROLE_AUDITOR = "AUDITOR";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String ANOMALY_ZSCORE = "zscore_breach";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String ANOMALY_FROZEN = "frozen_value";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String ANOMALY_ZERO = "zero_reading";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String ANOMALY_CONNECTIVITY = "connectivity_gap";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String STATUS_VERIFIED = "verified";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String STATUS_PENDING = "pending";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String STATUS_DISPUTED = "disputed";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String NOTIF_ANOMALY = "anomaly";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String NOTIF_CREDIT = "credit_issued";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String NOTIF_SYSTEM = "system";
    public static final float CO2_NORMAL_MAX = 600.0F;
    public static final float CO2_WARNING_MAX = 900.0F;
    public static final float COMPLIANCE_THRESHOLD = 0.85F;
    public static final float WATCH_THRESHOLD = 1.0F;
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String CHANNEL_ANOMALY = "anomaly_alerts";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String CHANNEL_CREDIT = "credit_alerts";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String CHANNEL_SYSTEM = "system_alerts";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String POLYGONSCAN_BASE_URL = "https://amoy.polygonscan.com/tx/";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String PERIOD_TODAY = "today";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String PERIOD_7_DAYS = "7days";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String PERIOD_30_DAYS = "30days";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String PERIOD_CUSTOM = "custom";
    @org.jetbrains.annotations.NotNull()
    public static final com.carboncredit.app.core.utils.Constants INSTANCE = null;
    
    private Constants() {
        super();
    }
}