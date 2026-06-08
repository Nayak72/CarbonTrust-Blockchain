package com.carboncredit.app.core.utils

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.content.FileProvider

/**
 * Resource wrapper for UI state management across all ViewModels.
 * Every ViewModel emits Resource<T> via StateFlow.
 * Every Composable screen handles all three states.
 */
sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)
    class Loading<T>(data: T? = null) : Resource<T>(data)
}

fun Context.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}

fun Float.formatTwoDecimals(): String = "%.2f".format(this)

fun Float.formatOneDecimal(): String = "%.1f".format(this)

fun String.truncateMiddle(maxLength: Int = 16): String {
    if (this.length <= maxLength) return this
    val half = (maxLength - 3) / 2
    return "${this.take(half)}...${this.takeLast(half)}"
}

fun Float.toCo2StatusColor(): Long {
    return when {
        this < Constants.CO2_NORMAL_MAX -> 0xFF4CAF50  // green
        this < Constants.CO2_WARNING_MAX -> 0xFFFF8F00 // amber
        else -> 0xFFC62828                              // red
    }
}
