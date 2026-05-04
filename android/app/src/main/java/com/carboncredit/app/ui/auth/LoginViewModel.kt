package com.carboncredit.app.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carboncredit.app.core.utils.Resource
import com.carboncredit.app.data.models.UserProfile
import com.carboncredit.app.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _loginState = MutableStateFlow<Resource<UserProfile>?>(null)
    val loginState: StateFlow<Resource<UserProfile>?> = _loginState

    private val _signUpState = MutableStateFlow<Resource<UserProfile>?>(null)
    val signUpState: StateFlow<Resource<UserProfile>?> = _signUpState

    fun signIn(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _loginState.value = Resource.Error("Please enter email and password")
            return
        }
        viewModelScope.launch {
            _loginState.value = Resource.Loading()
            try {
                val profile = authRepository.signIn(email, password)
                _loginState.value = Resource.Success(profile)
            } catch (e: Exception) {
                _loginState.value = Resource.Error(e.message ?: "Login failed")
            }
        }
    }

    fun signUp(
        fullName: String,
        email: String,
        password: String,
        confirmPassword: String,
        role: String
    ) {
        if (fullName.isBlank() || email.isBlank() || password.isBlank()) {
            _signUpState.value = Resource.Error("Please fill in all fields")
            return
        }
        if (password != confirmPassword) {
            _signUpState.value = Resource.Error("Passwords do not match")
            return
        }
        if (password.length < 6) {
            _signUpState.value = Resource.Error("Password must be at least 6 characters")
            return
        }
        viewModelScope.launch {
            _signUpState.value = Resource.Loading()
            try {
                val profile = authRepository.signUp(fullName, email, password, role)
                _signUpState.value = Resource.Success(profile)
            } catch (e: Exception) {
                _signUpState.value = Resource.Error(e.message ?: "Sign up failed")
            }
        }
    }

    fun clearLoginState() {
        _loginState.value = null
    }

    fun clearSignUpState() {
        _signUpState.value = null
    }
}
