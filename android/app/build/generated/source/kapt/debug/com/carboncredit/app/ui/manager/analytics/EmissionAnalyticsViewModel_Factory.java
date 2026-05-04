package com.carboncredit.app.ui.manager.analytics;

import com.carboncredit.app.core.security.TokenManager;
import com.carboncredit.app.data.repository.CreditRepository;
import com.carboncredit.app.data.repository.FacilityRepository;
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
public final class EmissionAnalyticsViewModel_Factory implements Factory<EmissionAnalyticsViewModel> {
  private final Provider<TokenManager> tokenManagerProvider;

  private final Provider<SensorRepository> sensorRepositoryProvider;

  private final Provider<CreditRepository> creditRepositoryProvider;

  private final Provider<FacilityRepository> facilityRepositoryProvider;

  public EmissionAnalyticsViewModel_Factory(Provider<TokenManager> tokenManagerProvider,
      Provider<SensorRepository> sensorRepositoryProvider,
      Provider<CreditRepository> creditRepositoryProvider,
      Provider<FacilityRepository> facilityRepositoryProvider) {
    this.tokenManagerProvider = tokenManagerProvider;
    this.sensorRepositoryProvider = sensorRepositoryProvider;
    this.creditRepositoryProvider = creditRepositoryProvider;
    this.facilityRepositoryProvider = facilityRepositoryProvider;
  }

  @Override
  public EmissionAnalyticsViewModel get() {
    return newInstance(tokenManagerProvider.get(), sensorRepositoryProvider.get(), creditRepositoryProvider.get(), facilityRepositoryProvider.get());
  }

  public static EmissionAnalyticsViewModel_Factory create(
      Provider<TokenManager> tokenManagerProvider,
      Provider<SensorRepository> sensorRepositoryProvider,
      Provider<CreditRepository> creditRepositoryProvider,
      Provider<FacilityRepository> facilityRepositoryProvider) {
    return new EmissionAnalyticsViewModel_Factory(tokenManagerProvider, sensorRepositoryProvider, creditRepositoryProvider, facilityRepositoryProvider);
  }

  public static EmissionAnalyticsViewModel newInstance(TokenManager tokenManager,
      SensorRepository sensorRepository, CreditRepository creditRepository,
      FacilityRepository facilityRepository) {
    return new EmissionAnalyticsViewModel(tokenManager, sensorRepository, creditRepository, facilityRepository);
  }
}
