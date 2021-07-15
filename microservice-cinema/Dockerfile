FROM bellsoft/liberica-openjdk-alpine:11.0.11

ARG JAR_FILE=build/libs/microservice-cinema-0.0.1-SNAPSHOT.jar

COPY ${JAR_FILE} app.jar

EXPOSE 8081

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","-Xms256m","-Xmx512m","/app.jar"]
