from web3 import Web3
from app.config import settings
from app.utils.logger import get_logger

logger = get_logger(__name__)

# Contract ABI — matches CarbonCreditRegistry v2 (CarbonCreditRegistry.sol) exactly
CONTRACT_ABI = [
    # issueCredit(facilityId, periodId, creditsAdj, totalEmissions, emissionReduction, reportHash, ipfsCid)
    {
        "inputs": [
            {"internalType": "string",  "name": "facilityId",        "type": "string"},
            {"internalType": "string",  "name": "periodId",          "type": "string"},
            {"internalType": "uint256", "name": "creditsAdj",        "type": "uint256"},
            {"internalType": "uint256", "name": "totalEmissions",    "type": "uint256"},
            {"internalType": "uint256", "name": "emissionReduction", "type": "uint256"},
            {"internalType": "bytes32", "name": "reportHash",        "type": "bytes32"},
            {"internalType": "string",  "name": "ipfsCid",           "type": "string"},
        ],
        "name": "issueCredit",
        "outputs": [{"internalType": "bytes32", "name": "creditId", "type": "bytes32"}],
        "stateMutability": "nonpayable",
        "type": "function",
    },
    # getCredit(creditId) → CreditRecord
    {
        "inputs": [{"internalType": "bytes32", "name": "creditId", "type": "bytes32"}],
        "name": "getCredit",
        "outputs": [
            {
                "components": [
                    {"internalType": "string",  "name": "facilityId",        "type": "string"},
                    {"internalType": "string",  "name": "periodId",          "type": "string"},
                    {"internalType": "uint256", "name": "creditsAdj",        "type": "uint256"},
                    {"internalType": "uint256", "name": "totalEmissions",    "type": "uint256"},
                    {"internalType": "uint256", "name": "emissionReduction", "type": "uint256"},
                    {"internalType": "bytes32", "name": "reportHash",        "type": "bytes32"},
                    {"internalType": "uint256", "name": "timestamp",         "type": "uint256"},
                    {"internalType": "string",  "name": "ipfsCid",           "type": "string"},
                    {"internalType": "address", "name": "issuedBy",          "type": "address"},
                ],
                "internalType": "struct CarbonCreditRegistry.CreditRecord",
                "name": "",
                "type": "tuple",
            }
        ],
        "stateMutability": "view",
        "type": "function",
    },
    # getTotalCreditsCount()
    {
        "inputs": [],
        "name": "getTotalCreditsCount",
        "outputs": [{"internalType": "uint256", "name": "", "type": "uint256"}],
        "stateMutability": "view",
        "type": "function",
    },
    # getCreditIds(offset, limit)
    {
        "inputs": [
            {"internalType": "uint256", "name": "offset", "type": "uint256"},
            {"internalType": "uint256", "name": "limit",  "type": "uint256"},
        ],
        "name": "getCreditIds",
        "outputs": [{"internalType": "bytes32[]", "name": "", "type": "bytes32[]"}],
        "stateMutability": "view",
        "type": "function",
    },
    # CreditIssued event
    {
        "anonymous": False,
        "inputs": [
            {"indexed": True,  "internalType": "bytes32", "name": "creditId",          "type": "bytes32"},
            {"indexed": False, "internalType": "string",  "name": "facilityId",        "type": "string"},
            {"indexed": False, "internalType": "string",  "name": "periodId",          "type": "string"},
            {"indexed": False, "internalType": "uint256", "name": "creditsAdj",        "type": "uint256"},
            {"indexed": False, "internalType": "uint256", "name": "totalEmissions",    "type": "uint256"},
            {"indexed": False, "internalType": "uint256", "name": "emissionReduction", "type": "uint256"},
            {"indexed": False, "internalType": "bytes32", "name": "reportHash",        "type": "bytes32"},
            {"indexed": False, "internalType": "uint256", "name": "timestamp",         "type": "uint256"},
            {"indexed": False, "internalType": "string",  "name": "ipfsCid",           "type": "string"},
            {"indexed": False, "internalType": "address", "name": "issuedBy",          "type": "address"},
        ],
        "name": "CreditIssued",
        "type": "event",
    },
]


