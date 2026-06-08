from pydantic_settings import BaseSettings


class Settings(BaseSettings):
    APP_ENV: str = "development"
    APP_HOST: str = "0.0.0.0"
    APP_PORT: int = 8000
    SECRET_KEY: str = "change-me"

    # Supabase
    SUPABASE_URL: str = ""
    SUPABASE_ANON_KEY: str = ""
    SUPABASE_SERVICE_ROLE_KEY: str = ""
    SUPABASE_JWT_SECRET: str = ""

    # MQTT
    MQTT_BROKER_HOST: str = "localhost"
    MQTT_BROKER_PORT: int = 8883
    MQTT_USERNAME: str = ""
    MQTT_PASSWORD: str = ""
    MQTT_TLS_ENABLED: bool = True
    MQTT_CA_CERT_PATH: str = "./mosquitto/certs/ca.crt"

    # Pinata IPFS
    PINATA_API_KEY: str = ""
    PINATA_SECRET_API_KEY: str = ""
    PINATA_GATEWAY: str = "https://gateway.pinata.cloud"

    # Polygon Blockchain
    POLYGON_RPC_URL: str = "https://rpc-amoy.polygon.technology/"
    POLYGON_CHAIN_ID: int = 80002
    CONTRACT_ADDRESS: str = ""
    DEPLOYER_PRIVATE_KEY: str = ""
    POLYGONSCAN_API_KEY: str = ""

    # Firebase
    FIREBASE_CREDENTIALS_PATH: str = "./firebase-credentials.json"

    # Emission Calculation
    AMBIENT_CO2_PPM: float = 400.0
    DEFAULT_FLOW_RATE_M3_S: float = 0.5
    CREDIT_CALCULATION_WINDOW_HOURS: float = 24.0
    ANOMALY_ZSCORE_THRESHOLD: float = 3.0
    ANOMALY_FROZEN_COUNT_THRESHOLD: int = 5

    # Analytical model parameters
    MOVING_AVERAGE_WINDOW: int = 10                  # Window w for C_avg(t) = (1/w) Σ C(t-i)
    EMISSION_ALPHA: float = 1.96e-3                  # Calibration factor kg/(ppm·m³) from CO₂ density
    CREDIT_BETA_KG: float = 1000.0                   # kg CO₂ per credit (1 tonne)
    QUALITY_MIN_READING_RATIO: float = 0.8           # Min fraction of expected readings for q=1

    # Simulator (for demo / presentation)
    # Simulators are now per-facility and auto-started from the database.
    # SIMULATOR_DEVICE_ID and SIMULATOR_AUTH_KEY are no longer used.
    SIMULATOR_ENABLED: bool = False
    SIMULATOR_INTERVAL_SECONDS: float = 10.0

    class Config:
        env_file = ".env"
        extra = "allow"


settings = Settings()

