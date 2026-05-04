package com.carboncredit.app.data.repository;

import com.carboncredit.app.data.models.Sensor;
import com.carboncredit.app.data.models.SensorReading;
import io.github.jan.supabase.SupabaseClient;
import io.github.jan.supabase.postgrest.query.Columns;
import io.github.jan.supabase.postgrest.query.Order;
import io.github.jan.supabase.realtime.PostgresAction;
import kotlinx.coroutines.flow.Flow;
import javax.inject.Inject;
import javax.inject.Singleton;

@javax.inject.Singleton()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000F\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0010 \n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0007\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0018\u0010\u0005\u001a\u0004\u0018\u00010\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0086@\u00a2\u0006\u0002\u0010\tJ\u0018\u0010\n\u001a\u0004\u0018\u00010\u00062\u0006\u0010\u000b\u001a\u00020\bH\u0086@\u00a2\u0006\u0002\u0010\tJ>\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u00060\r2\u0006\u0010\u0007\u001a\u00020\b2\n\b\u0002\u0010\u000e\u001a\u0004\u0018\u00010\b2\n\b\u0002\u0010\u000f\u001a\u0004\u0018\u00010\b2\b\b\u0002\u0010\u0010\u001a\u00020\u0011H\u0086@\u00a2\u0006\u0002\u0010\u0012J>\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00060\r2\u0006\u0010\u000b\u001a\u00020\b2\n\b\u0002\u0010\u000e\u001a\u0004\u0018\u00010\b2\n\b\u0002\u0010\u000f\u001a\u0004\u0018\u00010\b2\b\b\u0002\u0010\u0010\u001a\u00020\u0011H\u0086@\u00a2\u0006\u0002\u0010\u0012J\u0016\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u000b\u001a\u00020\bH\u0086@\u00a2\u0006\u0002\u0010\tJ\u001c\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00150\r2\u0006\u0010\u0007\u001a\u00020\bH\u0086@\u00a2\u0006\u0002\u0010\tJ\u0016\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0007\u001a\u00020\bH\u0086@\u00a2\u0006\u0002\u0010\tJ\u0014\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u00060\u001a2\u0006\u0010\u0007\u001a\u00020\bJ\u0016\u0010\u001b\u001a\u00020\u00182\u0006\u0010\u0007\u001a\u00020\bH\u0086@\u00a2\u0006\u0002\u0010\tR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001c"}, d2 = {"Lcom/carboncredit/app/data/repository/SensorRepository;", "", "supabase", "Lio/github/jan/supabase/SupabaseClient;", "(Lio/github/jan/supabase/SupabaseClient;)V", "getLatestReading", "Lcom/carboncredit/app/data/models/SensorReading;", "facilityId", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getLatestReadingForSensor", "sensorId", "getReadingsForFacility", "", "startTime", "endTime", "limit", "", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;ILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getReadingsForSensor", "getSensorById", "Lcom/carboncredit/app/data/models/Sensor;", "getSensorsForFacility", "subscribeChannel", "", "subscribeToReadings", "Lkotlinx/coroutines/flow/Flow;", "unsubscribeChannel", "app_debug"})
public final class SensorRepository {
    @org.jetbrains.annotations.NotNull()
    private final io.github.jan.supabase.SupabaseClient supabase = null;
    
    @javax.inject.Inject()
    public SensorRepository(@org.jetbrains.annotations.NotNull()
    io.github.jan.supabase.SupabaseClient supabase) {
        super();
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getSensorsForFacility(@org.jetbrains.annotations.NotNull()
    java.lang.String facilityId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.carboncredit.app.data.models.Sensor>> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getSensorById(@org.jetbrains.annotations.NotNull()
    java.lang.String sensorId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.carboncredit.app.data.models.Sensor> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getReadingsForSensor(@org.jetbrains.annotations.NotNull()
    java.lang.String sensorId, @org.jetbrains.annotations.Nullable()
    java.lang.String startTime, @org.jetbrains.annotations.Nullable()
    java.lang.String endTime, int limit, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.carboncredit.app.data.models.SensorReading>> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getReadingsForFacility(@org.jetbrains.annotations.NotNull()
    java.lang.String facilityId, @org.jetbrains.annotations.Nullable()
    java.lang.String startTime, @org.jetbrains.annotations.Nullable()
    java.lang.String endTime, int limit, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.carboncredit.app.data.models.SensorReading>> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getLatestReading(@org.jetbrains.annotations.NotNull()
    java.lang.String facilityId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.carboncredit.app.data.models.SensorReading> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getLatestReadingForSensor(@org.jetbrains.annotations.NotNull()
    java.lang.String sensorId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.carboncredit.app.data.models.SensorReading> $completion) {
        return null;
    }
    
    /**
     * Subscribe to real-time sensor reading inserts for a facility.
     * Returns a Flow of new SensorReading objects.
     */
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<com.carboncredit.app.data.models.SensorReading> subscribeToReadings(@org.jetbrains.annotations.NotNull()
    java.lang.String facilityId) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object subscribeChannel(@org.jetbrains.annotations.NotNull()
    java.lang.String facilityId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object unsubscribeChannel(@org.jetbrains.annotations.NotNull()
    java.lang.String facilityId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
}