FROM gradle:8.7-jdk21 AS build
WORKDIR /workspace
COPY . .
RUN gradle bootJar -x test

FROM eclipse-temurin:21-jre-alpine AS final
RUN apk add --no-cache ca-certificates
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
WORKDIR /rl-fin-flow-backend
COPY --from=build /workspace/build/libs/*.jar app.jar

RUN chown appuser:appgroup app.jar
USER appuser
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]