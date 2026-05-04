package com.carboncredit.app.ui.manager.sensors;

import androidx.lifecycle.ViewModel;
import com.carboncredit.app.core.utils.DateUtils;
import com.carboncredit.app.data.models.Sensor;
import com.carboncredit.app.data.models.SensorReading;
import com.carboncredit.app.data.repository.SensorRepository;
import dagger.hilt.android.lifecycle.HiltViewModel;
import kotlinx.coroutines.flow.StateFlow;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0003\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u001f\b\u0086\b\u0018\u00002\u00020\u0001Bi\u0012\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\u000e\b\u0002\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005\u0012\b\b\u0002\u0010\u0007\u001a\u00020\b\u0012\b\b\u0002\u0010\t\u001a\u00020\n\u0012\b\b\u0002\u0010\u000b\u001a\u00020\n\u0012\b\b\u0002\u0010\f\u001a\u00020\n\u0012\b\b\u0002\u0010\r\u001a\u00020\u000e\u0012\b\b\u0002\u0010\u000f\u001a\u00020\u0010\u0012\n\b\u0002\u0010\u0011\u001a\u0004\u0018\u00010\b\u00a2\u0006\u0002\u0010\u0012J\u000b\u0010!\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\u000f\u0010\"\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005H\u00c6\u0003J\t\u0010#\u001a\u00020\bH\u00c6\u0003J\t\u0010$\u001a\u00020\nH\u00c6\u0003J\t\u0010%\u001a\u00020\nH\u00c6\u0003J\t\u0010&\u001a\u00020\nH\u00c6\u0003J\t\u0010\'\u001a\u00020\u000eH\u00c6\u0003J\t\u0010(\u001a\u00020\u0010H\u00c6\u0003J\u000b\u0010)\u001a\u0004\u0018\u00010\bH\u00c6\u0003Jm\u0010*\u001a\u00020\u00002\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u00032\u000e\b\u0002\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u00052\b\b\u0002\u0010\u0007\u001a\u00020\b2\b\b\u0002\u0010\t\u001a\u00020\n2\b\b\u0002\u0010\u000b\u001a\u00020\n2\b\b\u0002\u0010\f\u001a\u00020\n2\b\b\u0002\u0010\r\u001a\u00020\u000e2\b\b\u0002\u0010\u000f\u001a\u00020\u00102\n\b\u0002\u0010\u0011\u001a\u0004\u0018\u00010\bH\u00c6\u0001J\u0013\u0010+\u001a\u00020\u00102\b\u0010,\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010-\u001a\u00020\u000eH\u00d6\u0001J\t\u0010.\u001a\u00020\bH\u00d6\u0001R\u0011\u0010\r\u001a\u00020\u000e\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014R\u0011\u0010\f\u001a\u00020\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0016R\u0013\u0010\u0011\u001a\u0004\u0018\u00010\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0018R\u0011\u0010\u000f\u001a\u00020\u0010\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0019R\u0011\u0010\u000b\u001a\u00020\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u0016R\u0011\u0010\t\u001a\u00020\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u0016R\u0017\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001c\u0010\u001dR\u0011\u0010\u0007\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001e\u0010\u0018R\u0013\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001f\u0010 \u00a8\u0006/"}, d2 = {"Lcom/carboncredit/app/ui/manager/sensors/SensorDetailUiState;", "", "sensor", "Lcom/carboncredit/app/data/models/Sensor;", "readings", "", "Lcom/carboncredit/app/data/models/SensorReading;", "selectedPeriod", "", "minPpm", "", "maxPpm", "avgPpm", "anomalyCount", "", "isLoading", "", "error", "(Lcom/carboncredit/app/data/models/Sensor;Ljava/util/List;Ljava/lang/String;FFFIZLjava/lang/String;)V", "getAnomalyCount", "()I", "getAvgPpm", "()F", "getError", "()Ljava/lang/String;", "()Z", "getMaxPpm", "getMinPpm", "getReadings", "()Ljava/util/List;", "getSelectedPeriod", "getSensor", "()Lcom/carboncredit/app/data/models/Sensor;", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "equals", "other", "hashCode", "toString", "app_debug"})
public final class SensorDetailUiState {
    @org.jetbrains.annotations.Nullable()
    private final com.carboncredit.app.data.models.Sensor sensor = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.carboncredit.app.data.models.SensorReading> readings = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String selectedPeriod = null;
    private final float minPpm = 0.0F;
    private final float maxPpm = 0.0F;
    private final float avgPpm = 0.0F;
    private final int anomalyCount = 0;
    private final boolean isLoading = false;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String error = null;
    
    public SensorDetailUiState(@org.jetbrains.annotations.Nullable()
    com.carboncredit.app.data.models.Sensor sensor, @org.jetbrains.annotations.NotNull()
    java.util.List<com.carboncredit.app.data.models.SensorReading> readings, @org.jetbrains.annotations.NotNull()
    java.lang.String selectedPeriod, float minPpm, float maxPpm, float avgPpm, int anomalyCount, boolean isLoading, @org.jetbrains.annotations.Nullable()
    java.lang.String error) {
        super();
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.carboncredit.app.data.models.Sensor getSensor() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.carboncredit.app.data.models.SensorReading> getReadings() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getSelectedPeriod() {
        return null;
    }
    
    public final float getMinPpm() {
        return 0.0F;
    }
    
    public final float getMaxPpm() {
        return 0.0F;
    }
    
    public final float getAvgPpm() {
        return 0.0F;
    }
    
    public final int getAnomalyCount() {
        return 0;
    }
    
    public final boolean isLoading() {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getError() {
        return null;
    }
    
    public SensorDetailUiState() {
        super();
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.carboncredit.app.data.models.Sensor component1() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.carboncredit.app.data.models.SensorReading> component2() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component3() {
        return null;
    }
    
    public final float component4() {
        return 0.0F;
    }
    
    public final float component5() {
        return 0.0F;
    }
    
    public final float component6() {
        return 0.0F;
    }
    
    public final int component7() {
        return 0;
    }
    
    public final boolean component8() {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component9() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.carboncredit.app.ui.manager.sensors.SensorDetailUiState copy(@org.jetbrains.annotations.Nullable()
    com.carboncredit.app.data.models.Sensor sensor, @org.jetbrains.annotations.NotNull()
    java.util.List<com.carboncredit.app.data.models.SensorReading> readings, @org.jetbrains.annotations.NotNull()
    java.lang.String selectedPeriod, float minPpm, float maxPpm, float avgPpm, int anomalyCount, boolean isLoading, @org.jetbrains.annotations.Nullable()
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