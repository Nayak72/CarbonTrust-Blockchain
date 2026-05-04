package com.carboncredit.app.di

import com.carboncredit.app.core.network.ApiService
import com.carboncredit.app.core.network.RetrofitClientProvider
import com.carboncredit.app.core.network.SupabaseClientProvider
import com.carboncredit.app.core.security.TokenManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.jan.supabase.SupabaseClient
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideSupabaseClient(): SupabaseClient {
        return SupabaseClientProvider.create()
    }

    @Provides
    @Singleton
    fun provideRetrofit(tokenManager: TokenManager): Retrofit {
        return RetrofitClientProvider.create(tokenManager)
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }
}
