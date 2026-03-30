# 🌿 Auto Grow System

Fully working end-to-end greenhouse automation: ESP32 + Node.js backend + Android app.

## ✅ Project Status
- [x] ESP32 firmware (Arduino C++)
- [x] Backend REST + SQLite server (Node.js, Express)
- [x] Android app (Java, OkHttp + Gson)
- [x] Real-time states + historical data
- [x] Security with API key header
- [x] Local dev tests passed

## 📁 Repository structure
```
arduino main code.cc
server                    # Node/Express backend file
app/                      # Android Studio app module
html/                     # optional React-based web UI code
.gitignore
README.md
package.json
.env.example
```

## 🛠️ Server setup (local or cloud)
1. `cd /home/linux/Desktop/greenhouseapp/auto-grow-system`
2. `npm install`
3. `cp .env.example .env`
4. Edit `.env`:
   - `API_KEY=your-secret-key`
   - `PORT=3000`
5. `node server`

### Start with PM2 (production)
```bash
pm i -g pm2
pm2 start server --name greenhouse-api --update-env
pm2 save
```

### Cloudflare tunnel (optional for public access)
*Configure `cloudflared` as tunnel*:\
`cloudflared tunnel create greenhouse`\
`cloudflared tunnel route dns greenhouse api.example.org`\
`cloudflared tunnel run greenhouse`

**APP URL**: `https://api.example.org`

## 🔐 API key
the backend expects `x-api-key` header on /api/data,/api/settings,/api/data/* endpoints.

## 📘 Node API
| Method | Path | Auth | Description |
|---|---|---|---|
| POST | /api/data | API Key | ESP32 sends sensor state |
| GET | /api/data/latest | API Key | Latest sensor snapshot |
| GET | /api/data/history?hours=N | API Key | History (default 24h) |
| GET | /api/settings | API Key | Read settings |
| POST | /api/settings | API Key | Write setting |
| GET | /api/health | open | Health check |

## 📲 Android setup
1. Open `app/` in Android Studio.
2. Set `ApiClient.BASE_URL` to your backend host (e.g. `https://api.example.org`).
3. Make sure `API_KEY` is the same as in server `.env`.
4. Build & run.

## 🧪 Quick local verification commands
```bash
curl -H 'x-api-key:yourkey' http://localhost:3000/api/health
curl -H 'x-api-key:yourkey' http://localhost:3000/api/settings
curl -X POST -H 'x-api-key:yourkey' -H 'Content-Type: application/json' -d '{"light_enabled":"0"}' http://localhost:3000/api/settings
curl -X POST -H 'x-api-key:yourkey' -H 'Content-Type: application/json' -d '{"moisture1":30,"moisture2":32,"moisture3":31,"temperature":22.5,"humidity":55.4,"pump1":1,"pump2":0,"pump3":0,"fan":1,"light":1}' http://localhost:3000/api/data
curl -H 'x-api-key:yourkey' http://localhost:3000/api/data/latest
``` 

## 🧹 GitHub prepare
- Keep `.env` out of repo.
- Keep actual API key secret.
- Use `.env.example` with placeholders.

## 🧾 Notes
- `server` file is the main Node.js backend, already refactored for robustness.
- `.env.example` should be in repo: no secret.
- If you want, add a `.github/workflows/nodejs.yml` then I can provide.
