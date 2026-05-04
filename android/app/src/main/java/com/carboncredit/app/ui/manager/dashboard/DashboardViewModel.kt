package com.carboncredit.app.ui.manager.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carboncredit.app.core.security.TokenManager
import com.carboncredit.app.core.utils.Constants
import com.carboncredit.app.core.utils.DateUtils
import com.carboncredit.app.core.utils.Resource
import com.carboncredit.app.data.models.AnomalyEvent
import com.carboncredit.app.data.models.Facility
import com.carboncredit.app.data.models.Sensor
import com.carboncredit.app.data.models.SensorReading
import com.carboncredit.app.data.repository.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DashboardUiState(
    val facility: Facility? = null,
    val latestReading: SensorReading? = null,
    val sensors: List<Sensor> = emptyList(),
    val todayEmissions: Float = 0f,
    val baselineEmissions: Float = 0f,
    val creditsToday: Float = 0f,
    val creditsMonth: Float = 0f,
    val recentAnomalies: List<AnomalyEvent> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
)

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val tokenManager: TokenManager,
    private val facilityRepository: FacilityRepository,
    private val sensorRepository: SensorRepository,
    private val creditRepository: CreditRepository,
    private val anomalyRepository: AnomalyRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState

    init {
        if (tokenManager.getRole() != Constants.ROLE_MANAGER) {
            _uiState.value = DashboardUiState(error = "Unauthorised access")
        } else {
            loadDashboard()
        }
    }

    fun loadDashboard() {
        val facilityId = tokenManager.getFacilityId() ?: return
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val facility = facilityRepository.getFacilityById(facilityId)
                val sensors = sensorRepository.getSensorsForFacility(facilityId)
                val latestReading = sensorRepository.getLatestReading(facilityId)
                val recentAnomalies = anomalyRepository.getRecentAnomalies(facilityId, 3)

                // Today's credits
                val todayStart = DateUtils.getStartOfToday()
                val todayCredits = creditRepository.getCreditsForFacility(facilityId, startTime = todayStart)
                val creditsToday = todayCredits.sumOf { it.creditsIssued.toDouble() }.toFloat()

                // This month's credits
                val monthStart = DateUtils.getDaysAgo(30)
                val monthCredits = creditRepository.getCreditsForFacility(facilityId, startTime = monthStart)
                val creditsMonth = monthCredits.sumOf { it.creditsIssued.toDouble() }.toFloat()

                // Today's readings for emissions estimate
                val todayReadings = sensorRepository.getReadingsForFacility(facilityId, startTime = todayStart)
                val todayEmissions = if (todayReadings.isNotEmpty()) {
                    todayReadings.sumOf { it.co2Ppm.toDouble() }.toFloat() / todayReadings.size * 0.001f
                } else 0f

                _uiState.value = DashboardUiState(
                    facility = facility,
                    latestReading = latestReading,
                    sensors = sensors,
                    todayEmissions = todayEmissions,
                    baselineEmissions = facility.baselineEmissions,
                    creditsToday = creditsToday,
                    creditsMonth = creditsMonth,
                    recentAnomalies = recentAnomalies,
                    isLoading = false
                )

                // Start realtime
                startRealtimeUpdates(facilityId)
            } catch (e: Exception) {
                _uiState.value = DashboardUiState(isLoading = false, error = e.message)
            }
        }
    }

    private fun startRealtimeUpdates(facilityId: String) {
        viewModelScope.launch {
            try {
                sensorRepository.subscribeChannel(facilityId)
                sensorRepository.subscribeToReadings(facilityId).collect { reading ->
                    _uiState.value = _uiState.value.copy(latestReading = reading)
                }
            } catch (_: Exception) { }
        }
    }

    override fun onCleared() {
        super.onCleared()
        val facilityId = tokenManager.getFacilityId() ?: return
        viewModelScope.launch {
            sensorRepository.unsubscribeChannel(facilityId)
        }
    }
}
