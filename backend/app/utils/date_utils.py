from datetime import datetime, timezone, timedelta


def now_utc() -> datetime:
    """Returns the current UTC datetime."""
    return datetime.now(timezone.utc)


def iso_now() -> str:
    """Returns the current UTC datetime as an ISO 8601 string."""
    return datetime.now(timezone.utc).isoformat()


def parse_iso(timestamp_str: str) -> datetime:
    """Parses an ISO 8601 timestamp string to a datetime object."""
    return datetime.fromisoformat(timestamp_str)


def hours_ago(hours: int) -> datetime:
    """Returns the UTC datetime N hours ago from now."""
    return datetime.now(timezone.utc) - timedelta(hours=hours)
