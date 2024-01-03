FROM openjdk:17
WORKDIR /app
ARG JAR_FILE=build/libs/*.jar
COPY $JAR_FILE /cross-puzzle.jar
CMD ["java", "-jar", "/cross-puzzle.jar"]