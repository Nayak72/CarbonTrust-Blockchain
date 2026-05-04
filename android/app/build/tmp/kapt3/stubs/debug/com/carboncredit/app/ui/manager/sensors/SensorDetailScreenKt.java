package com.carboncredit.app.ui.manager.sensors;

import android.content.Intent;
import androidx.compose.foundation.layout.*;
import androidx.compose.material.icons.Icons;
import androidx.compose.material.icons.filled.*;
import androidx.compose.material3.*;
import androidx.compose.runtime.*;
import androidx.compose.ui.Alignment;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.text.font.FontWeight;
import com.carboncredit.app.core.utils.DateUtils;
import com.carboncredit.app.ui.components.*;
import com.carboncredit.app.ui.theme.*;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000:\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0007\n\u0002\b\u0003\n\u0002\u0010\b\n\u0000\u001a\u0010\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u0003\u001a\b\u0010\u0004\u001a\u00020\u0001H\u0003\u001a(\u0010\u0005\u001a\u00020\u00012\u0006\u0010\u0006\u001a\u00020\u00072\f\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00010\t2\b\b\u0002\u0010\n\u001a\u00020\u000bH\u0007\u001a*\u0010\f\u001a\u00020\u00012\u0006\u0010\r\u001a\u00020\u00072\u0006\u0010\u000e\u001a\u00020\u00072\u0006\u0010\u000f\u001a\u00020\u0010H\u0003\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0011\u0010\u0012\u001a(\u0010\u0013\u001a\u00020\u00012\u0006\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u00152\u0006\u0010\u0017\u001a\u00020\u00152\u0006\u0010\u0018\u001a\u00020\u0019H\u0003\u0082\u0002\u0007\n\u0005\b\u00a1\u001e0\u0001\u00a8\u0006\u001a"}, d2 = {"ReadingRow", "", "reading", "Lcom/carboncredit/app/data/models/SensorReading;", "ReadingTableHeader", "SensorDetailScreen", "sensorId", "", "onBack", "Lkotlin/Function0;", "viewModel", "Lcom/carboncredit/app/ui/manager/sensors/SensorDetailViewModel;", "StatItem", "label", "value", "color", "Landroidx/compose/ui/graphics/Color;", "StatItem-mxwnekA", "(Ljava/lang/String;Ljava/lang/String;J)V", "StatsCard", "min", "", "max", "avg", "anomalyCount", "", "app_debug"})
public final class SensorDetailScreenKt {
    
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    public static final void SensorDetailScreen(@org.jetbrains.annotations.NotNull()
    java.lang.String sensorId, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onBack, @org.jetbrains.annotations.NotNull()
    com.carboncredit.app.ui.manager.sensors.SensorDetailViewModel viewModel) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void StatsCard(float min, float max, float avg, int anomalyCount) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void ReadingTableHeader() {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void ReadingRow(com.carboncredit.app.data.models.SensorReading reading) {
    }
}