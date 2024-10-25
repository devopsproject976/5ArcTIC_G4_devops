# Step 1: Build the Angular frontend app in the 'builder' image
FROM node:16 AS builder

# Set working directory to the Frontend directory
WORKDIR /usr/src/app



# Install dependencies
RUN yarn install

# Copy all other Frontend files and build the application
COPY Frontend/ .
RUN yarn build

# Step 2: Use NGINX to serve the built files
FROM nginx:stable-alpine
LABEL version="1.0"

# Copy custom NGINX configuration if necessary
COPY Frontend/nginx.conf /etc/nginx/nginx.conf

# Set the working directory for NGINX
WORKDIR /usr/share/nginx/html

# Copy the Angular build output from the builder stage
COPY --from=builder /usr/src/app/dist/summer-workshop-angular/ .

# Expose the default NGINX port
EXPOSE 80

# Start NGINX server
CMD ["nginx", "-g", "daemon off;"]
