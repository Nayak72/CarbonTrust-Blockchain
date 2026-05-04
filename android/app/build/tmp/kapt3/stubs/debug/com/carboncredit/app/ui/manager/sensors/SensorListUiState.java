package com.carboncredit.app.ui.manager.sensors;

import androidx.lifecycle.ViewModel;
import com.carboncredit.app.core.security.TokenManager;
import com.carboncredit.app.core.utils.Constants;
import com.carboncredit.app.data.models.Sensor;
import com.carboncredit.app.data.models.SensorReading;
import com.carboncredit.app.data.repository.SensorRepository;
import dagger.hilt.android.lifecycle.HiltViewModel;
import kotlinx.coroutines.flow.StateFlow;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\r\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\b\u0018\u00002\u00020\u0001B9\u0012\u001c\b\u0002\u0010\u0002\u001a\u0016\u0012\u0012\u0012\u0010\u0012\u0004\u0012\u00020\u0005\u0012\u0006\u0012\u0004\u0018\u00010\u00060\u00040\u0003\u0012\b\b\u0002\u0010\u0007\u001a\u00020\b\u0012\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\n\u00a2\u0006\u0002\u0010\u000bJ\u001d\u0010\u0011\u001a\u0016\u0012\u0012\u0012\u0010\u0012\u0004\u0012\u00020\u0005\u0012\u0006\u0012\u0004\u0018\u00010\u00060\u00040\u0003H\u00c6\u0003J\t\u0010\u0012\u001a\u00020\bH\u00c6\u0003J\u000b\u0010\u0013\u001a\u0004\u0018\u00010\nH\u00c6\u0003J=\u0010\u0014\u001a\u00020\u00002\u001c\b\u0002\u0010\u0002\u001a\u0016\u0012\u0012\u0012\u0010\u0012\u0004\u0012\u00020\u0005\u0012\u0006\u0012\u0004\u0018\u00010\u00060\u00040\u00032\b\b\u0002\u0010\u0007\u001a\u00020\b2\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\nH\u00c6\u0001J\u0013\u0010\u0015\u001a\u00020\b2\b\u0010\u0016\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u0017\u001a\u00020\u0018H\u00d6\u0001J\t\u0010\u0019\u001a\u00020\nH\u00d6\u0001R\u0013\u0010\t\u001a\u0004\u0018\u00010\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u0011\u0010\u0007\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\u000eR%\u0010\u0002\u001a\u0016\u0012\u0012\u0012\u0010\u0012\u0004\u0012\u00020\u0005\u0012\u0006\u0012\u0004\u0018\u00010\u00060\u00040\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010\u00a8\u0006\u001a"}, d2 = {"Lcom/carboncredit/app/ui/manager/sensors/SensorListUiState;", "", "sensors", "", "Lkotlin/Pair;", "Lcom/carboncredit/app/data/models/Sensor;", "Lcom/carboncredit/app/data/models/SensorReading;", "isLoading", "", "error", "", "(Ljava/util/List;ZLjava/lang/String;)V", "getError", "()Ljava/lang/String;", "()Z", "getSensors", "()Ljava/util/List;", "component1", "component2", "component3", "copy", "equals", "other", "hashCode", "", "toString", "app_debug"})
public final class SensorListUiState {
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<kotlin.Pair<com.carboncredit.app.data.models.Sensor, com.carboncredit.app.data.models.SensorReading>> sensors = null;
    private final boolean isLoading = false;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String error = null;
    
    public SensorListUiState(@org.jetbrains.annotations.NotNull()
    java.util.List<kotlin.Pair<com.carboncredit.app.data.models.Sensor, com.carboncredit.app.data.models.SensorReading>> sensors, boolean isLoading, @org.jetbrains.annotations.Nullable()
    java.lang.String error) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<kotlin.Pair<com.carboncredit.app.data.models.Sensor, com.carboncredit.app.data.models.SensorReading>> getSensors() {
        return null;
    }
    
    public final boolean isLoading() {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getError() {
        return null;
    }
    
    public SensorListUiState() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<kotlin.Pair<com.carboncredit.app.data.models.Sensor, com.carboncredit.app.data.models.SensorReading>> component1() {
        return null;
    }
    
    public final boolean component2() {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component3() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.carboncredit.app.ui.manager.sensors.SensorListUiState copy(@org.jetbrains.annotations.NotNull()
    java.util.List<kotlin.Pair<com.carboncredit.app.data.models.Sensor, com.carboncredit.app.data.models.SensorReading>> sensors, boolean isLoading, @org.jetbrains.annotations.Nullable()
    java.lang.String error) {
        return null;
    }
    
    @java.lang.Override()
    public boolean equals(@org.jetbrains.annotations.Nullable()
    java.lang.Object other) {
        return false;
    }
    
    @java.lang.Override()
    public int hashCode() {
        return 0;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public java.lang.String toString() {
        return null;
    }
}