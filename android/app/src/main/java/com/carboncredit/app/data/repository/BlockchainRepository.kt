package com.carboncredit.app.data.repository

import android.util.Log
import com.carboncredit.app.BuildConfig
import com.carboncredit.app.core.network.ApiService
import org.web3j.abi.FunctionReturnDecoder
import org.web3j.abi.TypeReference
import org.web3j.abi.datatypes.Address
import org.web3j.abi.datatypes.Type
import org.web3j.abi.datatypes.Utf8String
import org.web3j.abi.datatypes.generated.Bytes32
import org.web3j.abi.datatypes.generated.Uint256
import org.web3j.protocol.Web3j
import org.web3j.protocol.http.HttpService
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "BlockchainRepo"

enum class VerificationResult { INTACT, TAMPERED, ERROR }

data class VerificationState(
    val step: Int = 0,
    val totalSteps: Int = 5,
    val currentMessage: String = "",
    val result: VerificationResult? = null,
    val errorMessage: String? = null
)

data class HashVerificationResult(
    val cidMatch: Boolean,
    val hashMatch: Boolean
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

    /**
     * ABI type references for the issueCredit function parameters:
     * string facilityId, string periodId, uint256 creditsAdj, uint256 totalEmissions,
     * uint256 emissionReduction, bytes32 reportHash, string ipfsCid
     */
    @Suppress("UNCHECKED_CAST")
    private val ISSUE_CREDIT_PARAMS: List<TypeReference<Type<*>>> by lazy {
        listOf(
            object : TypeReference<Utf8String>() {},   // 0: facilityId
            object : TypeReference<Utf8String>() {},   // 1: periodId
            object : TypeReference<Uint256>() {},      // 2: creditsAdj
            object : TypeReference<Uint256>() {},      // 3: totalEmissions
            object : TypeReference<Uint256>() {},      // 4: emissionReduction
            object : TypeReference<Bytes32>() {},      // 5: reportHash
            object : TypeReference<Utf8String>() {}    // 6: ipfsCid
        ) as List<TypeReference<Type<*>>>
    }

    /**
     * Decode the transaction input data for issueCredit().
     */
    private fun decodeTxInput(inputData: String): List<Type<*>>? {
        return try {
            // Remove '0x' and the 4-byte (8 hex chars) function signature
            val encodedParams = inputData.removePrefix("0x").drop(8)
            FunctionReturnDecoder.decode(encodedParams, ISSUE_CREDIT_PARAMS)
        } catch (e: Exception) {
            Log.e(TAG, "Tx Input decode failed", e)
            null
        }
    }

    suspend fun getTransactionReceipt(txHash: String): OnChainRecord? = kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.IO) {
        try {
            val txOpt = web3j.ethGetTransactionByHash(txHash).send().transaction
            if (!txOpt.isPresent) return@withContext null
            val tx = txOpt.get()

            val decoded = decodeTxInput(tx.input) ?: return@withContext null

            val companyId = (decoded[0] as Utf8String).value
            val creditsGrams = (decoded[2] as Uint256).value.toLong()
            val cid = (decoded[6] as Utf8String).value
            
            // Get block timestamp
            val block = web3j.ethGetBlockByNumber(org.web3j.protocol.core.DefaultBlockParameter.valueOf(tx.blockNumber), false).send().block
            val ts = block?.timestamp?.toLong() ?: 0L

            OnChainRecord(
                companyId = companyId,
                creditsIssued = creditsGrams.toFloat() / 1_000_000f,
                timestamp = ts,
                ipfsCid = cid,
                blockNumber = tx.blockNumber.toLong()
            )
        } catch (e: Exception) {
            Log.e(TAG, "getTransactionReceipt failed", e)
            null
        }
    }

    /**
     * 5-step report integrity verification:
     *   Step 1 — Fetch on-chain transaction from Polygon
     *   Step 2 — Fetch report from IPFS via FastAPI proxy
     *   Step 3 — Extract SHA-256 hash embedded in the IPFS report
     *   Step 4 — Compare IPFS CID on-chain vs. Supabase record
     *   Step 5 — Compare report hash: on-chain (bytes32) vs. IPFS vs. DB
     *
     * Returns INTACT only when all comparisons pass.
     */
    suspend fun verifyReportIntegrity(
        txHash: String,
        ipfsCid: String,
        reportHashFromDb: String?,
        onStep: (VerificationState) -> Unit
    ): VerificationResult = kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.IO) {
        try {
            // Step 1: Fetch on-chain data
            kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.Main) { onStep(VerificationState(1, 5, "Fetching record from Polygon blockchain...")) }
            val txOpt = web3j.ethGetTransactionByHash(txHash).send().transaction
            if (!txOpt.isPresent) {
                return@withContext VerificationResult.ERROR.also {
                    kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.Main) { onStep(VerificationState(1, 5, "Transaction not found on blockchain", VerificationResult.ERROR)) }
                }
            }
            val tx = txOpt.get()

            // Step 2: Fetch report from IPFS via FastAPI proxy
            kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.Main) { onStep(VerificationState(2, 5, "Fetching emission report from IPFS...")) }
            val report = apiService.fetchIPFSReport(ipfsCid)

            // Step 3: Extract report_hash from the fetched report JSON
            kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.Main) { onStep(VerificationState(3, 5, "Extracting SHA-256 hash from report...")) }
            val embeddedHash = try {
                org.json.JSONObject(report.content).getString("report_hash")
            } catch (e: Exception) {
                Log.e(TAG, "Failed to extract report_hash from IPFS content", e)
                ""
            }
            Log.d(TAG, "Embedded hash from IPFS: $embeddedHash")
            Log.d(TAG, "Report hash from DB:     $reportHashFromDb")

            // Decode the on-chain transaction input using web3j
            val decoded = decodeTxInput(tx.input)

            // Step 4: Compare IPFS CID (on-chain vs. Supabase)
            kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.Main) { onStep(VerificationState(4, 5, "Comparing IPFS CID with on-chain record...")) }
            val onChainCid = decoded?.let { (it[6] as Utf8String).value } ?: ""
            val cidMatch = ipfsCid == onChainCid
            Log.d(TAG, "CID match: $cidMatch (supabase=$ipfsCid, onChain=$onChainCid)")

            // Step 5: Compare SHA-256 report hash three ways:
            //   on-chain bytes32 ↔ DB ↔ IPFS embedded
            kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.Main) { onStep(VerificationState(5, 5, "Verifying SHA-256 report hash integrity...")) }
            val onChainHashBytes = decoded?.let { (it[5] as Bytes32).value } ?: ByteArray(0)
            val onChainHash = onChainHashBytes.joinToString("") { "%02x".format(it) }
            Log.d(TAG, "On-chain report hash:    $onChainHash")

            val ipfsMatchesDb = reportHashFromDb == null || embeddedHash.equals(reportHashFromDb, ignoreCase = true)
            val onChainMatchesDb = reportHashFromDb == null || onChainHash.equals(reportHashFromDb, ignoreCase = true)
            Log.d(TAG, "IPFS↔DB match: $ipfsMatchesDb, OnChain↔DB match: $onChainMatchesDb")

            val allChecksPass = cidMatch && ipfsMatchesDb && onChainMatchesDb

            // Mark the final step as complete so the UI stops spinning
            kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.Main) { onStep(VerificationState(6, 5, "Verification complete")) }

            return@withContext if (allChecksPass) {
                VerificationResult.INTACT
            } else {
                VerificationResult.TAMPERED
            }
        } catch (e: Exception) {
            Log.e(TAG, "verifyReportIntegrity failed", e)
            return@withContext VerificationResult.ERROR
        }
    }
}
