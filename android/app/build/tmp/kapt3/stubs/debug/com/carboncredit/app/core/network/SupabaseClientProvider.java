package com.carboncredit.app.core.network;

import com.carboncredit.app.BuildConfig;
import io.github.jan.supabase.SupabaseClient;
import io.github.jan.supabase.gotrue.Auth;
import io.github.jan.supabase.postgrest.Postgrest;
import io.github.jan.supabase.realtime.Realtime;
import io.github.jan.supabase.storage.Storage;

/**
 * Singleton Supabase client configured with Auth, PostgREST, Realtime, and Storage modules.
 * Provided via Hilt DI — see NetworkModule.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0006\u0010\u0003\u001a\u00020\u0004\u00a8\u0006\u0005"}, d2 = {"Lcom/carboncredit/app/core/network/SupabaseClientProvider;", "", "()V", "create", "Lio/github/jan/supabase/SupabaseClient;", "app_debug"})
public final class SupabaseClientProvider {
    @org.jetbrains.annotations.NotNull()
    public static final com.carboncredit.app.core.network.SupabaseClientProvider INSTANCE = null;
    
    private SupabaseClientProvider() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final io.github.jan.supabase.SupabaseClient create() {
        return null;
    }
}