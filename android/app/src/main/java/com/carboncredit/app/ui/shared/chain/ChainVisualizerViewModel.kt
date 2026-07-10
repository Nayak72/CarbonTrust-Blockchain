package com.carboncredit.app.ui.shared.chain

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carboncredit.app.core.security.TokenManager
import com.carboncredit.app.core.utils.Constants
import com.carboncredit.app.core.utils.DateUtils
import com.carboncredit.app.data.models.CarbonCredit
import com.carboncredit.app.data.repository.CreditRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ChainVisualizerUiState(
    val credits: List<CarbonCredit> = emptyList(),
    val selectedFilter: String = "all",
    val isLoading: Boolean = true,
    val error: String? = null
)

/**
 * ViewModel dedicated to ChainVisualizerScreen.
 *
 * Bug fixed: The old implementation reused CreditLedgerViewModel which called
 * tokenManager.getFacilityId() ?: return — silently returning null for Auditors
 * (who have no single facility_id) and keeping isLoading = true forever.
 *
 * Fix: Role-aware loading:
 *   - MANAGER  → load credits for their single facility_id
 *   - AUDITOR  → load credits for ALL facilities (getAllCredits)
 *   - fallback → load all credits so the screen never stays blank
 */
@HiltViewModel
class ChainVisualizerViewModel @Inject constructor(
    private val tokenManager: TokenManager,
    private val creditRepository: CreditRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChainVisualizerUiState())
    val uiState: StateFlow<ChainVisualizerUiState> = _uiState

    init {
        loadCredits("all")
    }

    fun selectFilter(filter: String) {
        loadCredits(filter)
    }

    private fun loadCredits(filter: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null, selectedFilter = filter)
            try {
                val role = tokenManager.getRole()
                val facilityId = tokenManager.getFacilityId()

                val startTime = when (filter) {
                    "month" -> DateUtils.getDaysAgo(30)
                    "year" -> DateUtils.getDaysAgo(365)
                    else -> null
                }

                val credits: List<CarbonCredit> = when {
                    // Manager with a facility — load only their facility's credits
                    role == Constants.ROLE_MANAGER && facilityId != null -> {
                        creditRepository.getCreditsForFacility(
                            facilityId = facilityId,
                            startTime = startTime
                        )
                    }
                    // Auditor (no single facility_id) — load all credits across all facilities
                    role == Constants.ROLE_AUDITOR -> {
                        creditRepository.getAllCredits()
                    }
                    // Admin or any other role — load everything
                    else -> {
                        creditRepository.getAllCredits()
                    }
                }

                _uiState.value = ChainVisualizerUiState(
                    credits = credits,
                    selectedFilter = filter,
                    isLoading = false,
                    error = null
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Failed to load blockchain data"
                )
            }
        }
    }
}
