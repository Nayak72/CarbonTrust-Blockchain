package com.carboncredit.app.ui.manager.auditors

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carboncredit.app.core.network.AssignedAuditorResponse
import com.carboncredit.app.core.security.TokenManager
import com.carboncredit.app.data.models.UserProfile
import com.carboncredit.app.data.repository.AssignmentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AuditorsUiState(
    val isLoading: Boolean = false,
    val assignedAuditors: List<AssignedAuditorResponse> = emptyList(),
    val availableAuditors: List<UserProfile> = emptyList(),
    val isPickerVisible: Boolean = false,
    val isPickerLoading: Boolean = false,
    val successMessage: String? = null,
    val error: String? = null
)

@HiltViewModel
class AuditorsViewModel @Inject constructor(
    private val assignmentRepository: AssignmentRepository,
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuditorsUiState())
    val uiState: StateFlow<AuditorsUiState> = _uiState

    // facilityId resolved from encrypted token storage; may be overridden by caller
    private var facilityId: String = ""

    fun init(facilityIdOverride: String = "") {
        facilityId = facilityIdOverride.ifBlank { tokenManager.getFacilityId() ?: "" }
        if (facilityId.isNotBlank()) loadAssignedAuditors()
    }

    fun loadAssignedAuditors() {
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
        viewModelScope.launch {
            try {
                assignmentRepository.assignAuditor(auditorId, facilityId)
                _uiState.value = _uiState.value.copy(
                    isPickerVisible = false,
                    successMessage = "Auditor assigned successfully"
                )
                loadAssignedAuditors()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = "Failed to assign auditor: ${e.message}")
            }
        }
    }

    fun revokeAuditor(auditorId: String) {
        viewModelScope.launch {
            try {
                assignmentRepository.revokeAuditor(auditorId, facilityId)
                _uiState.value = _uiState.value.copy(successMessage = "Auditor access revoked")
                loadAssignedAuditors()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = "Failed to revoke auditor: ${e.message}")
            }
        }
    }

    fun clearMessages() {
        _uiState.value = _uiState.value.copy(successMessage = null, error = null)
    }
}
