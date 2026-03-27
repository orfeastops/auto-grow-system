# 🌿 Smart Grow Room Automation System

A fully autonomous indoor plant monitoring and control system built from scratch — spanning embedded firmware, cloud backend, and a native Android application. Designed and developed as a solo end-to-end project.

---

## 📸 Screenshots

| Dashboard | Controls | Settings |
|:-:|:-:|:-:|
| ![Dashboard](screenshots/dashboard.jpeg) | ![Controls](screenshots/controls.jpeg) | ![Settings](screenshots/settings.jpeg) |
| Live sensor readings & per-plant soil moisture | System kill switches & pump overrides | Configurable thresholds for all automation logic |

---

## 🏗️ System Architecture

```
┌─────────────────────────┐
│      Android App        │  ← Java, Android Studio
│  Dashboard / Controls   │
│       / Settings        │
└────────────┬────────────┘
             │ HTTPS REST
             ▼
┌─────────────────────────┐
│   Node.js / Express     │  ← Ubuntu laptop, PM2
│   SQLite Database       │
│   Cloudflare Tunnel     │  ← api.********.org
└────────────┬────────────┘
             │ HTTP POST / GET
             ▼
┌─────────────────────────┐
│      ESP32-S3           │  ← C++ / Arduino framework
│  Sensors · Relays · NTP │
└─────────────────────────┘
```

---

## ✨ Features

### Firmware (ESP32-S3)
- **Soil moisture monitoring** via ADS1115 ADC (3 capacitive sensors on channels A0, A1, A2)
- **Temperature & humidity** via DHT11 on GPIO18
- **Automated watering** with hysteresis-based state machine per plant:
  - Pump activates below 35% soil moisture, runs for exactly 10 seconds
  - Re-arm only after moisture exceeds 45% — prevents overwatering and relay chatter
- **NTP-synced lighting schedule**: 22 hours on / 2 hours off (02:00–04:00), timezone-aware (Greece/Athens)
- **Hysteresis-controlled ventilation**:
  - Fan ON at ≥50°C or ≥70% humidity
  - Fan OFF only when temp <40°C AND humidity <60%
- WiFi connectivity with automatic reconnect and NTP re-sync after reboot

### Backend (Node.js / Express)
- REST API serving live sensor data and accepting configuration updates
- SQLite database for historical readings and configuration persistence
- Managed with **PM2** for process resilience and auto-restart
- Exposed securely via **Cloudflare Tunnel** (no port forwarding required)
- Endpoint: `api.**********.org`

### Android App (Java)
- **Dashboard**: Real-time temperature, humidity, lighting status, ventilation status, and per-plant soil moisture with color-coded levels
- **Controls**: System kill switches for Master Lighting, Ventilation System, and individual Pump Overrides (Plant A/B/C) with live STATUS indicators
- **Settings**: Full remote configuration of all automation thresholds — moisture levels, watering duration, fan temperature & humidity limits, lighting schedule

---

## 🔧 Hardware

| Component | Role | GPIO / Interface |
|---|---|---|
| ESP32-S3 | Main microcontroller | — |
| ADS1115 | 16-bit ADC for soil sensors | I2C |
| Capacitive Soil Sensors (×3) | Per-plant moisture reading | ADS1115 A0–A2 |
| DHT11 | Temperature & ambient humidity | GPIO 18 |
| Relay Module (×4) | Controls pumps, light, fan | GPIO 4, 5, 6, 7, 8 |
| Water Pumps (×3) | Automated irrigation | GPIO 4, 5, 6 |
| Grow Light | NTP-scheduled lighting | GPIO 7 |
| Ventilation Fan | Hysteresis-controlled airflow | GPIO 8 |

**Estimated hardware cost: ~40€**

> 📷 *Hardware & wiring photo coming soon*

---

## 🚀 Setup & Installation

### 1. Firmware (ESP32-S3)

**Dependencies:**
- Arduino IDE or PlatformIO
- Libraries: `Adafruit ADS1X15`, `DHT sensor library`, `WiFi`, `NTPClient`

**Steps:**
```bash
# Open arduino main code.cc in Arduino IDE
# Set your WiFi credentials and server URL in the config section
# Select board: ESP32S3 Dev Module
# Flash via USB
```

**Key configuration constants** (top of `arduino main code.cc`):
```cpp
const char* WIFI_SSID     = "your_ssid";
const char* WIFI_PASSWORD = "your_password";
const char* SERVER_URL    = "https://api.********.org";

const int   MOISTURE_LOW  = 35;   // % — pump activates
const int   MOISTURE_HIGH = 45;   // % — pump re-arm threshold
const int   PUMP_DURATION = 10;   // seconds
const int   TEMP_HIGH     = 50;   // °C — fan on
const int   TEMP_LOW      = 40;   // °C — fan off
const int   HUM_HIGH      = 70;   // % — fan on
const int   HUM_LOW       = 60;   // % — fan off
```

---

### 2. Backend (Node.js)

**Requirements:** Node.js ≥ 18, npm, PM2, Cloudflare account

```bash
# Install dependencies
cd server
npm install

# Start with PM2
pm2 start server.js --name greenhouse-api
pm2 save
pm2 startup   # enable auto-start on boot
```

**Cloudflare Tunnel setup:**
```bash
cloudflared tunnel create greenhouse
cloudflared tunnel route dns greenhouse api.karnagio.org
cloudflared tunnel run greenhouse
```

Configure as a systemd service for persistent operation:
```bash
sudo cloudflared service install
sudo systemctl enable cloudflared
sudo systemctl start cloudflared
```

---

### 3. Android App

**Requirements:** Android Studio, JDK 17+, Android SDK

```bash
# Clone the repo and open in Android Studio
# Update the base URL in the network config:
#   BASE_URL = "https://api.********.org"

# Build → Generate Signed APK
# Install on device via ADB or sideload
```

---

## 📁 Repository Structure

```
arduino-ide/
├── arduino main code.cc    # ESP32-S3 firmware (C++)
├── server/                 # Node.js + Express backend
├── app/                    # Android application (Java)
├── html/                   # Optional web UI
└── domain ssh              # Cloudflare tunnel config notes
```

---

## 🔮 Roadmap

- [ ] Hardware photo & wiring diagram
- [ ] Real-time push notifications (moisture alerts, temperature warnings)
- [ ] Historical data charts in the Android app
- [ ] OTA firmware updates via the backend
- [ ] Multi-grow-room support

---

## 🛠️ Tech Stack

| Layer | Technology |
|---|---|
| Microcontroller | ESP32-S3, C++ (Arduino framework) |
| Sensors / ADC | ADS1115, DHT11 |
| Backend | Node.js, Express, SQLite |
| Process management | PM2 |
| Tunneling / DNS | Cloudflare Tunnel |
| Mobile | Android (Java), Android Studio |

---

## 👤 Author

**Orfeas** — Designed, wired, and built end-to-end as a solo project.

- GitHub: [@orfeastops](https://github.com/orfeastops)
- Repo: [github.com/orfeastops/arduino-ide](https://github.com/orfeastops/arduino-ide)

---

## 📄 License

This project is open source. Feel free to adapt it for your own automation projects.
