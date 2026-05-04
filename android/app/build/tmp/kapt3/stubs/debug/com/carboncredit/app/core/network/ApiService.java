package com.carboncredit.app.core.network;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * FastAPI endpoint interfaces.
 * Only operations requiring backend business logic go here.
 * Most data reads go through Supabase SDK directly.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\bf\u0018\u00002\u00020\u0001J\u0018\u0010\u0002\u001a\u00020\u00032\b\b\u0001\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u0018\u0010\u0007\u001a\u00020\b2\b\b\u0001\u0010\t\u001a\u00020\nH\u00a7@\u00a2\u0006\u0002\u0010\u000bJ\u0018\u0010\f\u001a\u00020\r2\b\b\u0001\u0010\u000e\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006\u00a8\u0006\u000f"}, d2 = {"Lcom/carboncredit/app/core/network/ApiService;", "", "fetchIPFSReport", "Lcom/carboncredit/app/core/network/IPFSReportResponse;", "cid", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "registerFCMToken", "Lcom/carboncredit/app/core/network/FCMTokenResponse;", "body", "Lcom/carboncredit/app/core/network/FCMTokenRequest;", "(Lcom/carboncredit/app/core/network/FCMTokenRequest;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "triggerRecalculation", "Lcom/carboncredit/app/core/network/RecalculationResponse;", "facilityId", "app_debug"})
public abstract interface ApiService {
    
    /**
     * Register or update FCM token for push notifications
     */
    @retrofit2.http.POST(value = "auth/fcm-token")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object registerFCMToken(@retrofit2.http.Body()
    @org.jetbrains.annotations.NotNull()
    com.carboncredit.app.core.network.FCMTokenRequest body, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.carboncredit.app.core.network.FCMTokenResponse> $completion);
    
    /**
     * Fetch IPFS report content (FastAPI proxies to avoid CORS/rate limits)
     */
    @retrofit2.http.GET(value = "ipfs/report/{cid}")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object fetchIPFSReport(@retrofit2.http.Path(value = "cid")
    @org.jetbrains.annotations.NotNull()
    java.lang.String cid, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.carboncredit.app.core.network.IPFSReportResponse> $completion);
    
    /**
     * Trigger manual credit recalculation for a facility
     */
    @retrofit2.http.POST(value = "credits/recalculate/{facility_id}")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object triggerRecalculation(@retrofit2.http.Path(value = "facility_id")
    @org.jetbrains.annotations.NotNull()
    java.lang.String facilityId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.carboncredit.app.core.network.RecalculationResponse> $completion);
}