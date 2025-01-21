FROM --platform=linux/x86_64 eclipse-temurin:21-jre-alpine

RUN apk --no-cache upgrade && apk --no-cache add curl ca-certificates

USER 65534
EXPOSE 8080

COPY build/libs/*.jar app.jar

ENTRYPOINT ["java","-Dspring.profiles.active=${ENV}","-jar", "/app.jar"]
