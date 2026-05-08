package com.carboncredit.app.di

import com.carboncredit.app.core.network.ApiService
import com.carboncredit.app.core.security.TokenManager
import com.carboncredit.app.data.repository.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.jan.supabase.SupabaseClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideAuthRepository(
        supabaseClient: SupabaseClient,
        tokenManager: TokenManager,
        apiService: ApiService
    ): AuthRepository {
        return AuthRepository(supabaseClient, tokenManager, apiService)
    }

    @Provides
    @Singleton
    fun provideSensorRepository(supabaseClient: SupabaseClient): SensorRepository {
        return SensorRepository(supabaseClient)
    }

    @Provides
    @Singleton
    fun provideCreditRepository(supabaseClient: SupabaseClient): CreditRepository {
        return CreditRepository(supabaseClient)
    }

    @Provides
    @Singleton
    fun provideAnomalyRepository(supabaseClient: SupabaseClient): AnomalyRepository {
        return AnomalyRepository(supabaseClient)
    }

    @Provides
    @Singleton
    fun provideFacilityRepository(
        supabaseClient: SupabaseClient,
        apiService: ApiService
    ): FacilityRepository {
        return FacilityRepository(supabaseClient, apiService)
    }

    @Provides
    @Singleton
    fun provideNotificationRepository(
        supabaseClient: SupabaseClient,
        apiService: ApiService
    ): NotificationRepository {
        return NotificationRepository(supabaseClient, apiService)
    }

    @Provides
    @Singleton
    fun provideBlockchainRepository(apiService: ApiService): BlockchainRepository {
        return BlockchainRepository(apiService)
    }
}
