package com.carboncredit.app.core.utils

import java.security.MessageDigest

object HashUtils {

    /**
     * Computes SHA-256 hash of the input string.
     * Used for IPFS report integrity verification on the auditor side.
     */
    fun sha256(input: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(input.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }

    fun sha256Bytes(input: ByteArray): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(input)
        return bytes.joinToString("") { "%02x".format(it) }
    }
}
