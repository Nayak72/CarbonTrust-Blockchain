from web3 import Web3
from app.config import settings
from app.utils.logger import get_logger

logger = get_logger(__name__)

# Contract ABI — matches CarbonCreditRegistry.sol exactly
CONTRACT_ABI = [
    {
        "inputs": [
            {"internalType": "string", "name": "facilityId", "type": "string"},
            {"internalType": "uint256", "name": "creditsIssued", "type": "uint256"},
            {"internalType": "string", "name": "ipfsCid", "type": "string"}
        ],
        "name": "issueCredit",
        "outputs": [{"internalType": "bytes32", "name": "creditId", "type": "bytes32"}],
        "stateMutability": "nonpayable",
        "type": "function"
    },
    {
        "inputs": [{"internalType": "bytes32", "name": "creditId", "type": "bytes32"}],
        "name": "getCredit",
        "outputs": [{
            "components": [
                {"internalType": "string", "name": "facilityId", "type": "string"},
                {"internalType": "uint256", "name": "creditsIssued", "type": "uint256"},
                {"internalType": "uint256", "name": "timestamp", "type": "uint256"},
                {"internalType": "string", "name": "ipfsCid", "type": "string"},
                {"internalType": "address", "name": "issuedBy", "type": "address"}
            ],
            "internalType": "struct CarbonCreditRegistry.CreditRecord",
            "name": "",
            "type": "tuple"
        }],
        "stateMutability": "view",
        "type": "function"
    },
    {
        "inputs": [],
        "name": "getTotalCreditsCount",
        "outputs": [{"internalType": "uint256", "name": "", "type": "uint256"}],
        "stateMutability": "view",
        "type": "function"
    },
    {
        "inputs": [
            {"internalType": "uint256", "name": "offset", "type": "uint256"},
            {"internalType": "uint256", "name": "limit", "type": "uint256"}
        ],
        "name": "getCreditIds",
        "outputs": [{"internalType": "bytes32[]", "name": "", "type": "bytes32[]"}],
        "stateMutability": "view",
        "type": "function"
    },
    {
        "anonymous": False,
        "inputs": [
            {"indexed": True, "internalType": "bytes32", "name": "creditId", "type": "bytes32"},
            {"indexed": False, "internalType": "string", "name": "facilityId", "type": "string"},
            {"indexed": False, "internalType": "uint256", "name": "creditsIssued", "type": "uint256"},
            {"indexed": False, "internalType": "uint256", "name": "timestamp", "type": "uint256"},
            {"indexed": False, "internalType": "string", "name": "ipfsCid", "type": "string"},
            {"indexed": False, "internalType": "address", "name": "issuedBy", "type": "address"}
        ],
        "name": "CreditIssued",
        "type": "event"
    }
]


class Web3Client:
    """
    Web3.py client for interacting with the CarbonCreditRegistry
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

    async def issue_credit_on_chain(
        self,
        facility_id: str,
        credits_kg: int,
        ipfs_cid: str
    ) -> dict:
        """
        Calls issueCredit() on the smart contract.
        credits_kg: credits in grams (1 tonne = 1,000,000 grams for precision)
        Returns: { tx_hash, block_number }
        """
        if not self.enabled:
            raise RuntimeError("Blockchain client is not initialized. Check your .env configuration.")

        nonce = self.w3.eth.get_transaction_count(self.account.address)
        gas_price = self.w3.eth.gas_price

        tx = self.contract.functions.issueCredit(
            facility_id,
            credits_kg,
            ipfs_cid
        ).build_transaction({
            'chainId': settings.POLYGON_CHAIN_ID,
            'gas': 300000,
            'gasPrice': gas_price,
            'nonce': nonce,
        })

        signed_tx = self.w3.eth.account.sign_transaction(tx, settings.DEPLOYER_PRIVATE_KEY)
        tx_hash = self.w3.eth.send_raw_transaction(signed_tx.raw_transaction)
        receipt = self.w3.eth.wait_for_transaction_receipt(tx_hash, timeout=120)

        return {
            "tx_hash": receipt.transactionHash.hex(),
            "block_number": receipt.blockNumber
        }


web3_client = Web3Client()
