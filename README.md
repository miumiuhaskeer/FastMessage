# FastMessage
FastMessage API provides an HTTP API for messenger applications (mobile app or web). </br>
You don't need a JWT to create an account or authenticate. But if you want to create chat or send message, you need JWT and Refresh token. </br>
Note: project under development!

## TODO
- [x] Registration/authentication flow
- [x] Creating chats
- [x] Sending messages
- [x] One chat update
- [x] Full chats updates
- [x] Search messages
- [x] Get user statistic
- [ ] Delete message
- [ ] Reply for message
- [ ] Move registration/authentication flow to new microservice

## Services architecture
![Architecture](https://github.com/miumiuhaskeer/FastMessage/blob/master/.github/images/Diagram.jpg)

## Technologies used
- Spring Boot
- Liquibase
- Docker - used bridge network driver
- PostgreSQL - needed to store information about user (email, encrypted password, refresh token)
- MongoDB - needed to store information about chats and messages
- Apache Kafka - send statistic information to [FastMessage-statistic](https://github.com/miumiuhaskeer/FastMessage-statistic) service

## Health checking
You can check service availability by sending request
```
http://fast-message-statistic:3173/fm/api/v1/health
```
The response is
```
OK
```
To check [FastMessage-statistic](https://github.com/miumiuhaskeer/FastMessage-statistic) service availability send this request
```
http://fast-message-statistic:3173/fm/api/v1/health/fms
```
The response is
```
OK
```

## See project on DockerHub
Follow the [link](https://hub.docker.com/repository/docker/heybitbro/fast-message) to see FastMessage project
