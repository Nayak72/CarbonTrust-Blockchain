package com.carboncredit.app.data.repository

import com.carboncredit.app.core.network.ApiService
import com.carboncredit.app.core.network.AssignAuditorRequest
import com.carboncredit.app.core.network.AssignedAuditorResponse
import com.carboncredit.app.core.network.AssignmentResponse
import com.carboncredit.app.core.network.GenericResponse
import com.carboncredit.app.core.network.RevokeAuditorRequest
import com.carboncredit.app.data.models.Facility
import com.carboncredit.app.data.models.UserProfile
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AssignmentRepository @Inject constructor(
    private val apiService: ApiService
) {

    /**
     * Assign an auditor to a facility.
     * Requires MANAGER role on the backend.
     */
    suspend fun assignAuditor(auditorId: String, facilityId: String): AssignmentResponse {
        return apiService.assignAuditor(
            AssignAuditorRequest(auditor_id = auditorId, facility_id = facilityId)
        )
    }

    /**
     * Revoke an auditor's access to a facility (soft delete — sets is_active=false).
     * Requires MANAGER role on the backend.
     */
    suspend fun revokeAuditor(auditorId: String, facilityId: String): GenericResponse {
        return apiService.revokeAuditor(
            RevokeAuditorRequest(auditor_id = auditorId, facility_id = facilityId)
        )
    }

    /**
     * Returns the list of facilities assigned to the currently logged-in auditor.
     * Requires AUDITOR role on the backend.
     */
    suspend fun getMyAssignedFacilities(): List<Facility> {
        return apiService.getMyAssignedFacilities()
    }

    /**
     * Returns all auditors currently assigned to a facility.
     * Requires MANAGER role on the backend.
     */
    suspend fun getFacilityAuditors(facilityId: String): List<AssignedAuditorResponse> {
        return apiService.getFacilityAuditors(facilityId)
    }

    /**
     * Returns all users with role=AUDITOR (for the picker UI).
     * Requires MANAGER role on the backend.
     */
    suspend fun getAvailableAuditors(): List<UserProfile> {
        return apiService.getAvailableAuditors()
    }
}
