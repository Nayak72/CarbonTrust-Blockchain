package com.carboncredit.app.ui.auditor.verification;

import androidx.lifecycle.ViewModel;
import com.carboncredit.app.data.models.CarbonCredit;
import com.carboncredit.app.data.repository.BlockchainRepository;
import com.carboncredit.app.data.repository.CreditRepository;
import com.carboncredit.app.data.repository.OnChainRecord;
import com.carboncredit.app.data.repository.VerificationResult;
import com.carboncredit.app.data.repository.VerificationState;
import dagger.hilt.android.lifecycle.HiltViewModel;
import kotlinx.coroutines.flow.StateFlow;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u001e\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\b\u0018\u00002\u00020\u0001Bo\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\u000e\b\u0002\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005\u0012\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\u0006\u0012\n\b\u0002\u0010\b\u001a\u0004\u0018\u00010\t\u0012\n\b\u0002\u0010\n\u001a\u0004\u0018\u00010\u000b\u0012\n\b\u0002\u0010\f\u001a\u0004\u0018\u00010\r\u0012\b\b\u0002\u0010\u000e\u001a\u00020\u000f\u0012\b\b\u0002\u0010\u0010\u001a\u00020\u000f\u0012\n\b\u0002\u0010\u0011\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\u0002\u0010\u0012J\t\u0010!\u001a\u00020\u0003H\u00c6\u0003J\u000f\u0010\"\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005H\u00c6\u0003J\u000b\u0010#\u001a\u0004\u0018\u00010\u0006H\u00c6\u0003J\u000b\u0010$\u001a\u0004\u0018\u00010\tH\u00c6\u0003J\u000b\u0010%\u001a\u0004\u0018\u00010\u000bH\u00c6\u0003J\u000b\u0010&\u001a\u0004\u0018\u00010\rH\u00c6\u0003J\t\u0010\'\u001a\u00020\u000fH\u00c6\u0003J\t\u0010(\u001a\u00020\u000fH\u00c6\u0003J\u000b\u0010)\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003Js\u0010*\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\u000e\b\u0002\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u00052\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\u00062\n\b\u0002\u0010\b\u001a\u0004\u0018\u00010\t2\n\b\u0002\u0010\n\u001a\u0004\u0018\u00010\u000b2\n\b\u0002\u0010\f\u001a\u0004\u0018\u00010\r2\b\b\u0002\u0010\u000e\u001a\u00020\u000f2\b\b\u0002\u0010\u0010\u001a\u00020\u000f2\n\b\u0002\u0010\u0011\u001a\u0004\u0018\u00010\u0003H\u00c6\u0001J\u0013\u0010+\u001a\u00020\u000f2\b\u0010,\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010-\u001a\u00020.H\u00d6\u0001J\t\u0010/\u001a\u00020\u0003H\u00d6\u0001R\u0013\u0010\u0011\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014R\u0011\u0010\u000e\u001a\u00020\u000f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u0015R\u0011\u0010\u0010\u001a\u00020\u000f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0015R\u0013\u0010\b\u001a\u0004\u0018\u00010\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0017R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0014R\u0017\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\u001aR\u0013\u0010\u0007\u001a\u0004\u0018\u00010\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u001cR\u0013\u0010\f\u001a\u0004\u0018\u00010\r\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001d\u0010\u001eR\u0013\u0010\n\u001a\u0004\u0018\u00010\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001f\u0010 \u00a8\u00060"}, d2 = {"Lcom/carboncredit/app/ui/auditor/verification/VerificationUiState;", "", "searchQuery", "", "searchResults", "", "Lcom/carboncredit/app/data/models/CarbonCredit;", "selectedCredit", "onChainRecord", "Lcom/carboncredit/app/data/repository/OnChainRecord;", "verificationState", "Lcom/carboncredit/app/data/repository/VerificationState;", "verificationResult", "Lcom/carboncredit/app/data/repository/VerificationResult;", "isSearching", "", "isVerifying", "error", "(Ljava/lang/String;Ljava/util/List;Lcom/carboncredit/app/data/models/CarbonCredit;Lcom/carboncredit/app/data/repository/OnChainRecord;Lcom/carboncredit/app/data/repository/VerificationState;Lcom/carboncredit/app/data/repository/VerificationResult;ZZLjava/lang/String;)V", "getError", "()Ljava/lang/String;", "()Z", "getOnChainRecord", "()Lcom/carboncredit/app/data/repository/OnChainRecord;", "getSearchQuery", "getSearchResults", "()Ljava/util/List;", "getSelectedCredit", "()Lcom/carboncredit/app/data/models/CarbonCredit;", "getVerificationResult", "()Lcom/carboncredit/app/data/repository/VerificationResult;", "getVerificationState", "()Lcom/carboncredit/app/data/repository/VerificationState;", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "equals", "other", "hashCode", "", "toString", "app_debug"})
public final class VerificationUiState {
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String searchQuery = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.carboncredit.app.data.models.CarbonCredit> searchResults = null;
    @org.jetbrains.annotations.Nullable()
    private final com.carboncredit.app.data.models.CarbonCredit selectedCredit = null;
    @org.jetbrains.annotations.Nullable()
    private final com.carboncredit.app.data.repository.OnChainRecord onChainRecord = null;
    @org.jetbrains.annotations.Nullable()
    private final com.carboncredit.app.data.repository.VerificationState verificationState = null;
    @org.jetbrains.annotations.Nullable()
    private final com.carboncredit.app.data.repository.VerificationResult verificationResult = null;
    private final boolean isSearching = false;
    private final boolean isVerifying = false;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String error = null;
    
