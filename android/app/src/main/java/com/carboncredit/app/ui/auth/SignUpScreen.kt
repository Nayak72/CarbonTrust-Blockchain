package com.carboncredit.app.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
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
import com.carboncredit.app.data.models.CompanyInfo
import com.carboncredit.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    onSignUpSuccess: (String) -> Unit,
    onBackToLogin: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
) {
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var selectedRole by remember { mutableStateOf("MANAGER") }
    var roleMenuExpanded by remember { mutableStateOf(false) }
    val signUpState by viewModel.signUpState.collectAsState()
    val focusManager = LocalFocusManager.current

    val roles = listOf("MANAGER", "AUDITOR")

    // Step 2 fields (MANAGER only)
    var currentStep by remember { mutableIntStateOf(1) }
    var companyName by remember { mutableStateOf("") }
    var facilityName by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var industryType by remember { mutableStateOf("Manufacturing") }
    var industryMenuExpanded by remember { mutableStateOf(false) }
    var baselineEmissions by remember { mutableStateOf("") }
    var step2Error by remember { mutableStateOf<String?>(null) }

    val industryTypes = listOf("Manufacturing", "Energy", "Chemical", "Cement", "Steel", "Other")

    LaunchedEffect(signUpState) {
        if (signUpState is Resource.Success) {
            val profile = (signUpState as Resource.Success).data!!
            onSignUpSuccess(profile.role)
        }
    }

    Box(
        modifier = Modifier.fillMaxSize().background(BackgroundDark),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (currentStep == 1) {
                // ============================================================
                // STEP 1 — Basic Info (identical to previous single-step form)
                // ============================================================
                Text("🌿", fontSize = 48.sp)
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    "Create Account",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "Join the carbon credit verification network",
                    fontSize = 13.sp,
                    color = TextSecondary,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(32.dp))

                // Full Name
                OutlinedTextField(
                    value = fullName, onValueChange = { fullName = it },
                    label = { Text("Full Name") },
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Down) }
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = BluePrimary,
                        unfocusedBorderColor = TextTertiary,
                        focusedLabelColor = BluePrimary,
                        cursorColor = BluePrimary,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    )
                )
                Spacer(modifier = Modifier.height(12.dp))

                // Email
                OutlinedTextField(
                    value = email, onValueChange = { email = it },
                    label = { Text("Email address") },
                    leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Down) }
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = BluePrimary,
                        unfocusedBorderColor = TextTertiary,
                        focusedLabelColor = BluePrimary,
                        cursorColor = BluePrimary,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    )
                )
                Spacer(modifier = Modifier.height(12.dp))

                // Role selector
                ExposedDropdownMenuBox(
                    expanded = roleMenuExpanded,
                    onExpandedChange = { roleMenuExpanded = it }
                ) {
                    OutlinedTextField(
                        value = selectedRole,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Role") },
                        leadingIcon = {
                            Icon(Icons.Default.Badge, contentDescription = null)
                        },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = roleMenuExpanded)
                        },
                        modifier = Modifier.fillMaxWidth().menuAnchor(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = BluePrimary,
                            unfocusedBorderColor = TextTertiary,
                            focusedLabelColor = BluePrimary,
                            cursorColor = BluePrimary,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        )
                    )
                    ExposedDropdownMenu(
                        expanded = roleMenuExpanded,
                        onDismissRequest = { roleMenuExpanded = false }
                    ) {
                        roles.forEach { role ->
                            DropdownMenuItem(
                                text = { Text(role) },
                                onClick = {
                                    selectedRole = role
                                    roleMenuExpanded = false
                                }
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))

                // Password
                OutlinedTextField(
                    value = password, onValueChange = { password = it },
                    label = { Text("Password") },
                    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                if (passwordVisible) Icons.Default.VisibilityOff
                                else Icons.Default.Visibility,
                                contentDescription = null
                            )
                        }
                    },
                    singleLine = true,
                    visualTransformation = if (passwordVisible) VisualTransformation.None
                    else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Down) }
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = BluePrimary,
                        unfocusedBorderColor = TextTertiary,
                        focusedLabelColor = BluePrimary,
                        cursorColor = BluePrimary,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    )
                )
                Spacer(modifier = Modifier.height(12.dp))

                // Confirm Password
                OutlinedTextField(
                    value = confirmPassword, onValueChange = { confirmPassword = it },
                    label = { Text("Confirm Password") },
                    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            if (selectedRole == "AUDITOR") {
                                viewModel.signUp(fullName, email, password, confirmPassword, selectedRole, null)
                            } else {
                                currentStep = 2
                            }
                        }
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = BluePrimary,
                        unfocusedBorderColor = TextTertiary,
                        focusedLabelColor = BluePrimary,
                        cursorColor = BluePrimary,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    )
                )
                Spacer(modifier = Modifier.height(24.dp))

                // Step 1 Button — "Continue" for MANAGER, "Create Account" for AUDITOR
                Button(
                    onClick = {
                        if (selectedRole == "AUDITOR") {
                            viewModel.signUp(fullName, email, password, confirmPassword, selectedRole, null)
                        } else {
                            // Validate step 1 fields before advancing to step 2
                            if (fullName.isBlank() || email.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
                                // Let the ViewModel handle the error display
                                viewModel.signUp(fullName, email, password, confirmPassword, selectedRole, null)
                            } else if (password != confirmPassword) {
                                viewModel.signUp(fullName, email, password, confirmPassword, selectedRole, null)
                            } else if (password.length < 6) {
                                viewModel.signUp(fullName, email, password, confirmPassword, selectedRole, null)
                            } else {
                                currentStep = 2
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    enabled = signUpState !is Resource.Loading,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedRole == "MANAGER") BluePrimary else GreenCompliant
                    )
                ) {
                    if (signUpState is Resource.Loading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = TextPrimary,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(
                            if (selectedRole == "MANAGER") "Continue" else "Create Account",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                if (signUpState is Resource.Error) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        (signUpState as Resource.Error).message ?: "Error",
                        color = RedCritical,
                        fontSize = 13.sp
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
                TextButton(onClick = onBackToLogin) {
                    Text("Already have an account? ", color = TextSecondary, fontSize = 14.sp)
                    Text("Sign In", color = BlueLight, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                }

            } else {
                // ============================================================
                // STEP 2 — Company Info (MANAGER only)
                // ============================================================

                // Back arrow
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { currentStep = 1 }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextPrimary)
                    }
                }

                Text("🏭", fontSize = 48.sp)
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    "Your Company",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "This sets up your facility and simulator",
                    fontSize = 13.sp,
                    color = TextSecondary,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(32.dp))

                // Company Name
                OutlinedTextField(
                    value = companyName, onValueChange = { companyName = it },
                    label = { Text("Company Name") },
                    leadingIcon = { Icon(Icons.Default.Business, contentDescription = null) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Down) }
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = BluePrimary,
                        unfocusedBorderColor = TextTertiary,
                        focusedLabelColor = BluePrimary,
                        cursorColor = BluePrimary,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    )
                )
                Spacer(modifier = Modifier.height(12.dp))

                // Facility Name
                OutlinedTextField(
                    value = facilityName, onValueChange = { facilityName = it },
                    label = { Text("Facility Name") },
                    leadingIcon = { Icon(Icons.Default.Factory, contentDescription = null) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Down) }
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = BluePrimary,
                        unfocusedBorderColor = TextTertiary,
                        focusedLabelColor = BluePrimary,
                        cursorColor = BluePrimary,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    )
                )
                Spacer(modifier = Modifier.height(12.dp))

                // Location (City)
                OutlinedTextField(
                    value = location, onValueChange = { location = it },
                    label = { Text("Location (City)") },
                    leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Down) }
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = BluePrimary,
                        unfocusedBorderColor = TextTertiary,
                        focusedLabelColor = BluePrimary,
                        cursorColor = BluePrimary,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    )
                )
                Spacer(modifier = Modifier.height(12.dp))

                // Industry Type dropdown
                ExposedDropdownMenuBox(
                    expanded = industryMenuExpanded,
                    onExpandedChange = { industryMenuExpanded = it }
                ) {
                    OutlinedTextField(
                        value = industryType,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Industry Type") },
                        leadingIcon = {
                            Icon(Icons.Default.Category, contentDescription = null)
                        },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = industryMenuExpanded)
                        },
                        modifier = Modifier.fillMaxWidth().menuAnchor(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = BluePrimary,
                            unfocusedBorderColor = TextTertiary,
                            focusedLabelColor = BluePrimary,
                            cursorColor = BluePrimary,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        )
                    )
                    ExposedDropdownMenu(
                        expanded = industryMenuExpanded,
                        onDismissRequest = { industryMenuExpanded = false }
                    ) {
                        industryTypes.forEach { type ->
                            DropdownMenuItem(
                                text = { Text(type) },
                                onClick = {
                                    industryType = type
                                    industryMenuExpanded = false
                                }
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))

                // Baseline Emissions
                OutlinedTextField(
                    value = baselineEmissions, onValueChange = { baselineEmissions = it },
                    label = { Text("Monthly Baseline CO₂ (tonnes)") },
                    leadingIcon = { Icon(Icons.Default.Air, contentDescription = null) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = { focusManager.clearFocus() }
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = BluePrimary,
                        unfocusedBorderColor = TextTertiary,
                        focusedLabelColor = BluePrimary,
                        cursorColor = BluePrimary,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    )
                )
                Text(
                    "Your facility's average monthly CO₂ output before any reduction measures",
                    color = TextSecondary,
                    fontSize = 11.sp,
                    modifier = Modifier.fillMaxWidth().padding(start = 16.dp, top = 4.dp)
                )
                Spacer(modifier = Modifier.height(24.dp))

                // Create Account button (Step 2)
                Button(
                    onClick = {
                        step2Error = null
                        // Validate step 2 fields
                        if (companyName.isBlank()) {
                            step2Error = "Company name is required"
                        } else if (facilityName.isBlank()) {
                            step2Error = "Facility name is required"
                        } else if (baselineEmissions.isBlank()) {
                            step2Error = "Baseline emissions is required"
                        } else {
                            val emissions = baselineEmissions.toFloatOrNull()
                            if (emissions == null || emissions <= 0f) {
                                step2Error = "Please enter a valid positive number for emissions"
                            } else {
                                viewModel.signUp(
                                    fullName, email, password, confirmPassword, "MANAGER",
                                    CompanyInfo(
                                        companyName = companyName,
                                        facilityName = facilityName,
                                        location = location,
                                        industryType = industryType,
                                        baselineEmissions = emissions
                                    )
                                )
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    enabled = signUpState !is Resource.Loading,
                    colors = ButtonDefaults.buttonColors(containerColor = GreenCompliant)
                ) {
                    if (signUpState is Resource.Loading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = TextPrimary,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text("Create Account", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                    }
                }

                // Error display for step 2
                if (step2Error != null) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(step2Error!!, color = RedCritical, fontSize = 13.sp)
                }

                if (signUpState is Resource.Error) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        (signUpState as Resource.Error).message ?: "Error",
                        color = RedCritical,
                        fontSize = 13.sp
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}
