# Sensor Dataset Description

## Overview
This dataset contains time-series environmental sensor readings along with corresponding anomaly labels. The data is recorded at regular 5-minute intervals. The dataset includes a total of 50,001 data points.

## File Information
- **File Name:** `sensor_data.csv`
- **Format:** Comma-Separated Values (CSV)
- **Total Rows:** 50,001 (excluding header)
- **Time Resolution:** 5 minutes

## Columns and Features
The dataset consists of 6 columns representing timestamps, environmental metrics, and anomaly classifications:

| Column Name     | Data Type           | Description                                                                                                     |
| :-------------- | :------------------ | :-------------------------------------------------------------------------------------------------------------- |
| `timestamp`     | Datetime (ISO 8601) | The UTC timestamp of the sensor reading (e.g., `2026-06-25T00:00:00Z`).                                         |
| `co2_ppm`       | Float               | Carbon Dioxide concentration in parts per million (ppm). Normal values typically range between 400 and 600 ppm. |
| `temperature_c` | Float               | Temperature reading in degrees Celsius (°C). Normal operating temperatures appear to be around 20-25 °C.        |
| `humidity_perc` | Float               | Relative humidity percentage (%). The normal range is approximately 40-60%.                                     |
| `is_anomaly`    | Boolean             | A binary label indicating whether the current reading is considered anomalous (`True`) or normal (`False`).     |
| `anomaly_type`  | String              | A categorical label specifying the type of anomaly.                                                             |

### Anomaly Types
The `anomaly_type` column can contain the following values:
*   `NONE`: Indicates a normal reading without any anomalies.
*   `DROP`: Indicates an abnormally low reading across the sensors (e.g., temperature dropping below 15°C, CO2 dropping below 300 ppm).
*   `SPIKE`: Indicates an abnormally high reading across the sensors (e.g., temperature spiking above 30°C, CO2 spiking above 1200 ppm).

## Potential Use Cases
This dataset is well-structured and labeled, making it highly suitable for:
1.  **Time-Series Anomaly Detection:** Applying statistical models (e.g., Z-Score, Moving Averages) to identify unusual patterns or sensor failures.
2.  **Environmental Monitoring:** Analyzing trends in indoor or outdoor environmental quality.
3.  **Predictive Maintenance:** Correlating sensor spikes or drops with potential equipment malfunctions.

## Integration Guide

### 1. Application Integration Workflow
To use this dataset in your application, you will typically follow a pipeline that involves data ingestion, preprocessing, statistical analysis, and action/visualization.

**Flow Diagram:**
```text
 🗄️ sensor_data.csv
        │
        ▼ (Data Loading)
 ⚙️ Data Preprocessing
        │
        ▼ (Cleaned Data)
 🔀 Application Mode
   ┌────┴────┐
   │         │
   ▼         ▼
 📐 Compute  🚀 Real-time Execution
 Thresholds  │
    │        ▼ (Evaluation)
    └──────▶🚨 Anomaly Detection Service
             │
             ▼
            📊 Alerts & Dashboard
```

### 2. Implementation Steps

#### A. Data Loading & Parsing
- Read the CSV file into a Pandas DataFrame (Python) or a similar data structure in your backend language.
- Parse the `timestamp` column into native Datetime objects to enable time-series indexing and sorting.

#### B. Data Preprocessing
- **Handling Missing Values:** Verify if any readings are missing and apply interpolation if necessary.
- **Smoothing:** Apply moving averages or exponential smoothing to reduce noise in the sensor readings.
- **Transformation:** Calculate derivatives (rate of change) if you want to detect rapid spikes or drops using statistical thresholds.

#### C. Statistical Anomaly Detection
Instead of machine learning, this workflow applies statistical techniques (e.g., Z-scores, standard deviation thresholds, or interquartile range (IQR)). You can use the `is_anomaly` column to evaluate the accuracy of your chosen statistical thresholds.

**Statistical Model Pipeline:**
```text
[Data Prep]
 📄 Raw Data ──▶ 🧹 Handle Missing ──▶ 📈 Compute Mean/Std Dev
                                              │
                                              ▼
[Thresholding Phase]
 📉 Evaluate ◀── 📐 Apply Z-Score / IQR Rules
 Accuracy           (Compare with 'is_anomaly')
      │
      ▼
[Deployment]
 🚀 Deploy Statistical Rules
```

#### D. Real-Time Inference Simulation
To simulate a real-time environment in your project:
1. Load the CSV into memory.
2. Create a script or service that iterates through the rows one by one, simulating a 5-minute interval (or running at an accelerated rate).
3. Send this data via a message broker (like MQTT, Kafka) or a REST API to your application backend as if it were a physical sensor.

**System Sequence:**
```text
 📡 Streamer       🌐 Backend API       📈 Stat Engine     💻 Dashboard
      │                 │                 │                 │
      ├── POST data ───▶│                 │                 │
      │                 ├── Run Check ───▶│                 │
      │                 │                 │                 │
      │                 │◀── Anomaly Flag ┤                 │
      │                 │                 │                 │
      │                 ├── Update UI & Alerts ────────────▶│
      │                 │                 │                 │
```
