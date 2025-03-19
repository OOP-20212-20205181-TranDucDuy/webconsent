# Sử dụng Node.js 20 để build React app
FROM node:20-alpine AS build-stage

WORKDIR /app

# Copy package.json và package-lock.json trước
COPY package.json package-lock.json ./

# Cài đặt dependencies
RUN npm install --frozen-lockfile

# Copy toàn bộ source code vào container
COPY . .

# Kiểm tra thư mục `node_modules` và các file quan trọng trước khi build
RUN ls -l /app && ls -l /app/node_modules

# Build ứng dụng React (Vite output vào `dist/`)
RUN npm run build && ls -l /app/dist

# Dùng Nginx để chạy ứng dụng React đã build
FROM nginx:alpine AS production-stage

# Đảm bảo thư mục tồn tại
RUN mkdir -p /usr/share/nginx/html

# Copy thư mục build từ build-stage sang thư mục Nginx (Đổi `build/` thành `dist/`)
COPY --from=build-stage /app/dist /usr/share/nginx/html

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
