package com.carboncredit.app.ui.auditor.facility;

import androidx.lifecycle.ViewModel;
import com.carboncredit.app.core.security.TokenManager;
import com.carboncredit.app.core.utils.DateUtils;
import com.carboncredit.app.data.models.*;
import com.carboncredit.app.data.repository.*;
import dagger.hilt.android.lifecycle.HiltViewModel;
import kotlinx.coroutines.flow.StateFlow;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000H\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0007\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u001c\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\b\u0018\u00002\u00020\u0001B{\u0012\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\u000e\b\u0002\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005\u0012\u000e\b\u0002\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\b0\u0005\u0012\u000e\b\u0002\u0010\t\u001a\b\u0012\u0004\u0012\u00020\n0\u0005\u0012\u000e\b\u0002\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\f0\u0005\u0012\b\b\u0002\u0010\r\u001a\u00020\u000e\u0012\b\b\u0002\u0010\u000f\u001a\u00020\u0010\u0012\b\b\u0002\u0010\u0011\u001a\u00020\u0012\u0012\n\b\u0002\u0010\u0013\u001a\u0004\u0018\u00010\u0010\u00a2\u0006\u0002\u0010\u0014J\u000b\u0010\"\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\u000f\u0010#\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005H\u00c6\u0003J\u000f\u0010$\u001a\b\u0012\u0004\u0012\u00020\b0\u0005H\u00c6\u0003J\u000f\u0010%\u001a\b\u0012\u0004\u0012\u00020\n0\u0005H\u00c6\u0003J\u000f\u0010&\u001a\b\u0012\u0004\u0012\u00020\f0\u0005H\u00c6\u0003J\t\u0010\'\u001a\u00020\u000eH\u00c6\u0003J\t\u0010(\u001a\u00020\u0010H\u00c6\u0003J\t\u0010)\u001a\u00020\u0012H\u00c6\u0003J\u000b\u0010*\u001a\u0004\u0018\u00010\u0010H\u00c6\u0003J\u007f\u0010+\u001a\u00020\u00002\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u00032\u000e\b\u0002\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u00052\u000e\b\u0002\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\b0\u00052\u000e\b\u0002\u0010\t\u001a\b\u0012\u0004\u0012\u00020\n0\u00052\u000e\b\u0002\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\f0\u00052\b\b\u0002\u0010\r\u001a\u00020\u000e2\b\b\u0002\u0010\u000f\u001a\u00020\u00102\b\b\u0002\u0010\u0011\u001a\u00020\u00122\n\b\u0002\u0010\u0013\u001a\u0004\u0018\u00010\u0010H\u00c6\u0001J\u0013\u0010,\u001a\u00020\u00122\b\u0010-\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010.\u001a\u00020/H\u00d6\u0001J\t\u00100\u001a\u00020\u0010H\u00d6\u0001R\u0017\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\b0\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0016R\u0011\u0010\u000f\u001a\u00020\u0010\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0018R\u0017\u0010\t\u001a\b\u0012\u0004\u0012\u00020\n0\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\u0016R\u0013\u0010\u0013\u001a\u0004\u0018\u00010\u0010\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u0018R\u0013\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u001cR\u0011\u0010\u0011\u001a\u00020\u0012\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u001dR\u0017\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\f0\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001e\u0010\u0016R\u0017\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001f\u0010\u0016R\u0011\u0010\r\u001a\u00020\u000e\u00a2\u0006\b\n\u0000\u001a\u0004\b \u0010!\u00a8\u00061"}, d2 = {"Lcom/carboncredit/app/ui/auditor/facility/FacilityDetailUiState;", "", "facility", "Lcom/carboncredit/app/data/models/Facility;", "sensors", "", "Lcom/carboncredit/app/data/models/Sensor;", "anomalies", "Lcom/carboncredit/app/data/models/AnomalyEvent;", "credits", "Lcom/carboncredit/app/data/models/CarbonCredit;", "readings", "Lcom/carboncredit/app/data/models/SensorReading;", "totalEmissions", "", "compliance", "", "isLoading", "", "error", "(Lcom/carboncredit/app/data/models/Facility;Ljava/util/List;Ljava/util/List;Ljava/util/List;Ljava/util/List;FLjava/lang/String;ZLjava/lang/String;)V", "getAnomalies", "()Ljava/util/List;", "getCompliance", "()Ljava/lang/String;", "getCredits", "getError", "getFacility", "()Lcom/carboncredit/app/data/models/Facility;", "()Z", "getReadings", "getSensors", "getTotalEmissions", "()F", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "equals", "other", "hashCode", "", "toString", "app_debug"})
public final class FacilityDetailUiState {
    @org.jetbrains.annotations.Nullable()
    private final com.carboncredit.app.data.models.Facility facility = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.carboncredit.app.data.models.Sensor> sensors = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.carboncredit.app.data.models.AnomalyEvent> anomalies = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.carboncredit.app.data.models.CarbonCredit> credits = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.carboncredit.app.data.models.SensorReading> readings = null;
    private final float totalEmissions = 0.0F;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String compliance = null;
    private final boolean isLoading = false;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String error = null;
    
