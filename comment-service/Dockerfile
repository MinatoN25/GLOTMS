FROM openjdk:11
ADD ./target/comment-service-0.0.1-SNAPSHOT.jar /usr/src/comment-service-0.0.1-SNAPSHOT.jar
WORKDIR usr/src
ENTRYPOINT ["java","-jar", "comment-service-0.0.1-SNAPSHOT.jar"]
