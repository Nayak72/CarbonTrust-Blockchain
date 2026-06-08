package com.carboncredit.app.ui.manager.auditors

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.carboncredit.app.core.network.AssignedAuditorResponse
import com.carboncredit.app.data.models.UserProfile
import com.carboncredit.app.ui.components.EmptyState
import com.carboncredit.app.ui.components.ErrorState
import com.carboncredit.app.ui.components.ShimmerList
import com.carboncredit.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuditorsScreen(
    facilityId: String,
    onBack: () -> Unit,
    viewModel: AuditorsViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(facilityId) { viewModel.init(facilityId) }

    // Success / error snackbar
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(state.successMessage, state.error) {
        val msg = state.successMessage ?: state.error
        if (msg != null) {
            snackbarHostState.showSnackbar(msg)
            viewModel.clearMessages()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = BackgroundDark,
        topBar = {
            TopAppBar(
                title = { Text("Manage Auditors", color = TextPrimary) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, null, tint = TextPrimary)
                    }
                },
                actions = {
                    FilledTonalButton(
                        onClick = { viewModel.openAuditorPicker() },
                        modifier = Modifier.padding(end = 12.dp),
                        colors = ButtonDefaults.filledTonalButtonColors(
                            containerColor = BluePrimary.copy(alpha = 0.15f),
                            contentColor = BlueLight
                        )
                    ) {
                        Icon(Icons.Default.PersonAdd, null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Assign", fontWeight = FontWeight.Medium)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = BackgroundDark)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when {
                state.isLoading -> ShimmerList(count = 3)
                state.error != null && state.assignedAuditors.isEmpty() ->
                    ErrorState(state.error!!, onRetry = { viewModel.loadAssignedAuditors() })
                else -> {
                    if (state.assignedAuditors.isEmpty()) {
                        EmptyState(
                            title = "No auditors assigned yet",
                            subtitle = "Tap 'Assign' to give an auditor access to this facility"
                        )
                    } else {
                        LazyColumn(
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            item {
                                Text(
                                    "Assigned Auditors (${state.assignedAuditors.size})",
                                    color = TextSecondary,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium,
                                    modifier = Modifier.padding(bottom = 4.dp)
                                )
                            }
                            items(state.assignedAuditors, key = { it.id }) { auditor ->
                                AuditorCard(
                                    auditor = auditor,
                                    onRevoke = { viewModel.revokeAuditor(auditor.id) }
                                )
                            }
                        }
                    }
                }
            }
        }

        // Auditor picker bottom sheet
        if (state.isPickerVisible) {
            AuditorPickerSheet(
                isLoading = state.isPickerLoading,
                auditors = state.availableAuditors,
                onSelect = { viewModel.assignAuditor(it.id) },
                onDismiss = { viewModel.dismissPicker() }
            )
        }
    }
}

@Composable
private fun AuditorCard(
    auditor: AssignedAuditorResponse,
    onRevoke: () -> Unit
) {
    var showConfirm by remember { mutableStateOf(false) }

    Card(
        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar circle with initial
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(BluePrimary.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    auditor.full_name.firstOrNull()?.uppercase() ?: "A",
                    color = BlueLight,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(auditor.full_name, fontWeight = FontWeight.SemiBold, color = TextPrimary, fontSize = 14.sp)
                Text(auditor.email, color = TextSecondary, fontSize = 12.sp)
                auditor.assigned_at?.let { at ->
                    Text("Assigned: ${at.take(10)}", color = TextTertiary, fontSize = 11.sp)
                }
            }
            // Revoke button
            if (!showConfirm) {
                IconButton(onClick = { showConfirm = true }) {
                    Icon(
                        Icons.Default.PersonRemove,
                        contentDescription = "Revoke",
                        tint = RedCritical.copy(alpha = 0.7f),
                        modifier = Modifier.size(22.dp)
                    )
                }
            } else {
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    TextButton(onClick = { showConfirm = false }) {
                        Text("Cancel", color = TextSecondary, fontSize = 12.sp)
                    }
                    TextButton(onClick = {
                        showConfirm = false
                        onRevoke()
                    }) {
                        Text("Revoke", color = RedCritical, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AuditorPickerSheet(
    isLoading: Boolean,
    auditors: List<UserProfile>,
    onSelect: (UserProfile) -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = SurfaceCard,
        scrimColor = BackgroundDark.copy(alpha = 0.8f)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                "Available Auditors",
                style = MaterialTheme.typography.titleLarge,
                color = TextPrimary,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                "Select an auditor to give them access to your facility",
                color = TextSecondary,
                fontSize = 13.sp
            )
            Spacer(modifier = Modifier.height(16.dp))

            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = BluePrimary)
                }
            } else if (auditors.isEmpty()) {
                EmptyState(
                    title = "No auditors available",
                    subtitle = "All users with the Auditor role will appear here"
                )
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(auditors) { auditor ->
                        Card(
                            onClick = { onSelect(auditor) },
                            colors = CardDefaults.cardColors(containerColor = BackgroundDark),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(38.dp)
                                        .clip(CircleShape)
                                        .background(BlockchainGold.copy(alpha = 0.2f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        auditor.fullName.firstOrNull()?.uppercase() ?: "A",
                                        color = BlockchainGold,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(auditor.fullName, color = TextPrimary, fontWeight = FontWeight.Medium)
                                    Text(auditor.email, color = TextSecondary, fontSize = 12.sp)
                                }
                                Icon(
                                    Icons.Default.ChevronRight,
                                    null,
                                    tint = TextTertiary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
