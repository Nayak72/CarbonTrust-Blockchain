package com.carboncredit.app.data.repository;

import com.carboncredit.app.core.security.TokenManager;
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
public final class AuthRepository_Factory implements Factory<AuthRepository> {
  private final Provider<SupabaseClient> supabaseProvider;

  private final Provider<TokenManager> tokenManagerProvider;

  public AuthRepository_Factory(Provider<SupabaseClient> supabaseProvider,
      Provider<TokenManager> tokenManagerProvider) {
    this.supabaseProvider = supabaseProvider;
    this.tokenManagerProvider = tokenManagerProvider;
  }

  @Override
  public AuthRepository get() {
    return newInstance(supabaseProvider.get(), tokenManagerProvider.get());
  }

  public static AuthRepository_Factory create(Provider<SupabaseClient> supabaseProvider,
      Provider<TokenManager> tokenManagerProvider) {
    return new AuthRepository_Factory(supabaseProvider, tokenManagerProvider);
  }

  public static AuthRepository newInstance(SupabaseClient supabase, TokenManager tokenManager) {
    return new AuthRepository(supabase, tokenManager);
  }
}
