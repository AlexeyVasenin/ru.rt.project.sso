FROM adoptopenjdk:11-jre-hotspot

RUN mkdir /usr/share/app

WORKDIR /usr/share/app

COPY build/libs/eureka-server-0.0.1-SNAPSHOT.jar /usr/share/app/app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
