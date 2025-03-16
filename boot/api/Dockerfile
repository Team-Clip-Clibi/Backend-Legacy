FROM amazoncorretto:21-alpine-jdk
ARG JAR_FILE=boot/api/build/libs/api.jar
COPY ${JAR_FILE} app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-Duser.timezone=Asia/Seoul", "-jar", "/app.jar"]