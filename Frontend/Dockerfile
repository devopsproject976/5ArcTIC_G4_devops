# Stage 1: Build the Angular app
FROM node:20-alpine AS build

# Set the working directory
WORKDIR /app

# Copy package.json and package-lock.json
COPY package*.json ./

# Install dependencies
RUN npm install

# Copy the rest of the Angular app code
COPY . .

# Build the Angular app for production
RUN npm run build --prod

# Stage 2: Serve the Angular app using Nginx
FROM nginx:alpine

# Copy the built Angular app from the previous stage to the Nginx html folder
COPY --from=build /app/dist/summer-workshop-angular /usr/share/nginx/html

# Expose port 80 for the web app
EXPOSE 80

# Start Nginx server
CMD ["nginx", "-g", "daemon off;"]

