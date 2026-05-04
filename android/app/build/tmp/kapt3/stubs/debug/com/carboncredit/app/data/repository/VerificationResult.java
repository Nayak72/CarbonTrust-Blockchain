package com.carboncredit.app.data.repository;

import com.carboncredit.app.BuildConfig;
import com.carboncredit.app.core.network.ApiService;
import com.carboncredit.app.core.utils.HashUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import javax.inject.Inject;
import javax.inject.Singleton;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0005\b\u0086\u0081\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004j\u0002\b\u0005\u00a8\u0006\u0006"}, d2 = {"Lcom/carboncredit/app/data/repository/VerificationResult;", "", "(Ljava/lang/String;I)V", "INTACT", "TAMPERED", "ERROR", "app_debug"})
public enum VerificationResult {
    /*public static final*/ INTACT /* = new INTACT() */,
    /*public static final*/ TAMPERED /* = new TAMPERED() */,
    /*public static final*/ ERROR /* = new ERROR() */;
    
    VerificationResult() {
    }
    
    @org.jetbrains.annotations.NotNull()
    public static kotlin.enums.EnumEntries<com.carboncredit.app.data.repository.VerificationResult> getEntries() {
        return null;
    }
}