package com.carboncredit.app.ui.manager.credits;

import com.carboncredit.app.core.security.TokenManager;
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
public final class CreditLedgerViewModel_Factory implements Factory<CreditLedgerViewModel> {
  private final Provider<TokenManager> tokenManagerProvider;

  private final Provider<CreditRepository> creditRepositoryProvider;

  public CreditLedgerViewModel_Factory(Provider<TokenManager> tokenManagerProvider,
      Provider<CreditRepository> creditRepositoryProvider) {
    this.tokenManagerProvider = tokenManagerProvider;
    this.creditRepositoryProvider = creditRepositoryProvider;
  }

  @Override
  public CreditLedgerViewModel get() {
    return newInstance(tokenManagerProvider.get(), creditRepositoryProvider.get());
  }

  public static CreditLedgerViewModel_Factory create(Provider<TokenManager> tokenManagerProvider,
      Provider<CreditRepository> creditRepositoryProvider) {
    return new CreditLedgerViewModel_Factory(tokenManagerProvider, creditRepositoryProvider);
  }

  public static CreditLedgerViewModel newInstance(TokenManager tokenManager,
      CreditRepository creditRepository) {
    return new CreditLedgerViewModel(tokenManager, creditRepository);
  }
}
