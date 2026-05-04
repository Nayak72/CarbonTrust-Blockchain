package com.carboncredit.app.data.repository

import com.carboncredit.app.data.models.Sensor
import com.carboncredit.app.data.models.SensorReading
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.Order
import io.github.jan.supabase.realtime.channel
import io.github.jan.supabase.realtime.postgresChangeFlow
import io.github.jan.supabase.realtime.realtime
import io.github.jan.supabase.realtime.PostgresAction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SensorRepository @Inject constructor(
    private val supabase: SupabaseClient
) {

    suspend fun getSensorsForFacility(facilityId: String): List<Sensor> {
        return supabase.postgrest["sensors"]
            .select { filter { eq("facility_id", facilityId) } }
            .decodeList<Sensor>()
    }

    suspend fun getSensorById(sensorId: String): Sensor {
        return supabase.postgrest["sensors"]
            .select { filter { eq("id", sensorId) } }
            .decodeSingle<Sensor>()
    }

    suspend fun getReadingsForSensor(
        sensorId: String,
        startTime: String? = null,
        endTime: String? = null,
        limit: Int = 500
    ): List<SensorReading> {
        return supabase.postgrest["sensor_readings"]
            .select {
                filter {
                    eq("sensor_id", sensorId)
                    if (startTime != null) gte("timestamp", startTime)
                    if (endTime != null) lte("timestamp", endTime)
                }
                order("timestamp", Order.ASCENDING)
                limit(limit.toLong())
            }
            .decodeList<SensorReading>()
    }

    suspend fun getReadingsForFacility(
        facilityId: String,
        startTime: String? = null,
        endTime: String? = null,
        limit: Int = 1000
    ): List<SensorReading> {
        return supabase.postgrest["sensor_readings"]
            .select {
                filter {
                    eq("facility_id", facilityId)
                    if (startTime != null) gte("timestamp", startTime)
                    if (endTime != null) lte("timestamp", endTime)
                }
                order("timestamp", Order.ASCENDING)
                limit(limit.toLong())
            }
            .decodeList<SensorReading>()
    }

    suspend fun getLatestReading(facilityId: String): SensorReading? {
        val results = supabase.postgrest["sensor_readings"]
            .select {
                filter { eq("facility_id", facilityId) }
                order("timestamp", Order.DESCENDING)
                limit(1)
            }
            .decodeList<SensorReading>()
        return results.firstOrNull()
    }

    suspend fun getLatestReadingForSensor(sensorId: String): SensorReading? {
        val results = supabase.postgrest["sensor_readings"]
            .select {
                filter { eq("sensor_id", sensorId) }
                order("timestamp", Order.DESCENDING)
                limit(1)
            }
            .decodeList<SensorReading>()
        return results.firstOrNull()
    }

    /**
     * Subscribe to real-time sensor reading inserts for a facility.
     * Returns a Flow of new SensorReading objects.
     */
    fun subscribeToReadings(facilityId: String): Flow<SensorReading> {
        val channel = supabase.realtime.channel("readings-$facilityId")
        val flow = channel.postgresChangeFlow<PostgresAction.Insert>(schema = "public") {
            table = "sensor_readings"
            filter = "facility_id=eq.$facilityId"
        }
        return flow.map { action ->
            val record = action.record
            SensorReading(
                id = record["id"]?.jsonPrimitive?.content ?: "",
                sensorId = record["sensor_id"]?.jsonPrimitive?.content ?: "",
                facilityId = record["facility_id"]?.jsonPrimitive?.content ?: "",
                co2Ppm = record["co2_ppm"]?.jsonPrimitive?.content?.toFloatOrNull() ?: 0f,
                temperature = record["temperature"]?.jsonPrimitive?.content?.toFloatOrNull() ?: 0f,
                humidity = record["humidity"]?.jsonPrimitive?.content?.toFloatOrNull() ?: 0f,
                isAnomaly = record["is_anomaly"]?.jsonPrimitive?.content?.toBooleanStrictOrNull() ?: false,
                anomalyType = record["anomaly_type"]?.jsonPrimitive?.content,
                zScore = record["z_score"]?.jsonPrimitive?.content?.toFloatOrNull(),
                timestamp = record["timestamp"]?.jsonPrimitive?.content ?: "",
                createdAt = record["created_at"]?.jsonPrimitive?.content ?: ""
            )
        }
    }

    suspend fun subscribeChannel(facilityId: String) {
        val channel = supabase.realtime.channel("readings-$facilityId")
        channel.subscribe()
    }

    suspend fun unsubscribeChannel(facilityId: String) {
        try {
            val channel = supabase.realtime.channel("readings-$facilityId")
            channel.unsubscribe()
        } catch (_: Exception) { }
    }
}
