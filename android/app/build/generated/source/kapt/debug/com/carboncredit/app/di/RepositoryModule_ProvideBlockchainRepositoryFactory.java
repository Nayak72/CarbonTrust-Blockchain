package com.carboncredit.app.di;

import com.carboncredit.app.core.network.ApiService;
import com.carboncredit.app.data.repository.BlockchainRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
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
public final class RepositoryModule_ProvideBlockchainRepositoryFactory implements Factory<BlockchainRepository> {
  private final Provider<ApiService> apiServiceProvider;

  public RepositoryModule_ProvideBlockchainRepositoryFactory(
      Provider<ApiService> apiServiceProvider) {
    this.apiServiceProvider = apiServiceProvider;
  }

  @Override
  public BlockchainRepository get() {
    return provideBlockchainRepository(apiServiceProvider.get());
  }

  public static RepositoryModule_ProvideBlockchainRepositoryFactory create(
      Provider<ApiService> apiServiceProvider) {
    return new RepositoryModule_ProvideBlockchainRepositoryFactory(apiServiceProvider);
  }

  public static BlockchainRepository provideBlockchainRepository(ApiService apiService) {
    return Preconditions.checkNotNullFromProvides(RepositoryModule.INSTANCE.provideBlockchainRepository(apiService));
  }
}
