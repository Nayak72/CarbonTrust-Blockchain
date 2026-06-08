# Carbon Credit Verification System

A full-stack blockchain-based carbon credit verification and emission monitoring platform. The system uses IoT sensors to collect CO₂ emission data, verifies it through anomaly detection, calculates emission reductions against baselines, and issues verifiable carbon credits stored on the Polygon blockchain with immutable IPFS reports.

## Architecture

```
┌─────────────────┐     ┌──────────────┐     ┌────────────────────┐     ┌──────────────┐     ┌──────────────┐
│  ESP32 + SCD30  │────→│ MQTT Broker  │────→│   FastAPI Backend  │────→│  Supabase    │────→│ Android App  │
│  CO₂ Sensor     │     │  (Mosquitto) │     │  (Python 3.11+)   │     │  PostgreSQL  │     │  (Kotlin)    │
└─────────────────┘     └──────────────┘     └────────────────────┘     └──────────────┘     └──────────────┘
                                                   │                           │
                                                   ▼                           ▼
                                          ┌──────────────┐           ┌──────────────┐
                                          │    Pinata    │           │   Polygon    │
                                          │    (IPFS)    │           │  Blockchain  │
                                          └──────────────┘           └──────────────┘
                                                   │                           │
                                                   └───────────────┬───────────┘
                                                                   ▼
                                                          ┌──────────────┐
                                                          │    FCM       │
                                                          │ Push Notif.  │
                                                          └──────────────┘
```

## Tech Stack

### Backend
- **Framework:** FastAPI (Python 3.11+)
- **Database:** Supabase (PostgreSQL with Row-Level Security)
- **Authentication:** Supabase Auth (JWT)
- **Blockchain:** Polygon Amoy Testnet (Solidity / Hardhat)
- **IPFS Storage:** Pinata (managed pinning)
- **MQTT Broker:** Mosquitto (TLS-enabled)
- **Push Notifications:** Firebase Cloud Messaging (FCM)

### Android App
- **Language:** Kotlin
- **UI Framework:** Jetpack Compose
- **Dependency Injection:** Hilt
- **Authentication:** Supabase Auth (Kotlin SDK)
- **Networking:** Retrofit + OkHttp (for FastAPI)
- **State Management:** Kotlin Flow + ViewModel

## Project Structure

```
.
├── android/                          # Android application (Kotlin + Jetpack Compose)
│   ├── app/src/main/java/com/carboncredit/app/
│   │   ├── core/                     # Network, security, Supabase client
│   │   ├── data/                     # Repositories, models
│   │   ├── ui/                       # Screens (Compose), ViewModels
│   │   └── di/                       # Hilt dependency injection modules
│   └── app/build.gradle.kts          # Build config with API keys
│
├── backend/                          # FastAPI backend
│   ├── app/
│   │   ├── api/routes/               # REST endpoints
│   │   ├── core/                     # MQTT client, simulator, auth
│   │   ├── models/                   # Pydantic models
│   │   ├── services/                 # Pipeline, credit calculation
│   │   └── main.py                   # FastAPI entry point
│   ├── hardhat/                      # Solidity smart contract
│   │   ├── contracts/                # CarbonCreditRegistry.sol
│   │   ├── scripts/deploy.js         # Deployment script
│   │   └── hardhat.config.js         # Network configuration
│   ├── .env.example                  # Environment variables template
│   └── requirements.txt              # Python dependencies
│
└── SETUP_GUIDE.md                    # Detailed setup instructions
```

## Quick Start

### Prerequisites
- Python 3.11+
- Node.js 18+ (for Hardhat)
- Android Studio Hedgehog or newer
- Supabase project (free tier works)
- Pinata account (for IPFS)
- MetaMask wallet with Amoy testnet MATIC

### 1. Clone the repository
```bash
git clone https://github.com/yourusername/carbon-credit-verification.git
cd carbon-credit-verification
```

### 2. Backend Setup

```bash
cd backend

# Create virtual environment
python -m venv venv
source venv/bin/activate
pip install -r requirements.txt

# Copy environment template
cp .env.example .env
# Edit .env with your API keys (see SETUP_GUIDE.md for details)
```

### 3. Database Setup (Supabase)

Run the full SQL schema in your Supabase SQL Editor:

