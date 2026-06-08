# Carbon Credit Verification System — Windows Run Guide

This guide provides detailed, step-by-step instructions for setting up, configuring, and running the entire Carbon Credit Verification System stack on a Windows machine.

---

## Table of Contents

1. [Prerequisites](#1-prerequisites)
2. [Step 1: Database Setup (Supabase)](#step-1-database-setup-supabase)
3. [Step 2: Smart Contract Setup & Deployment (Hardhat)](#step-2-smart-contract-setup--deployment-hardhat)
4. [Step 3: Backend Setup & Running (FastAPI)](#step-3-backend-setup--running-fastapi)
5. [Step 4: Running the Android App](#step-4-running-the-android-app)
6. [Troubleshooting & Windows-Specific Tips](#troubleshooting--windows-specific-tips)

---

## 1. Prerequisites

Before starting, ensure you have the following installed on your Windows system:

### A. Python (3.11+)
1. Download from [python.org](https://www.python.org/downloads/).
2. **CRITICAL:** During installation, ensure the checkbox **"Add Python to PATH"** is checked.
3. Verify in PowerShell:
   ```powershell
   python --version
   pip --version
   ```

### B. Node.js (v18+)
1. Download the LTS installer from [nodejs.org](https://nodejs.org/).
2. Run the installer and proceed with default options.
3. Verify in PowerShell:
   ```powershell
   node -v
   npm -v
   ```

### C. Git
1. Download from [git-scm.com](https://git-scm.com/download/win).
2. Install with default options.
3. Verify in PowerShell:
   ```powershell
   git --version
   ```

### D. Android Studio
1. Download and install [Android Studio](https://developer.android.com/studio).
2. Install the Android SDK and setup a virtual device (Emulator) via the Device Manager.

---

## Step 1: Database Setup (Supabase)

The system relies on Supabase for PostgreSQL database, authentication, and user profile management.

1. Sign up/log in to [Supabase](https://supabase.com).
2. Click **New Project**, fill in project details, and save your database password.
3. Once initialized, go to **SQL Editor** (left menu sidebar) → click **New Query**.
4. Copy the SQL schema below and paste it in the SQL Editor, then click **Run**:

   ```sql
   -- Users / Profiles (Custom Auth)
   create table user_profiles (
       id uuid primary key,
       role text not null check (role in ('MANAGER', 'AUDITOR')),
       facility_id uuid,
       full_name text,
       email text,
       password_hash text,
       fcm_token text,
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

   -- ESP32/Virtual Sensors
   create table sensors (
       id uuid default gen_random_uuid() primary key,
       device_id text not null unique,
       auth_key text not null,
       sim_auth_key_raw text, -- Required for virtual/simulator sensors to auto-start
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
       facility_id uuid references facilities(id) on delete cascade not null,
       period_id text not null,
       period_start timestamptz,
       period_end timestamptz,
       credits_issued numeric not null,
       credits_raw numeric,
       quality_factor numeric,
       actual_emissions numeric,
       emission_reduction numeric,
       baseline_used numeric,
       total_emissions_kg numeric,
       emission_reduction_kg numeric,
       anomaly_count integer default 0,
       report_hash text,
       tx_hash text,
       block_number bigint,
       ipfs_cid text,
       status text default 'verified',
       created_at timestamptz default now()
   );

   -- Auditor Assignments
   create table auditor_assignments (
       id uuid default gen_random_uuid() primary key,
       auditor_id uuid references user_profiles(id) on delete cascade not null,
       facility_id uuid references facilities(id) on delete cascade not null,
       assigned_by uuid references user_profiles(id),
       is_active boolean default true,
       assigned_at timestamptz default now(),
       unique(auditor_id, facility_id)
   );

   -- FCM Tokens (Legacy, if needed)
   create table fcm_tokens (
       id uuid default gen_random_uuid() primary key,
       user_id uuid references user_profiles(id) on delete cascade,
       token text not null,
       created_at timestamptz default now()
   );

   -- Notifications
   create table notifications (
       id uuid default gen_random_uuid() primary key,
       user_id uuid references user_profiles(id) on delete cascade not null,
       title text not null,
       body text not null,
       type text not null,
       related_id uuid,
       is_read boolean default false,
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

5. Go to **Project Settings** → **API** and copy these values for later configuration:
   - `SUPABASE_URL` (under Project API keys)
   - `SUPABASE_ANON_KEY`
   - `SUPABASE_SERVICE_ROLE_KEY`
   - `SUPABASE_JWT_SECRET` (under JWT Settings)

---

## Step 2: Smart Contract Setup & Deployment (Hardhat)

The smart contracts run on the Polygon Amoy Testnet. You will compile and deploy them using Hardhat.

1. Open PowerShell and navigate to the hardhat folder:
   ```powershell
   cd backend\hardhat
   ```
2. Install the required Node dependencies:
   ```powershell
   npm install
   ```
3. Compile the Solidity contracts:
   ```powershell
   npx hardhat compile
   ```
4. Set up your wallet & get Test MATIC:
   - Get the **MetaMask** extension and switch to **Polygon Amoy Testnet** (Chain ID: `80002`, RPC: `https://rpc-amoy.polygon.technology/`).
   - Request free testnet MATIC tokens from a faucet like [faucet.polygon.technology](https://faucet.polygon.technology/) or [amoyfaucet.com](https://amoyfaucet.com/).
   - Copy your MetaMask private key (Settings -> Account Details -> Show Private Key).
5. Before deploying, configure the deployment variables:
   - Hardhat expects `DEPLOYER_PRIVATE_KEY` in the environment. Set it in your current terminal session:
   ```powershell
   $env:DEPLOYER_PRIVATE_KEY="0xYourMetaMaskPrivateKeyGoesHere"
   ```
6. Deploy the smart contract:
   ```powershell
   npx hardhat run scripts/deploy.js --network amoy
   ```
7. Copy the contract address printed in the console:
   ```text
   CarbonCreditRegistry deployed to: 0x...
   ```
   Save this address for your `.env` configuration file in the next step.

---

## Step 3: Backend Setup & Running (FastAPI)

1. Open PowerShell and navigate to the `backend` directory:
   ```powershell
   cd d:\IDP\IDP 2\backend
   ```
2. Create a Python virtual environment:
   ```powershell
   python -m venv venv
   ```
3. Set the execution policy in your PowerShell session to allow running activation scripts, then activate the environment:
   ```powershell
    Set-ExecutionPolicy -ExecutionPolicy RemoteSigned -Scope Process
    .\venv\Scripts\Activate.ps1
    ```
   *(You should now see `(venv)` prepended to your command prompt).*

4. Install the backend Python dependencies:
   ```powershell
   pip install -r requirements.txt
   ```

5. Copy the environment template to create your `.env` configuration:
   ```powershell
   Copy-Item .env.example .env
   ```

6. Open the newly created `.env` file in a text editor (e.g. VS Code, Notepad) and update the variables:
   * **FastAPI Configuration**: Set `SECRET_KEY` (you can generate one with `openssl rand -hex 32` or just type a random 64-character string).
   * **Supabase Configuration**: Fill in `SUPABASE_URL`, `SUPABASE_ANON_KEY`, `SUPABASE_SERVICE_ROLE_KEY`, and `SUPABASE_JWT_SECRET`.
   * **IPFS (Pinata) Configuration**: Fill in `PINATA_API_KEY` and `PINATA_SECRET_API_KEY` from your [Pinata](https://pinata.cloud) account.
   * **Blockchain Configuration**: Paste the `CONTRACT_ADDRESS` you copied in Step 2 and your `DEPLOYER_PRIVATE_KEY`.
   * **Simulator Mode**: For testing without hardware, make sure these are set to:
     ```env
     SIMULATOR_ENABLED=true
     SIMULATOR_INTERVAL_SECONDS=10
     ```

7. Seed sample facility and virtual sensor data inside the Supabase database. Run these SQL commands in the Supabase SQL Editor:
   ```sql
   -- Create a sample facility
   insert into facilities (name, company_name, location, industry_type, baseline_emissions)
   values ('Demo Factory', 'EcoCorp Demo', 'Mumbai, India', 'Manufacturing', 400.0)
   returning id;
   
   -- Register the demo sensor (replace <facility-uuid> with the exact returned ID above)
   -- Note: device_id MUST start with 'SIM_' to enable the automatic simulator runner.
   insert into sensors (device_id, auth_key, sim_auth_key_raw, location_label, facility_id)
   values (
       'SIM_SENSOR_001',
       -- SHA-256 hash of 'demo-auth-key-12345'
       'b9d5e518459fdb3cf3579f507402e90a3eb4c38e9aa62b12cff1e71db5c806a6',
       'demo-auth-key-12345',
       'Chimney Stack A',
       '<facility-uuid>'
   );
   ```

8. Start the FastAPI development server:
   ```powershell
   uvicorn app.main:app --host 0.0.0.0 --port 8000 --reload
   ```
   Verify the simulator is working. You should see logs indicating simulator data is being periodically generated and successfully processed through the pipeline.

---

## Step 4: Running the Android App

1. Launch **Android Studio**.
2. Select **Open** and choose the `android` directory in `d:\IDP\IDP 2\android`.
3. Open `android/app/build.gradle.kts` and update the BuildConfig fields with your Supabase values and backend URL:
   ```kotlin
   buildConfigField("String", "SUPABASE_URL", "\"https://your-project.supabase.co\"")
   buildConfigField("String", "SUPABASE_ANON_KEY", "\"your-anon-key\"")
   
   // Set this to 10.0.2.2 to point to your computer's localhost if running inside an Android Emulator
   buildConfigField("String", "FASTAPI_BASE_URL", "\"http://10.0.2.2:8000/api/v1/\"")
   ```
   *Note: If testing on a physical Android device, connect the phone to the same Wi-Fi network and replace `10.0.2.2` with your Windows machine's local IP address (e.g. `http://192.168.1.15:8000/api/v1/`).*

4. Sync your Gradle project (**File -> Sync Project with Gradle Files**).
5. Choose your target emulator device or physical phone and click the green **Run (Play button)** icon.

---

## Troubleshooting & Windows-Specific Tips

* **Execution Policy Errors**: If you get a script execution policy error while activating `venv`, make sure you run:
  ```powershell
  Set-ExecutionPolicy -ExecutionPolicy RemoteSigned -Scope Process
  ```
  This setting is applied only to the current terminal session, ensuring your global system settings remain secure.

* **CMD (Command Prompt) Activation**: If you are using CMD instead of PowerShell, activate the virtual environment with:
  ```cmd
  .\venv\Scripts\activate.bat
  ```

* **Python Command Conflicts**: If `python` command doesn't work but triggers the Windows Store, ensure you add Python's installation folder to your Windows System environment variables (`PATH`), or try using `py` or `python3` instead.

* **Hardhat OpenSSL Error**: On some older Node/Windows installations, Hardhat might throw an SSL error. Update Node.js to the latest LTS version to resolve this.

* **Localhost Access (10.0.2.2)**: Remember that Android Emulator isolates network connections. Always use `10.0.2.2` to communicate with services hosted on the host computer (`localhost`), not `127.0.0.1` or `localhost`.
