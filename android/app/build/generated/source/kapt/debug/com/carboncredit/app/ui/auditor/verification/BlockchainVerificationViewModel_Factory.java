package com.carboncredit.app.ui.auditor.verification;

import com.carboncredit.app.data.repository.BlockchainRepository;
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
public final class BlockchainVerificationViewModel_Factory implements Factory<BlockchainVerificationViewModel> {
  private final Provider<BlockchainRepository> blockchainRepositoryProvider;

  private final Provider<CreditRepository> creditRepositoryProvider;

  public BlockchainVerificationViewModel_Factory(
      Provider<BlockchainRepository> blockchainRepositoryProvider,
      Provider<CreditRepository> creditRepositoryProvider) {
    this.blockchainRepositoryProvider = blockchainRepositoryProvider;
    this.creditRepositoryProvider = creditRepositoryProvider;
  }

  @Override
  public BlockchainVerificationViewModel get() {
    return newInstance(blockchainRepositoryProvider.get(), creditRepositoryProvider.get());
  }

  public static BlockchainVerificationViewModel_Factory create(
      Provider<BlockchainRepository> blockchainRepositoryProvider,
      Provider<CreditRepository> creditRepositoryProvider) {
    return new BlockchainVerificationViewModel_Factory(blockchainRepositoryProvider, creditRepositoryProvider);
  }

  public static BlockchainVerificationViewModel newInstance(
      BlockchainRepository blockchainRepository, CreditRepository creditRepository) {
    return new BlockchainVerificationViewModel(blockchainRepository, creditRepository);
  }
}
