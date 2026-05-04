package com.carboncredit.app.ui.auditor.facility

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carboncredit.app.core.security.TokenManager
import com.carboncredit.app.core.utils.DateUtils
import com.carboncredit.app.data.models.*
import com.carboncredit.app.data.repository.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class FacilityDetailUiState(
    val facility: Facility? = null,
    val sensors: List<Sensor> = emptyList(),
    val anomalies: List<AnomalyEvent> = emptyList(),
    val credits: List<CarbonCredit> = emptyList(),
    val readings: List<SensorReading> = emptyList(),
    val totalEmissions: Float = 0f,
    val compliance: String = "",
    val isLoading: Boolean = true,
    val error: String? = null
)

@HiltViewModel
class FacilityDetailViewModel @Inject constructor(
    private val facilityRepository: FacilityRepository,
    private val sensorRepository: SensorRepository,
    private val anomalyRepository: AnomalyRepository,
    private val creditRepository: CreditRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(FacilityDetailUiState())
    val uiState: StateFlow<FacilityDetailUiState> = _uiState

    fun loadFacility(facilityId: String) {
        viewModelScope.launch {
            _uiState.value = FacilityDetailUiState(isLoading = true)
            try {
                val facility = facilityRepository.getFacilityById(facilityId)
                val sensors = sensorRepository.getSensorsForFacility(facilityId)
                val monthStart = DateUtils.getDaysAgo(30)
                val anomalies = anomalyRepository.getAnomaliesForFacility(facilityId)
                val credits = creditRepository.getCreditsForFacility(facilityId)
                val readings = sensorRepository.getReadingsForFacility(facilityId, startTime = monthStart)
                val totalEm = readings.sumOf { it.co2Ppm.toDouble() }.toFloat() * 0.0001f
                val ratio = if (facility.baselineEmissions > 0) totalEm / facility.baselineEmissions else 1f
                val compliance = when {
                    ratio < 0.85f -> "COMPLIANT"
                    ratio <= 1.0f -> "WATCH"
                    else -> "NON-COMPLIANT"
                }
                _uiState.value = FacilityDetailUiState(facility = facility, sensors = sensors, anomalies = anomalies, credits = credits, readings = readings, totalEmissions = totalEm, compliance = compliance, isLoading = false)
            } catch (e: Exception) {
                _uiState.value = FacilityDetailUiState(isLoading = false, error = e.message)
            }
        }
    }
}
