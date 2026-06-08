package com.carboncredit.app.ui.manager.notifications

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carboncredit.app.core.security.TokenManager
import com.carboncredit.app.data.models.Notification
import com.carboncredit.app.data.repository.NotificationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class NotificationsUiState(
    val notifications: List<Notification> = emptyList(),
    val selectedTab: String = "all",
    val isLoading: Boolean = true,
    val error: String? = null
)

@HiltViewModel
class NotificationsViewModel @Inject constructor(
    private val tokenManager: TokenManager,
    private val notificationRepository: NotificationRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(NotificationsUiState())
    val uiState: StateFlow<NotificationsUiState> = _uiState

    init { loadNotifications() }

    fun selectTab(tab: String) {
        _uiState.value = _uiState.value.copy(selectedTab = tab)
        loadNotifications(if (tab == "all") null else tab)
    }

    fun loadNotifications(type: String? = null) {
        val userId = tokenManager.getUserId() ?: return
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val notifs = notificationRepository.getNotifications(userId, type)
                _uiState.value = _uiState.value.copy(notifications = notifs, isLoading = false)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false, error = e.message)
            }
        }
    }

    fun markAllRead() {
        val userId = tokenManager.getUserId() ?: return
        viewModelScope.launch {
            try {
                notificationRepository.markAllAsRead(userId)
                loadNotifications()
            } catch (_: Exception) { }
        }
    }

    fun markRead(notifId: String) {
        viewModelScope.launch {
            try {
                notificationRepository.markAsRead(notifId)
                loadNotifications()
            } catch (_: Exception) { }
        }
    }
}
