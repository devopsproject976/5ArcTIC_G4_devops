# Step 1: Build Angular application
FROM node:16 as build
WORKDIR /app
COPY package*.json ./
RUN ls -al /app  # List files in /app to confirm package.json is present
RUN npm install
COPY . .
RUN npm run build --prod

# Step 2: Serve application via NGINX
FROM nginx:alpine
COPY --from=build /app/dist/your-angular-project /usr/share/nginx/html
EXPOSE 80
