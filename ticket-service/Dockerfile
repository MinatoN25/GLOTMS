FROM openjdk:11
ADD ./target/ticket-service-0.0.1-SNAPSHOT.jar /usr/src/ticket-service-0.0.1-SNAPSHOT.jar
WORKDIR usr/src
ENTRYPOINT ["java","-jar", "ticket-service-0.0.1-SNAPSHOT.jar"]
