FROM openjdk:17-slim AS builder

WORKDIR /app

COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .
COPY src src
ENV JAVA_OPTS="-Xms256m -Xmx512m"

RUN chmod +x ./gradlew

RUN ./gradlew build -x test

FROM openjdk:17-jdk-slim

WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