    public VerificationUiState(@org.jetbrains.annotations.NotNull()
    java.lang.String searchQuery, @org.jetbrains.annotations.NotNull()
    java.util.List<com.carboncredit.app.data.models.CarbonCredit> searchResults, @org.jetbrains.annotations.Nullable()
    com.carboncredit.app.data.models.CarbonCredit selectedCredit, @org.jetbrains.annotations.Nullable()
    com.carboncredit.app.data.repository.OnChainRecord onChainRecord, @org.jetbrains.annotations.Nullable()
    com.carboncredit.app.data.repository.VerificationState verificationState, @org.jetbrains.annotations.Nullable()
    com.carboncredit.app.data.repository.VerificationResult verificationResult, boolean isSearching, boolean isVerifying, @org.jetbrains.annotations.Nullable()
    java.lang.String error) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getSearchQuery() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.carboncredit.app.data.models.CarbonCredit> getSearchResults() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.carboncredit.app.data.models.CarbonCredit getSelectedCredit() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.carboncredit.app.data.repository.OnChainRecord getOnChainRecord() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.carboncredit.app.data.repository.VerificationState getVerificationState() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.carboncredit.app.data.repository.VerificationResult getVerificationResult() {
        return null;
    }
    
    public final boolean isSearching() {
        return false;
    }
    
    public final boolean isVerifying() {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getError() {
        return null;
    }
    
    public VerificationUiState() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component1() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.carboncredit.app.data.models.CarbonCredit> component2() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.carboncredit.app.data.models.CarbonCredit component3() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.carboncredit.app.data.repository.OnChainRecord component4() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.carboncredit.app.data.repository.VerificationState component5() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.carboncredit.app.data.repository.VerificationResult component6() {
        return null;
    }
    
    public final boolean component7() {
        return false;
    }
    
    public final boolean component8() {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component9() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.carboncredit.app.ui.auditor.verification.VerificationUiState copy(@org.jetbrains.annotations.NotNull()
    java.lang.String searchQuery, @org.jetbrains.annotations.NotNull()
    java.util.List<com.carboncredit.app.data.models.CarbonCredit> searchResults, @org.jetbrains.annotations.Nullable()
    com.carboncredit.app.data.models.CarbonCredit selectedCredit, @org.jetbrains.annotations.Nullable()
    com.carboncredit.app.data.repository.OnChainRecord onChainRecord, @org.jetbrains.annotations.Nullable()
    com.carboncredit.app.data.repository.VerificationState verificationState, @org.jetbrains.annotations.Nullable()
    com.carboncredit.app.data.repository.VerificationResult verificationResult, boolean isSearching, boolean isVerifying, @org.jetbrains.annotations.Nullable()
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