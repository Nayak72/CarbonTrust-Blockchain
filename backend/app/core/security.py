from fastapi import Depends, HTTPException, status
from fastapi.security import HTTPBearer, HTTPAuthorizationCredentials
from jose import jwt, JWTError
from app.config import settings
from app.core.supabase_client import get_supabase

bearer_scheme = HTTPBearer()


async def get_current_user(
    credentials: HTTPAuthorizationCredentials = Depends(bearer_scheme)
) -> dict:
    """
    Validates the Supabase JWT sent by the Android app.
    Returns the decoded user payload with role and user_id.

    The Android app sends the Supabase access token in the
    Authorization header as a Bearer token.
    """
    token = credentials.credentials
    try:
        # Supabase JWTs are signed with the JWT secret from project settings
        payload = jwt.decode(
            token,
            settings.SUPABASE_JWT_SECRET,
            algorithms=["HS256"],
            audience="authenticated"
        )
        user_id = payload.get("sub")
        if not user_id:
            raise HTTPException(
                status_code=status.HTTP_401_UNAUTHORIZED,
                detail="Invalid token: missing subject"
            )

        # Fetch role from user_profiles
        supabase = get_supabase()
        result = supabase.table("user_profiles") \
            .select("role, facility_id") \
            .eq("id", user_id) \
            .single() \
            .execute()

        if not result.data:
            raise HTTPException(
                status_code=status.HTTP_401_UNAUTHORIZED,
                detail="User profile not found"
            )

        return {
            "user_id": user_id,
            "role": result.data["role"],
            "facility_id": result.data.get("facility_id")
        }
    except JWTError as e:
        raise HTTPException(
            status_code=status.HTTP_401_UNAUTHORIZED,
            detail=f"Token validation failed: {str(e)}"
        )


async def require_manager(user=Depends(get_current_user)):
    """Dependency that ensures the authenticated user is a MANAGER."""
    if user["role"] != "MANAGER":
        raise HTTPException(
            status_code=status.HTTP_403_FORBIDDEN,
            detail="Manager access required"
        )
    return user


async def require_auditor(user=Depends(get_current_user)):
    """Dependency that ensures the authenticated user is an AUDITOR."""
    if user["role"] != "AUDITOR":
        raise HTTPException(
            status_code=status.HTTP_403_FORBIDDEN,
            detail="Auditor access required"
        )
    return user
