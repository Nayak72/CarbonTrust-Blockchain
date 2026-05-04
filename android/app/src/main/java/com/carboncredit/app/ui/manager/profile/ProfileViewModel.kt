package com.carboncredit.app.ui.manager.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carboncredit.app.core.security.TokenManager
import com.carboncredit.app.data.models.Facility
import com.carboncredit.app.data.models.UserProfile
import com.carboncredit.app.data.repository.AuthRepository
import com.carboncredit.app.data.repository.FacilityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProfileUiState(
    val profile: UserProfile? = null,
    val facility: Facility? = null,
    val isLoading: Boolean = true,
    val passwordUpdating: Boolean = false,
    val passwordSuccess: Boolean = false,
    val error: String? = null,
    val loggedOut: Boolean = false
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val tokenManager: TokenManager,
    private val authRepository: AuthRepository,
    private val facilityRepository: FacilityRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState

    init { loadProfile() }

    private fun loadProfile() {
        viewModelScope.launch {
            try {
                val profile = authRepository.getCurrentProfile()
                val facility = profile.facilityId?.let { facilityRepository.getFacilityById(it) }
                _uiState.value = ProfileUiState(profile = profile, facility = facility, isLoading = false)
            } catch (e: Exception) {
                _uiState.value = ProfileUiState(isLoading = false, error = e.message)
            }
        }
    }

    fun updatePassword(newPassword: String, confirmPassword: String) {
        if (newPassword != confirmPassword) {
            _uiState.value = _uiState.value.copy(error = "Passwords do not match")
            return
        }
        if (newPassword.length < 6) {
            _uiState.value = _uiState.value.copy(error = "Password must be at least 6 characters")
            return
        }
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(passwordUpdating = true, error = null)
            try {
                authRepository.updatePassword(newPassword)
                _uiState.value = _uiState.value.copy(passwordUpdating = false, passwordSuccess = true)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(passwordUpdating = false, error = e.message)
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.signOut()
            _uiState.value = _uiState.value.copy(loggedOut = true)
        }
    }
}
