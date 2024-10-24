# Étape 1 : Construction de l'application Angular
FROM node:16 as build
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
RUN npm run build --prod

# Étape 2 : Servir l'application via NGINX
FROM nginx:alpine
COPY --from=build /app/dist/mon-projet-angular /usr/share/nginx/html
EXPOSE 80
