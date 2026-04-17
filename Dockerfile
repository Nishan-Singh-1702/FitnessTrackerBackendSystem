FROM eclipse-temurin:17-jre
WORKDIR /app
COPY target/fitness-monolith-0.0.1-SNAPSHOT.jar /app/fitness-monolith-0.0.1-SNAPSHOT.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "fitness-monolith-0.0.1-SNAPSHOT.jar"]