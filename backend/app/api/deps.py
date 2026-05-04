from fastapi import Depends
from app.core.security import get_current_user, require_manager, require_auditor
from app.core.supabase_client import get_supabase

# Re-export common dependencies for clean imports in route files
__all__ = [
    "get_current_user",
    "require_manager",
    "require_auditor",
    "get_supabase",
    "Depends",
]
