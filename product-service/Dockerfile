FROM eclipse-temurin:17-jdk-jammy

ARG JAR_FILE=build/libs/*.jar

# Create a non-root user to run the application
RUN adduser --system --group app

COPY ${JAR_FILE} app.jar

# Switch to the non-root user
USER app

ENTRYPOINT ["java", "-jar", "/app.jar"]