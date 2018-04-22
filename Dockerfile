FROM openjdk:8-jdk
RUN apt-get update && apt-get install -y --no-install-recommends openjfx && rm -rf /var/lib/apt/lists/*

ADD build/libs/client-1.0-SNAPSHOT-standalone.jar app.jar

ENTRYPOINT ["java","-jar","/app.jar"]