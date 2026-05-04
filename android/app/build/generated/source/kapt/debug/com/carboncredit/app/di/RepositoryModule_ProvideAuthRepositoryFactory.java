package com.carboncredit.app.di;

import com.carboncredit.app.core.security.TokenManager;
import com.carboncredit.app.data.repository.AuthRepository;
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
public final class RepositoryModule_ProvideAuthRepositoryFactory implements Factory<AuthRepository> {
  private final Provider<SupabaseClient> supabaseClientProvider;

  private final Provider<TokenManager> tokenManagerProvider;

  public RepositoryModule_ProvideAuthRepositoryFactory(
      Provider<SupabaseClient> supabaseClientProvider,
      Provider<TokenManager> tokenManagerProvider) {
    this.supabaseClientProvider = supabaseClientProvider;
    this.tokenManagerProvider = tokenManagerProvider;
  }

  @Override
  public AuthRepository get() {
    return provideAuthRepository(supabaseClientProvider.get(), tokenManagerProvider.get());
  }

  public static RepositoryModule_ProvideAuthRepositoryFactory create(
      Provider<SupabaseClient> supabaseClientProvider,
      Provider<TokenManager> tokenManagerProvider) {
    return new RepositoryModule_ProvideAuthRepositoryFactory(supabaseClientProvider, tokenManagerProvider);
  }

  public static AuthRepository provideAuthRepository(SupabaseClient supabaseClient,
      TokenManager tokenManager) {
    return Preconditions.checkNotNullFromProvides(RepositoryModule.INSTANCE.provideAuthRepository(supabaseClient, tokenManager));
  }
}
