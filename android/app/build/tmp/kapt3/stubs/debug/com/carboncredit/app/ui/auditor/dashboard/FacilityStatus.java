package com.carboncredit.app.ui.auditor.dashboard;

import androidx.lifecycle.ViewModel;
import com.carboncredit.app.core.security.TokenManager;
import com.carboncredit.app.core.utils.Constants;
import com.carboncredit.app.core.utils.DateUtils;
import com.carboncredit.app.data.models.Facility;
import com.carboncredit.app.data.repository.*;
import dagger.hilt.android.lifecycle.HiltViewModel;
import kotlinx.coroutines.flow.StateFlow;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0007\n\u0000\n\u0002\u0010\b\n\u0002\b\u0012\n\u0002\u0010\u000b\n\u0002\b\u0004\b\u0086\b\u0018\u00002\u00020\u0001B-\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u0007\u00a2\u0006\u0002\u0010\u000bJ\t\u0010\u0015\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0016\u001a\u00020\u0005H\u00c6\u0003J\t\u0010\u0017\u001a\u00020\u0007H\u00c6\u0003J\t\u0010\u0018\u001a\u00020\tH\u00c6\u0003J\t\u0010\u0019\u001a\u00020\u0007H\u00c6\u0003J;\u0010\u001a\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00072\b\b\u0002\u0010\b\u001a\u00020\t2\b\b\u0002\u0010\n\u001a\u00020\u0007H\u00c6\u0001J\u0013\u0010\u001b\u001a\u00020\u001c2\b\u0010\u001d\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u001e\u001a\u00020\tH\u00d6\u0001J\t\u0010\u001f\u001a\u00020\u0005H\u00d6\u0001R\u0011\u0010\n\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R\u0011\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\rR\u0011\u0010\b\u001a\u00020\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014\u00a8\u0006 "}, d2 = {"Lcom/carboncredit/app/ui/auditor/dashboard/FacilityStatus;", "", "facility", "Lcom/carboncredit/app/data/models/Facility;", "compliance", "", "monthCredits", "", "unacknowledgedAnomalies", "", "actualEmissions", "(Lcom/carboncredit/app/data/models/Facility;Ljava/lang/String;FIF)V", "getActualEmissions", "()F", "getCompliance", "()Ljava/lang/String;", "getFacility", "()Lcom/carboncredit/app/data/models/Facility;", "getMonthCredits", "getUnacknowledgedAnomalies", "()I", "component1", "component2", "component3", "component4", "component5", "copy", "equals", "", "other", "hashCode", "toString", "app_debug"})
public final class FacilityStatus {
    @org.jetbrains.annotations.NotNull()
    private final com.carboncredit.app.data.models.Facility facility = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String compliance = null;
    private final float monthCredits = 0.0F;
    private final int unacknowledgedAnomalies = 0;
    private final float actualEmissions = 0.0F;
    
    public FacilityStatus(@org.jetbrains.annotations.NotNull()
    com.carboncredit.app.data.models.Facility facility, @org.jetbrains.annotations.NotNull()
    java.lang.String compliance, float monthCredits, int unacknowledgedAnomalies, float actualEmissions) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.carboncredit.app.data.models.Facility getFacility() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getCompliance() {
        return null;
    }
    
    public final float getMonthCredits() {
        return 0.0F;
    }
    
    public final int getUnacknowledgedAnomalies() {
        return 0;
    }
    
    public final float getActualEmissions() {
        return 0.0F;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.carboncredit.app.data.models.Facility component1() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component2() {
        return null;
    }
    
    public final float component3() {
        return 0.0F;
    }
    
    public final int component4() {
        return 0;
    }
    
    public final float component5() {
        return 0.0F;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.carboncredit.app.ui.auditor.dashboard.FacilityStatus copy(@org.jetbrains.annotations.NotNull()
    com.carboncredit.app.data.models.Facility facility, @org.jetbrains.annotations.NotNull()
    java.lang.String compliance, float monthCredits, int unacknowledgedAnomalies, float actualEmissions) {
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