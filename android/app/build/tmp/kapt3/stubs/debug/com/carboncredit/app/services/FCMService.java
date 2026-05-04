package com.carboncredit.app.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import com.carboncredit.app.R;
import com.carboncredit.app.core.security.TokenManager;
import com.carboncredit.app.core.utils.Constants;
import com.carboncredit.app.data.repository.NotificationRepository;
import com.carboncredit.app.ui.manager.ManagerActivity;
import com.carboncredit.app.ui.auditor.AuditorActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import dagger.hilt.android.AndroidEntryPoint;
import kotlinx.coroutines.Dispatchers;
import javax.inject.Inject;

@dagger.hilt.android.AndroidEntryPoint()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0006\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0012H\u0016J\u0010\u0010\u0013\u001a\u00020\u00102\u0006\u0010\u0014\u001a\u00020\u0015H\u0016J*\u0010\u0016\u001a\u00020\u00102\u0006\u0010\u0017\u001a\u00020\u00152\u0006\u0010\u0018\u001a\u00020\u00152\u0006\u0010\u0019\u001a\u00020\u00152\b\u0010\u001a\u001a\u0004\u0018\u00010\u0015H\u0002R\u001e\u0010\u0003\u001a\u00020\u00048\u0006@\u0006X\u0087.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\u001e\u0010\t\u001a\u00020\n8\u0006@\u0006X\u0087.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u000e\u00a8\u0006\u001b"}, d2 = {"Lcom/carboncredit/app/services/FCMService;", "Lcom/google/firebase/messaging/FirebaseMessagingService;", "()V", "notificationRepository", "Lcom/carboncredit/app/data/repository/NotificationRepository;", "getNotificationRepository", "()Lcom/carboncredit/app/data/repository/NotificationRepository;", "setNotificationRepository", "(Lcom/carboncredit/app/data/repository/NotificationRepository;)V", "tokenManager", "Lcom/carboncredit/app/core/security/TokenManager;", "getTokenManager", "()Lcom/carboncredit/app/core/security/TokenManager;", "setTokenManager", "(Lcom/carboncredit/app/core/security/TokenManager;)V", "onMessageReceived", "", "message", "Lcom/google/firebase/messaging/RemoteMessage;", "onNewToken", "token", "", "showNotification", "title", "body", "type", "relatedId", "app_debug"})
public final class FCMService extends com.google.firebase.messaging.FirebaseMessagingService {
    @javax.inject.Inject()
    public com.carboncredit.app.data.repository.NotificationRepository notificationRepository;
    @javax.inject.Inject()
    public com.carboncredit.app.core.security.TokenManager tokenManager;
    
    public FCMService() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.carboncredit.app.data.repository.NotificationRepository getNotificationRepository() {
        return null;
    }
    
    public final void setNotificationRepository(@org.jetbrains.annotations.NotNull()
    com.carboncredit.app.data.repository.NotificationRepository p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.carboncredit.app.core.security.TokenManager getTokenManager() {
        return null;
    }
    
    public final void setTokenManager(@org.jetbrains.annotations.NotNull()
    com.carboncredit.app.core.security.TokenManager p0) {
    }
    
    @java.lang.Override()
    public void onMessageReceived(@org.jetbrains.annotations.NotNull()
    com.google.firebase.messaging.RemoteMessage message) {
    }
    
    @java.lang.Override()
    public void onNewToken(@org.jetbrains.annotations.NotNull()
    java.lang.String token) {
    }
    
    private final void showNotification(java.lang.String title, java.lang.String body, java.lang.String type, java.lang.String relatedId) {
    }
}