FROM openjdk:11-jdk
COPY build/libs/*SNAPSHOT.jar app.jar

EXPOSE 8081
ENTRYPOINT ["java", "${JAVA_OPTS} -jar", "/app.jar"]
