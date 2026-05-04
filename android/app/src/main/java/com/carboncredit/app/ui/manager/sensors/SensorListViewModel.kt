package com.carboncredit.app.ui.manager.sensors

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carboncredit.app.core.security.TokenManager
import com.carboncredit.app.core.utils.Constants
import com.carboncredit.app.data.models.Sensor
import com.carboncredit.app.data.models.SensorReading
import com.carboncredit.app.data.repository.SensorRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SensorListUiState(
    val sensors: List<Pair<Sensor, SensorReading?>> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
)

@HiltViewModel
class SensorListViewModel @Inject constructor(
    private val tokenManager: TokenManager,
    private val sensorRepository: SensorRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SensorListUiState())
    val uiState: StateFlow<SensorListUiState> = _uiState

    init { loadSensors() }

    fun loadSensors() {
        val facilityId = tokenManager.getFacilityId() ?: return
        viewModelScope.launch {
            _uiState.value = SensorListUiState(isLoading = true)
            try {
                val sensors = sensorRepository.getSensorsForFacility(facilityId)
                val sensorWithReadings = sensors.map { sensor ->
                    val latest = sensorRepository.getLatestReadingForSensor(sensor.id)
                    sensor to latest
                }
                _uiState.value = SensorListUiState(sensors = sensorWithReadings, isLoading = false)
            } catch (e: Exception) {
                _uiState.value = SensorListUiState(isLoading = false, error = e.message)
            }
        }
    }
}
