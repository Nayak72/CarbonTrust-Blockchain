package com.carboncredit.app.ui.auditor.comparison;

import androidx.lifecycle.ViewModel;
import com.carboncredit.app.core.security.TokenManager;
import com.carboncredit.app.core.utils.DateUtils;
import com.carboncredit.app.data.models.Facility;
import com.carboncredit.app.data.repository.*;
import com.carboncredit.app.ui.auditor.dashboard.FacilityStatus;
import dagger.hilt.android.lifecycle.HiltViewModel;
import kotlinx.coroutines.flow.StateFlow;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000J\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\b\u0007\u0018\u00002\u00020\u0001B/\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u000b\u00a2\u0006\u0002\u0010\fJ\u0006\u0010\u0014\u001a\u00020\u0015J\b\u0010\u0016\u001a\u00020\u0015H\u0002J\u000e\u0010\u0017\u001a\u00020\u00152\u0006\u0010\u0018\u001a\u00020\u0019R\u0014\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u000f0\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u000f0\u0011\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0013\u00a8\u0006\u001a"}, d2 = {"Lcom/carboncredit/app/ui/auditor/comparison/FacilityComparisonViewModel;", "Landroidx/lifecycle/ViewModel;", "tokenManager", "Lcom/carboncredit/app/core/security/TokenManager;", "facilityRepository", "Lcom/carboncredit/app/data/repository/FacilityRepository;", "creditRepository", "Lcom/carboncredit/app/data/repository/CreditRepository;", "anomalyRepository", "Lcom/carboncredit/app/data/repository/AnomalyRepository;", "sensorRepository", "Lcom/carboncredit/app/data/repository/SensorRepository;", "(Lcom/carboncredit/app/core/security/TokenManager;Lcom/carboncredit/app/data/repository/FacilityRepository;Lcom/carboncredit/app/data/repository/CreditRepository;Lcom/carboncredit/app/data/repository/AnomalyRepository;Lcom/carboncredit/app/data/repository/SensorRepository;)V", "_uiState", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/carboncredit/app/ui/auditor/comparison/ComparisonUiState;", "uiState", "Lkotlinx/coroutines/flow/StateFlow;", "getUiState", "()Lkotlinx/coroutines/flow/StateFlow;", "compare", "", "loadFacilities", "toggleFacility", "facilityId", "", "app_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class FacilityComparisonViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.carboncredit.app.core.security.TokenManager tokenManager = null;
    @org.jetbrains.annotations.NotNull()
    private final com.carboncredit.app.data.repository.FacilityRepository facilityRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.carboncredit.app.data.repository.CreditRepository creditRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.carboncredit.app.data.repository.AnomalyRepository anomalyRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.carboncredit.app.data.repository.SensorRepository sensorRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.carboncredit.app.ui.auditor.comparison.ComparisonUiState> _uiState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.carboncredit.app.ui.auditor.comparison.ComparisonUiState> uiState = null;
    
    @javax.inject.Inject()
    public FacilityComparisonViewModel(@org.jetbrains.annotations.NotNull()
    com.carboncredit.app.core.security.TokenManager tokenManager, @org.jetbrains.annotations.NotNull()
    com.carboncredit.app.data.repository.FacilityRepository facilityRepository, @org.jetbrains.annotations.NotNull()
    com.carboncredit.app.data.repository.CreditRepository creditRepository, @org.jetbrains.annotations.NotNull()
    com.carboncredit.app.data.repository.AnomalyRepository anomalyRepository, @org.jetbrains.annotations.NotNull()
    com.carboncredit.app.data.repository.SensorRepository sensorRepository) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.carboncredit.app.ui.auditor.comparison.ComparisonUiState> getUiState() {
        return null;
    }
    
    private final void loadFacilities() {
    }
    
    public final void toggleFacility(@org.jetbrains.annotations.NotNull()
    java.lang.String facilityId) {
    }
    
    public final void compare() {
    }
}