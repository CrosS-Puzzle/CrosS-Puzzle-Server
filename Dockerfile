FROM openjdk:17
WORKDIR /app
ARG JAR_FILE=build/libs/CrosS-Puzzle-Server-0.0.1-SNAPSHOT.jar
COPY $JAR_FILE /CrosS-Puzzle-Server.jar
CMD ["java", "-jar", "/CrosS-Puzzle-Server.jar"]