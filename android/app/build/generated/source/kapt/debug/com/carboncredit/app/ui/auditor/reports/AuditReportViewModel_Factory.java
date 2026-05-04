package com.carboncredit.app.ui.auditor.reports;

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
public final class AuditReportViewModel_Factory implements Factory<AuditReportViewModel> {
  private final Provider<TokenManager> tokenManagerProvider;

  private final Provider<FacilityRepository> facilityRepositoryProvider;

  private final Provider<SensorRepository> sensorRepositoryProvider;

  private final Provider<AnomalyRepository> anomalyRepositoryProvider;

  private final Provider<CreditRepository> creditRepositoryProvider;

  public AuditReportViewModel_Factory(Provider<TokenManager> tokenManagerProvider,
      Provider<FacilityRepository> facilityRepositoryProvider,
      Provider<SensorRepository> sensorRepositoryProvider,
      Provider<AnomalyRepository> anomalyRepositoryProvider,
      Provider<CreditRepository> creditRepositoryProvider) {
    this.tokenManagerProvider = tokenManagerProvider;
    this.facilityRepositoryProvider = facilityRepositoryProvider;
    this.sensorRepositoryProvider = sensorRepositoryProvider;
    this.anomalyRepositoryProvider = anomalyRepositoryProvider;
    this.creditRepositoryProvider = creditRepositoryProvider;
  }

  @Override
  public AuditReportViewModel get() {
    return newInstance(tokenManagerProvider.get(), facilityRepositoryProvider.get(), sensorRepositoryProvider.get(), anomalyRepositoryProvider.get(), creditRepositoryProvider.get());
  }

  public static AuditReportViewModel_Factory create(Provider<TokenManager> tokenManagerProvider,
      Provider<FacilityRepository> facilityRepositoryProvider,
      Provider<SensorRepository> sensorRepositoryProvider,
      Provider<AnomalyRepository> anomalyRepositoryProvider,
      Provider<CreditRepository> creditRepositoryProvider) {
    return new AuditReportViewModel_Factory(tokenManagerProvider, facilityRepositoryProvider, sensorRepositoryProvider, anomalyRepositoryProvider, creditRepositoryProvider);
  }

  public static AuditReportViewModel newInstance(TokenManager tokenManager,
      FacilityRepository facilityRepository, SensorRepository sensorRepository,
      AnomalyRepository anomalyRepository, CreditRepository creditRepository) {
    return new AuditReportViewModel(tokenManager, facilityRepository, sensorRepository, anomalyRepository, creditRepository);
  }
}
