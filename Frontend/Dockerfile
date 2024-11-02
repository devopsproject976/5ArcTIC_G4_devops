# Stage 1: Build the Angular app
FROM node:18 AS builder

# Set the working directory inside the container
WORKDIR /app

# Copy package.json and package-lock.json (if present) to install dependencies
COPY package*.json ./

# Install dependencies
RUN npm install

# Copy the rest of the code to the working directory
COPY . .

# Build the Angular project in production mode
RUN npm run build -- --configuration production

# Stage 2: Serve the app with NGINX
FROM nginx:alpine

# Copy the built Angular files to NGINX's default HTML folder
COPY --from=builder /app/dist/frontend /usr/share/nginx/html

# Expose port 80 to the outside world
EXPOSE 80

# Start NGINX
CMD ["nginx", "-g", "daemon off;"]
