FROM gradle:8.7-jdk21 AS build
WORKDIR /workspace
COPY . .
RUN gradle bootJar

FROM eclipse-temurin:21-jre
WORKDIR /rl-fin-flow-backend
COPY --from=build /workspace/build/libs/*.jar app.jar

EXPOSE 8081

CMD ["java", "-jar", "app.jar"]