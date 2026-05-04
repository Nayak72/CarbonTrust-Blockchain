package com.carboncredit.app.ui.manager.credits;

import com.carboncredit.app.data.repository.CreditRepository;
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
public final class CreditDetailViewModel_Factory implements Factory<CreditDetailViewModel> {
  private final Provider<CreditRepository> creditRepositoryProvider;

  public CreditDetailViewModel_Factory(Provider<CreditRepository> creditRepositoryProvider) {
    this.creditRepositoryProvider = creditRepositoryProvider;
  }

  @Override
  public CreditDetailViewModel get() {
    return newInstance(creditRepositoryProvider.get());
  }

  public static CreditDetailViewModel_Factory create(
      Provider<CreditRepository> creditRepositoryProvider) {
    return new CreditDetailViewModel_Factory(creditRepositoryProvider);
  }

  public static CreditDetailViewModel newInstance(CreditRepository creditRepository) {
    return new CreditDetailViewModel(creditRepository);
  }
}
