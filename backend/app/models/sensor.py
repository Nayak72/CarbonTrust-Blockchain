from pydantic import BaseModel, Field
from typing import Optional


class SensorReading(BaseModel):
    """Incoming sensor reading from ESP32 via MQTT."""
    device_id: str = Field(..., description="Unique device identifier (e.g. ESP32_FACTORY_001)")
    auth_key: str = Field(..., description="Raw authentication key from the device")
    co2_ppm: float = Field(..., ge=0, description="CO₂ concentration in parts per million")
    temperature: float = Field(..., description="Temperature in Celsius")
    humidity: float = Field(..., ge=0, le=100, description="Relative humidity percentage")
    timestamp: str = Field(..., description="ISO 8601 timestamp of the reading")


class SensorReadingDB(BaseModel):
    """Sensor reading as stored in Supabase."""
    id: str
    sensor_id: str
    facility_id: str
    co2_ppm: float
    temperature: float
    humidity: float
    is_anomaly: bool = False
    anomaly_type: Optional[str] = None
    z_score: Optional[float] = None
    timestamp: str
    created_at: Optional[str] = None


class SensorRegistration(BaseModel):
    """Request body for registering a new sensor device."""
    device_id: str = Field(..., description="Unique device identifier")
    auth_key: str = Field(..., description="Raw auth key (will be hashed before storage)")
    location_label: str = Field(..., description="Human-readable location (e.g. 'Chimney Stack A')")
    facility_id: str = Field(..., description="UUID of the facility this sensor belongs to")


class SensorDB(BaseModel):
    """Sensor record as stored in Supabase."""
    id: str
    facility_id: str
    device_id: str
    location_label: Optional[str] = None
    is_active: bool = True
    last_seen: Optional[str] = None
    created_at: Optional[str] = None