```sql
-- Users / Profiles (linked to Supabase Auth)
create table user_profiles (
    id uuid references auth.users on delete cascade primary key,
    role text not null check (role in ('MANAGER', 'AUDITOR')),
    facility_id uuid,
    full_name text,
    email text,
    created_at timestamptz default now()
);

-- Facilities
create table facilities (
    id uuid default gen_random_uuid() primary key,
    name text not null,
    company_name text not null,
    location text,
    industry_type text,
    baseline_emissions float not null,
    created_at timestamptz default now()
);

-- ESP32 Sensors
create table sensors (
    id uuid default gen_random_uuid() primary key,
    device_id text not null unique,
    auth_key text not null,
    location_label text,
    facility_id uuid references facilities(id) on delete cascade,
    is_active boolean default true,
    last_seen timestamptz,
    created_at timestamptz default now()
);

-- Sensor Readings
create table sensor_readings (
    id uuid default gen_random_uuid() primary key,
    sensor_id uuid references sensors(id) on delete cascade,
    facility_id uuid references facilities(id) on delete cascade,
    co2_ppm float not null,
    temperature float not null,
    humidity float not null,
    is_anomaly boolean default false,
    anomaly_type text,
    z_score float,
    timestamp timestamptz not null,
    created_at timestamptz default now()
);

-- Anomaly Events
create table anomaly_events (
    id uuid default gen_random_uuid() primary key,
    sensor_id uuid references sensors(id) on delete cascade,
    facility_id uuid references facilities(id) on delete cascade,
    anomaly_type text not null,
    z_score float,
    timestamp timestamptz not null,
    created_at timestamptz default now()
);

-- Carbon Credits
create table carbon_credits (
    id uuid default gen_random_uuid() primary key,
    facility_id uuid references facilities(id) on delete cascade,
    credits_issued float not null,
    period_start timestamptz not null,
    period_end timestamptz not null,
    ipfs_cid text,
    blockchain_tx_hash text,
    blockchain_token_id text,
    created_at timestamptz default now()
);

-- Auditor Assignments
create table auditor_assignments (
    id uuid default gen_random_uuid() primary key,
    auditor_id uuid references auth.users on delete cascade not null,
    facility_id uuid references facilities(id) on delete cascade not null,
    assigned_at timestamptz default now(),
    unique(auditor_id, facility_id)
);

-- FCM Tokens (for push notifications)
create table fcm_tokens (
    id uuid default gen_random_uuid() primary key,
    user_id uuid references auth.users on delete cascade,
    token text not null,
    created_at timestamptz default now()
);

-- Enable Row Level Security
alter table user_profiles enable row level security;
alter table facilities enable row level security;
alter table sensors enable row level security;
alter table sensor_readings enable row level security;
alter table anomaly_events enable row level security;
alter table carbon_credits enable row level security;
alter table auditor_assignments enable row level security;
alter table fcm_tokens enable row level security;
```

### 4. Smart Contract Deployment

```bash
cd backend/hardhat
npm install
npx hardhat compile
npx hardhat run scripts/deploy.js --network amoy
# Copy the deployed contract address to your .env CONTRACT_ADDRESS
```

### 5. Run the Backend

```bash
# From backend/ directory
source venv/bin/activate
uvicorn app.main:app --host 0.0.0.0 --port 8000 --reload
```

### 6. Android App Setup

1. Open `android/` folder in Android Studio
2. Update `android/app/build.gradle.kts` with your Supabase URL and anon key:
   ```kotlin
   buildConfigField("String", "SUPABASE_URL", "\"https://your-project.supabase.co\"")
   buildConfigField("String", "SUPABASE_ANON_KEY", "\"your-anon-key\"")
   buildConfigField("String", "FASTAPI_BASE_URL", "\"http://10.0.2.2:8000/api/v1/\"")
   ```
3. Sync Gradle and run on emulator or device

> **Note:** For emulator, `10.0.2.2` points to your host machine's localhost.

## Demo Mode (No Hardware Required)

For presentations or testing without physical sensors, enable the built-in simulator in `.env`:

```bash
SIMULATOR_ENABLED=true
SIMULATOR_DEVICE_ID=DEMO_SENSOR_001
SIMULATOR_AUTH_KEY=demo-auth-key-12345
SIMULATOR_INTERVAL_SECONDS=10
```

Register the demo sensor first (via API or SQL seed):

