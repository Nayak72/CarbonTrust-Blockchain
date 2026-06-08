package com.carboncredit.app.ui.auditor.reports

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carboncredit.app.core.network.ApiService
import com.carboncredit.app.core.security.TokenManager
import com.carboncredit.app.core.utils.PdfGenerator
import com.carboncredit.app.data.models.*
import com.carboncredit.app.data.repository.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

data class AuditReportUiState(
    val facilities: List<Facility> = emptyList(),
    val selectedFacilityId: String? = null,
    val creditsForSelectedFacility: List<CarbonCredit> = emptyList(),
    val selectedCreditId: String? = null,
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
    private val creditRepository: CreditRepository,
    private val apiService: ApiService
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
        _uiState.value = _uiState.value.copy(
            selectedFacilityId = id, 
            reportReady = false, 
            creditsForSelectedFacility = emptyList(),
            selectedCreditId = null
        )
        // Fetch credits for this facility
        viewModelScope.launch {
            try {
                val credits = creditRepository.getCreditsForFacility(id)
                _uiState.value = _uiState.value.copy(creditsForSelectedFacility = credits)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }

    fun selectCredit(id: String) {
        _uiState.value = _uiState.value.copy(selectedCreditId = id, reportReady = false)
    }

    fun generateReport() {
        val facilityId = _uiState.value.selectedFacilityId ?: return
        val creditId = _uiState.value.selectedCreditId ?: return
        val credit = _uiState.value.creditsForSelectedFacility.find { it.id == creditId } ?: return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isGenerating = true, error = null)
            try {
                val facility = facilityRepository.getFacilityById(facilityId)
                
                // Fetch IPFS content
                val ipfsResponse = apiService.fetchIPFSReport(credit.ipfsCid)
                val ipfsJson = JSONObject(ipfsResponse.content)
                val emissionData = ipfsJson.optJSONObject("emission_data")
                val methodology = ipfsJson.optJSONObject("methodology")

                val summary = buildString {
                    appendLine("===========================================")
                    appendLine("      CARBON CREDIT AUDIT REPORT")
                    appendLine("===========================================")
                    appendLine()
                    appendLine("AUDITOR DETAILS")
                    appendLine("---------------")
                    appendLine("Name: ${tokenManager.getUserName()}")
                    appendLine("Role: Independent Certified Auditor")
                    appendLine()
                    appendLine("FACILITY DETAILS")
                    appendLine("----------------")
                    appendLine("Facility: ${facility.name} (${facility.companyName})")
                    appendLine("Location: ${facility.location ?: "N/A"}")
                    appendLine("Industry: ${facility.industryType ?: "N/A"}")
                    appendLine("Baseline: ${facility.baselineEmissions} t/month")
                    appendLine()
                    appendLine("ISSUANCE DETAILS")
                    appendLine("----------------")
                    appendLine("Credit ID: ${credit.id.take(8)}...")
                    appendLine("IPFS CID: ${credit.ipfsCid}")
                    if (emissionData != null) {
                        appendLine("Actual Emissions: ${emissionData.optDouble("actual_emissions_tonnes", 0.0)} t")
                        appendLine("Baseline Used: ${emissionData.optDouble("baseline_tonnes", 0.0)} t")
                        appendLine("Reduction: ${emissionData.optDouble("emission_reduction_tonnes", 0.0)} t")
                        appendLine("Quality Factor: ${emissionData.optDouble("quality_factor", 1.0)}")
                        appendLine("Credits Issued (Adj): ${emissionData.optDouble("credits_adj", credit.creditsIssued.toDouble())}")
                        appendLine("Sensors Active: ${emissionData.optInt("reading_count", 0)} readings recorded")
                        appendLine("Anomalies: ${emissionData.optInt("anomaly_count", 0)} during period")
                    } else {
                        appendLine("Credits Issued: ${credit.creditsIssued}")
                    }
                    appendLine()
                    appendLine("METHODOLOGY & PARAMETERS")
                    appendLine("------------------------")
                    if (methodology != null) {
                        appendLine("Standard: ${methodology.optString("credit_standard", "N/A")}")
                        appendLine("Calculation: ${methodology.optString("calculation_method", "N/A")}")
                        appendLine("Alpha Calibration: ${methodology.optDouble("alpha_calibration", 0.0)}")
                    }
                    appendLine()
                    appendLine("IPFS Verification hash:")
                    appendLine(ipfsJson.optString("report_hash", "Pending On-Chain Verification"))
                    appendLine("===========================================")
                    val rawDate = ipfsJson.optString("generated_at", "")
                    val formattedDate = if (rawDate.length >= 19) {
                        try {
                            val parser = java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", java.util.Locale.getDefault())
                            val formatter = java.text.SimpleDateFormat("MMM dd, yyyy 'at' hh:mm a", java.util.Locale.getDefault())
                            val dateObj = parser.parse(rawDate.substring(0, 19))
                            if (dateObj != null) formatter.format(dateObj) else rawDate
                        } catch(e: Exception) {
                            rawDate
                        }
                    } else rawDate
                    appendLine("Generated At: $formattedDate")
                }
                
                generatedPdfContent = summary
                _uiState.value = _uiState.value.copy(isGenerating = false, reportReady = true, reportSummary = summary)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isGenerating = false, error = e.message)
            }
        }
    }

    fun getPdfUri(context: Context): Uri? {
        return PdfGenerator.generatePdf(context, generatedPdfContent)
    }
}
