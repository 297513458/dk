# dockerfile 基础配置
FROM openjdk:8-jre
MAINTAINER daxingxing
VOLUME /tmp
ARG JAR_FILE
ADD ${JAR_FILE} /app/app.jar
WORKDIR /app/
EXPOSE 8080
ENTRYPOINT ["/usr/bin/java","-jar","./app.jar"]