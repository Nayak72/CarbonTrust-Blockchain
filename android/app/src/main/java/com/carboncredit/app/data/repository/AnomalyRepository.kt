package com.carboncredit.app.data.repository

import com.carboncredit.app.data.models.AnomalyEvent
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Order
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AnomalyRepository @Inject constructor(
    private val supabase: SupabaseClient
) {

    suspend fun getAnomaliesForFacility(
        facilityId: String,
        anomalyType: String? = null,
        startTime: String? = null,
        endTime: String? = null,
        limit: Int = 200
    ): List<AnomalyEvent> {
        return supabase.postgrest["anomaly_events"]
            .select {
                filter {
                    eq("facility_id", facilityId)
                    if (anomalyType != null) eq("anomaly_type", anomalyType)
                    if (startTime != null) gte("timestamp", startTime)
                    if (endTime != null) lte("timestamp", endTime)
                }
                order("timestamp", Order.DESCENDING)
                limit(limit.toLong())
            }
            .decodeList<AnomalyEvent>()
    }

    suspend fun getRecentAnomalies(facilityId: String, limit: Int = 3): List<AnomalyEvent> {
        return supabase.postgrest["anomaly_events"]
            .select {
                filter { eq("facility_id", facilityId) }
                order("timestamp", Order.DESCENDING)
                limit(limit.toLong())
            }
            .decodeList<AnomalyEvent>()
    }

    suspend fun getUnacknowledgedCount(facilityId: String): Int {
        return try {
            val anomalies = supabase.postgrest["anomaly_events"]
                .select {
                    filter { eq("facility_id", facilityId) }
                    order("timestamp", Order.DESCENDING)
                    limit(200)
                }
                .decodeList<AnomalyEvent>()
            anomalies.count { !it.isAcknowledged }
        } catch (_: Exception) {
            0
        }
    }

    suspend fun getUnacknowledgedCountForFacilities(facilityIds: List<String>): Int {
        return try {
            val anomalies = supabase.postgrest["anomaly_events"]
                .select {
                    filter { isIn("facility_id", facilityIds) }
                    order("timestamp", Order.DESCENDING)
                    limit(500)
                }
                .decodeList<AnomalyEvent>()
            anomalies.count { !it.isAcknowledged }
        } catch (_: Exception) {
            0
        }
    }

    suspend fun acknowledgeAnomaly(
        anomalyId: String,
        userId: String,
        note: String?
    ) {
        supabase.postgrest["anomaly_events"]
            .update(
                {
                    set("is_acknowledged", true)
                    set("acknowledged_by", userId)
                    set("acknowledged_at", java.time.Instant.now().toString())
                    if (note != null) set("acknowledgement_note", note)
                }
            ) {
                filter { eq("id", anomalyId) }
            }
    }

    suspend fun getAnomalyById(anomalyId: String): AnomalyEvent {
        return supabase.postgrest["anomaly_events"]
            .select { filter { eq("id", anomalyId) } }
            .decodeSingle<AnomalyEvent>()
    }
}
