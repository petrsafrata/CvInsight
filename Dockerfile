# =========================
# BUILD STAGE
# =========================
FROM maven:3.9-eclipse-temurin-17 AS build

WORKDIR /app

# Copy Maven configuration first for better layer caching
COPY pom.xml .

# Download dependencies
RUN mvn dependency:go-offline -B

# Copy application sources
COPY src ./src

# Build application
RUN mvn clean package -DskipTests -B


# =========================
# RUNTIME STAGE
# =========================
FROM eclipse-temurin:17-jre

LABEL author="petrsafrata"
LABEL org.opencontainers.image.title="CV Insight"
LABEL org.opencontainers.image.description="REST API for CV extraction, validation, AI parsing and analysis"
LABEL version="1.0.0"
LABEL org.opencontainers.image.source="https://github.com/petrsafrata/CvInsight"
LABEL org.opencontainers.image.licenses="Apache-2.0"

WORKDIR /app

# Create non-root application user
RUN groupadd --system cvinsight \
    && useradd --system --gid cvinsight cvinsight

# Copy application JAR
COPY --from=build /app/target/cv-insight-*.jar /app/app.jar

# Create application directories
RUN mkdir -p /app/logs \
    && chown -R cvinsight:cvinsight /app

USER cvinsight

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]