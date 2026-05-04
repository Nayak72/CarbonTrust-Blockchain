package com.carboncredit.app.ui.splash;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import androidx.activity.ComponentActivity;
import androidx.compose.animation.core.*;
import androidx.compose.foundation.layout.*;
import androidx.compose.runtime.*;
import androidx.compose.ui.Alignment;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.text.font.FontWeight;
import com.carboncredit.app.core.security.TokenManager;
import com.carboncredit.app.core.utils.Constants;
import com.carboncredit.app.ui.auth.LoginActivity;
import com.carboncredit.app.ui.manager.ManagerActivity;
import com.carboncredit.app.ui.auditor.AuditorActivity;
import com.carboncredit.app.ui.theme.*;
import dagger.hilt.android.AndroidEntryPoint;
import javax.inject.Inject;

@dagger.hilt.android.AndroidEntryPoint()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\t\u001a\u00020\nH\u0002J\u0012\u0010\u000b\u001a\u00020\n2\b\u0010\f\u001a\u0004\u0018\u00010\rH\u0014R\u001e\u0010\u0003\u001a\u00020\u00048\u0006@\u0006X\u0087.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\b\u00a8\u0006\u000e"}, d2 = {"Lcom/carboncredit/app/ui/splash/SplashActivity;", "Landroidx/activity/ComponentActivity;", "()V", "tokenManager", "Lcom/carboncredit/app/core/security/TokenManager;", "getTokenManager", "()Lcom/carboncredit/app/core/security/TokenManager;", "setTokenManager", "(Lcom/carboncredit/app/core/security/TokenManager;)V", "navigateBasedOnRole", "", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "app_debug"})
@android.annotation.SuppressLint(value = {"CustomSplashScreen"})
public final class SplashActivity extends androidx.activity.ComponentActivity {
    @javax.inject.Inject()
    public com.carboncredit.app.core.security.TokenManager tokenManager;
    
    public SplashActivity() {
        super(0);
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.carboncredit.app.core.security.TokenManager getTokenManager() {
        return null;
    }
    
    public final void setTokenManager(@org.jetbrains.annotations.NotNull()
    com.carboncredit.app.core.security.TokenManager p0) {
    }
    
    @java.lang.Override()
    protected void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    private final void navigateBasedOnRole() {
    }
}