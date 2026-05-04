package com.carboncredit.app.ui.auth

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import com.carboncredit.app.core.utils.Constants
import com.carboncredit.app.ui.manager.ManagerActivity
import com.carboncredit.app.ui.auditor.AuditorActivity
import com.carboncredit.app.ui.theme.CarbonCreditTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CarbonCreditTheme {
                var showSignUp by remember { mutableStateOf(false) }

                if (showSignUp) {
                    SignUpScreen(
                        onSignUpSuccess = { role -> navigateToRole(role) },
                        onBackToLogin = { showSignUp = false }
                    )
                } else {
                    LoginScreen(
                        onLoginSuccess = { role -> navigateToRole(role) },
                        onSignUp = { showSignUp = true }
                    )
                }
            }
        }
    }

    private fun navigateToRole(role: String) {
        val intent = when (role) {
            Constants.ROLE_MANAGER -> Intent(this, ManagerActivity::class.java)
            Constants.ROLE_AUDITOR -> Intent(this, AuditorActivity::class.java)
            else -> return
        }
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}
