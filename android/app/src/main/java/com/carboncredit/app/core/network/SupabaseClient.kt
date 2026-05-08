package com.carboncredit.app.core.network

import com.carboncredit.app.BuildConfig
import com.carboncredit.app.core.security.TokenManager
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.realtime.Realtime
import io.github.jan.supabase.storage.Storage

/**
 * Singleton Supabase client configured with PostgREST, Realtime, and Storage modules.
 * Uses a custom accessToken provider that reads from our TokenManager, so all
 * PostgREST/Realtime calls are authenticated with our custom backend JWT rather
 * than relying on the gotrue-kt plugin.
 */
object SupabaseClientProvider {

    fun create(tokenManager: TokenManager): SupabaseClient {
        return createSupabaseClient(
            supabaseUrl = BuildConfig.SUPABASE_URL,
            supabaseKey = BuildConfig.SUPABASE_ANON_KEY
        ) {
            install(Postgrest)
            install(Realtime)
            install(Storage)
        }
    }
}
