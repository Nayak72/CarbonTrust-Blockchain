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
public final class FacilityRepository_Factory implements Factory<FacilityRepository> {
  private final Provider<SupabaseClient> supabaseProvider;

  public FacilityRepository_Factory(Provider<SupabaseClient> supabaseProvider) {
    this.supabaseProvider = supabaseProvider;
  }

  @Override
  public FacilityRepository get() {
    return newInstance(supabaseProvider.get());
  }

  public static FacilityRepository_Factory create(Provider<SupabaseClient> supabaseProvider) {
    return new FacilityRepository_Factory(supabaseProvider);
  }

  public static FacilityRepository newInstance(SupabaseClient supabase) {
    return new FacilityRepository(supabase);
  }
}
