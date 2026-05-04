package com.carboncredit.app.ui.auditor.reports;

import androidx.lifecycle.ViewModel;
import com.carboncredit.app.core.security.TokenManager;
import com.carboncredit.app.data.models.*;
import com.carboncredit.app.data.repository.*;
import dagger.hilt.android.lifecycle.HiltViewModel;
import kotlinx.coroutines.flow.StateFlow;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000J\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0005\b\u0007\u0018\u00002\u00020\u0001B/\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u000b\u00a2\u0006\u0002\u0010\fJ\u0006\u0010\u0016\u001a\u00020\u0017J\u0006\u0010\u0018\u001a\u00020\u0011J\b\u0010\u0019\u001a\u00020\u0017H\u0002J\u000e\u0010\u001a\u001a\u00020\u00172\u0006\u0010\u001b\u001a\u00020\u0011R\u0014\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u000f0\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0011X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u000f0\u0013\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0015\u00a8\u0006\u001c"}, d2 = {"Lcom/carboncredit/app/ui/auditor/reports/AuditReportViewModel;", "Landroidx/lifecycle/ViewModel;", "tokenManager", "Lcom/carboncredit/app/core/security/TokenManager;", "facilityRepository", "Lcom/carboncredit/app/data/repository/FacilityRepository;", "sensorRepository", "Lcom/carboncredit/app/data/repository/SensorRepository;", "anomalyRepository", "Lcom/carboncredit/app/data/repository/AnomalyRepository;", "creditRepository", "Lcom/carboncredit/app/data/repository/CreditRepository;", "(Lcom/carboncredit/app/core/security/TokenManager;Lcom/carboncredit/app/data/repository/FacilityRepository;Lcom/carboncredit/app/data/repository/SensorRepository;Lcom/carboncredit/app/data/repository/AnomalyRepository;Lcom/carboncredit/app/data/repository/CreditRepository;)V", "_uiState", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/carboncredit/app/ui/auditor/reports/AuditReportUiState;", "generatedPdfContent", "", "uiState", "Lkotlinx/coroutines/flow/StateFlow;", "getUiState", "()Lkotlinx/coroutines/flow/StateFlow;", "generateReport", "", "getPdfContent", "loadFacilities", "selectFacility", "id", "app_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class AuditReportViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.carboncredit.app.core.security.TokenManager tokenManager = null;
    @org.jetbrains.annotations.NotNull()
    private final com.carboncredit.app.data.repository.FacilityRepository facilityRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.carboncredit.app.data.repository.SensorRepository sensorRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.carboncredit.app.data.repository.AnomalyRepository anomalyRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.carboncredit.app.data.repository.CreditRepository creditRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.carboncredit.app.ui.auditor.reports.AuditReportUiState> _uiState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.carboncredit.app.ui.auditor.reports.AuditReportUiState> uiState = null;
    @org.jetbrains.annotations.NotNull()
    private java.lang.String generatedPdfContent = "";
    
    @javax.inject.Inject()
    public AuditReportViewModel(@org.jetbrains.annotations.NotNull()
    com.carboncredit.app.core.security.TokenManager tokenManager, @org.jetbrains.annotations.NotNull()
    com.carboncredit.app.data.repository.FacilityRepository facilityRepository, @org.jetbrains.annotations.NotNull()
    com.carboncredit.app.data.repository.SensorRepository sensorRepository, @org.jetbrains.annotations.NotNull()
    com.carboncredit.app.data.repository.AnomalyRepository anomalyRepository, @org.jetbrains.annotations.NotNull()
    com.carboncredit.app.data.repository.CreditRepository creditRepository) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.carboncredit.app.ui.auditor.reports.AuditReportUiState> getUiState() {
        return null;
    }
    
    private final void loadFacilities() {
    }
    
    public final void selectFacility(@org.jetbrains.annotations.NotNull()
    java.lang.String id) {
    }
    
    public final void generateReport() {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getPdfContent() {
        return null;
    }
}