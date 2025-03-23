FROM maven:latest AS builder
WORKDIR /app
COPY . /app
RUN mvn -f /app/wishlist/pom.xml clean package

FROM openjdk:21
WORKDIR /app
COPY ./opentelemetry-javaagent.jar /app/opentelemetry-javaagent.jar
COPY --from=builder /app/wishlist/target/*.jar /app/application.jar
EXPOSE 8181
ENTRYPOINT ["java", "-jar", "/app/application.jar"]