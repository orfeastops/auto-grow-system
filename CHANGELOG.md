# Changelog

All notable changes to this project will be documented in this file.

## [1.0.0] - 2026-03-30

### Added
- ✅ Complete ESP32-S3 firmware with NTP time sync
- ✅ Node.js Express backend with SQLite persistence
- ✅ Native Android app (Java) with dashboard, controls, settings
- ✅ REST API with 6 documented endpoints
- ✅ API key authentication on all data endpoints
- ✅ Cloudflare Tunnel integration for secure public access
- ✅ PM2 process management support
- ✅ Professional README with architecture diagrams
- ✅ Local development environment setup
- ✅ Real-time sensor data streaming
- ✅ Per-plant automated watering with hysteresis
- ✅ NTP-synced lighting schedule (22h on / 2h off)
- ✅ Hysteresis-based ventilation control
- ✅ Historical data tracking (24-hour default)
- ✅ System kill switches and pump overrides

### Features
- **Sensor Monitoring**: 3 soil moisture sensors, DHT11 temperature/humidity, ADS1115 ADC
- **Actuators**: 3 water pumps, grow light, ventilation fan via relay modules
- **Automation**: Hysteresis-based state machines for watering and ventilation
- **UI**: Real-time dashboard with color-coded sensor readings
- **Configuration**: Remote settings management via REST API
- **Data**: SQLite persistent storage with queryable history
- **Security**: API key-based authentication
- **Deployment**: Ready for production with PM2 and Cloudflare Tunnel

### Tech Stack
- **Firmware**: ESP32-S3, C++ (Arduino framework)
- **Backend**: Node.js 18+, Express, SQLite3
- **Mobile**: Android, Java, OkHttp3, Gson
- **Deployment**: PM2, Cloudflare Tunnel, Docker-ready
- **Build**: Gradle, npm

### Documentation
- Complete README with system architecture
- API endpoint reference with curl examples
- Hardware bill of materials (~40€)
- Setup instructions for local dev and production
- Security best practices
- Roadmap for future enhancements

### Testing
- ✅ Local backend tested with curl
- ✅ All endpoints verified
- ✅ Android app tested on real device
- ✅ Integration between server and app validated

---

## Future Roadmap

### Planned Features
- [ ] Push notifications (moisture/temperature alerts)
- [ ] Historical data charts in Android app
- [ ] OTA firmware updates
- [ ] Multi-grow-room support
- [ ] OAuth2 authentication
- [ ] Docker containerization with docker-compose
- [ ] Firebase Cloud Messaging integration
- [ ] Web dashboard (React/Vue)
- [ ] Machine learning-based watering optimization
- [ ] Mobile app for iOS (React Native or native)

---

## Contributing

This is a personal portfolio project. If you find this useful, feel free to fork and customize for your own setup.

---

## License

Open source. Adapt freely for your projects.
