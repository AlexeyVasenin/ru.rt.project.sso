#FROM adoptopenjdk:11-jre-hotspot
#RUN mkdir /usr/share/app
#WORKDIR /usr/share/app
#COPY build/libs/eureka-server-0.0.1-SNAPSHOT.jar /usr/share/app/app.jar
#ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]

FROM bellsoft/liberica-openjdk-alpine:11.0.11

ARG JAR_FILE=build/libs/eureka-server-0.0.1-SNAPSHOT.jar

COPY ${JAR_FILE} app.jar

EXPOSE 8761

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","-Xms256m","-Xmx512m","/app.jar"]

