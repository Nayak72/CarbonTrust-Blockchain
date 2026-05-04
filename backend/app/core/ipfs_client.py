import httpx
from app.config import settings
from app.utils.logger import get_logger

logger = get_logger(__name__)


class IPFSClient:
    """
    Pinata-based IPFS client for uploading and fetching
    emission report JSON documents.
    """

    def __init__(self):
        self.headers = {
            "pinata_api_key": settings.PINATA_API_KEY,
            "pinata_secret_api_key": settings.PINATA_SECRET_API_KEY
        }

    async def upload_json(self, data: dict, name: str = "emission_report") -> str:
        """
        Uploads a JSON object to IPFS via Pinata.
        Returns the IPFS CID (content hash).
        """
        payload = {
            "pinataOptions": {"cidVersion": 1},
            "pinataMetadata": {"name": name},
            "pinataContent": data
        }

        async with httpx.AsyncClient(timeout=30.0) as client:
            response = await client.post(
                "https://api.pinata.cloud/pinning/pinJSONToIPFS",
                json=payload,
                headers=self.headers
            )
            response.raise_for_status()
            result = response.json()
            cid = result["IpfsHash"]
            logger.info(f"IPFS upload successful: {cid}")
            return cid

    async def fetch_json(self, cid: str) -> dict:
        """
        Fetches a JSON object from IPFS by CID.
        Used by the IPFS proxy endpoint for the Android auditor screen.
        """
        url = f"{settings.PINATA_GATEWAY}/ipfs/{cid}"
        async with httpx.AsyncClient(timeout=30.0) as client:
            response = await client.get(url)
            response.raise_for_status()
            return response.json()


ipfs_client = IPFSClient()
