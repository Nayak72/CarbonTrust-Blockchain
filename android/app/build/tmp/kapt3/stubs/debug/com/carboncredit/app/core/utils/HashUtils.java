package com.carboncredit.app.core.utils;

import java.security.MessageDigest;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u0012\n\u0000\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0004J\u000e\u0010\u0006\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0007\u00a8\u0006\b"}, d2 = {"Lcom/carboncredit/app/core/utils/HashUtils;", "", "()V", "sha256", "", "input", "sha256Bytes", "", "app_debug"})
public final class HashUtils {
    @org.jetbrains.annotations.NotNull()
    public static final com.carboncredit.app.core.utils.HashUtils INSTANCE = null;
    
    private HashUtils() {
        super();
    }
    
    /**
     * Computes SHA-256 hash of the input string.
     * Used for IPFS report integrity verification on the auditor side.
     */
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String sha256(@org.jetbrains.annotations.NotNull()
    java.lang.String input) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String sha256Bytes(@org.jetbrains.annotations.NotNull()
    byte[] input) {
        return null;
    }
}