# Build (tag it properly)
docker build -t fitness-app/userservice:1.0 .

# Run
docker run -d --name userservice \
-p 8080:8080 \
--restart unless-stopped \
-e SPRING_PROFILES_ACTIVE=prod \
-e JAVA_TOOL_OPTIONS="-XX:MaxRAMPercentage=75.0" \
fitness-app/userservice:1.0
