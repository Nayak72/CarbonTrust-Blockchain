package com.carboncredit.app.ui.manager.credits

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carboncredit.app.core.security.TokenManager
import com.carboncredit.app.core.utils.DateUtils
import com.carboncredit.app.data.models.CarbonCredit
import com.carboncredit.app.data.repository.CreditRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CreditLedgerUiState(
    val credits: List<CarbonCredit> = emptyList(),
    val totalCredits: Float = 0f,
    val selectedFilter: String = "all",
    val isLoading: Boolean = true,
    val error: String? = null
)

@HiltViewModel
class CreditLedgerViewModel @Inject constructor(
    private val tokenManager: TokenManager,
    private val creditRepository: CreditRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreditLedgerUiState())
    val uiState: StateFlow<CreditLedgerUiState> = _uiState

    init { loadCredits("all") }

    fun selectFilter(filter: String) { loadCredits(filter) }

    private fun loadCredits(filter: String) {
        val facilityId = tokenManager.getFacilityId() ?: return
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, selectedFilter = filter)
            try {
                val startTime = when (filter) {
                    "month" -> DateUtils.getDaysAgo(30)
                    "year" -> DateUtils.getDaysAgo(365)
                    else -> null
                }
                val credits = creditRepository.getCreditsForFacility(facilityId, startTime = startTime)
                val total = credits.sumOf { it.creditsIssued.toDouble() }.toFloat()
                _uiState.value = CreditLedgerUiState(
                    credits = credits, totalCredits = total,
                    selectedFilter = filter, isLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false, error = e.message)
            }
        }
    }
}
