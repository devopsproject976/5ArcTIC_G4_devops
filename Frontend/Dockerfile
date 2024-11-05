# Use the official Node.js image as the base image
FROM node:18 AS builder

# Set the working directory inside the container
WORKDIR /app

# Copy package.json and package-lock.json
COPY package*.json ./

# Install all dependencies, including dev dependencies
RUN npm install

# Copy the rest of the application files
COPY . .

# Build the Angular application for production
RUN npm install -g @angular/cli && ng build --prod

# Use a lightweight server to serve the application
FROM node:18 AS production

# Set the working directory in the production stage
WORKDIR /app

# Copy the built Angular files from the builder stage
COPY --from=builder /app/dist/summer-workshop-angular ./dist/summer-workshop-angular

# Install http-server to serve static files
RUN npm install -g http-server

# Expose the port the app runs on
EXPOSE 4200

# Start the server
CMD ["http-server", "dist/summer-workshop-angular", "-p", "4200"]
