package com.carboncredit.app.ui.auditor.facility;

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
public final class FacilityDetailViewModel_Factory implements Factory<FacilityDetailViewModel> {
  private final Provider<FacilityRepository> facilityRepositoryProvider;

  private final Provider<SensorRepository> sensorRepositoryProvider;

  private final Provider<AnomalyRepository> anomalyRepositoryProvider;

  private final Provider<CreditRepository> creditRepositoryProvider;

  public FacilityDetailViewModel_Factory(Provider<FacilityRepository> facilityRepositoryProvider,
      Provider<SensorRepository> sensorRepositoryProvider,
      Provider<AnomalyRepository> anomalyRepositoryProvider,
      Provider<CreditRepository> creditRepositoryProvider) {
    this.facilityRepositoryProvider = facilityRepositoryProvider;
    this.sensorRepositoryProvider = sensorRepositoryProvider;
    this.anomalyRepositoryProvider = anomalyRepositoryProvider;
    this.creditRepositoryProvider = creditRepositoryProvider;
  }

  @Override
  public FacilityDetailViewModel get() {
    return newInstance(facilityRepositoryProvider.get(), sensorRepositoryProvider.get(), anomalyRepositoryProvider.get(), creditRepositoryProvider.get());
  }

  public static FacilityDetailViewModel_Factory create(
      Provider<FacilityRepository> facilityRepositoryProvider,
      Provider<SensorRepository> sensorRepositoryProvider,
      Provider<AnomalyRepository> anomalyRepositoryProvider,
      Provider<CreditRepository> creditRepositoryProvider) {
    return new FacilityDetailViewModel_Factory(facilityRepositoryProvider, sensorRepositoryProvider, anomalyRepositoryProvider, creditRepositoryProvider);
  }

  public static FacilityDetailViewModel newInstance(FacilityRepository facilityRepository,
      SensorRepository sensorRepository, AnomalyRepository anomalyRepository,
      CreditRepository creditRepository) {
    return new FacilityDetailViewModel(facilityRepository, sensorRepository, anomalyRepository, creditRepository);
  }
}
