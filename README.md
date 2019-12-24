<p align="center"><img width=12.5% src="https://github.com/dle8/EZPing/blob/master/images/EZPing.png"></p>

# [EZPing](https://github.com/dle8/EZPing) &middot; [![GitHub license](https://img.shields.io/badge/license-MIT-blue.svg)](https://github.com/dle8/Kronos/blob/master/LICENSE)

A real-time, scalable chat app that utilizes distributed systems design, distributed NoSQL database and message broker to improve chatting experience (just like its name).

## 📚 Table of contents

- [Technical stack](#technical-stack)
  - [Frontend](#frontend)
  - [Backend](#backend)
- [Features](#features)
- [Installation](#installation)
- [Notes](#notes)
- [Todo](#todo)
- [How to contribute](#how-to-contribute)
- [Acknowledgements](#acknowledgements)
- [Author](#author)
- [License](#license)

## 🛠 Technical stack

### 📙 Frontend
- Programming language(s): [JavaScript](https://developer.mozilla.org/en-US/docs/Web/JavaScript)
- Framework(s): [React](https://reactjs.org/)

### ⚙ Backend
- Programming language(s): [Java](https://www.java.com/en/download/)
- App framework: [Spring](https://spring.io/)
- Database: [Cassandra](http://cassandra.apache.org/)
- Distributed in-memory database: [Redis](https://redis.io/)
- Query languange: [MySQL](https://www.mysql.com/)
- Message broker: [RabbitMQ](https://www.rabbitmq.com/)
- Distributed memory caching: [Memcached](https://memcached.org/)
- HTTP Load Balancing: [NGINX](https://www.nginx.com/)

## 🚀 Features
- Create a new account with admin role
- Join a chatroom as any role
- Send public messages to joined chat rooms
- Send private messages to other users
- See active users in any chat room
- Receive messages from other users when going from offline to online
- Internalization supported with 6 languages: English, Chinese, German, Spanish, Japanese, Portuguese.

## ⬇ Installation

- Download docker & docker-compose: [Docker](https://docs.docker.com/v17.12/install/#server).
- Install [maven](https://www.baeldung.com/install-maven-on-windows-linux-mac) for your OS. (Maven works best with [JDK 8](https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)).
- Start the containers from ```docker-compose.yml``` with: ```docker-compose up -d```. (Some docker-compose [commands](https://docs.docker.com/compose/gettingstarted/)).
- Execute the jar file: ```java -jar target/EZPing-0.1.0.jar```.

Now, the app is up at ```http://localhost:8080```.

## 📋 Notes

Remember to mark EZPing directory as Source Root.

## 📝 Todo

- Add more UI to the sites.
- Upload project design & explaination.
- Perhaps use NGINX with Google Cloud Compute Engine? Elaborate more in this [article](https://cloud.google.com/community/tutorials/https-load-balancing-nginx).

## 👏 How to contribute

## 🎉 Acknowledgements

## 👨‍💻 Author

- [Dung Tuan Le](https://github.com/dle8) <br/>
Computer Science major.  
University of Rochester '21.  

## 📄 License

EZPing is [MIT licensed](./LICENSE).

