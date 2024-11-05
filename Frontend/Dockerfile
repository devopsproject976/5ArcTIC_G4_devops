# Stage 1: Build the Angular application
FROM node:18 AS build

WORKDIR /app

# Copy package.json and package-lock.json to install dependencies
COPY package*.json ./

# Install dependencies
RUN npm install

# Copy the rest of the Angular project files
COPY . .

# Build the Angular project
RUN npm run build -- --configuration production

# Stage 2: Serve the application with NGINX
FROM nginx:alpine

# Copy built Angular files to NGINX's default HTML folder
COPY --from=build /app/dist/summer-workshop-angular /usr/share/nginx/html

# Expose port 80 to the outside world
EXPOSE 80
