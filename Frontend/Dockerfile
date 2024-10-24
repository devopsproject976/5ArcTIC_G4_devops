# Use the official Node image for building the Angular project
FROM node:16 AS build

# Set the working directory
WORKDIR /app

# Copy package.json and package-lock.json (if available) first to leverage Docker caching
COPY package*.json ./

# Install Node.js dependencies
RUN npm install

# Copy the rest of the application files
COPY . .

# Install dependencies and build the Angular project for production
RUN npm run build --prod

# Use the official NGINX image to serve the application
FROM nginx:alpine

# Copy the built Angular app from the previous stage
COPY --from=build /app/dist/summer-workshop-angular /usr/share/nginx/html

# Expose port 80 to allow access to the application
EXPOSE 80

# Start NGINX server
CMD ["nginx", "-g", "daemon off;"]
