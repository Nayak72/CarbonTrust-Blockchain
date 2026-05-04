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

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000B\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0007\u0018\u00002\u00020\u0001B\u0017\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\u0006\u0010\u000e\u001a\u00020\u000fJ\u000e\u0010\u0010\u001a\u00020\u000f2\u0006\u0010\u0011\u001a\u00020\u0012J\u000e\u0010\u0013\u001a\u00020\u000f2\u0006\u0010\u0014\u001a\u00020\u0015J\u0006\u0010\u0016\u001a\u00020\u000fR\u0014\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\n\u001a\b\u0012\u0004\u0012\u00020\t0\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\r\u00a8\u0006\u0017"}, d2 = {"Lcom/carboncredit/app/ui/auditor/verification/BlockchainVerificationViewModel;", "Landroidx/lifecycle/ViewModel;", "blockchainRepository", "Lcom/carboncredit/app/data/repository/BlockchainRepository;", "creditRepository", "Lcom/carboncredit/app/data/repository/CreditRepository;", "(Lcom/carboncredit/app/data/repository/BlockchainRepository;Lcom/carboncredit/app/data/repository/CreditRepository;)V", "_uiState", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/carboncredit/app/ui/auditor/verification/VerificationUiState;", "uiState", "Lkotlinx/coroutines/flow/StateFlow;", "getUiState", "()Lkotlinx/coroutines/flow/StateFlow;", "clearSelection", "", "search", "query", "", "selectCredit", "credit", "Lcom/carboncredit/app/data/models/CarbonCredit;", "verifyIntegrity", "app_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class BlockchainVerificationViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.carboncredit.app.data.repository.BlockchainRepository blockchainRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.carboncredit.app.data.repository.CreditRepository creditRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.carboncredit.app.ui.auditor.verification.VerificationUiState> _uiState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.carboncredit.app.ui.auditor.verification.VerificationUiState> uiState = null;
    
    @javax.inject.Inject()
    public BlockchainVerificationViewModel(@org.jetbrains.annotations.NotNull()
    com.carboncredit.app.data.repository.BlockchainRepository blockchainRepository, @org.jetbrains.annotations.NotNull()
    com.carboncredit.app.data.repository.CreditRepository creditRepository) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.carboncredit.app.ui.auditor.verification.VerificationUiState> getUiState() {
        return null;
    }
    
    public final void search(@org.jetbrains.annotations.NotNull()
    java.lang.String query) {
    }
    
    public final void selectCredit(@org.jetbrains.annotations.NotNull()
    com.carboncredit.app.data.models.CarbonCredit credit) {
    }
    
    public final void verifyIntegrity() {
    }
    
    public final void clearSelection() {
    }
}