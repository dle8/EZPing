FROM cassandra:3.0
FROM mysql:5.7
FROM redis:3.0.6
FROM byteflair/rabbitmq-stomp

MAINTAINER dle8@u.rochester.edu
VOLUME /tmp
#RUN apk add docker
#RUN docker
#RUN apk add py-pip
#RUN apk add python-dev libffi-dev openssl-dev gcc libc-dev make
#RUN pip install docker-compose
#RUN apk update && apk add curl curl-dev
#RUN curl -L "https://github.com/docker/compose/releases/download/1.25.0/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
#RUN chmod +x /usr/local/bin/docker-compose
#COPY --from=docker/compose:1.25.0-alpine /usr/local/bin/docker-compose /usr/local/bin/
FROM openjdk:8-jdk-alpine
CMD ["java","-jar","target/EZPing-0.1.0.jar"]
#RUN docker-compose up
#CMD java -jar target/EZPing-0.1.0.jar