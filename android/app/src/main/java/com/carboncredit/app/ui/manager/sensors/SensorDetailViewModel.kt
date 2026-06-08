package com.carboncredit.app.ui.manager.sensors

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carboncredit.app.core.utils.DateUtils
import com.carboncredit.app.data.models.Sensor
import com.carboncredit.app.data.models.SensorReading
import com.carboncredit.app.data.repository.SensorRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SensorDetailUiState(
    val sensor: Sensor? = null,
    val readings: List<SensorReading> = emptyList(),
    val selectedPeriod: String = "today",
    val minPpm: Float = 0f,
    val maxPpm: Float = 0f,
    val avgPpm: Float = 0f,
    val anomalyCount: Int = 0,
    val isLoading: Boolean = true,
    val error: String? = null
)

@HiltViewModel
class SensorDetailViewModel @Inject constructor(
    private val sensorRepository: SensorRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SensorDetailUiState())
    val uiState: StateFlow<SensorDetailUiState> = _uiState

    fun loadSensor(sensorId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val sensor = sensorRepository.getSensorById(sensorId)
                _uiState.value = _uiState.value.copy(sensor = sensor)
                loadReadings(sensorId, "today")
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false, error = e.message)
            }
        }
    }

    fun selectPeriod(sensorId: String, period: String) {
        loadReadings(sensorId, period)
    }

    private fun loadReadings(sensorId: String, period: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, selectedPeriod = period)
            try {
                val startTime = when (period) {
                    "today" -> DateUtils.getStartOfToday()
                    "7days" -> DateUtils.getDaysAgo(7)
                    "30days" -> DateUtils.getDaysAgo(30)
                    else -> null
                }
                val readings = sensorRepository.getReadingsForSensor(sensorId, startTime = startTime)
                val ppmValues = readings.map { it.co2Ppm }
                _uiState.value = _uiState.value.copy(
                    readings = readings,
                    minPpm = ppmValues.minOrNull() ?: 0f,
                    maxPpm = ppmValues.maxOrNull() ?: 0f,
                    avgPpm = if (ppmValues.isNotEmpty()) ppmValues.average().toFloat() else 0f,
                    anomalyCount = readings.count { it.isAnomaly },
                    isLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false, error = e.message)
            }
        }
    }

    fun generateCsv(): String {
        val sb = StringBuilder("Timestamp,CO2 (ppm),Temperature (°C),Humidity (%),Anomaly\n")
        _uiState.value.readings.forEach { r ->
            sb.appendLine("${r.timestamp},${r.co2Ppm},${r.temperature},${r.humidity},${r.isAnomaly}")
        }
        return sb.toString()
    }
}
