package com.carboncredit.app.ui.manager.anomalies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carboncredit.app.core.security.TokenManager
import com.carboncredit.app.core.utils.DateUtils
import com.carboncredit.app.data.models.AnomalyEvent
import com.carboncredit.app.data.repository.AnomalyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AnomalyLogUiState(
    val anomalies: List<AnomalyEvent> = emptyList(),
    val selectedType: String? = null,
    val isLoading: Boolean = true,
    val error: String? = null,
    val acknowledging: String? = null
)

@HiltViewModel
class AnomalyLogViewModel @Inject constructor(
    private val tokenManager: TokenManager,
    private val anomalyRepository: AnomalyRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AnomalyLogUiState())
    val uiState: StateFlow<AnomalyLogUiState> = _uiState

    init { loadAnomalies() }

    fun filterByType(type: String?) {
        loadAnomalies(type)
    }

    fun loadAnomalies(type: String? = _uiState.value.selectedType) {
        val facilityId = tokenManager.getFacilityId() ?: return
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, selectedType = type)
            try {
                val anomalies = anomalyRepository.getAnomaliesForFacility(facilityId, anomalyType = type)
                _uiState.value = _uiState.value.copy(anomalies = anomalies, isLoading = false)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false, error = e.message)
            }
        }
    }

    fun acknowledgeAnomaly(anomalyId: String, note: String?) {
        val userId = tokenManager.getUserId() ?: return
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(acknowledging = anomalyId)
            try {
                anomalyRepository.acknowledgeAnomaly(anomalyId, userId, note)
                loadAnomalies()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message, acknowledging = null)
            }
        }
    }
}
