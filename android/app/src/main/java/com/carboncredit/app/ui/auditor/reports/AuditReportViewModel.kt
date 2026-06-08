package com.carboncredit.app.ui.auditor.reports

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carboncredit.app.core.security.TokenManager
import com.carboncredit.app.data.models.*
import com.carboncredit.app.data.repository.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AuditReportUiState(
    val facilities: List<Facility> = emptyList(),
    val selectedFacilityId: String? = null,
    val isGenerating: Boolean = false,
    val reportReady: Boolean = false,
    val reportSummary: String = "",
    val error: String? = null,
    val isLoading: Boolean = true
)

@HiltViewModel
class AuditReportViewModel @Inject constructor(
    private val tokenManager: TokenManager,
    private val facilityRepository: FacilityRepository,
    private val sensorRepository: SensorRepository,
    private val anomalyRepository: AnomalyRepository,
    private val creditRepository: CreditRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuditReportUiState())
    val uiState: StateFlow<AuditReportUiState> = _uiState
    private var generatedPdfContent: String = ""

    init { loadFacilities() }

    private fun loadFacilities() {
        val userId = tokenManager.getUserId() ?: return
        viewModelScope.launch {
            try {
                val facs = facilityRepository.getAssignedFacilities(userId)
                _uiState.value = AuditReportUiState(facilities = facs, isLoading = false)
            } catch (e: Exception) {
                _uiState.value = AuditReportUiState(isLoading = false, error = e.message)
            }
        }
    }

    fun selectFacility(id: String) {
        _uiState.value = _uiState.value.copy(selectedFacilityId = id, reportReady = false)
    }

    fun generateReport() {
        val facilityId = _uiState.value.selectedFacilityId ?: return
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isGenerating = true, error = null)
            try {
                val facility = facilityRepository.getFacilityById(facilityId)
                val sensors = sensorRepository.getSensorsForFacility(facilityId)
                val anomalies = anomalyRepository.getAnomaliesForFacility(facilityId)
                val credits = creditRepository.getCreditsForFacility(facilityId)
                val totalCredits = credits.sumOf { it.creditsIssued.toDouble() }.toFloat()
                val totalAnomalies = anomalies.size
                val ackedAnomalies = anomalies.count { it.isAcknowledged }

                val summary = buildString {
                    appendLine("CARBON CREDIT AUDIT REPORT")
                    appendLine("Auditor: ${tokenManager.getUserName()}")
                    appendLine("Facility: ${facility.name} (${facility.companyName})")
                    appendLine("Location: ${facility.location ?: "N/A"}")
                    appendLine("Baseline: ${facility.baselineEmissions} t/month")
                    appendLine()
                    appendLine("SENSORS: ${sensors.size} total, ${sensors.count { it.isActive }} active")
                    appendLine("ANOMALIES: $totalAnomalies total, $ackedAnomalies acknowledged")
                    appendLine("CREDITS ISSUED: $totalCredits credits across ${credits.size} issuances")
                }
                generatedPdfContent = summary
                _uiState.value = _uiState.value.copy(isGenerating = false, reportReady = true, reportSummary = summary)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isGenerating = false, error = e.message)
            }
        }
    }

    fun getPdfContent(): String = generatedPdfContent
}
