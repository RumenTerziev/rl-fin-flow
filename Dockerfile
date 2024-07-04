FROM gradle:8.7-jdk21 AS build
WORKDIR /workspace
COPY . .
RUN gradle bootJar

FROM openjdk:21
WORKDIR /rl-fin-flow-backend
COPY --from=build /workspace/build/libs/*.jar app.jar
CMD ["java", "-jar", "app.jar"]