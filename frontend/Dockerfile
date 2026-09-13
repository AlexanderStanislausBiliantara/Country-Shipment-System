# Frontend multi-stage builder — React SPA (Vite + npm).
# Expects YOU to add: package.json + src/ + index.html + vite.config.js
# (standard Vite React layout) into ./frontend.
# Build context is ./frontend (see docker-compose.yaml).
# No other template files needed — nginx config is created inline.

# ---------- Stage 1: build ----------
FROM node:20-alpine AS build
WORKDIR /app

# Bake the backend URL into the SPA at build time.
# Overridden from docker-compose.yaml (frontend.build.args).
ARG VITE_API_URL=http://localhost:8080
ENV VITE_API_URL=$VITE_API_URL

COPY package.json package-lock.json* ./
RUN npm ci || npm install

COPY . .
RUN npm run build

# ---------- Stage 2: serve ----------
FROM nginx:alpine

# SPA routing fallback (no separate nginx.conf file needed)
RUN printf 'server {\n\
  listen 80;\n\
  root /usr/share/nginx/html;\n\
  index index.html;\n\
  location / {\n\
    try_files $uri $uri/ /index.html;\n\
  }\n\
}\n' > /etc/nginx/conf.d/default.conf

COPY --from=build /app/dist /usr/share/nginx/html

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
