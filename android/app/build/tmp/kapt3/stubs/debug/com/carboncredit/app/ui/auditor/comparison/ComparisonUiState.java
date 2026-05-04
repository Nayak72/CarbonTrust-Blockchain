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

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\"\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0015\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\b\u0018\u00002\u00020\u0001BW\u0012\u000e\b\u0002\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u0012\u000e\b\u0002\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006\u0012\u000e\b\u0002\u0010\b\u001a\b\u0012\u0004\u0012\u00020\t0\u0003\u0012\n\b\u0002\u0010\n\u001a\u0004\u0018\u00010\u0007\u0012\b\b\u0002\u0010\u000b\u001a\u00020\f\u0012\n\b\u0002\u0010\r\u001a\u0004\u0018\u00010\u0007\u00a2\u0006\u0002\u0010\u000eJ\u000f\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003H\u00c6\u0003J\u000f\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006H\u00c6\u0003J\u000f\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\t0\u0003H\u00c6\u0003J\u000b\u0010\u001b\u001a\u0004\u0018\u00010\u0007H\u00c6\u0003J\t\u0010\u001c\u001a\u00020\fH\u00c6\u0003J\u000b\u0010\u001d\u001a\u0004\u0018\u00010\u0007H\u00c6\u0003J[\u0010\u001e\u001a\u00020\u00002\u000e\b\u0002\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\u000e\b\u0002\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u00062\u000e\b\u0002\u0010\b\u001a\b\u0012\u0004\u0012\u00020\t0\u00032\n\b\u0002\u0010\n\u001a\u0004\u0018\u00010\u00072\b\b\u0002\u0010\u000b\u001a\u00020\f2\n\b\u0002\u0010\r\u001a\u0004\u0018\u00010\u0007H\u00c6\u0001J\u0013\u0010\u001f\u001a\u00020\f2\b\u0010 \u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010!\u001a\u00020\"H\u00d6\u0001J\t\u0010#\u001a\u00020\u0007H\u00d6\u0001R\u0017\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u0017\u0010\b\u001a\b\u0012\u0004\u0012\u00020\t0\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0010R\u0013\u0010\r\u001a\u0004\u0018\u00010\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0013R\u0011\u0010\u000b\u001a\u00020\f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\u0014R\u0013\u0010\n\u001a\u0004\u0018\u00010\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0013R\u0017\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0017\u00a8\u0006$"}, d2 = {"Lcom/carboncredit/app/ui/auditor/comparison/ComparisonUiState;", "", "allFacilities", "", "Lcom/carboncredit/app/data/models/Facility;", "selectedIds", "", "", "comparisonData", "Lcom/carboncredit/app/ui/auditor/dashboard/FacilityStatus;", "outlierWarning", "isLoading", "", "error", "(Ljava/util/List;Ljava/util/Set;Ljava/util/List;Ljava/lang/String;ZLjava/lang/String;)V", "getAllFacilities", "()Ljava/util/List;", "getComparisonData", "getError", "()Ljava/lang/String;", "()Z", "getOutlierWarning", "getSelectedIds", "()Ljava/util/Set;", "component1", "component2", "component3", "component4", "component5", "component6", "copy", "equals", "other", "hashCode", "", "toString", "app_debug"})
public final class ComparisonUiState {
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.carboncredit.app.data.models.Facility> allFacilities = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.Set<java.lang.String> selectedIds = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.carboncredit.app.ui.auditor.dashboard.FacilityStatus> comparisonData = null;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String outlierWarning = null;
    private final boolean isLoading = false;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String error = null;
    
    public ComparisonUiState(@org.jetbrains.annotations.NotNull()
    java.util.List<com.carboncredit.app.data.models.Facility> allFacilities, @org.jetbrains.annotations.NotNull()
    java.util.Set<java.lang.String> selectedIds, @org.jetbrains.annotations.NotNull()
    java.util.List<com.carboncredit.app.ui.auditor.dashboard.FacilityStatus> comparisonData, @org.jetbrains.annotations.Nullable()
    java.lang.String outlierWarning, boolean isLoading, @org.jetbrains.annotations.Nullable()
    java.lang.String error) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.carboncredit.app.data.models.Facility> getAllFacilities() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.Set<java.lang.String> getSelectedIds() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.carboncredit.app.ui.auditor.dashboard.FacilityStatus> getComparisonData() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getOutlierWarning() {
        return null;
    }
    
    public final boolean isLoading() {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getError() {
        return null;
    }
    
    public ComparisonUiState() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.carboncredit.app.data.models.Facility> component1() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.Set<java.lang.String> component2() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.carboncredit.app.ui.auditor.dashboard.FacilityStatus> component3() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component4() {
        return null;
    }
    
    public final boolean component5() {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component6() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.carboncredit.app.ui.auditor.comparison.ComparisonUiState copy(@org.jetbrains.annotations.NotNull()
    java.util.List<com.carboncredit.app.data.models.Facility> allFacilities, @org.jetbrains.annotations.NotNull()
    java.util.Set<java.lang.String> selectedIds, @org.jetbrains.annotations.NotNull()
    java.util.List<com.carboncredit.app.ui.auditor.dashboard.FacilityStatus> comparisonData, @org.jetbrains.annotations.Nullable()
    java.lang.String outlierWarning, boolean isLoading, @org.jetbrains.annotations.Nullable()
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