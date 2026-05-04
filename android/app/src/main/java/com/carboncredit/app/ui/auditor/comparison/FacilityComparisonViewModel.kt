package com.carboncredit.app.ui.auditor.comparison

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carboncredit.app.core.security.TokenManager
import com.carboncredit.app.core.utils.DateUtils
import com.carboncredit.app.data.models.Facility
import com.carboncredit.app.data.repository.*
import com.carboncredit.app.ui.auditor.dashboard.FacilityStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ComparisonUiState(
    val allFacilities: List<Facility> = emptyList(),
    val selectedIds: Set<String> = emptySet(),
    val comparisonData: List<FacilityStatus> = emptyList(),
    val outlierWarning: String? = null,
    val isLoading: Boolean = true,
    val error: String? = null
)

@HiltViewModel
class FacilityComparisonViewModel @Inject constructor(
    private val tokenManager: TokenManager,
    private val facilityRepository: FacilityRepository,
    private val creditRepository: CreditRepository,
    private val anomalyRepository: AnomalyRepository,
    private val sensorRepository: SensorRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ComparisonUiState())
    val uiState: StateFlow<ComparisonUiState> = _uiState

    init { loadFacilities() }

    private fun loadFacilities() {
        val userId = tokenManager.getUserId() ?: return
        viewModelScope.launch {
            try {
                val facilities = facilityRepository.getAssignedFacilities(userId)
                _uiState.value = ComparisonUiState(allFacilities = facilities, isLoading = false)
            } catch (e: Exception) {
                _uiState.value = ComparisonUiState(isLoading = false, error = e.message)
            }
        }
    }

    fun toggleFacility(facilityId: String) {
        val current = _uiState.value.selectedIds.toMutableSet()
        if (current.contains(facilityId)) current.remove(facilityId)
        else if (current.size < 4) current.add(facilityId)
        _uiState.value = _uiState.value.copy(selectedIds = current)
    }

    fun compare() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val monthStart = DateUtils.getDaysAgo(30)
                val data = _uiState.value.selectedIds.mapNotNull { id ->
                    val fac = _uiState.value.allFacilities.find { it.id == id } ?: return@mapNotNull null
                    val credits = creditRepository.getCreditsForFacility(id, startTime = monthStart)
                    val mc = credits.sumOf { it.creditsIssued.toDouble() }.toFloat()
                    val unacked = anomalyRepository.getUnacknowledgedCount(id)
                    val readings = sensorRepository.getReadingsForFacility(id, startTime = monthStart)
                    val actual = readings.sumOf { it.co2Ppm.toDouble() }.toFloat() * 0.0001f
                    val ratio = if (fac.baselineEmissions > 0) actual / fac.baselineEmissions else 1f
                    val comp = when { ratio < 0.85f -> "COMPLIANT"; ratio <= 1.0f -> "WATCH"; else -> "NON-COMPLIANT" }
                    FacilityStatus(fac, comp, mc, unacked, actual)
                }
                val avgCredits = if (data.isNotEmpty()) data.map { it.monthCredits }.average() else 0.0
                val outlier = data.find { it.monthCredits > avgCredits * 1.5 && it.unacknowledgedAnomalies > 5 }
                val warning = outlier?.let { "⚠️ ${it.facility.name} has high credits but ${it.unacknowledgedAnomalies} unacknowledged anomalies" }
                _uiState.value = _uiState.value.copy(comparisonData = data, outlierWarning = warning, isLoading = false)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false, error = e.message)
            }
        }
    }
}
