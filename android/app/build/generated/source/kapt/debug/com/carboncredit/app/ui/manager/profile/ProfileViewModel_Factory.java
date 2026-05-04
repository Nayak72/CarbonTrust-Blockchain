package com.carboncredit.app.ui.manager.profile;

import com.carboncredit.app.core.security.TokenManager;
import com.carboncredit.app.data.repository.AuthRepository;
import com.carboncredit.app.data.repository.FacilityRepository;
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
public final class ProfileViewModel_Factory implements Factory<ProfileViewModel> {
  private final Provider<TokenManager> tokenManagerProvider;

  private final Provider<AuthRepository> authRepositoryProvider;

  private final Provider<FacilityRepository> facilityRepositoryProvider;

  public ProfileViewModel_Factory(Provider<TokenManager> tokenManagerProvider,
      Provider<AuthRepository> authRepositoryProvider,
      Provider<FacilityRepository> facilityRepositoryProvider) {
    this.tokenManagerProvider = tokenManagerProvider;
    this.authRepositoryProvider = authRepositoryProvider;
    this.facilityRepositoryProvider = facilityRepositoryProvider;
  }

  @Override
  public ProfileViewModel get() {
    return newInstance(tokenManagerProvider.get(), authRepositoryProvider.get(), facilityRepositoryProvider.get());
  }

  public static ProfileViewModel_Factory create(Provider<TokenManager> tokenManagerProvider,
      Provider<AuthRepository> authRepositoryProvider,
      Provider<FacilityRepository> facilityRepositoryProvider) {
    return new ProfileViewModel_Factory(tokenManagerProvider, authRepositoryProvider, facilityRepositoryProvider);
  }

  public static ProfileViewModel newInstance(TokenManager tokenManager,
      AuthRepository authRepository, FacilityRepository facilityRepository) {
    return new ProfileViewModel(tokenManager, authRepository, facilityRepository);
  }
}
