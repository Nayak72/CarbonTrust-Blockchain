package com.carboncredit.app.di;

import com.carboncredit.app.core.network.ApiService;
import com.carboncredit.app.core.security.TokenManager;
import com.carboncredit.app.data.repository.*;
import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import io.github.jan.supabase.SupabaseClient;
import javax.inject.Singleton;

@dagger.Module()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000H\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0007J\u0018\u0010\u0007\u001a\u00020\b2\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\t\u001a\u00020\nH\u0007J\u0010\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000eH\u0007J\u0010\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0005\u001a\u00020\u0006H\u0007J\u0010\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0005\u001a\u00020\u0006H\u0007J\u0018\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\r\u001a\u00020\u000eH\u0007J\u0010\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0005\u001a\u00020\u0006H\u0007\u00a8\u0006\u0017"}, d2 = {"Lcom/carboncredit/app/di/RepositoryModule;", "", "()V", "provideAnomalyRepository", "Lcom/carboncredit/app/data/repository/AnomalyRepository;", "supabaseClient", "Lio/github/jan/supabase/SupabaseClient;", "provideAuthRepository", "Lcom/carboncredit/app/data/repository/AuthRepository;", "tokenManager", "Lcom/carboncredit/app/core/security/TokenManager;", "provideBlockchainRepository", "Lcom/carboncredit/app/data/repository/BlockchainRepository;", "apiService", "Lcom/carboncredit/app/core/network/ApiService;", "provideCreditRepository", "Lcom/carboncredit/app/data/repository/CreditRepository;", "provideFacilityRepository", "Lcom/carboncredit/app/data/repository/FacilityRepository;", "provideNotificationRepository", "Lcom/carboncredit/app/data/repository/NotificationRepository;", "provideSensorRepository", "Lcom/carboncredit/app/data/repository/SensorRepository;", "app_debug"})
@dagger.hilt.InstallIn(value = {dagger.hilt.components.SingletonComponent.class})
public final class RepositoryModule {
    @org.jetbrains.annotations.NotNull()
    public static final com.carboncredit.app.di.RepositoryModule INSTANCE = null;
    
    private RepositoryModule() {
        super();
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final com.carboncredit.app.data.repository.AuthRepository provideAuthRepository(@org.jetbrains.annotations.NotNull()
    io.github.jan.supabase.SupabaseClient supabaseClient, @org.jetbrains.annotations.NotNull()
    com.carboncredit.app.core.security.TokenManager tokenManager) {
        return null;
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final com.carboncredit.app.data.repository.SensorRepository provideSensorRepository(@org.jetbrains.annotations.NotNull()
    io.github.jan.supabase.SupabaseClient supabaseClient) {
        return null;
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final com.carboncredit.app.data.repository.CreditRepository provideCreditRepository(@org.jetbrains.annotations.NotNull()
    io.github.jan.supabase.SupabaseClient supabaseClient) {
        return null;
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final com.carboncredit.app.data.repository.AnomalyRepository provideAnomalyRepository(@org.jetbrains.annotations.NotNull()
    io.github.jan.supabase.SupabaseClient supabaseClient) {
        return null;
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final com.carboncredit.app.data.repository.FacilityRepository provideFacilityRepository(@org.jetbrains.annotations.NotNull()
    io.github.jan.supabase.SupabaseClient supabaseClient) {
        return null;
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final com.carboncredit.app.data.repository.NotificationRepository provideNotificationRepository(@org.jetbrains.annotations.NotNull()
    io.github.jan.supabase.SupabaseClient supabaseClient, @org.jetbrains.annotations.NotNull()
    com.carboncredit.app.core.network.ApiService apiService) {
        return null;
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final com.carboncredit.app.data.repository.BlockchainRepository provideBlockchainRepository(@org.jetbrains.annotations.NotNull()
    com.carboncredit.app.core.network.ApiService apiService) {
        return null;
    }
}