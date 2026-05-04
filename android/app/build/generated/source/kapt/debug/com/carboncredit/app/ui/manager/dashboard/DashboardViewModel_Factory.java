package com.carboncredit.app.ui.manager.dashboard;

import com.carboncredit.app.core.security.TokenManager;
import com.carboncredit.app.data.repository.AnomalyRepository;
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
public final class DashboardViewModel_Factory implements Factory<DashboardViewModel> {
  private final Provider<TokenManager> tokenManagerProvider;

  private final Provider<FacilityRepository> facilityRepositoryProvider;

  private final Provider<SensorRepository> sensorRepositoryProvider;

  private final Provider<CreditRepository> creditRepositoryProvider;

  private final Provider<AnomalyRepository> anomalyRepositoryProvider;

  public DashboardViewModel_Factory(Provider<TokenManager> tokenManagerProvider,
      Provider<FacilityRepository> facilityRepositoryProvider,
      Provider<SensorRepository> sensorRepositoryProvider,
      Provider<CreditRepository> creditRepositoryProvider,
      Provider<AnomalyRepository> anomalyRepositoryProvider) {
    this.tokenManagerProvider = tokenManagerProvider;
    this.facilityRepositoryProvider = facilityRepositoryProvider;
    this.sensorRepositoryProvider = sensorRepositoryProvider;
    this.creditRepositoryProvider = creditRepositoryProvider;
    this.anomalyRepositoryProvider = anomalyRepositoryProvider;
  }

  @Override
  public DashboardViewModel get() {
    return newInstance(tokenManagerProvider.get(), facilityRepositoryProvider.get(), sensorRepositoryProvider.get(), creditRepositoryProvider.get(), anomalyRepositoryProvider.get());
  }

  public static DashboardViewModel_Factory create(Provider<TokenManager> tokenManagerProvider,
      Provider<FacilityRepository> facilityRepositoryProvider,
      Provider<SensorRepository> sensorRepositoryProvider,
      Provider<CreditRepository> creditRepositoryProvider,
      Provider<AnomalyRepository> anomalyRepositoryProvider) {
    return new DashboardViewModel_Factory(tokenManagerProvider, facilityRepositoryProvider, sensorRepositoryProvider, creditRepositoryProvider, anomalyRepositoryProvider);
  }

  public static DashboardViewModel newInstance(TokenManager tokenManager,
      FacilityRepository facilityRepository, SensorRepository sensorRepository,
      CreditRepository creditRepository, AnomalyRepository anomalyRepository) {
    return new DashboardViewModel(tokenManager, facilityRepository, sensorRepository, creditRepository, anomalyRepository);
  }
}
