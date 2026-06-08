package com.carboncredit.app.data.repository

import com.carboncredit.app.data.models.AuditorAssignment
import com.carboncredit.app.data.models.Facility
import com.carboncredit.app.core.network.ApiService
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FacilityRepository @Inject constructor(
    private val supabase: SupabaseClient,
    private val apiService: ApiService
) {

    suspend fun getFacilityById(facilityId: String): Facility {
        return apiService.getFacilityById(facilityId)
    }

    suspend fun getAssignedFacilities(auditorId: String): List<Facility> {
        val assignments = supabase.postgrest["auditor_assignments"]
            .select { filter { eq("auditor_id", auditorId) } }
            .decodeList<AuditorAssignment>()

        if (assignments.isEmpty()) return emptyList()

        val facilityIds = assignments.map { it.facilityId }
        return supabase.postgrest["facilities"]
            .select {
                filter { isIn("id", facilityIds) }
            }
            .decodeList<Facility>()
    }

    suspend fun getAllFacilities(): List<Facility> {
        return apiService.listFacilities()
    }
}