    public FacilityDetailUiState(@org.jetbrains.annotations.Nullable()
    com.carboncredit.app.data.models.Facility facility, @org.jetbrains.annotations.NotNull()
    java.util.List<com.carboncredit.app.data.models.Sensor> sensors, @org.jetbrains.annotations.NotNull()
    java.util.List<com.carboncredit.app.data.models.AnomalyEvent> anomalies, @org.jetbrains.annotations.NotNull()
    java.util.List<com.carboncredit.app.data.models.CarbonCredit> credits, @org.jetbrains.annotations.NotNull()
    java.util.List<com.carboncredit.app.data.models.SensorReading> readings, float totalEmissions, @org.jetbrains.annotations.NotNull()
    java.lang.String compliance, boolean isLoading, @org.jetbrains.annotations.Nullable()
    java.lang.String error) {
        super();
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.carboncredit.app.data.models.Facility getFacility() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.carboncredit.app.data.models.Sensor> getSensors() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.carboncredit.app.data.models.AnomalyEvent> getAnomalies() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.carboncredit.app.data.models.CarbonCredit> getCredits() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.carboncredit.app.data.models.SensorReading> getReadings() {
        return null;
    }
    
    public final float getTotalEmissions() {
        return 0.0F;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getCompliance() {
        return null;
    }
    
    public final boolean isLoading() {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getError() {
        return null;
    }
    
    public FacilityDetailUiState() {
        super();
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.carboncredit.app.data.models.Facility component1() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.carboncredit.app.data.models.Sensor> component2() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.carboncredit.app.data.models.AnomalyEvent> component3() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.carboncredit.app.data.models.CarbonCredit> component4() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.carboncredit.app.data.models.SensorReading> component5() {
        return null;
    }
    
    public final float component6() {
        return 0.0F;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component7() {
        return null;
    }
    
    public final boolean component8() {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component9() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.carboncredit.app.ui.auditor.facility.FacilityDetailUiState copy(@org.jetbrains.annotations.Nullable()
    com.carboncredit.app.data.models.Facility facility, @org.jetbrains.annotations.NotNull()
    java.util.List<com.carboncredit.app.data.models.Sensor> sensors, @org.jetbrains.annotations.NotNull()
    java.util.List<com.carboncredit.app.data.models.AnomalyEvent> anomalies, @org.jetbrains.annotations.NotNull()
    java.util.List<com.carboncredit.app.data.models.CarbonCredit> credits, @org.jetbrains.annotations.NotNull()
    java.util.List<com.carboncredit.app.data.models.SensorReading> readings, float totalEmissions, @org.jetbrains.annotations.NotNull()
    java.lang.String compliance, boolean isLoading, @org.jetbrains.annotations.Nullable()
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