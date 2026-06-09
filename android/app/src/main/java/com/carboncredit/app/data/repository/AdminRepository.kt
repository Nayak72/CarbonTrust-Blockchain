package com.carboncredit.app.data.repository

import com.carboncredit.app.core.network.ApiService
import com.carboncredit.app.core.network.CompanyDashboardData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AdminRepository @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun getDashboardData(): List<CompanyDashboardData> {
        return apiService.getAdminDashboard()
    }
}
