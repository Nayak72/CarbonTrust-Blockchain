package com.carboncredit.app.di;

import com.carboncredit.app.data.repository.FacilityRepository;
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
public final class RepositoryModule_ProvideFacilityRepositoryFactory implements Factory<FacilityRepository> {
  private final Provider<SupabaseClient> supabaseClientProvider;

  public RepositoryModule_ProvideFacilityRepositoryFactory(
      Provider<SupabaseClient> supabaseClientProvider) {
    this.supabaseClientProvider = supabaseClientProvider;
  }

  @Override
  public FacilityRepository get() {
    return provideFacilityRepository(supabaseClientProvider.get());
  }

  public static RepositoryModule_ProvideFacilityRepositoryFactory create(
      Provider<SupabaseClient> supabaseClientProvider) {
    return new RepositoryModule_ProvideFacilityRepositoryFactory(supabaseClientProvider);
  }

  public static FacilityRepository provideFacilityRepository(SupabaseClient supabaseClient) {
    return Preconditions.checkNotNullFromProvides(RepositoryModule.INSTANCE.provideFacilityRepository(supabaseClient));
  }
}
