package com.carboncredit.app.core.network;

import com.carboncredit.app.BuildConfig;
import com.carboncredit.app.core.security.TokenManager;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import java.util.concurrent.TimeUnit;

/**
 * Retrofit client factory for FastAPI backend communication.
 * Provided via Hilt DI — see NetworkModule.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006\u00a8\u0006\u0007"}, d2 = {"Lcom/carboncredit/app/core/network/RetrofitClientProvider;", "", "()V", "create", "Lretrofit2/Retrofit;", "tokenManager", "Lcom/carboncredit/app/core/security/TokenManager;", "app_debug"})
public final class RetrofitClientProvider {
    @org.jetbrains.annotations.NotNull()
    public static final com.carboncredit.app.core.network.RetrofitClientProvider INSTANCE = null;
    
    private RetrofitClientProvider() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final retrofit2.Retrofit create(@org.jetbrains.annotations.NotNull()
    com.carboncredit.app.core.security.TokenManager tokenManager) {
        return null;
    }
}