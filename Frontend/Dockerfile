# Use the official node image for building Angular project
FROM node:16 as build

WORKDIR /app

# Copy the Angular project files
COPY . .

# Install dependencies and build the project
RUN npm install
RUN npm run build --prod

# Serve the application with NGINX
FROM nginx:alpine
COPY --from=build /app/dist/your-angular-app /usr/share/nginx/html
EXPOSE 80
