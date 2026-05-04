package com.carboncredit.app.data.repository

import com.carboncredit.app.data.models.AuditorAssignment
import com.carboncredit.app.data.models.Facility
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FacilityRepository @Inject constructor(
    private val supabase: SupabaseClient
) {

    suspend fun getFacilityById(facilityId: String): Facility {
        return supabase.postgrest["facilities"]
            .select { filter { eq("id", facilityId) } }
            .decodeSingle<Facility>()
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
        return supabase.postgrest["facilities"]
            .select()
            .decodeList<Facility>()
    }
}
