package com.carboncredit.app.ui.auditor.dashboard;

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
public final class AuditorDashboardViewModel_Factory implements Factory<AuditorDashboardViewModel> {
  private final Provider<TokenManager> tokenManagerProvider;

  private final Provider<FacilityRepository> facilityRepositoryProvider;

  private final Provider<CreditRepository> creditRepositoryProvider;

  private final Provider<AnomalyRepository> anomalyRepositoryProvider;

  private final Provider<SensorRepository> sensorRepositoryProvider;

  public AuditorDashboardViewModel_Factory(Provider<TokenManager> tokenManagerProvider,
      Provider<FacilityRepository> facilityRepositoryProvider,
      Provider<CreditRepository> creditRepositoryProvider,
      Provider<AnomalyRepository> anomalyRepositoryProvider,
      Provider<SensorRepository> sensorRepositoryProvider) {
    this.tokenManagerProvider = tokenManagerProvider;
    this.facilityRepositoryProvider = facilityRepositoryProvider;
    this.creditRepositoryProvider = creditRepositoryProvider;
    this.anomalyRepositoryProvider = anomalyRepositoryProvider;
    this.sensorRepositoryProvider = sensorRepositoryProvider;
  }

  @Override
  public AuditorDashboardViewModel get() {
    return newInstance(tokenManagerProvider.get(), facilityRepositoryProvider.get(), creditRepositoryProvider.get(), anomalyRepositoryProvider.get(), sensorRepositoryProvider.get());
  }

  public static AuditorDashboardViewModel_Factory create(
      Provider<TokenManager> tokenManagerProvider,
      Provider<FacilityRepository> facilityRepositoryProvider,
      Provider<CreditRepository> creditRepositoryProvider,
      Provider<AnomalyRepository> anomalyRepositoryProvider,
      Provider<SensorRepository> sensorRepositoryProvider) {
    return new AuditorDashboardViewModel_Factory(tokenManagerProvider, facilityRepositoryProvider, creditRepositoryProvider, anomalyRepositoryProvider, sensorRepositoryProvider);
  }

  public static AuditorDashboardViewModel newInstance(TokenManager tokenManager,
      FacilityRepository facilityRepository, CreditRepository creditRepository,
      AnomalyRepository anomalyRepository, SensorRepository sensorRepository) {
    return new AuditorDashboardViewModel(tokenManager, facilityRepository, creditRepository, anomalyRepository, sensorRepository);
  }
}
