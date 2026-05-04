package com.carboncredit.app.data.repository;

import com.carboncredit.app.core.network.ApiService;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import io.github.jan.supabase.SupabaseClient;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast"
})
public final class NotificationRepository_Factory implements Factory<NotificationRepository> {
  private final Provider<SupabaseClient> supabaseProvider;

  private final Provider<ApiService> apiServiceProvider;

  public NotificationRepository_Factory(Provider<SupabaseClient> supabaseProvider,
      Provider<ApiService> apiServiceProvider) {
    this.supabaseProvider = supabaseProvider;
    this.apiServiceProvider = apiServiceProvider;
  }

  @Override
  public NotificationRepository get() {
    return newInstance(supabaseProvider.get(), apiServiceProvider.get());
  }

  public static NotificationRepository_Factory create(Provider<SupabaseClient> supabaseProvider,
      Provider<ApiService> apiServiceProvider) {
    return new NotificationRepository_Factory(supabaseProvider, apiServiceProvider);
  }

  public static NotificationRepository newInstance(SupabaseClient supabase, ApiService apiService) {
    return new NotificationRepository(supabase, apiService);
  }
}
