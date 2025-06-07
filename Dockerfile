FROM openjdk:20-jdk

WORKDIR /app

EXPOSE 3000

ARG jarfile=target/rate-limiter-0.0.1-SNAPSHOT.jar

COPY ${jarfile} rate-limit.jar

ENTRYPOINT ["java", "-jar", "/app/rate-limit.jar"]