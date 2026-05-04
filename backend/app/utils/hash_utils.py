import hashlib


def sha256_hash(data: str) -> str:
    """Returns the SHA-256 hex digest of the input string."""
    return hashlib.sha256(data.encode("utf-8")).hexdigest()


def sha256_hash_bytes(data: bytes) -> str:
    """Returns the SHA-256 hex digest of raw bytes."""
    return hashlib.sha256(data).hexdigest()
