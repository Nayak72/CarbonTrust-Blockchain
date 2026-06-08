package com.carboncredit.app.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.carboncredit.app.core.security.TokenManager
import com.carboncredit.app.core.utils.Constants
import com.carboncredit.app.ui.auth.LoginActivity
import com.carboncredit.app.ui.manager.ManagerActivity
import com.carboncredit.app.ui.auditor.AuditorActivity
import com.carboncredit.app.ui.theme.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import javax.inject.Inject

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : ComponentActivity() {

    @Inject lateinit var tokenManager: TokenManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CarbonCreditTheme {
                SplashScreen {
                    navigateBasedOnRole()
                }
            }
        }
    }

    private fun navigateBasedOnRole() {
        val intent = when {
            !tokenManager.isLoggedIn() -> Intent(this, LoginActivity::class.java)
            tokenManager.getRole() == Constants.ROLE_MANAGER -> Intent(this, ManagerActivity::class.java)
            tokenManager.getRole() == Constants.ROLE_AUDITOR -> Intent(this, AuditorActivity::class.java)
            else -> Intent(this, LoginActivity::class.java)
        }
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}

@Composable
private fun SplashScreen(onFinish: () -> Unit) {
    val alpha = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        alpha.animateTo(1f, animationSpec = tween(800))
        delay(1200)
        onFinish()
    }

    Box(
        modifier = Modifier.fillMaxSize().background(BackgroundDark),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.alpha(alpha.value)
        ) {
            Text("🌿", fontSize = 64.sp)
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "Carbon Credit",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                "Blockchain Verification",
                fontSize = 14.sp,
                color = TextSecondary
            )
        }
    }
}
