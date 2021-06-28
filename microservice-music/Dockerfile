FROM adoptopenjdk:11-jre-hotspot

RUN mkdir /usr/share/app

WORKDIR /usr/share/app

COPY build/libs/microservice-music-0.0.1-SNAPSHOT.jar /usr/share/app/app.jar

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]
