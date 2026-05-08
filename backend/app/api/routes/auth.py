from fastapi import APIRouter, Depends, HTTPException, status
from pydantic import BaseModel, EmailStr
from typing import Optional
from app.core.supabase_client import get_supabase
from app.core.security import get_current_user, get_password_hash, verify_password, create_access_token
from app.utils.logger import get_logger
import uuid

router = APIRouter(prefix="/auth", tags=["auth"])
logger = get_logger(__name__)


class FCMTokenRequest(BaseModel):
    fcm_token: str

class UserSignupRequest(BaseModel):
    full_name: str
    email: EmailStr
    password: str
    role: str
    facility_id: Optional[str] = None

class UserLoginRequest(BaseModel):
    email: EmailStr
    password: str

class TokenResponse(BaseModel):
    access_token: str
    token_type: str
    user: dict

@router.post("/signup", response_model=TokenResponse)
async def signup(body: UserSignupRequest):
    supabase = get_supabase()
    
    # 1. Check if user already exists
    existing_user = supabase.table("user_profiles").select("id").eq("email", body.email).execute()
    if existing_user.data:
        raise HTTPException(status_code=400, detail="Email already registered")
        
    # 2. Hash password
    hashed_password = get_password_hash(body.password)
    
    # 3. Create user profile
    new_user_id = str(uuid.uuid4())
    user_data = {
        "id": new_user_id,
        "full_name": body.full_name,
        "email": body.email,
        "role": body.role,
        "password_hash": hashed_password
    }
    
    if body.facility_id:
        user_data["facility_id"] = body.facility_id
        
    try:
        supabase.table("user_profiles").insert(user_data).execute()
    except Exception as e:
        logger.error(f"Failed to create user profile: {str(e)}")
        raise HTTPException(status_code=500, detail="Failed to create user profile")
        
    # 4. Generate JWT
    access_token = create_access_token(
        data={"sub": new_user_id, "aud": "authenticated"}
    )
    
    return {
        "access_token": access_token,
        "token_type": "bearer",
        "user": user_data
    }

@router.post("/login", response_model=TokenResponse)
async def login(body: UserLoginRequest):
    supabase = get_supabase()
    
    # 1. Find user by email
    result = supabase.table("user_profiles").select("*").eq("email", body.email).execute()
    if not result.data:
        raise HTTPException(status_code=status.HTTP_401_UNAUTHORIZED, detail="Incorrect email or password")
        
    user = result.data[0]
    
    # 2. Verify password
    if not user.get("password_hash") or not verify_password(body.password, user["password_hash"]):
        raise HTTPException(status_code=status.HTTP_401_UNAUTHORIZED, detail="Incorrect email or password")
        
    # 3. Generate JWT
    access_token = create_access_token(
        data={"sub": user["id"], "aud": "authenticated"}
    )
    
    return {
        "access_token": access_token,
        "token_type": "bearer",
        "user": user
    }


@router.get("/me")
async def get_me(user=Depends(get_current_user)):
    """
    Returns the current authenticated user's profile from user_profiles.
    Used by the Android app to fetch profile data without relying on Supabase RLS.
    """
    supabase = get_supabase()
    result = supabase.table("user_profiles") \
        .select("id, full_name, email, role, facility_id, avatar_url, created_at") \
        .eq("id", user["user_id"]) \
        .single() \
        .execute()

    if not result.data:
        raise HTTPException(status_code=404, detail="Profile not found")

    return result.data


@router.post("/fcm-token")
async def register_fcm_token(
    body: FCMTokenRequest,
    user=Depends(get_current_user)
):
    """
    Called by Android app on login and when FCM token refreshes.
    Stores the FCM token in user_profiles for push notification delivery.
    """
    supabase = get_supabase()
    supabase.table("user_profiles") \
        .update({"fcm_token": body.fcm_token}) \
        .eq("id", user["user_id"]) \
        .execute()
    logger.info(f"FCM token registered for user {user['user_id']}")
    return {"status": "ok", "message": "FCM token registered"}
