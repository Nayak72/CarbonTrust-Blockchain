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
public final class SensorRepository_Factory implements Factory<SensorRepository> {
  private final Provider<SupabaseClient> supabaseProvider;

  public SensorRepository_Factory(Provider<SupabaseClient> supabaseProvider) {
    this.supabaseProvider = supabaseProvider;
  }

  @Override
  public SensorRepository get() {
    return newInstance(supabaseProvider.get());
  }

  public static SensorRepository_Factory create(Provider<SupabaseClient> supabaseProvider) {
    return new SensorRepository_Factory(supabaseProvider);
  }

  public static SensorRepository newInstance(SupabaseClient supabase) {
    return new SensorRepository(supabase);
  }
}
