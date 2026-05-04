package com.carboncredit.app.di;

import com.carboncredit.app.core.security.TokenManager;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;
import retrofit2.Retrofit;

@ScopeMetadata("javax.inject.Singleton")
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
public final class NetworkModule_ProvideRetrofitFactory implements Factory<Retrofit> {
  private final Provider<TokenManager> tokenManagerProvider;

  public NetworkModule_ProvideRetrofitFactory(Provider<TokenManager> tokenManagerProvider) {
    this.tokenManagerProvider = tokenManagerProvider;
  }

  @Override
  public Retrofit get() {
    return provideRetrofit(tokenManagerProvider.get());
  }

  public static NetworkModule_ProvideRetrofitFactory create(
      Provider<TokenManager> tokenManagerProvider) {
    return new NetworkModule_ProvideRetrofitFactory(tokenManagerProvider);
  }

  public static Retrofit provideRetrofit(TokenManager tokenManager) {
    return Preconditions.checkNotNullFromProvides(NetworkModule.INSTANCE.provideRetrofit(tokenManager));
  }
}
