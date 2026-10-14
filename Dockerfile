FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY target/zugferd-api-*.jar app.jar

# Non-root user
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
