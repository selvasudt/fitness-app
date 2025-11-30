# ---------- Build stage ----------
# Use the official Maven image that includes a JDK 25 (fast, cached Maven + JDK)
# (choose the tag that matches your org's preference: eclipse-temurin, amazoncorretto, etc.)
FROM maven:3.9-eclipse-temurin-25 AS builder

WORKDIR /workspace

# Copy only Maven metadata first to leverage Docker layer cache
COPY pom.xml mvnw* ./
COPY .mvn .mvn
# If you use a settings.xml add it here (optional)
# COPY .github/maven-settings.xml /root/.m2/settings.xml

# Download dependencies (no project sources yet) — speeds rebuilds
RUN --mount=type=cache,target=/root/.m2 mvn -B -e -q dependency:go-offline

# Copy full source
COPY src ./src
COPY pom.xml ./

# Build the application (skip tests in build image or run them depending on policy)
# Produces target/*.jar (adjust -DskipTests as per your CI policy)
RUN --mount=type=cache,target=/root/.m2 mvn -B -DskipTests package

# ---------- Runtime stage (small) ----------
# Option A: Use a small Temurin JRE runtime (standard)
FROM eclipse-temurin:25-jre AS runtime

# Create a non-root user for security
RUN addgroup --system appgroup && adduser --system --ingroup appgroup appuser

WORKDIR /app

# Copy jar built in builder stage (assumes a single jar in target/)
COPY --from=builder /workspace/target/*.jar app.jar

# Set sensible JVM defaults for container (tune per service)
ENV JAVA_TOOL_OPTIONS="-XX:MaxRAMPercentage=75.0 -XX:+UseContainerSupport"

# Allow docker healthcheck to call an internal endpoint (example)
HEALTHCHECK --interval=30s --timeout=5s --start-period=10s \
  CMD curl -f http://localhost:8082/actuator/health || exit 1

# Run as non-root
USER appuser

EXPOSE 8082

ENTRYPOINT ["sh", "-c", "exec java $JAVA_TOOL_OPTIONS -jar /app/app.jar"]
