package com.carboncredit.app.data.repository

import com.carboncredit.app.data.models.CarbonCredit
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Order
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CreditRepository @Inject constructor(
    private val supabase: SupabaseClient
) {

    suspend fun getCreditsForFacility(
        facilityId: String,
        startTime: String? = null,
        endTime: String? = null
    ): List<CarbonCredit> {
        return supabase.postgrest["carbon_credits"]
            .select {
                filter {
                    eq("facility_id", facilityId)
                    if (startTime != null) gte("created_at", startTime)
                    if (endTime != null) lte("created_at", endTime)
                }
                order("created_at", Order.DESCENDING)
            }
            .decodeList<CarbonCredit>()
    }

    suspend fun getCreditById(creditId: String): CarbonCredit {
        return supabase.postgrest["carbon_credits"]
            .select { filter { eq("id", creditId) } }
            .decodeSingle<CarbonCredit>()
    }

    suspend fun getAllCredits(): List<CarbonCredit> {
        return supabase.postgrest["carbon_credits"]
            .select {
                order("created_at", Order.DESCENDING)
            }
            .decodeList<CarbonCredit>()
    }

    suspend fun getCreditsForFacilities(facilityIds: List<String>): List<CarbonCredit> {
        return supabase.postgrest["carbon_credits"]
            .select {
                filter {
                    isIn("facility_id", facilityIds)
                }
                order("created_at", Order.DESCENDING)
            }
            .decodeList<CarbonCredit>()
    }

    suspend fun getTotalCreditsForFacility(facilityId: String): Float {
        val credits = getCreditsForFacility(facilityId)
        return credits.sumOf { it.creditsIssued.toDouble() }.toFloat()
    }

    suspend fun getCreditByTxHash(txHash: String): CarbonCredit? {
        val results = supabase.postgrest["carbon_credits"]
            .select { filter { eq("tx_hash", txHash) } }
            .decodeList<CarbonCredit>()
        return results.firstOrNull()
    }
}
