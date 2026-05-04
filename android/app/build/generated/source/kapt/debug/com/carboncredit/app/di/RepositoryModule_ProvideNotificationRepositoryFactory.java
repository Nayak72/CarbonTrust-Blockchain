package com.carboncredit.app.di;

import com.carboncredit.app.core.network.ApiService;
import com.carboncredit.app.data.repository.NotificationRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class RepositoryModule_ProvideNotificationRepositoryFactory implements Factory<NotificationRepository> {
  private final Provider<SupabaseClient> supabaseClientProvider;

  private final Provider<ApiService> apiServiceProvider;

  public RepositoryModule_ProvideNotificationRepositoryFactory(
      Provider<SupabaseClient> supabaseClientProvider, Provider<ApiService> apiServiceProvider) {
    this.supabaseClientProvider = supabaseClientProvider;
    this.apiServiceProvider = apiServiceProvider;
  }

  @Override
  public NotificationRepository get() {
    return provideNotificationRepository(supabaseClientProvider.get(), apiServiceProvider.get());
  }

  public static RepositoryModule_ProvideNotificationRepositoryFactory create(
      Provider<SupabaseClient> supabaseClientProvider, Provider<ApiService> apiServiceProvider) {
    return new RepositoryModule_ProvideNotificationRepositoryFactory(supabaseClientProvider, apiServiceProvider);
  }

  public static NotificationRepository provideNotificationRepository(SupabaseClient supabaseClient,
      ApiService apiService) {
    return Preconditions.checkNotNullFromProvides(RepositoryModule.INSTANCE.provideNotificationRepository(supabaseClient, apiService));
  }
}
