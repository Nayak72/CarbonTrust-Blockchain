package com.carboncredit.app.ui.manager.profile

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.carboncredit.app.ui.auth.LoginActivity
import com.carboncredit.app.ui.components.*
import com.carboncredit.app.ui.theme.*

@Composable
fun ProfileScreen(viewModel: ProfileViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    LaunchedEffect(state.loggedOut) {
        if (state.loggedOut) {
            val intent = Intent(context, LoginActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            context.startActivity(intent)
        }
    }

    when {
        state.isLoading -> ShimmerList(count = 3)
        state.error != null && state.profile == null -> ErrorState(state.error!!)
        else -> {
            Column(modifier = Modifier.fillMaxSize().background(BackgroundDark).verticalScroll(rememberScrollState()).padding(16.dp)) {
                Text("Profile", style = MaterialTheme.typography.headlineMedium, color = TextPrimary)
                Spacer(modifier = Modifier.height(20.dp))

                // Avatar + Info
                Card(colors = CardDefaults.cardColors(containerColor = SurfaceCard), shape = RoundedCornerShape(12.dp)) {
                    Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        if (state.profile?.avatarUrl != null) {
                            AsyncImage(model = state.profile!!.avatarUrl, contentDescription = "Avatar", modifier = Modifier.size(80.dp).clip(CircleShape))
                        } else {
                            Box(modifier = Modifier.size(80.dp).clip(CircleShape).background(BluePrimary), contentAlignment = Alignment.Center) {
                                Text(state.profile?.fullName?.take(1)?.uppercase() ?: "?", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                            }
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(state.profile?.fullName ?: "", fontWeight = FontWeight.Bold, color = TextPrimary, fontSize = 18.sp)
                        Text(state.profile?.email ?: "", color = TextSecondary, fontSize = 14.sp)
                        if (state.facility != null) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(state.facility!!.name, color = BlueLight, fontSize = 13.sp)
                            Text(state.facility!!.companyName, color = TextSecondary, fontSize = 12.sp)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))

                // Change Password
                Card(colors = CardDefaults.cardColors(containerColor = SurfaceCard), shape = RoundedCornerShape(12.dp)) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Change Password", fontWeight = FontWeight.Bold, color = TextPrimary, fontSize = 16.sp)
                        Spacer(modifier = Modifier.height(12.dp))
                        OutlinedTextField(value = newPassword, onValueChange = { newPassword = it }, label = { Text("New Password") }, visualTransformation = PasswordVisualTransformation(), modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = BluePrimary, unfocusedBorderColor = TextTertiary, focusedTextColor = TextPrimary, unfocusedTextColor = TextPrimary, cursorColor = BluePrimary))
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(value = confirmPassword, onValueChange = { confirmPassword = it }, label = { Text("Confirm Password") }, visualTransformation = PasswordVisualTransformation(), modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = BluePrimary, unfocusedBorderColor = TextTertiary, focusedTextColor = TextPrimary, unfocusedTextColor = TextPrimary, cursorColor = BluePrimary))
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(onClick = { viewModel.updatePassword(newPassword, confirmPassword) }, enabled = !state.passwordUpdating, colors = ButtonDefaults.buttonColors(containerColor = BluePrimary), modifier = Modifier.fillMaxWidth()) {
                            if (state.passwordUpdating) CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp, color = TextPrimary)
                            else Text("Save")
                        }
                        if (state.passwordSuccess) { Spacer(modifier = Modifier.height(8.dp)); Text("Password updated!", color = GreenLight, fontSize = 13.sp) }
                        state.error?.let { Spacer(modifier = Modifier.height(8.dp)); Text(it, color = RedCritical, fontSize = 13.sp) }
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))

                // App info
                Text("Carbon Credit v1.0.0", color = TextTertiary, fontSize = 12.sp, modifier = Modifier.align(Alignment.CenterHorizontally))
                Spacer(modifier = Modifier.height(16.dp))

                // Logout
                Button(onClick = { viewModel.logout() }, colors = ButtonDefaults.buttonColors(containerColor = RedCritical), modifier = Modifier.fillMaxWidth()) {
                    Icon(Icons.Default.Logout, null, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Logout")
                }
            }
        }
    }
}
