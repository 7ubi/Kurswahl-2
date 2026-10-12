# --- Build stage ---
FROM node:22-alpine AS builder
WORKDIR /app

# Install dependencies in their own layer so they are cached until the lockfile changes
COPY package.json package-lock.json ./
RUN npm ci --no-audit --no-fund

COPY . .
RUN npm run build

# --- Runtime stage ---
FROM nginx:alpine
COPY nginx.conf /etc/nginx/conf.d/default.conf
COPY --from=builder /app/dist/kurswahl-ui/ /usr/share/nginx/html/