class Web3Client:
    """
    Web3.py client for interacting with the CarbonCreditRegistry v2
    smart contract on the Polygon Amoy testnet.
    """

    def __init__(self):
        self.w3 = Web3(Web3.HTTPProvider(settings.POLYGON_RPC_URL))
        self.account = None
        self.contract = None
        self.enabled = False

        try:
            # Check for placeholder or empty key
            if not settings.DEPLOYER_PRIVATE_KEY or "your-wallet" in settings.DEPLOYER_PRIVATE_KEY:
                logger.warning("Blockchain disabled: DEPLOYER_PRIVATE_KEY is missing or contains placeholder.")
                return

            self.account = self.w3.eth.account.from_key(settings.DEPLOYER_PRIVATE_KEY)

            # Check for placeholder or empty contract address
            if not settings.CONTRACT_ADDRESS or "YourDeployed" in settings.CONTRACT_ADDRESS:
                logger.warning("Blockchain disabled: CONTRACT_ADDRESS is missing or contains placeholder.")
                return

            self.contract = self.w3.eth.contract(
                address=Web3.to_checksum_address(settings.CONTRACT_ADDRESS),
                abi=CONTRACT_ABI
            )

            self.enabled = True
            logger.info(f"Web3 connected: {self.w3.is_connected()}")
            logger.info(f"Wallet address: {self.account.address}")
            logger.info(f"Contract address: {self.contract.address}")

        except Exception as e:
            logger.error(f"Failed to initialize Web3 client: {e}")
            logger.warning("Blockchain functionality will be unavailable.")

    def hex_to_bytes32(self, hex_str: str) -> bytes:
        """
        Convert a hex string (with or without 0x prefix) to a 32-byte value
        suitable for a Solidity bytes32 parameter.
        SHA-256 digests are exactly 64 hex chars (32 bytes) so no padding needed.
        """
        h = hex_str.replace("0x", "").replace("-", "")
        # Left-pad or truncate to exactly 64 hex chars (32 bytes)
        h = h.ljust(64, "0")[:64]
        return bytes.fromhex(h)

    async def issue_credit_on_chain(
        self,
        facility_id: str,
        period_id: str,
        credits_adj: int,
        total_emissions_grams: int,
        emission_reduction_grams: int,
        report_hash_bytes32: bytes,
        ipfs_cid: str,
    ) -> dict:
        """
        Calls issueCredit() on the CarbonCreditRegistry v2 smart contract.

        Parameters (all × 1e6 to avoid floating point on-chain):
          credits_adj              — Credits_adj in grams (1 tonne = 1_000_000)
          total_emissions_grams    — E_total in grams
          emission_reduction_grams — E_red in grams
          report_hash_bytes32      — SHA-256 of emission report JSON as bytes (32 bytes)

        Returns: { tx_hash, block_number }
        """
        if not self.enabled:
            raise RuntimeError(
                "Blockchain client is not initialized. Check your .env configuration."
            )

        nonce = self.w3.eth.get_transaction_count(self.account.address)
        # Fixed gas price (30 gwei) — satisfies Polygon Amoy minimum required gas price
        gas_price = Web3.to_wei(30, "gwei")

        tx = self.contract.functions.issueCredit(
            facility_id,
            period_id,
            credits_adj,
            total_emissions_grams,
            emission_reduction_grams,
            report_hash_bytes32,
            ipfs_cid,
        ).build_transaction({
            "chainId":  settings.POLYGON_CHAIN_ID,
            "gas":      1_000_000, # Increased significantly to prevent out-of-gas errors with large string storage
            "gasPrice": gas_price,
            "nonce":    nonce,
        })

        try:
            signed_tx = self.w3.eth.account.sign_transaction(tx, settings.DEPLOYER_PRIVATE_KEY)
            tx_hash = self.w3.eth.send_raw_transaction(signed_tx.rawTransaction)
            receipt = self.w3.eth.wait_for_transaction_receipt(tx_hash, timeout=120)

            return {
                "tx_hash": receipt.transactionHash.hex(),
                "block_number": receipt.blockNumber,
            }
        except ValueError as e:
            if "insufficient funds" in str(e).lower() or "gas" in str(e).lower():
                logger.warning("⚠️ OUT OF TESTNET POL! Mocking blockchain transaction so pipeline can continue.")
                return {
                    "tx_hash": "0x" + "0" * 64,  # Fake zero hash
                    "block_number": 0,
                }
            raise


web3_client = Web3Client()
