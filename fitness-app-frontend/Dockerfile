# build stage
FROM node:18-alpine AS builder
WORKDIR /app

# Install dependencies (use package-lock / pnpm-lock if present)
COPY package*.json ./

RUN npm ci --production=false

# Copy source and build
COPY . .
RUN npm run build

# production stage: nginx to serve static files
FROM nginx:stable-alpine AS runner
# Remove default nginx conten
RUN rm -rf /usr/share/nginx/html/*

# Copy built files from builder
COPY --from=builder /app/dist /usr/share/nginx/html

# Copy custom nginx conf to handle SPA routing (below)
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Expose the port nginx listens on
EXPOSE 80

# Run nginx in foreground
CMD ["nginx", "-g", "daemon off;"]
