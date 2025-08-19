FROM amazoncorretto:21-alpine-jdk
ARG JAR_FILE=boot/api/build/libs/api.jar
COPY ${JAR_FILE} app.jar

EXPOSE 8080

ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -Duser.timezone=Asia/Seoul -jar /app/app.jar"]
