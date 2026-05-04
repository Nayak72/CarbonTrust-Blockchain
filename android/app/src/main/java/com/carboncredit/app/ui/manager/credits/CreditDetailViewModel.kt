package com.carboncredit.app.ui.manager.credits

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carboncredit.app.data.models.CarbonCredit
import com.carboncredit.app.data.repository.CreditRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CreditDetailUiState(
    val credit: CarbonCredit? = null,
    val isLoading: Boolean = true,
    val error: String? = null
)

@HiltViewModel
class CreditDetailViewModel @Inject constructor(
    private val creditRepository: CreditRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreditDetailUiState())
    val uiState: StateFlow<CreditDetailUiState> = _uiState

    fun loadCredit(creditId: String) {
        viewModelScope.launch {
            _uiState.value = CreditDetailUiState(isLoading = true)
            try {
                val credit = creditRepository.getCreditById(creditId)
                _uiState.value = CreditDetailUiState(credit = credit, isLoading = false)
            } catch (e: Exception) {
                _uiState.value = CreditDetailUiState(isLoading = false, error = e.message)
            }
        }
    }
}
