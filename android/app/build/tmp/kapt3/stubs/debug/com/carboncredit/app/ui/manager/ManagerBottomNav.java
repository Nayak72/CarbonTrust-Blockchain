package com.carboncredit.app.ui.manager;

import android.os.Bundle;
import androidx.activity.ComponentActivity;
import androidx.compose.material.icons.Icons;
import androidx.compose.material.icons.filled.*;
import androidx.compose.material3.*;
import androidx.compose.runtime.*;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.graphics.vector.ImageVector;
import com.carboncredit.app.ui.theme.*;
import dagger.hilt.android.AndroidEntryPoint;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\b6\u0018\u00002\u00020\u0001:\u0005\r\u000e\u000f\u0010\u0011B\u001f\b\u0004\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\u0002\u0010\u0007R\u0011\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u0011\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\u000b\u0082\u0001\u0005\u0012\u0013\u0014\u0015\u0016\u00a8\u0006\u0017"}, d2 = {"Lcom/carboncredit/app/ui/manager/ManagerBottomNav;", "", "route", "", "title", "icon", "Landroidx/compose/ui/graphics/vector/ImageVector;", "(Ljava/lang/String;Ljava/lang/String;Landroidx/compose/ui/graphics/vector/ImageVector;)V", "getIcon", "()Landroidx/compose/ui/graphics/vector/ImageVector;", "getRoute", "()Ljava/lang/String;", "getTitle", "Analytics", "Credits", "Dashboard", "More", "Sensors", "Lcom/carboncredit/app/ui/manager/ManagerBottomNav$Analytics;", "Lcom/carboncredit/app/ui/manager/ManagerBottomNav$Credits;", "Lcom/carboncredit/app/ui/manager/ManagerBottomNav$Dashboard;", "Lcom/carboncredit/app/ui/manager/ManagerBottomNav$More;", "Lcom/carboncredit/app/ui/manager/ManagerBottomNav$Sensors;", "app_debug"})
public abstract class ManagerBottomNav {
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String route = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String title = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.compose.ui.graphics.vector.ImageVector icon = null;
    
    private ManagerBottomNav(java.lang.String route, java.lang.String title, androidx.compose.ui.graphics.vector.ImageVector icon) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getRoute() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getTitle() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.compose.ui.graphics.vector.ImageVector getIcon() {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/carboncredit/app/ui/manager/ManagerBottomNav$Analytics;", "Lcom/carboncredit/app/ui/manager/ManagerBottomNav;", "()V", "app_debug"})
    public static final class Analytics extends com.carboncredit.app.ui.manager.ManagerBottomNav {
        @org.jetbrains.annotations.NotNull()
        public static final com.carboncredit.app.ui.manager.ManagerBottomNav.Analytics INSTANCE = null;
        
        private Analytics() {
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/carboncredit/app/ui/manager/ManagerBottomNav$Credits;", "Lcom/carboncredit/app/ui/manager/ManagerBottomNav;", "()V", "app_debug"})
    public static final class Credits extends com.carboncredit.app.ui.manager.ManagerBottomNav {
        @org.jetbrains.annotations.NotNull()
        public static final com.carboncredit.app.ui.manager.ManagerBottomNav.Credits INSTANCE = null;
        
        private Credits() {
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/carboncredit/app/ui/manager/ManagerBottomNav$Dashboard;", "Lcom/carboncredit/app/ui/manager/ManagerBottomNav;", "()V", "app_debug"})
    public static final class Dashboard extends com.carboncredit.app.ui.manager.ManagerBottomNav {
        @org.jetbrains.annotations.NotNull()
        public static final com.carboncredit.app.ui.manager.ManagerBottomNav.Dashboard INSTANCE = null;
        
        private Dashboard() {
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/carboncredit/app/ui/manager/ManagerBottomNav$More;", "Lcom/carboncredit/app/ui/manager/ManagerBottomNav;", "()V", "app_debug"})
    public static final class More extends com.carboncredit.app.ui.manager.ManagerBottomNav {
        @org.jetbrains.annotations.NotNull()
        public static final com.carboncredit.app.ui.manager.ManagerBottomNav.More INSTANCE = null;
        
        private More() {
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/carboncredit/app/ui/manager/ManagerBottomNav$Sensors;", "Lcom/carboncredit/app/ui/manager/ManagerBottomNav;", "()V", "app_debug"})
    public static final class Sensors extends com.carboncredit.app.ui.manager.ManagerBottomNav {
        @org.jetbrains.annotations.NotNull()
        public static final com.carboncredit.app.ui.manager.ManagerBottomNav.Sensors INSTANCE = null;
        
        private Sensors() {
        }
    }
}