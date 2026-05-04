package com.carboncredit.app.ui.auditor.verification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carboncredit.app.data.models.CarbonCredit
import com.carboncredit.app.data.repository.BlockchainRepository
import com.carboncredit.app.data.repository.CreditRepository
import com.carboncredit.app.data.repository.OnChainRecord
import com.carboncredit.app.data.repository.VerificationResult
import com.carboncredit.app.data.repository.VerificationState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class VerificationUiState(
    val searchQuery: String = "",
    val searchResults: List<CarbonCredit> = emptyList(),
    val selectedCredit: CarbonCredit? = null,
    val onChainRecord: OnChainRecord? = null,
    val verificationState: VerificationState? = null,
    val verificationResult: VerificationResult? = null,
    val isSearching: Boolean = false,
    val isVerifying: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class BlockchainVerificationViewModel @Inject constructor(
    private val blockchainRepository: BlockchainRepository,
    private val creditRepository: CreditRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(VerificationUiState())
    val uiState: StateFlow<VerificationUiState> = _uiState

    fun search(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
        if (query.length < 3) return
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSearching = true, error = null)
            try {
                // Try as TX hash first
                val byTx = creditRepository.getCreditByTxHash(query)
                if (byTx != null) {
                    _uiState.value = _uiState.value.copy(searchResults = listOf(byTx), isSearching = false)
                    return@launch
                }
                // Try as credit ID
                try {
                    val byId = creditRepository.getCreditById(query)
                    _uiState.value = _uiState.value.copy(searchResults = listOf(byId), isSearching = false)
                } catch (_: Exception) {
                    _uiState.value = _uiState.value.copy(searchResults = emptyList(), isSearching = false)
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isSearching = false, error = e.message)
            }
        }
    }

    fun selectCredit(credit: CarbonCredit) {
        _uiState.value = _uiState.value.copy(selectedCredit = credit, verificationResult = null, verificationState = null)
        // Load on-chain record
        viewModelScope.launch {
            try {
                val record = blockchainRepository.getTransactionReceipt(credit.txHash)
                _uiState.value = _uiState.value.copy(onChainRecord = record)
            } catch (_: Exception) { }
        }
    }

    fun verifyIntegrity() {
        val credit = _uiState.value.selectedCredit ?: return
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isVerifying = true, verificationResult = null)
            val result = blockchainRepository.verifyReportIntegrity(
                txHash = credit.txHash,
                ipfsCid = credit.ipfsCid,
                onStep = { step -> _uiState.value = _uiState.value.copy(verificationState = step) }
            )
            _uiState.value = _uiState.value.copy(isVerifying = false, verificationResult = result)
        }
    }

    fun clearSelection() {
        _uiState.value = _uiState.value.copy(selectedCredit = null, onChainRecord = null, verificationState = null, verificationResult = null)
    }
}
