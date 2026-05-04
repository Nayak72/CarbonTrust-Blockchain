package com.carboncredit.app.ui.manager.anomalies;

import androidx.compose.foundation.layout.*;
import androidx.compose.material.icons.Icons;
import androidx.compose.material.icons.filled.*;
import androidx.compose.material3.*;
import androidx.compose.runtime.*;
import androidx.compose.ui.Alignment;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.text.font.FontWeight;
import com.carboncredit.app.core.utils.DateUtils;
import com.carboncredit.app.data.models.AnomalyEvent;
import com.carboncredit.app.ui.components.*;
import com.carboncredit.app.ui.theme.*;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u00000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\u001af\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00052\u0006\u0010\u0007\u001a\u00020\b2\f\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00010\n2\u0012\u0010\u000b\u001a\u000e\u0012\u0004\u0012\u00020\b\u0012\u0004\u0012\u00020\u00010\f2\f\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u00010\n2\f\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00010\nH\u0003\u001a(\u0010\u000f\u001a\u00020\u00012\u0014\b\u0002\u0010\u000e\u001a\u000e\u0012\u0004\u0012\u00020\b\u0012\u0004\u0012\u00020\u00010\f2\b\b\u0002\u0010\u0010\u001a\u00020\u0011H\u0007\u00a8\u0006\u0012"}, d2 = {"AnomalyCard", "", "anomaly", "Lcom/carboncredit/app/data/models/AnomalyEvent;", "isExpanded", "", "isAcknowledging", "ackNote", "", "onToggle", "Lkotlin/Function0;", "onNoteChange", "Lkotlin/Function1;", "onAcknowledge", "onViewSensor", "AnomalyLogScreen", "viewModel", "Lcom/carboncredit/app/ui/manager/anomalies/AnomalyLogViewModel;", "app_debug"})
public final class AnomalyLogScreenKt {
    
    @androidx.compose.runtime.Composable()
    public static final void AnomalyLogScreen(@org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onViewSensor, @org.jetbrains.annotations.NotNull()
    com.carboncredit.app.ui.manager.anomalies.AnomalyLogViewModel viewModel) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void AnomalyCard(com.carboncredit.app.data.models.AnomalyEvent anomaly, boolean isExpanded, boolean isAcknowledging, java.lang.String ackNote, kotlin.jvm.functions.Function0<kotlin.Unit> onToggle, kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onNoteChange, kotlin.jvm.functions.Function0<kotlin.Unit> onAcknowledge, kotlin.jvm.functions.Function0<kotlin.Unit> onViewSensor) {
    }
}