package com.carboncredit.app.services;

import com.carboncredit.app.core.security.TokenManager;
import com.carboncredit.app.data.repository.NotificationRepository;
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
public final class FCMService_MembersInjector implements MembersInjector<FCMService> {
  private final Provider<NotificationRepository> notificationRepositoryProvider;

  private final Provider<TokenManager> tokenManagerProvider;

  public FCMService_MembersInjector(Provider<NotificationRepository> notificationRepositoryProvider,
      Provider<TokenManager> tokenManagerProvider) {
    this.notificationRepositoryProvider = notificationRepositoryProvider;
    this.tokenManagerProvider = tokenManagerProvider;
  }

  public static MembersInjector<FCMService> create(
      Provider<NotificationRepository> notificationRepositoryProvider,
      Provider<TokenManager> tokenManagerProvider) {
    return new FCMService_MembersInjector(notificationRepositoryProvider, tokenManagerProvider);
  }

  @Override
  public void injectMembers(FCMService instance) {
    injectNotificationRepository(instance, notificationRepositoryProvider.get());
    injectTokenManager(instance, tokenManagerProvider.get());
  }

  @InjectedFieldSignature("com.carboncredit.app.services.FCMService.notificationRepository")
  public static void injectNotificationRepository(FCMService instance,
      NotificationRepository notificationRepository) {
    instance.notificationRepository = notificationRepository;
  }

  @InjectedFieldSignature("com.carboncredit.app.services.FCMService.tokenManager")
  public static void injectTokenManager(FCMService instance, TokenManager tokenManager) {
    instance.tokenManager = tokenManager;
  }
}
