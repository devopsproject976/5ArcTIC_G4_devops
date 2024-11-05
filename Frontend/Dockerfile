# Stage 1: Build the Angular application
FROM node:16 AS build

WORKDIR /app

# Copy the Angular project files
COPY . .

# Install dependencies and build the project
RUN npm install
RUN npm run build --prod

# Stage 2: Serve the application with NGINX
FROM nginx:alpine

# Copy built Angular files to NGINX's default HTML folder
COPY --from=build /app/dist/summer-workshop-angular /usr/share/nginx/html

# Expose port 80 to the outside world
EXPOSE 80
