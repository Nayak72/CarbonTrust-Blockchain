package com.carboncredit.app.ui.manager.profile;

import androidx.lifecycle.ViewModel;
import com.carboncredit.app.core.security.TokenManager;
import com.carboncredit.app.data.models.Facility;
import com.carboncredit.app.data.models.UserProfile;
import com.carboncredit.app.data.repository.AuthRepository;
import com.carboncredit.app.data.repository.FacilityRepository;
import dagger.hilt.android.lifecycle.HiltViewModel;
import kotlinx.coroutines.flow.StateFlow;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0017\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\b\u0018\u00002\u00020\u0001BQ\u0012\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0007\u0012\b\b\u0002\u0010\b\u001a\u00020\u0007\u0012\b\b\u0002\u0010\t\u001a\u00020\u0007\u0012\n\b\u0002\u0010\n\u001a\u0004\u0018\u00010\u000b\u0012\b\b\u0002\u0010\f\u001a\u00020\u0007\u00a2\u0006\u0002\u0010\rJ\u000b\u0010\u0018\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\u000b\u0010\u0019\u001a\u0004\u0018\u00010\u0005H\u00c6\u0003J\t\u0010\u001a\u001a\u00020\u0007H\u00c6\u0003J\t\u0010\u001b\u001a\u00020\u0007H\u00c6\u0003J\t\u0010\u001c\u001a\u00020\u0007H\u00c6\u0003J\u000b\u0010\u001d\u001a\u0004\u0018\u00010\u000bH\u00c6\u0003J\t\u0010\u001e\u001a\u00020\u0007H\u00c6\u0003JU\u0010\u001f\u001a\u00020\u00002\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00072\b\b\u0002\u0010\b\u001a\u00020\u00072\b\b\u0002\u0010\t\u001a\u00020\u00072\n\b\u0002\u0010\n\u001a\u0004\u0018\u00010\u000b2\b\b\u0002\u0010\f\u001a\u00020\u0007H\u00c6\u0001J\u0013\u0010 \u001a\u00020\u00072\b\u0010!\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\"\u001a\u00020#H\u00d6\u0001J\t\u0010$\u001a\u00020\u000bH\u00d6\u0001R\u0013\u0010\n\u001a\u0004\u0018\u00010\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u0013\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R\u0011\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0012R\u0011\u0010\f\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0012R\u0011\u0010\t\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0012R\u0011\u0010\b\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0012R\u0013\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0017\u00a8\u0006%"}, d2 = {"Lcom/carboncredit/app/ui/manager/profile/ProfileUiState;", "", "profile", "Lcom/carboncredit/app/data/models/UserProfile;", "facility", "Lcom/carboncredit/app/data/models/Facility;", "isLoading", "", "passwordUpdating", "passwordSuccess", "error", "", "loggedOut", "(Lcom/carboncredit/app/data/models/UserProfile;Lcom/carboncredit/app/data/models/Facility;ZZZLjava/lang/String;Z)V", "getError", "()Ljava/lang/String;", "getFacility", "()Lcom/carboncredit/app/data/models/Facility;", "()Z", "getLoggedOut", "getPasswordSuccess", "getPasswordUpdating", "getProfile", "()Lcom/carboncredit/app/data/models/UserProfile;", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "copy", "equals", "other", "hashCode", "", "toString", "app_debug"})
public final class ProfileUiState {
    @org.jetbrains.annotations.Nullable()
    private final com.carboncredit.app.data.models.UserProfile profile = null;
    @org.jetbrains.annotations.Nullable()
    private final com.carboncredit.app.data.models.Facility facility = null;
    private final boolean isLoading = false;
    private final boolean passwordUpdating = false;
    private final boolean passwordSuccess = false;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String error = null;
    private final boolean loggedOut = false;
    
    public ProfileUiState(@org.jetbrains.annotations.Nullable()
    com.carboncredit.app.data.models.UserProfile profile, @org.jetbrains.annotations.Nullable()
    com.carboncredit.app.data.models.Facility facility, boolean isLoading, boolean passwordUpdating, boolean passwordSuccess, @org.jetbrains.annotations.Nullable()
    java.lang.String error, boolean loggedOut) {
        super();
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.carboncredit.app.data.models.UserProfile getProfile() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.carboncredit.app.data.models.Facility getFacility() {
        return null;
    }
    
    public final boolean isLoading() {
        return false;
    }
    
    public final boolean getPasswordUpdating() {
        return false;
    }
    
    public final boolean getPasswordSuccess() {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getError() {
        return null;
    }
    
    public final boolean getLoggedOut() {
        return false;
    }
    
    public ProfileUiState() {
        super();
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.carboncredit.app.data.models.UserProfile component1() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.carboncredit.app.data.models.Facility component2() {
        return null;
    }
    
    public final boolean component3() {
        return false;
    }
    
    public final boolean component4() {
        return false;
    }
    
    public final boolean component5() {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component6() {
        return null;
    }
    
    public final boolean component7() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.carboncredit.app.ui.manager.profile.ProfileUiState copy(@org.jetbrains.annotations.Nullable()
    com.carboncredit.app.data.models.UserProfile profile, @org.jetbrains.annotations.Nullable()
    com.carboncredit.app.data.models.Facility facility, boolean isLoading, boolean passwordUpdating, boolean passwordSuccess, @org.jetbrains.annotations.Nullable()
    java.lang.String error, boolean loggedOut) {
        return null;
    }
    
    @java.lang.Override()
    public boolean equals(@org.jetbrains.annotations.Nullable()
    java.lang.Object other) {
        return false;
    }
    
    @java.lang.Override()
    public int hashCode() {
        return 0;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public java.lang.String toString() {
        return null;
    }
}