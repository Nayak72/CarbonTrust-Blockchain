package com.carboncredit.app.ui.manager.notifications;

import com.carboncredit.app.core.security.TokenManager;
import com.carboncredit.app.data.repository.NotificationRepository;
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
public final class NotificationsViewModel_Factory implements Factory<NotificationsViewModel> {
  private final Provider<TokenManager> tokenManagerProvider;

  private final Provider<NotificationRepository> notificationRepositoryProvider;

  public NotificationsViewModel_Factory(Provider<TokenManager> tokenManagerProvider,
      Provider<NotificationRepository> notificationRepositoryProvider) {
    this.tokenManagerProvider = tokenManagerProvider;
    this.notificationRepositoryProvider = notificationRepositoryProvider;
  }

  @Override
  public NotificationsViewModel get() {
    return newInstance(tokenManagerProvider.get(), notificationRepositoryProvider.get());
  }

  public static NotificationsViewModel_Factory create(Provider<TokenManager> tokenManagerProvider,
      Provider<NotificationRepository> notificationRepositoryProvider) {
    return new NotificationsViewModel_Factory(tokenManagerProvider, notificationRepositoryProvider);
  }

  public static NotificationsViewModel newInstance(TokenManager tokenManager,
      NotificationRepository notificationRepository) {
    return new NotificationsViewModel(tokenManager, notificationRepository);
  }
}
