# build stage
FROM node:20-alpine AS build
LABEL authors="Petr"
WORKDIR /app
COPY package*.json ./
RUN npm ci
COPY . .
RUN npm run build

# runtime stage - Apache
FROM httpd:2.4-alpine
COPY --from=build /app/dist/ /usr/local/apache2/htdocs/
COPY apache/httpd.conf /usr/local/apache2/conf/httpd.conf
EXPOSE 80