package com.carboncredit.app.ui.splash;

import com.carboncredit.app.core.security.TokenManager;
import dagger.MembersInjector;
import dagger.internal.DaggerGenerated;
import dagger.internal.InjectedFieldSignature;
import dagger.internal.QualifierMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class SplashActivity_MembersInjector implements MembersInjector<SplashActivity> {
  private final Provider<TokenManager> tokenManagerProvider;

  public SplashActivity_MembersInjector(Provider<TokenManager> tokenManagerProvider) {
    this.tokenManagerProvider = tokenManagerProvider;
  }

  public static MembersInjector<SplashActivity> create(
      Provider<TokenManager> tokenManagerProvider) {
    return new SplashActivity_MembersInjector(tokenManagerProvider);
  }

  @Override
  public void injectMembers(SplashActivity instance) {
    injectTokenManager(instance, tokenManagerProvider.get());
  }

  @InjectedFieldSignature("com.carboncredit.app.ui.splash.SplashActivity.tokenManager")
  public static void injectTokenManager(SplashActivity instance, TokenManager tokenManager) {
    instance.tokenManager = tokenManager;
  }
}
