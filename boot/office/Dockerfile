FROM amazoncorretto:17-alpine-jdk
ARG JAR_FILE=boot/office/build/libs/office.jar
COPY ${JAR_FILE} app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","/app.jar"]