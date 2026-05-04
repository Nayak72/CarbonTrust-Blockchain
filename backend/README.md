# Carbon Credit Verification Backend

Blockchain-based carbon credit verification and emission monitoring system backend.

## Architecture

```
[ESP32 + SCD30 Sensor] → [MQTT Broker] → [FastAPI Pipeline] → [Supabase + IPFS + Polygon] → [Android App]
```

## Tech Stack

- **Backend:** FastAPI + Python 3.11+
- **Database:** Supabase (PostgreSQL)
- **Blockchain:** Polygon Amoy Testnet (Solidity smart contract)
- **IPFS:** Pinata (managed pinning)
- **MQTT:** Mosquitto broker
- **Push Notifications:** Firebase Cloud Messaging

## Quick Start

### 1. Clone and setup environment
```bash
cp .env.example .env
# Fill in all values in .env
```

### 2. Install Python dependencies
```bash
python -m venv venv
source venv/bin/activate
pip install -r requirements.txt
```

### 3. Deploy Smart Contract
```bash
cd hardhat
npm install
npx hardhat compile
npx hardhat run scripts/deploy.js --network amoy
# Copy the contract address to .env CONTRACT_ADDRESS
```

### 4. Setup Supabase
Run the SQL schema in your Supabase SQL editor (see project documentation).

### 5. Run the backend
```bash
uvicorn app.main:app --host 0.0.0.0 --port 8000 --reload
```

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

1. Receive MQTT sensor reading
2. Authenticate device (device_id + auth_key)
3. Store raw reading in Supabase
4. Run anomaly detection (Z-score, frozen value, zero reading)
5. If anomaly → flag, notify, stop
6. If clean → check credit calculatmion window
7. Calculate credits (trapezoidal integration)
8. Upload emission report to IPFS
9. Record credit on Polygon blockchain
10. Store in Supabase
11. Send FCM push notification
