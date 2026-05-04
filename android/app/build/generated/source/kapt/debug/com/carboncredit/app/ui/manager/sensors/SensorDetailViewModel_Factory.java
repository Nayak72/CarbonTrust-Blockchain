package com.carboncredit.app.ui.manager.sensors;

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
public final class SensorDetailViewModel_Factory implements Factory<SensorDetailViewModel> {
  private final Provider<SensorRepository> sensorRepositoryProvider;

  public SensorDetailViewModel_Factory(Provider<SensorRepository> sensorRepositoryProvider) {
    this.sensorRepositoryProvider = sensorRepositoryProvider;
  }

  @Override
  public SensorDetailViewModel get() {
    return newInstance(sensorRepositoryProvider.get());
  }

  public static SensorDetailViewModel_Factory create(
      Provider<SensorRepository> sensorRepositoryProvider) {
    return new SensorDetailViewModel_Factory(sensorRepositoryProvider);
  }

  public static SensorDetailViewModel newInstance(SensorRepository sensorRepository) {
    return new SensorDetailViewModel(sensorRepository);
  }
}
