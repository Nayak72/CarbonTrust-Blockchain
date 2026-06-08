package com.carboncredit.app.ui.manager.analytics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carboncredit.app.core.security.TokenManager
import com.carboncredit.app.core.utils.DateUtils
import com.carboncredit.app.data.models.SensorReading
import com.carboncredit.app.data.repository.CreditRepository
import com.carboncredit.app.data.repository.FacilityRepository
import com.carboncredit.app.data.repository.SensorRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AnalyticsUiState(
    val totalEmitted: Float = 0f,
    val emissionReduction: Float = 0f,
    val belowBaselinePct: Float = 0f,
    val creditsEarned: Float = 0f,
    val baseline: Float = 0f,
    val readings: List<SensorReading> = emptyList(),
    val selectedPeriod: String = "7days",
    val isLoading: Boolean = true,
    val error: String? = null
)

@HiltViewModel
class EmissionAnalyticsViewModel @Inject constructor(
    private val tokenManager: TokenManager,
    private val sensorRepository: SensorRepository,
    private val creditRepository: CreditRepository,
    private val facilityRepository: FacilityRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AnalyticsUiState())
    val uiState: StateFlow<AnalyticsUiState> = _uiState

    init { loadAnalytics("7days") }

    fun selectPeriod(period: String) { loadAnalytics(period) }

    private fun loadAnalytics(period: String) {
        val facilityId = tokenManager.getFacilityId() ?: return
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, selectedPeriod = period)
            try {
                val facility = facilityRepository.getFacilityById(facilityId)
                val startTime = when (period) {
                    "today" -> DateUtils.getStartOfToday()
                    "7days" -> DateUtils.getDaysAgo(7)
                    "30days" -> DateUtils.getDaysAgo(30)
                    else -> null
                }
                val readings = sensorRepository.getReadingsForFacility(facilityId, startTime = startTime)
                val credits = creditRepository.getCreditsForFacility(facilityId, startTime = startTime)
                val totalEmitted = readings.sumOf { it.co2Ppm.toDouble() }.toFloat() * 0.0001f
                val creditsEarned = credits.sumOf { it.creditsIssued.toDouble() }.toFloat()
                val reduction = facility.baselineEmissions - totalEmitted
                val pct = if (facility.baselineEmissions > 0) (reduction / facility.baselineEmissions * 100) else 0f

                _uiState.value = AnalyticsUiState(
                    totalEmitted = totalEmitted,
                    emissionReduction = if (reduction > 0) reduction else 0f,
                    belowBaselinePct = if (pct > 0) pct else 0f,
                    creditsEarned = creditsEarned,
                    baseline = facility.baselineEmissions,
                    readings = readings,
                    selectedPeriod = period,
                    isLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false, error = e.message)
            }
        }
    }
}
