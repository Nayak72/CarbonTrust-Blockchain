package com.carboncredit.app.ui.manager.sensors;

import com.carboncredit.app.core.security.TokenManager;
import com.carboncredit.app.data.repository.SensorRepository;
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
public final class SensorListViewModel_Factory implements Factory<SensorListViewModel> {
  private final Provider<TokenManager> tokenManagerProvider;

  private final Provider<SensorRepository> sensorRepositoryProvider;

  public SensorListViewModel_Factory(Provider<TokenManager> tokenManagerProvider,
      Provider<SensorRepository> sensorRepositoryProvider) {
    this.tokenManagerProvider = tokenManagerProvider;
    this.sensorRepositoryProvider = sensorRepositoryProvider;
  }

  @Override
  public SensorListViewModel get() {
    return newInstance(tokenManagerProvider.get(), sensorRepositoryProvider.get());
  }

  public static SensorListViewModel_Factory create(Provider<TokenManager> tokenManagerProvider,
      Provider<SensorRepository> sensorRepositoryProvider) {
    return new SensorListViewModel_Factory(tokenManagerProvider, sensorRepositoryProvider);
  }

  public static SensorListViewModel newInstance(TokenManager tokenManager,
      SensorRepository sensorRepository) {
    return new SensorListViewModel(tokenManager, sensorRepository);
  }
}
