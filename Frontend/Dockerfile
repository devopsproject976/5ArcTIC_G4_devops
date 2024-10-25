# Step 1: Build the app in image 'builder'
FROM node:16 AS builder

WORKDIR /usr/src/app

COPY package*.json ./
RUN npm install
COPY . .
RUN npm run build --prod

# Step 2: Serve application via NGINX
FROM nginx:alpine
COPY --from=builder /usr/src/app/dist/summer-workshop-angular /usr/share/nginx/html
EXPOSE 80
