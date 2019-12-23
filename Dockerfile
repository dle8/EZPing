# docker build -t springbootapp .
# docker run -t springbootapp
FROM openjdk:8-jdk-alpine
MAINTAINER dle8@u.rochester.edu
VOLUME /tmp
RUN apk add docker
RUN apk add py-pip
RUN apk add python-dev libffi-dev openssl-dev gcc libc-dev make
CMD ["docker-compose", "up", "and", "java","-jar","target/EZPing-0.1.0.jar"]
EXPOSE 8080:8080