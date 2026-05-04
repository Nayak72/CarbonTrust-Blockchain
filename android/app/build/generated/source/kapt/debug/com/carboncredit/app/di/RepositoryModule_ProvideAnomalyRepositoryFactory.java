package com.carboncredit.app.di;

import com.carboncredit.app.data.repository.AnomalyRepository;
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
public final class RepositoryModule_ProvideAnomalyRepositoryFactory implements Factory<AnomalyRepository> {
  private final Provider<SupabaseClient> supabaseClientProvider;

  public RepositoryModule_ProvideAnomalyRepositoryFactory(
      Provider<SupabaseClient> supabaseClientProvider) {
    this.supabaseClientProvider = supabaseClientProvider;
  }

  @Override
  public AnomalyRepository get() {
    return provideAnomalyRepository(supabaseClientProvider.get());
  }

  public static RepositoryModule_ProvideAnomalyRepositoryFactory create(
      Provider<SupabaseClient> supabaseClientProvider) {
    return new RepositoryModule_ProvideAnomalyRepositoryFactory(supabaseClientProvider);
  }

  public static AnomalyRepository provideAnomalyRepository(SupabaseClient supabaseClient) {
    return Preconditions.checkNotNullFromProvides(RepositoryModule.INSTANCE.provideAnomalyRepository(supabaseClient));
  }
}
