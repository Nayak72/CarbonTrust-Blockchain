package com.carboncredit.app.ui.manager.notifications;

import androidx.lifecycle.ViewModel;
import com.carboncredit.app.core.security.TokenManager;
import com.carboncredit.app.data.models.Notification;
import com.carboncredit.app.data.repository.NotificationRepository;
import dagger.hilt.android.lifecycle.HiltViewModel;
import kotlinx.coroutines.flow.StateFlow;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0006\b\u0007\u0018\u00002\u00020\u0001B\u0017\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\u0012\u0010\u000e\u001a\u00020\u000f2\n\b\u0002\u0010\u0010\u001a\u0004\u0018\u00010\u0011J\u0006\u0010\u0012\u001a\u00020\u000fJ\u000e\u0010\u0013\u001a\u00020\u000f2\u0006\u0010\u0014\u001a\u00020\u0011J\u000e\u0010\u0015\u001a\u00020\u000f2\u0006\u0010\u0016\u001a\u00020\u0011R\u0014\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\n\u001a\b\u0012\u0004\u0012\u00020\t0\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\r\u00a8\u0006\u0017"}, d2 = {"Lcom/carboncredit/app/ui/manager/notifications/NotificationsViewModel;", "Landroidx/lifecycle/ViewModel;", "tokenManager", "Lcom/carboncredit/app/core/security/TokenManager;", "notificationRepository", "Lcom/carboncredit/app/data/repository/NotificationRepository;", "(Lcom/carboncredit/app/core/security/TokenManager;Lcom/carboncredit/app/data/repository/NotificationRepository;)V", "_uiState", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/carboncredit/app/ui/manager/notifications/NotificationsUiState;", "uiState", "Lkotlinx/coroutines/flow/StateFlow;", "getUiState", "()Lkotlinx/coroutines/flow/StateFlow;", "loadNotifications", "", "type", "", "markAllRead", "markRead", "notifId", "selectTab", "tab", "app_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class NotificationsViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.carboncredit.app.core.security.TokenManager tokenManager = null;
    @org.jetbrains.annotations.NotNull()
    private final com.carboncredit.app.data.repository.NotificationRepository notificationRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.carboncredit.app.ui.manager.notifications.NotificationsUiState> _uiState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.carboncredit.app.ui.manager.notifications.NotificationsUiState> uiState = null;
    
    @javax.inject.Inject()
    public NotificationsViewModel(@org.jetbrains.annotations.NotNull()
    com.carboncredit.app.core.security.TokenManager tokenManager, @org.jetbrains.annotations.NotNull()
    com.carboncredit.app.data.repository.NotificationRepository notificationRepository) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.carboncredit.app.ui.manager.notifications.NotificationsUiState> getUiState() {
        return null;
    }
    
    public final void selectTab(@org.jetbrains.annotations.NotNull()
    java.lang.String tab) {
    }
    
    public final void loadNotifications(@org.jetbrains.annotations.Nullable()
    java.lang.String type) {
    }
    
    public final void markAllRead() {
    }
    
    public final void markRead(@org.jetbrains.annotations.NotNull()
    java.lang.String notifId) {
    }
}