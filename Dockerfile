FROM openjdk:17
WORKDIR /app
ARG JAR_FILE=build/libs/*.jar
COPY $JAR_FILE /cross-puzzle.jar
CMD ["java", "-jar","-Dspring.profiles.active=local", "/cross-puzzle.jar"]