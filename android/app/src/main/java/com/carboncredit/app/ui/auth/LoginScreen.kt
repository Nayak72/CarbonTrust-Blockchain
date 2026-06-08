package com.carboncredit.app.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.carboncredit.app.core.utils.Resource
import com.carboncredit.app.ui.theme.*

@Composable
fun LoginScreen(
    onLoginSuccess: (String) -> Unit,
    onSignUp: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    val loginState by viewModel.loginState.collectAsState()
    val focusManager = LocalFocusManager.current

    LaunchedEffect(loginState) {
        if (loginState is Resource.Success) {
            val profile = (loginState as Resource.Success).data!!
            onLoginSuccess(profile.role)
        }
    }

    Box(
        modifier = Modifier.fillMaxSize().background(BackgroundDark),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("🌿", fontSize = 48.sp)
            Spacer(modifier = Modifier.height(12.dp))
            Text("Carbon Credit Verification", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = TextPrimary, textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.height(4.dp))
            Text("Blockchain-backed emission monitoring", fontSize = 13.sp, color = TextSecondary, textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.height(40.dp))

            OutlinedTextField(
                value = email, onValueChange = { email = it },
                label = { Text("Email address") },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = BluePrimary, unfocusedBorderColor = TextTertiary,
                    focusedLabelColor = BluePrimary, cursorColor = BluePrimary,
                    focusedTextColor = TextPrimary, unfocusedTextColor = TextPrimary
                )
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password, onValueChange = { password = it },
                label = { Text("Password") },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility, contentDescription = null)
                    }
                },
                singleLine = true,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { viewModel.signIn(email, password) }),
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = BluePrimary, unfocusedBorderColor = TextTertiary,
                    focusedLabelColor = BluePrimary, cursorColor = BluePrimary,
                    focusedTextColor = TextPrimary, unfocusedTextColor = TextPrimary
                )
            )
            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { viewModel.signIn(email, password) },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                enabled = loginState !is Resource.Loading,
                colors = ButtonDefaults.buttonColors(containerColor = BluePrimary)
            ) {
                if (loginState is Resource.Loading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = TextPrimary, strokeWidth = 2.dp)
                } else {
                    Text("Sign In", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                }
            }

            if (loginState is Resource.Error) {
                Spacer(modifier = Modifier.height(12.dp))
                Text((loginState as Resource.Error).message ?: "Error", color = RedCritical, fontSize = 13.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))
            TextButton(onClick = onSignUp) {
                Text("Don't have an account? ", color = TextSecondary, fontSize = 14.sp)
                Text("Sign Up", color = BlueLight, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

