package com.carboncredit.app.ui.manager.anomalies;

import com.carboncredit.app.core.security.TokenManager;
import com.carboncredit.app.data.repository.AnomalyRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
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
public final class AnomalyLogViewModel_Factory implements Factory<AnomalyLogViewModel> {
  private final Provider<TokenManager> tokenManagerProvider;

  private final Provider<AnomalyRepository> anomalyRepositoryProvider;

  public AnomalyLogViewModel_Factory(Provider<TokenManager> tokenManagerProvider,
      Provider<AnomalyRepository> anomalyRepositoryProvider) {
    this.tokenManagerProvider = tokenManagerProvider;
    this.anomalyRepositoryProvider = anomalyRepositoryProvider;
  }

  @Override
  public AnomalyLogViewModel get() {
    return newInstance(tokenManagerProvider.get(), anomalyRepositoryProvider.get());
  }

  public static AnomalyLogViewModel_Factory create(Provider<TokenManager> tokenManagerProvider,
      Provider<AnomalyRepository> anomalyRepositoryProvider) {
    return new AnomalyLogViewModel_Factory(tokenManagerProvider, anomalyRepositoryProvider);
  }

  public static AnomalyLogViewModel newInstance(TokenManager tokenManager,
      AnomalyRepository anomalyRepository) {
    return new AnomalyLogViewModel(tokenManager, anomalyRepository);
  }
}
