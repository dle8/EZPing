FROM cassandra:3.0
FROM mysql:5.7
FROM redis:3.0.6
FROM byteflair/rabbitmq-stomp

MAINTAINER dle8@u.rochester.edu
FROM openjdk:8-jdk-alpine
RUN ls
CMD ["java","-jar","target/EZPing-0.1.0.jar"]
