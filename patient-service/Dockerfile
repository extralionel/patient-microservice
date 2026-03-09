FROM maven:3.9.9-eclipse-temurin-21 AS builder

WORKDIR /app

COPY pom.xml .

RUN mvn dependency:go-offline -B

COPY src ./src

RUN mvn clean package

# Changed from openjdk to eclipse-temurin JRE
FROM eclipse-temurin:21-jre AS runner

WORKDIR /app

# Fixed the path from ./app/target to /app/target
COPY --from=builder /app/target/patient-service-0.0.1-SNAPSHOT.jar ./app.jar

EXPOSE 4000

ENTRYPOINT ["java", "-jar", "app.jar"]