```sql
-- Insert demo facility
insert into facilities (name, company_name, location, industry_type, baseline_emissions)
values ('Demo Factory', 'CarbonTrust Demo Corp', 'Presentation Venue', 'Manufacturing', 100.0)
returning id;

-- Insert demo sensor (use the facility_id from above)
insert into sensors (device_id, auth_key, location_label, facility_id)
values (
    'DEMO_SENSOR_001',
    'b9d5e518459fdb3cf3579f507402e90a3eb4c38e9aa62b12cff1e71db5c806a6',
    'Chimney Stack A',
    'your-facility-uuid'
);
```

The simulator generates realistic CO₂/temperature/humidity readings every 10 seconds and feeds them through the complete pipeline.

## API Endpoints

| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| GET | `/health` | None | Health check |
| POST | `/api/v1/auth/fcm-token` | JWT | Register FCM token |
| GET | `/api/v1/ipfs/report/{cid}` | JWT | Fetch IPFS report |
| POST | `/api/v1/credits/recalculate/{facility_id}` | MANAGER | Manual credit recalculation |
| POST | `/api/v1/sensors/register` | JWT | Register sensor device |
| POST | `/api/v1/readings/submit` | JWT | Manual reading submission |
| POST | `/api/v1/facilities/create` | JWT | Create facility |
| GET | `/api/v1/facilities/{id}` | JWT | Get facility details |
| GET | `/api/v1/anomalies/{facility_id}` | JWT | Get anomaly events |
| PUT | `/api/v1/anomalies/{id}/acknowledge` | JWT | Acknowledge anomaly |

## Pipeline Flow

1. **Receive** MQTT sensor reading (or simulator)
2. **Authenticate** device (`device_id` + SHA-256 hashed `auth_key`)
3. **Store** raw reading in Supabase
4. **Detect** anomalies (Z-score > 3.0, frozen value, zero reading)
5. **Flag** anomaly → store event, send FCM push notification
6. **Calculate** credits if clean data window (24h default) using trapezoidal integration
7. **Build** emission report JSON (facility metadata, sensor inventory, anomalies)
8. **Upload** report to IPFS via Pinata
9. **Mint** credit on Polygon Amoy blockchain via smart contract
10. **Store** credit record with IPFS CID and blockchain tx hash in Supabase
11. **Notify** users via FCM push notification

## Environment Variables

| Variable | Description | Required |
|----------|-------------|----------|
| `SUPABASE_URL` | Supabase project URL | Yes |
| `SUPABASE_ANON_KEY` | Supabase anon/public key | Yes |
| `SUPABASE_SERVICE_ROLE_KEY` | Supabase service role key | Yes |
| `SUPABASE_JWT_SECRET` | Supabase JWT secret | Yes |
| `SECRET_KEY` | FastAPI secret key | Yes |
| `POLYGON_RPC_URL` | Polygon RPC endpoint | Yes |
| `POLYGON_CHAIN_ID` | Polygon chain ID (80002 for Amoy) | Yes |
| `CONTRACT_ADDRESS` | Deployed contract address | Yes |
| `DEPLOYER_PRIVATE_KEY` | MetaMask wallet private key | Yes |
| `PINATA_API_KEY` | Pinata API key | Yes |
| `PINATA_SECRET_API_KEY` | Pinata secret API key | Yes |
| `FIREBASE_CREDENTIALS_PATH` | Firebase Admin SDK JSON path | For FCM |
| `MQTT_BROKER_HOST` | MQTT broker hostname | For real sensors |
| `SIMULATOR_ENABLED` | Enable demo simulator | No (default: false) |

See `SETUP_GUIDE.md` for detailed instructions on obtaining each credential.

## Key Features

- **Real-time Monitoring:** CO₂, temperature, and humidity tracking from ESP32 sensors
- **Anomaly Detection:** Statistical Z-score analysis with configurable thresholds
- **Automated Credit Calculation:** Trapezoidal integration of emission reductions
- **Blockchain Verification:** Immutable credit records on Polygon Amoy testnet
- **IPFS Reports:** Tamper-proof emission reports with content-addressed storage
- **Role-based Access:** MANAGER and AUDITOR roles with Row-Level Security
- **Push Notifications:** FCM alerts for anomalies and credit issuance
- **Demo Simulator:** Built-in sensor simulator for presentations and testing

## Screenshots

<!-- Add your screenshots here -->
<!-- ![Dashboard](screenshots/dashboard.png) -->
<!-- ![Sensor Data](screenshots/sensor_data.png) -->
<!-- ![Credits](screenshots/credits.png) -->

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
