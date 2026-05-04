package com.carboncredit.app.data.repository;

import com.carboncredit.app.core.network.ApiService;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
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
public final class BlockchainRepository_Factory implements Factory<BlockchainRepository> {
  private final Provider<ApiService> apiServiceProvider;

  public BlockchainRepository_Factory(Provider<ApiService> apiServiceProvider) {
    this.apiServiceProvider = apiServiceProvider;
  }

  @Override
  public BlockchainRepository get() {
    return newInstance(apiServiceProvider.get());
  }

  public static BlockchainRepository_Factory create(Provider<ApiService> apiServiceProvider) {
    return new BlockchainRepository_Factory(apiServiceProvider);
  }

  public static BlockchainRepository newInstance(ApiService apiService) {
    return new BlockchainRepository(apiService);
  }
}
