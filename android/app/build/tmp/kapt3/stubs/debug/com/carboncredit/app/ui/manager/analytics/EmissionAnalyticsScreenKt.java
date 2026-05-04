package com.carboncredit.app.ui.manager.analytics;

import androidx.compose.foundation.layout.*;
import androidx.compose.material.icons.Icons;
import androidx.compose.material.icons.filled.*;
import androidx.compose.material3.*;
import androidx.compose.runtime.*;
import androidx.compose.ui.Alignment;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.text.font.FontWeight;
import com.carboncredit.app.ui.components.*;
import com.carboncredit.app.ui.theme.*;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000F\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0007\n\u0002\b\u0005\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\u001a:\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00032\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nH\u0003\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u000b\u0010\f\u001a2\u0010\r\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u000e2\u0006\u0010\u0007\u001a\u00020\bH\u0003\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0010\u0010\u0011\u001a2\u0010\u0012\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u000e2\u0006\u0010\u0007\u001a\u00020\bH\u0003\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0016\u0010\u0017\u001a\u0012\u0010\u0018\u001a\u00020\u00012\b\b\u0002\u0010\u0019\u001a\u00020\u001aH\u0007\u001a\u0018\u0010\u001b\u001a\u00020\u00012\u0006\u0010\u001c\u001a\u00020\u000e2\u0006\u0010\u001d\u001a\u00020\u000eH\u0003\u001a\u0016\u0010\u001e\u001a\u00020\u00012\f\u0010\u001f\u001a\b\u0012\u0004\u0012\u00020!0 H\u0003\u0082\u0002\u0007\n\u0005\b\u00a1\u001e0\u0001\u00a8\u0006\""}, d2 = {"AnalyticsStat", "", "label", "", "value", "icon", "Landroidx/compose/ui/graphics/vector/ImageVector;", "color", "Landroidx/compose/ui/graphics/Color;", "modifier", "Landroidx/compose/ui/Modifier;", "AnalyticsStat-42QJj7c", "(Ljava/lang/String;Ljava/lang/String;Landroidx/compose/ui/graphics/vector/ImageVector;JLandroidx/compose/ui/Modifier;)V", "BarRow", "", "max", "BarRow-g2O1Hgs", "(Ljava/lang/String;FFJ)V", "DistRow", "count", "", "total", "DistRow-g2O1Hgs", "(Ljava/lang/String;IFJ)V", "EmissionAnalyticsScreen", "viewModel", "Lcom/carboncredit/app/ui/manager/analytics/EmissionAnalyticsViewModel;", "EmissionBarComparison", "actual", "baseline", "ReadingDistribution", "readings", "", "Lcom/carboncredit/app/data/models/SensorReading;", "app_debug"})
public final class EmissionAnalyticsScreenKt {
    
    @androidx.compose.runtime.Composable()
    public static final void EmissionAnalyticsScreen(@org.jetbrains.annotations.NotNull()
    com.carboncredit.app.ui.manager.analytics.EmissionAnalyticsViewModel viewModel) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void EmissionBarComparison(float actual, float baseline) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void ReadingDistribution(java.util.List<com.carboncredit.app.data.models.SensorReading> readings) {
    }
}