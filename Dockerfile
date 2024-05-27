FROM adoptopenjdk/openjdk8:alpine-jre
COPY /target/demo-0.0.1-SNAPSHOT.jar /target/demo-0.0.1-SNAPSHOT.jar
WORKDIR /target
ENTRYPOINT ["java","-jar", "demo-0.0.1-SNAPSHOT.jar"]