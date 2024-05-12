FROM gradle:8.7-jdk21
ADD . rl-fin-flow
WORKDIR rl-fin-flow
COPY build/libs/rl-fin-flow-1.0.0-SNAPSHOT.jar rl-fin-flow.jar
ENTRYPOINT ["java", "-jar", "rl-fin-flow.jar"]