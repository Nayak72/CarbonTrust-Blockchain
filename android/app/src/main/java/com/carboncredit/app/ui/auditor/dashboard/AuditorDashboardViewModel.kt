package com.carboncredit.app.ui.auditor.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carboncredit.app.core.security.TokenManager
import com.carboncredit.app.core.utils.Constants
import com.carboncredit.app.core.utils.DateUtils
import com.carboncredit.app.data.models.Facility
import com.carboncredit.app.data.repository.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class FacilityStatus(
    val facility: Facility,
    val compliance: String,  // COMPLIANT, WATCH, NON-COMPLIANT
    val monthCredits: Float,
    val unacknowledgedAnomalies: Int,
    val actualEmissions: Float
)

data class AuditorDashboardUiState(
    val auditorName: String = "",
    val facilityStatuses: List<FacilityStatus> = emptyList(),
    val totalFacilities: Int = 0,
    val totalCreditsMonth: Float = 0f,
    val totalActiveAnomalies: Int = 0,
    val nonCompliantCount: Int = 0,
    val isLoading: Boolean = true,
    val error: String? = null
)

@HiltViewModel
class AuditorDashboardViewModel @Inject constructor(
    private val tokenManager: TokenManager,
    private val facilityRepository: FacilityRepository,
    private val creditRepository: CreditRepository,
    private val anomalyRepository: AnomalyRepository,
    private val sensorRepository: SensorRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuditorDashboardUiState())
    val uiState: StateFlow<AuditorDashboardUiState> = _uiState

    init {
        if (tokenManager.getRole() != Constants.ROLE_AUDITOR) {
            _uiState.value = AuditorDashboardUiState(error = "Unauthorised access")
        } else { loadDashboard() }
    }

    fun loadDashboard() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val facilities = facilityRepository.getAllFacilities()
                val monthStart = DateUtils.getDaysAgo(30)
                val statuses = facilities.map { facility ->
                    val credits = creditRepository.getCreditsForFacility(facility.id, startTime = monthStart)
                    val monthCredits = credits.sumOf { it.creditsIssued.toDouble() }.toFloat()
                    val unacked = anomalyRepository.getUnacknowledgedCount(facility.id)
                    val readings = sensorRepository.getReadingsForFacility(facility.id, startTime = monthStart)
                    val actual = readings.sumOf { it.co2Ppm.toDouble() }.toFloat() * 0.0001f
                    val ratio = if (facility.baselineEmissions > 0) actual / facility.baselineEmissions else 1f
                    val compliance = when {
                        ratio < Constants.COMPLIANCE_THRESHOLD -> "COMPLIANT"
                        ratio <= Constants.WATCH_THRESHOLD -> "WATCH"
                        else -> "NON-COMPLIANT"
                    }
                    FacilityStatus(facility, compliance, monthCredits, unacked, actual)
                }

                _uiState.value = AuditorDashboardUiState(
                    auditorName = tokenManager.getUserName() ?: "Auditor",
                    facilityStatuses = statuses,
                    totalFacilities = facilities.size,
                    totalCreditsMonth = statuses.sumOf { it.monthCredits.toDouble() }.toFloat(),
                    totalActiveAnomalies = statuses.sumOf { it.unacknowledgedAnomalies },
                    nonCompliantCount = statuses.count { it.compliance == "NON-COMPLIANT" },
                    isLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false, error = e.message)
            }
        }
    }
}
