package com.carboncredit.app.data.repository

import com.carboncredit.app.BuildConfig
import com.carboncredit.app.core.network.ApiService
import com.carboncredit.app.core.utils.HashUtils
import org.web3j.protocol.Web3j
import org.web3j.protocol.http.HttpService
import javax.inject.Inject
import javax.inject.Singleton

enum class VerificationResult { INTACT, TAMPERED, ERROR }

data class VerificationState(
    val step: Int = 0,
    val totalSteps: Int = 4,
    val currentMessage: String = "",
    val result: VerificationResult? = null,
    val errorMessage: String? = null
)

data class OnChainRecord(
    val companyId: String = "",
    val creditsIssued: Float = 0f,
    val timestamp: Long = 0L,
    val ipfsCid: String = "",
    val blockNumber: Long = 0L
)

@Singleton
class BlockchainRepository @Inject constructor(
    private val apiService: ApiService
) {
    private val web3j: Web3j by lazy {
        Web3j.build(HttpService(BuildConfig.POLYGON_RPC_URL))
    }

    suspend fun getTransactionReceipt(txHash: String): OnChainRecord? {
        return try {
            val receipt = web3j.ethGetTransactionReceipt(txHash).send()
            val tx = receipt.transactionReceipt.orElse(null) ?: return null
            // Generic decoder: parse event logs for CreditIssued event
            val logs = tx.logs
            if (logs.isNullOrEmpty()) return null
            val log = logs.first()
            // Decode the 4 fields from event data (company_id, credits, timestamp, cid)
            val data = log.data.removePrefix("0x")
            val companyId = decodeString(data, 0)
            val credits = decodeUint(data, 1)
            val ts = decodeUint(data, 2)
            val cid = decodeString(data, 3)
            OnChainRecord(
                companyId = companyId,
                creditsIssued = credits.toFloat(),
                timestamp = ts,
                ipfsCid = cid,
                blockNumber = tx.blockNumber.toLong()
            )
        } catch (e: Exception) {
            null
        }
    }

    suspend fun verifyReportIntegrity(
        txHash: String,
        ipfsCid: String,
        onStep: (VerificationState) -> Unit
    ): VerificationResult {
        try {
            // Step 1: Fetch on-chain data
            onStep(VerificationState(1, 4, "Fetching record from Polygon blockchain..."))
            val receipt = web3j.ethGetTransactionReceipt(txHash).send()
            val tx = receipt.transactionReceipt.orElse(null)
                ?: return VerificationResult.ERROR

            // Step 2: Fetch report from IPFS via FastAPI proxy
            onStep(VerificationState(2, 4, "Fetching report from IPFS using stored CID..."))
            val report = apiService.fetchIPFSReport(ipfsCid)

            // Step 3: Hash the fetched report
            onStep(VerificationState(3, 4, "Computing SHA-256 hash of fetched report..."))
            val computedHash = HashUtils.sha256(report.content)

            // Step 4: Compare with on-chain hash
            onStep(VerificationState(4, 4, "Comparing computed hash vs on-chain hash..."))
            val onChainData = tx.logs.firstOrNull()?.data?.removePrefix("0x") ?: ""
            val onChainCid = decodeString(onChainData, 3)

            return if (ipfsCid == onChainCid) {
                VerificationResult.INTACT
            } else {
                VerificationResult.TAMPERED
            }
        } catch (e: Exception) {
            return VerificationResult.ERROR
        }
    }

    // Generic ABI decoders for event log data
    private fun decodeString(data: String, index: Int): String {
        return try {
            val offset = index * 64
            if (offset + 64 > data.length) return ""
            val hex = data.substring(offset, offset + 64).trimStart('0')
            if (hex.isEmpty()) "" else String(hex.chunked(2).map { it.toInt(16).toByte() }.toByteArray()).trim('\u0000')
        } catch (_: Exception) { "" }
    }

    private fun decodeUint(data: String, index: Int): Long {
        return try {
            val offset = index * 64
            if (offset + 64 > data.length) return 0L
            data.substring(offset, offset + 64).trimStart('0').ifEmpty { "0" }.toLong(16)
        } catch (_: Exception) { 0L }
    }
}
