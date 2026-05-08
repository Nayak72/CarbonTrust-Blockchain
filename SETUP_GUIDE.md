# Carbon Credit Verification System — Complete Setup Guide

This guide walks you through obtaining every API key, credential, password, and config value needed to run the full stack.

---

## Table of Contents

1. [Supabase (Database + Auth)](#1-supabase-database--auth)
2. [Pinata (IPFS)](#2-pinata-ipfs)
3. [Polygon Amoy (Blockchain)](#3-polygon-amoy-blockchain)
4. [Firebase (Push Notifications)](#4-firebase-push-notifications)
5. [MQTT (Mosquitto Broker)](#5-mqtt-mosquitto-broker)
6. [Smart Contract Deployment](#6-smart-contract-deployment)
7. [Supabase Database Schema](#7-supabase-database-schema)
8. [Final .env Checklist](#8-final-env-checklist)

---

## 1. Supabase (Database + Auth)

Supabase hosts your PostgreSQL database, authentication, and JWT tokens.

### Steps

1. Go to [https://supabase.com](https://supabase.com) and sign up / log in.
2. Click **"New Project"**.
3. Choose a name, database password, and region (pick one close to your backend server).
4. Wait for the project to initialize.

### Values to copy

| Variable | Where to find |
|---|---|
| `SUPABASE_URL` | Project Settings → API → URL (looks like `https://xxxxx.supabase.co`) |
| `SUPABASE_ANON_KEY` | Project Settings → API → `anon public` key |
| `SUPABASE_SERVICE_ROLE_KEY` | Project Settings → API → `service_role secret` key |
| `SUPABASE_JWT_SECRET` | Project Settings → API → JWT Settings → JWT Secret |
| `SECRET_KEY` | Generate yourself: `openssl rand -hex 32` or any random 64-char string |

### Set up Auth (for Android app users)

1. In Supabase Dashboard → Authentication → Providers
2. Enable **Email** provider (or any provider your app uses).
3. Go to Authentication → URL Configuration and set your site URL.

---

## 2. Pinata (IPFS)

Pinata stores emission report PDFs on IPFS before blockchain anchoring.

### Steps

1. Go to [https://pinata.cloud](https://pinata.cloud) and sign up.
2. Navigate to **API Keys** in the dashboard.
3. Click **New Key** → name it "Carbon Credit Backend".
4. Select scopes: `pinFileToIPFS` and `pinJSONToIPFS`.

### Values to copy

| Variable | Where to find |
|---|---|
| `PINATA_API_KEY` | API Keys → click your key → "API Key" |
| `PINATA_SECRET_API_KEY` | API Keys → click your key → "API Secret" |
| `PINATA_GATEWAY` | Default is `https://gateway.pinata.cloud` (or your custom gateway) |

---

## 3. Polygon Amoy (Blockchain)

The smart contract runs on Polygon Amoy Testnet.

### Steps

1. Install **MetaMask** browser extension.
2. Add **Polygon Amoy Testnet** to MetaMask:
   - Network Name: `Polygon Amoy`
   - RPC URL: `https://rpc-amoy.polygon.technology/`
   - Chain ID: `80002`
   - Currency Symbol: `MATIC`
3. Get free test MATIC from a faucet:
   - [https://faucet.polygon.technology](https://faucet.polygon.technology)
   - Or [https://amoyfaucet.com](https://amoyfaucet.com)
4. In MetaMask, click your account → **Account Details** → **Show private key**.
5. Copy the private key (starts with `0x`).

### Values

| Variable | Value / Source |
|---|---|
| `POLYGON_RPC_URL` | `https://rpc-amoy.polygon.technology/` |
| `POLYGON_CHAIN_ID` | `80002` |
| `DEPLOYER_PRIVATE_KEY` | Your MetaMask account private key |
| `CONTRACT_ADDRESS` | Obtained after deploying the contract (see Section 6) |

**IMPORTANT:** Never share or commit your private key. Keep it only in `.env`.

---

## 4. Firebase (Push Notifications)

Firebase Cloud Messaging sends anomaly and credit-issued push notifications to the Android app.

### Steps

1. Go to [https://console.firebase.google.com](https://console.firebase.google.com) and create a new project (or use existing).
2. Click the **gear icon** → **Project Settings**.
3. Go to **Service Accounts** tab.
4. Click **Generate new private key**.
5. Download the JSON file (looks like `carbon-credit-xxx-firebase-adminsdk-xxx.json`).
6. Move it to `backend/firebase-credentials.json`.

### Value

| Variable | Value |
|---|---|
| `FIREBASE_CREDENTIALS_PATH` | `./firebase-credentials.json` (relative to backend root) |

---

## 5. MQTT (Mosquitto Broker)

MQTT receives real-time sensor readings from ESP32 devices. For the demo, you can skip this if using the simulator.

### Option A: Local Mosquitto (for testing)

```bash
sudo apt install mosquitto mosquitto-clients
sudo systemctl start mosquitto
```

Use default config:

```env
MQTT_BROKER_HOST=localhost
MQTT_BROKER_PORT=1883
MQTT_USERNAME=
MQTT_PASSWORD=
MQTT_TLS_ENABLED=false
```

### Option B: Remote/Cloud MQTT (production)

Use HiveMQ, EMQX, or Mosquitto on a VPS.

| Variable | Value |
|---|---|
| `MQTT_BROKER_HOST` | Your broker hostname |
| `MQTT_BROKER_PORT` | `1883` (non-TLS) or `8883` (TLS) |
| `MQTT_USERNAME` | Your broker username |
| `MQTT_PASSWORD` | Your broker password |
| `MQTT_TLS_ENABLED` | `true` if port 8883, else `false` |
| `MQTT_CA_CERT_PATH` | Path to CA certificate file (if using TLS) |

---

## 6. Smart Contract Deployment

Deploy the `CarbonCreditRegistry` contract to Polygon Amoy.

### Prerequisites

- Node.js + npm installed
- `DEPLOYER_PRIVATE_KEY` set in `.env`
- Test MATIC in your wallet

### Steps

```bash
cd backend/hardhat
npm install
npx hardhat compile
npx hardhat run scripts/deploy.js --network amoy
```

After deployment, the script prints:

```
CarbonCreditRegistry deployed to: 0x...
Add this to your .env: CONTRACT_ADDRESS=0x...
```

Copy that address into your `.env`.

---

## 7. Supabase Database Schema

Run this SQL in Supabase Dashboard → SQL Editor → New Query.

```sql
-- Users / Profiles (linked to Supabase Auth)
create table user_profiles (
    id uuid references auth.users on delete cascade primary key,
    role text not null check (role in ('MANAGER', 'AUDITOR')),
    facility_id uuid,
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

-- FCM Tokens (for push notifications)
create table fcm_tokens (
    id uuid default gen_random_uuid() primary key,
    user_id uuid references auth.users on delete cascade,
    token text not null,
    created_at timestamptz default now()
);

-- Enable Row Level Security (RLS) on all tables
alter table user_profiles enable row level security;
alter table facilities enable row level security;
alter table sensors enable row level security;
alter table sensor_readings enable row level security;
alter table anomaly_events enable row level security;
alter table carbon_credits enable row level security;
alter table fcm_tokens enable row level security;
```

### Seed sample data (optional, for demo)

```sql
-- Create a sample facility
insert into facilities (name, location, baseline_co2_ppm, flow_rate_m3_s)
values ('Factory A', 'Mumbai, India', 400.0, 0.5)
returning id;
-- Copy the returned UUID for the next step

-- Register the demo sensor (replace <facility-uuid> with actual ID)
insert into sensors (device_id, auth_key, location_label, facility_id)
values (
    'DEMO_SENSOR_001',
    -- SHA-256 hash of 'demo-auth-key-12345'
    'c3ab8ff13720e8ad9047dd39466b3c8974e592c2fa383d4a3960714caef0c4f2',
    'Chimney Stack A',
    '<facility-uuid>'
);
```

> **Note:** The `auth_key` must be the SHA-256 hash of the raw key. The backend hashes incoming raw keys before comparing.
> For `demo-auth-key-12345`, the hash is `c3ab8ff13720e8ad9047dd39466b3c8974e592c2fa383d4a3960714caef0c4f2`.

---

## 8. Final .env Checklist

Copy `.env.example` to `.env` and fill in every value below:

```env
# FastAPI
APP_ENV=development
APP_HOST=0.0.0.0
APP_PORT=8000
SECRET_KEY=<openssl rand -hex 32>
SUPABASE_JWT_SECRET=<from Supabase Settings → API>

# Supabase
SUPABASE_URL=https://<project>.supabase.co
SUPABASE_ANON_KEY=<anon public key>
SUPABASE_SERVICE_ROLE_KEY=<service_role secret key>

# MQTT (skip if only using simulator)
MQTT_BROKER_HOST=localhost
MQTT_BROKER_PORT=1883
MQTT_USERNAME=
MQTT_PASSWORD=
MQTT_TLS_ENABLED=false
MQTT_CA_CERT_PATH=./mosquitto/certs/ca.crt

# Pinata IPFS
PINATA_API_KEY=<from Pinata API Keys>
PINATA_SECRET_API_KEY=<from Pinata API Keys>
PINATA_GATEWAY=https://gateway.pinata.cloud

# Polygon Blockchain
POLYGON_RPC_URL=https://rpc-amoy.polygon.technology/
POLYGON_CHAIN_ID=80002
CONTRACT_ADDRESS=<after deployment>
DEPLOYER_PRIVATE_KEY=<MetaMask private key — keep secret!>

# Firebase Admin SDK
FIREBASE_CREDENTIALS_PATH=./firebase-credentials.json

# Emission Calculation (defaults are fine for demo)
AMBIENT_CO2_PPM=400
DEFAULT_FLOW_RATE_M3_S=0.5
CREDIT_CALCULATION_WINDOW_HOURS=24
ANOMALY_ZSCORE_THRESHOLD=3.0
ANOMALY_FROZEN_COUNT_THRESHOLD=5

# Polygonscan (optional, for contract verification)
POLYGONSCAN_API_KEY=<from polygonscan.com>

# Simulator (for demo / presentation without real sensors)
SIMULATOR_ENABLED=true
SIMULATOR_DEVICE_ID=DEMO_SENSOR_001
SIMULATOR_AUTH_KEY=demo-auth-key-12345
SIMULATOR_INTERVAL_SECONDS=10
```

---

## Quick Verification After Setup

1. **Database** — Tables created in Supabase SQL Editor.
2. **Smart Contract** — Deployed, address copied to `.env`.
3. **Backend** — Run:
   ```bash
   cd backend
   source venv/bin/activate
   uvicorn app.main:app --host 0.0.0.0 --port 8000 --reload
   ```
4. **Simulator** — With `SIMULATOR_ENABLED=true`, you should see logs like:
   ```
   Simulator sending reading: CO2=542.3 ppm, T=30.1C, H=54.2%
   Reading stored: ... for facility ...
   ```
5. **Android App** — Sign in with Supabase Auth, view live readings and credits.

---

## Need Help?

- **Supabase:** [supabase.com/docs](https://supabase.com/docs)
- **Pinata:** [docs.pinata.cloud](https://docs.pinata.cloud)
- **Polygon:** [docs.polygon.technology](https://docs.polygon.technology)
- **Firebase FCM:** [firebase.google.com/docs/cloud-messaging](https://firebase.google.com/docs/cloud-messaging)
- **Hardhat:** [hardhat.org/docs](https://hardhat.org/docs)
