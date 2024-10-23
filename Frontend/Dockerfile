# Use the official node image for building Angular project
FROM node:20 as build

WORKDIR /app

COPY frontend/package*.json ./

# Install dependencies
RUN npm install

# Copy the rest of the project files
COPY . .

# Build the Angular project
RUN npm run build --prod

# Serve the application with NGINX
FROM nginx:alpine
COPY --from=build /app/dist/summer-workshop-angular /usr/share/nginx/html
EXPOSE 80
