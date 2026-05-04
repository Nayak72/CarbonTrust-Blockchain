package com.carboncredit.app.data.repository;

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
public final class CreditRepository_Factory implements Factory<CreditRepository> {
  private final Provider<SupabaseClient> supabaseProvider;

  public CreditRepository_Factory(Provider<SupabaseClient> supabaseProvider) {
    this.supabaseProvider = supabaseProvider;
  }

  @Override
  public CreditRepository get() {
    return newInstance(supabaseProvider.get());
  }

  public static CreditRepository_Factory create(Provider<SupabaseClient> supabaseProvider) {
    return new CreditRepository_Factory(supabaseProvider);
  }

  public static CreditRepository newInstance(SupabaseClient supabase) {
    return new CreditRepository(supabase);
  }
}
