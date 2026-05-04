package com.carboncredit.app.ui.manager.dashboard;

import androidx.lifecycle.ViewModel;
import com.carboncredit.app.core.security.TokenManager;
import com.carboncredit.app.core.utils.Constants;
import com.carboncredit.app.core.utils.DateUtils;
import com.carboncredit.app.core.utils.Resource;
import com.carboncredit.app.data.models.AnomalyEvent;
import com.carboncredit.app.data.models.Facility;
import com.carboncredit.app.data.models.Sensor;
import com.carboncredit.app.data.models.SensorReading;
import com.carboncredit.app.data.repository.*;
import dagger.hilt.android.lifecycle.HiltViewModel;
import kotlinx.coroutines.flow.StateFlow;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000D\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u001e\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\b\u0018\u00002\u00020\u0001B{\u0012\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u0012\u000e\b\u0002\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u0007\u0012\b\b\u0002\u0010\t\u001a\u00020\n\u0012\b\b\u0002\u0010\u000b\u001a\u00020\n\u0012\b\b\u0002\u0010\f\u001a\u00020\n\u0012\b\b\u0002\u0010\r\u001a\u00020\n\u0012\u000e\b\u0002\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u000f0\u0007\u0012\b\b\u0002\u0010\u0010\u001a\u00020\u0011\u0012\n\b\u0002\u0010\u0012\u001a\u0004\u0018\u00010\u0013\u00a2\u0006\u0002\u0010\u0014J\u000b\u0010$\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\u000b\u0010%\u001a\u0004\u0018\u00010\u0013H\u00c6\u0003J\u000b\u0010&\u001a\u0004\u0018\u00010\u0005H\u00c6\u0003J\u000f\u0010\'\u001a\b\u0012\u0004\u0012\u00020\b0\u0007H\u00c6\u0003J\t\u0010(\u001a\u00020\nH\u00c6\u0003J\t\u0010)\u001a\u00020\nH\u00c6\u0003J\t\u0010*\u001a\u00020\nH\u00c6\u0003J\t\u0010+\u001a\u00020\nH\u00c6\u0003J\u000f\u0010,\u001a\b\u0012\u0004\u0012\u00020\u000f0\u0007H\u00c6\u0003J\t\u0010-\u001a\u00020\u0011H\u00c6\u0003J\u007f\u0010.\u001a\u00020\u00002\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u00052\u000e\b\u0002\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u00072\b\b\u0002\u0010\t\u001a\u00020\n2\b\b\u0002\u0010\u000b\u001a\u00020\n2\b\b\u0002\u0010\f\u001a\u00020\n2\b\b\u0002\u0010\r\u001a\u00020\n2\u000e\b\u0002\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u000f0\u00072\b\b\u0002\u0010\u0010\u001a\u00020\u00112\n\b\u0002\u0010\u0012\u001a\u0004\u0018\u00010\u0013H\u00c6\u0001J\u0013\u0010/\u001a\u00020\u00112\b\u00100\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u00101\u001a\u000202H\u00d6\u0001J\t\u00103\u001a\u00020\u0013H\u00d6\u0001R\u0011\u0010\u000b\u001a\u00020\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0016R\u0011\u0010\r\u001a\u00020\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0016R\u0011\u0010\f\u001a\u00020\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0016R\u0013\u0010\u0012\u001a\u0004\u0018\u00010\u0013\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\u001aR\u0013\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u001cR\u0011\u0010\u0010\u001a\u00020\u0011\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u001dR\u0013\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001e\u0010\u001fR\u0017\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u000f0\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b \u0010!R\u0017\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\"\u0010!R\u0011\u0010\t\u001a\u00020\n\u00a2\u0006\b\n\u0000\u001a\u0004\b#\u0010\u0016\u00a8\u00064"}, d2 = {"Lcom/carboncredit/app/ui/manager/dashboard/DashboardUiState;", "", "facility", "Lcom/carboncredit/app/data/models/Facility;", "latestReading", "Lcom/carboncredit/app/data/models/SensorReading;", "sensors", "", "Lcom/carboncredit/app/data/models/Sensor;", "todayEmissions", "", "baselineEmissions", "creditsToday", "creditsMonth", "recentAnomalies", "Lcom/carboncredit/app/data/models/AnomalyEvent;", "isLoading", "", "error", "", "(Lcom/carboncredit/app/data/models/Facility;Lcom/carboncredit/app/data/models/SensorReading;Ljava/util/List;FFFFLjava/util/List;ZLjava/lang/String;)V", "getBaselineEmissions", "()F", "getCreditsMonth", "getCreditsToday", "getError", "()Ljava/lang/String;", "getFacility", "()Lcom/carboncredit/app/data/models/Facility;", "()Z", "getLatestReading", "()Lcom/carboncredit/app/data/models/SensorReading;", "getRecentAnomalies", "()Ljava/util/List;", "getSensors", "getTodayEmissions", "component1", "component10", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "equals", "other", "hashCode", "", "toString", "app_debug"})
public final class DashboardUiState {
    @org.jetbrains.annotations.Nullable()
    private final com.carboncredit.app.data.models.Facility facility = null;
    @org.jetbrains.annotations.Nullable()
    private final com.carboncredit.app.data.models.SensorReading latestReading = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.carboncredit.app.data.models.Sensor> sensors = null;
    private final float todayEmissions = 0.0F;
    private final float baselineEmissions = 0.0F;
    private final float creditsToday = 0.0F;
    private final float creditsMonth = 0.0F;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.carboncredit.app.data.models.AnomalyEvent> recentAnomalies = null;
    private final boolean isLoading = false;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String error = null;
    
    public DashboardUiState(@org.jetbrains.annotations.Nullable()
    com.carboncredit.app.data.models.Facility facility, @org.jetbrains.annotations.Nullable()
    com.carboncredit.app.data.models.SensorReading latestReading, @org.jetbrains.annotations.NotNull()
    java.util.List<com.carboncredit.app.data.models.Sensor> sensors, float todayEmissions, float baselineEmissions, float creditsToday, float creditsMonth, @org.jetbrains.annotations.NotNull()
    java.util.List<com.carboncredit.app.data.models.AnomalyEvent> recentAnomalies, boolean isLoading, @org.jetbrains.annotations.Nullable()
    java.lang.String error) {
        super();
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.carboncredit.app.data.models.Facility getFacility() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.carboncredit.app.data.models.SensorReading getLatestReading() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.carboncredit.app.data.models.Sensor> getSensors() {
        return null;
    }
    
    public final float getTodayEmissions() {
        return 0.0F;
    }
    
    public final float getBaselineEmissions() {
        return 0.0F;
    }
    
    public final float getCreditsToday() {
        return 0.0F;
    }
    
    public final float getCreditsMonth() {
        return 0.0F;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.carboncredit.app.data.models.AnomalyEvent> getRecentAnomalies() {
        return null;
    }
    
    public final boolean isLoading() {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getError() {
        return null;
    }
    
    public DashboardUiState() {
        super();
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.carboncredit.app.data.models.Facility component1() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component10() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.carboncredit.app.data.models.SensorReading component2() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.carboncredit.app.data.models.Sensor> component3() {
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
    
    public final float component7() {
        return 0.0F;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.carboncredit.app.data.models.AnomalyEvent> component8() {
        return null;
    }
    
    public final boolean component9() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.carboncredit.app.ui.manager.dashboard.DashboardUiState copy(@org.jetbrains.annotations.Nullable()
    com.carboncredit.app.data.models.Facility facility, @org.jetbrains.annotations.Nullable()
    com.carboncredit.app.data.models.SensorReading latestReading, @org.jetbrains.annotations.NotNull()
    java.util.List<com.carboncredit.app.data.models.Sensor> sensors, float todayEmissions, float baselineEmissions, float creditsToday, float creditsMonth, @org.jetbrains.annotations.NotNull()
    java.util.List<com.carboncredit.app.data.models.AnomalyEvent> recentAnomalies, boolean isLoading, @org.jetbrains.annotations.Nullable()
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