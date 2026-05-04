package com.carboncredit.app.data.repository;

import com.carboncredit.app.BuildConfig;
import com.carboncredit.app.core.network.ApiService;
import com.carboncredit.app.core.utils.HashUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import javax.inject.Inject;
import javax.inject.Singleton;

@javax.inject.Singleton()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000N\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\b\u0007\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0018\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\f2\u0006\u0010\u000e\u001a\u00020\u000fH\u0002J\u0018\u0010\u0010\u001a\u00020\u00112\u0006\u0010\r\u001a\u00020\f2\u0006\u0010\u000e\u001a\u00020\u000fH\u0002J\u0018\u0010\u0012\u001a\u0004\u0018\u00010\u00132\u0006\u0010\u0014\u001a\u00020\fH\u0086@\u00a2\u0006\u0002\u0010\u0015J2\u0010\u0016\u001a\u00020\u00172\u0006\u0010\u0014\u001a\u00020\f2\u0006\u0010\u0018\u001a\u00020\f2\u0012\u0010\u0019\u001a\u000e\u0012\u0004\u0012\u00020\u001b\u0012\u0004\u0012\u00020\u001c0\u001aH\u0086@\u00a2\u0006\u0002\u0010\u001dR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001b\u0010\u0005\u001a\u00020\u00068BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\t\u0010\n\u001a\u0004\b\u0007\u0010\b\u00a8\u0006\u001e"}, d2 = {"Lcom/carboncredit/app/data/repository/BlockchainRepository;", "", "apiService", "Lcom/carboncredit/app/core/network/ApiService;", "(Lcom/carboncredit/app/core/network/ApiService;)V", "web3j", "Lorg/web3j/protocol/Web3j;", "getWeb3j", "()Lorg/web3j/protocol/Web3j;", "web3j$delegate", "Lkotlin/Lazy;", "decodeString", "", "data", "index", "", "decodeUint", "", "getTransactionReceipt", "Lcom/carboncredit/app/data/repository/OnChainRecord;", "txHash", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "verifyReportIntegrity", "Lcom/carboncredit/app/data/repository/VerificationResult;", "ipfsCid", "onStep", "Lkotlin/Function1;", "Lcom/carboncredit/app/data/repository/VerificationState;", "", "(Ljava/lang/String;Ljava/lang/String;Lkotlin/jvm/functions/Function1;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
public final class BlockchainRepository {
    @org.jetbrains.annotations.NotNull()
    private final com.carboncredit.app.core.network.ApiService apiService = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.Lazy web3j$delegate = null;
    
    @javax.inject.Inject()
    public BlockchainRepository(@org.jetbrains.annotations.NotNull()
    com.carboncredit.app.core.network.ApiService apiService) {
        super();
    }
    
    private final org.web3j.protocol.Web3j getWeb3j() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getTransactionReceipt(@org.jetbrains.annotations.NotNull()
    java.lang.String txHash, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.carboncredit.app.data.repository.OnChainRecord> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object verifyReportIntegrity(@org.jetbrains.annotations.NotNull()
    java.lang.String txHash, @org.jetbrains.annotations.NotNull()
    java.lang.String ipfsCid, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super com.carboncredit.app.data.repository.VerificationState, kotlin.Unit> onStep, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.carboncredit.app.data.repository.VerificationResult> $completion) {
        return null;
    }
    
    private final java.lang.String decodeString(java.lang.String data, int index) {
        return null;
    }
    
    private final long decodeUint(java.lang.String data, int index) {
        return 0L;
    }
}