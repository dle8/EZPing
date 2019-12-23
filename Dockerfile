FROM cassandra:3.0
FROM mysql:5.7
FROM redis:3.0.6
FROM byteflair/rabbitmq-stomp
FROM docker

MAINTAINER dle8@u.rochester.edu

RUN docker run -d --name cassandra -p 9042:9042 cassandra:3.0
RUN docker run -d --name redis -p 6379:6379 redis:3.0.6
RUN docker run -d --name mysql -e MYSQL_DATABASE=EZPing -e MYSQL_ROOT_PASSWORD=root -p 3306:3306 mysql:5.7
RUN docker run -d --name rabbitmq-stomp -p 5672:5672 -p 15672:15672 -p 61613:61613 byteflair/rabbitmq-stomp


FROM openjdk:8-jdk-alpine
RUN ls
CMD ["java","-jar","target/EZPing-0.1.0.jar"]
