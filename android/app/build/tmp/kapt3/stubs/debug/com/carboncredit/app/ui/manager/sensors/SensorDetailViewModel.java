package com.carboncredit.app.ui.manager.sensors;

import androidx.lifecycle.ViewModel;
import com.carboncredit.app.core.utils.DateUtils;
import com.carboncredit.app.data.models.Sensor;
import com.carboncredit.app.data.models.SensorReading;
import com.carboncredit.app.data.repository.SensorRepository;
import dagger.hilt.android.lifecycle.HiltViewModel;
import kotlinx.coroutines.flow.StateFlow;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0005\b\u0007\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0006\u0010\f\u001a\u00020\rJ\u0018\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\r2\u0006\u0010\u0011\u001a\u00020\rH\u0002J\u000e\u0010\u0012\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\rJ\u0016\u0010\u0013\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\r2\u0006\u0010\u0011\u001a\u00020\rR\u0014\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00070\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000b\u00a8\u0006\u0014"}, d2 = {"Lcom/carboncredit/app/ui/manager/sensors/SensorDetailViewModel;", "Landroidx/lifecycle/ViewModel;", "sensorRepository", "Lcom/carboncredit/app/data/repository/SensorRepository;", "(Lcom/carboncredit/app/data/repository/SensorRepository;)V", "_uiState", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/carboncredit/app/ui/manager/sensors/SensorDetailUiState;", "uiState", "Lkotlinx/coroutines/flow/StateFlow;", "getUiState", "()Lkotlinx/coroutines/flow/StateFlow;", "generateCsv", "", "loadReadings", "", "sensorId", "period", "loadSensor", "selectPeriod", "app_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class SensorDetailViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.carboncredit.app.data.repository.SensorRepository sensorRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.carboncredit.app.ui.manager.sensors.SensorDetailUiState> _uiState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.carboncredit.app.ui.manager.sensors.SensorDetailUiState> uiState = null;
    
    @javax.inject.Inject()
    public SensorDetailViewModel(@org.jetbrains.annotations.NotNull()
    com.carboncredit.app.data.repository.SensorRepository sensorRepository) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.carboncredit.app.ui.manager.sensors.SensorDetailUiState> getUiState() {
        return null;
    }
    
    public final void loadSensor(@org.jetbrains.annotations.NotNull()
    java.lang.String sensorId) {
    }
    
    public final void selectPeriod(@org.jetbrains.annotations.NotNull()
    java.lang.String sensorId, @org.jetbrains.annotations.NotNull()
    java.lang.String period) {
    }
    
    private final void loadReadings(java.lang.String sensorId, java.lang.String period) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String generateCsv() {
        return null;
    }
}