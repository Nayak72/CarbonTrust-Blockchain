package com.carboncredit.app.di;

import com.carboncredit.app.data.repository.CreditRepository;
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
public final class RepositoryModule_ProvideCreditRepositoryFactory implements Factory<CreditRepository> {
  private final Provider<SupabaseClient> supabaseClientProvider;

  public RepositoryModule_ProvideCreditRepositoryFactory(
      Provider<SupabaseClient> supabaseClientProvider) {
    this.supabaseClientProvider = supabaseClientProvider;
  }

  @Override
  public CreditRepository get() {
    return provideCreditRepository(supabaseClientProvider.get());
  }

  public static RepositoryModule_ProvideCreditRepositoryFactory create(
      Provider<SupabaseClient> supabaseClientProvider) {
    return new RepositoryModule_ProvideCreditRepositoryFactory(supabaseClientProvider);
  }

  public static CreditRepository provideCreditRepository(SupabaseClient supabaseClient) {
    return Preconditions.checkNotNullFromProvides(RepositoryModule.INSTANCE.provideCreditRepository(supabaseClient));
  }
}
