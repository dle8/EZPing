# docker build -t springbootapp .
# docker run -t springbootapp
FROM openjdk:8-jdk-alpine
MAINTAINER dle8@u.rochester.edu
VOLUME /tmp
#ENTRYPOINT ["java","-jar","target/EZPing-0.1.0.jar"]
ENTRYPOINT ["ls"]
EXPOSE 8080:8080