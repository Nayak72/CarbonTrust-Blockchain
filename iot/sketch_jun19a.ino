#include <WiFi.h>
#include <WiFiClientSecure.h>
#include <PubSubClient.h>
#include <ArduinoJson.h>
#include <DHT.h>
#include <time.h>

// ==============================
// CONFIGURATION
// ==============================
const char* ssid = "Nayak";
const char* password = "Koushik2006";

// MQTT Config
const char* mqtt_server = "172.29.249.181"; // Backend IP
const int mqtt_port = 8883; // <-- CHANGED TO 8883 (TLS port)
const char* mqtt_user = "backend_user"; // From backend .env
const char* mqtt_password = "hello123"; // From backend .env

// Device / DB Config
const char* device_id = "SIM_5544F5B2";
const char* auth_key = "a109c714-63b0-42e3-88f0-325b7c5083bd"; // Plain text key
const char* facility_id = "5544f5b2-fd39-483f-ab0f-a708aac1a8eb"; // Target facility

// Hardware Pins
#define DHTPIN 4
#define DHTTYPE DHT11
#define MQ135_PIN 34

// ==============================

DHT dht(DHTPIN, DHTTYPE);
WiFiClientSecure espClient;
PubSubClient client(espClient);

void setup_wifi() {
  delay(10);
  Serial.println();
  Serial.print("Connecting to ");
  Serial.println(ssid);

  WiFi.begin(ssid, password);
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }
  Serial.println("\nWiFi connected");
  
  // Initialize NTP for timestamps
  configTime(0, 0, "pool.ntp.org");
}

void reconnect() {
  while (!client.connected()) {
    Serial.print("Attempting MQTT connection...");
    String clientId = "ESP32Client-" + String(random(0, 1000));
    
    if (client.connect(clientId.c_str(), mqtt_user, mqtt_password)) {
      Serial.println("connected");
    } else {
      Serial.print("failed, rc=");
      Serial.print(client.state());
      Serial.println(" try again in 5 seconds");
      delay(5000);
    }
  }
}

String getISO8601Time() {
  struct tm timeinfo;
  if (!getLocalTime(&timeinfo)) {
    return "1970-01-01T00:00:00Z"; // Fallback
  }
  char buffer[30];
  strftime(buffer, sizeof(buffer), "%Y-%m-%dT%H:%M:%SZ", &timeinfo);
  return String(buffer);
}

void setup() {
  Serial.begin(115200);
  dht.begin();
  
  setup_wifi();
  
  // Since we use TLS (Port 8883), but don't have the backend's CA cert hardcoded,
  // we set the client to insecure mode for local testing.
  espClient.setInsecure();
  
  client.setServer(mqtt_server, mqtt_port);
}

void loop() {
  if (!client.connected()) {
    reconnect();
  }
  client.loop();

  // Read sensors
  float humidity = dht.readHumidity();
  float temperature = dht.readTemperature();
  
  // Basic MQ135 Read (0-4095 mapping to approx 400-2000 ppm)
  // For production, calibrate using MQ135 library
  int raw_mq135 = analogRead(MQ135_PIN);
  float co2_ppm = map(raw_mq135, 0, 4095, 400, 2000); 

  if (isnan(humidity) || isnan(temperature)) {
    Serial.println("Failed to read from DHT sensor!");
    delay(5000);
    return;
  }

  // Construct JSON (ArduinoJson v7+ Syntax)
  JsonDocument doc; 
  doc["device_id"] = device_id;
  doc["auth_key"] = auth_key;
  doc["co2_ppm"] = co2_ppm;
  doc["temperature"] = temperature;
  doc["humidity"] = humidity;
  doc["timestamp"] = getISO8601Time();

  char jsonBuffer[256];
  serializeJson(doc, jsonBuffer);

  // Publish to topic
  String topic = String("factory/") + facility_id + "/readings";
  Serial.print("Publishing to ");
  Serial.print(topic);
  Serial.print(": ");
  Serial.println(jsonBuffer);
  
  client.publish(topic.c_str(), jsonBuffer);

  delay(10000); // Send every 10 seconds
}
