package com.carboncredit.app.ui.admin.auditors

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carboncredit.app.core.network.AssignedAuditorResponse
import com.carboncredit.app.data.models.Facility
import com.carboncredit.app.data.models.UserProfile
import com.carboncredit.app.data.repository.AssignmentRepository
import com.carboncredit.app.data.repository.FacilityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AdminAuditorsUiState(
    val isLoading: Boolean = false,
    val facilities: List<Facility> = emptyList(),
    val selectedFacility: Facility? = null,
    val assignedAuditors: List<AssignedAuditorResponse> = emptyList(),
    val availableAuditors: List<UserProfile> = emptyList(),
    val isPickerVisible: Boolean = false,
    val isPickerLoading: Boolean = false,
    val successMessage: String? = null,
    val error: String? = null
)

@HiltViewModel
class AdminAuditorsViewModel @Inject constructor(
    private val assignmentRepository: AssignmentRepository,
    private val facilityRepository: FacilityRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AdminAuditorsUiState())
    val uiState: StateFlow<AdminAuditorsUiState> = _uiState

    init {
        loadFacilities()
    }

    private fun loadFacilities() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val facilitiesList = facilityRepository.getAllFacilities()
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    facilities = facilitiesList,
                    selectedFacility = facilitiesList.firstOrNull()
                )
                if (facilitiesList.isNotEmpty()) {
                    loadAssignedAuditors(facilitiesList.first().id)
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false, error = e.message)
            }
        }
    }

    fun selectFacility(facility: Facility) {
        _uiState.value = _uiState.value.copy(selectedFacility = facility, assignedAuditors = emptyList())
        loadAssignedAuditors(facility.id)
    }

    fun loadAssignedAuditors(facilityId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val auditors = assignmentRepository.getFacilityAuditors(facilityId)
                _uiState.value = _uiState.value.copy(isLoading = false, assignedAuditors = auditors)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false, error = e.message)
            }
        }
    }

    fun openAuditorPicker() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isPickerVisible = true, isPickerLoading = true)
            try {
                val available = assignmentRepository.getAvailableAuditors()
                _uiState.value = _uiState.value.copy(isPickerLoading = false, availableAuditors = available)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isPickerLoading = false, error = e.message, isPickerVisible = false)
            }
        }
    }

    fun dismissPicker() {
        _uiState.value = _uiState.value.copy(isPickerVisible = false)
    }

    fun assignAuditor(auditorId: String) {
        val facilityId = _uiState.value.selectedFacility?.id ?: return
        viewModelScope.launch {
            try {
                assignmentRepository.assignAuditor(auditorId, facilityId)
                _uiState.value = _uiState.value.copy(
                    isPickerVisible = false,
                    successMessage = "Auditor assigned successfully"
                )
                loadAssignedAuditors(facilityId)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = "Failed to assign auditor: ${e.message}")
            }
        }
    }

    fun revokeAuditor(auditorId: String) {
        val facilityId = _uiState.value.selectedFacility?.id ?: return
        viewModelScope.launch {
            try {
                assignmentRepository.revokeAuditor(auditorId, facilityId)
                _uiState.value = _uiState.value.copy(successMessage = "Auditor access revoked")
                loadAssignedAuditors(facilityId)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = "Failed to revoke auditor: ${e.message}")
            }
        }
    }

    fun clearMessages() {
        _uiState.value = _uiState.value.copy(successMessage = null, error = null)
    }
}
