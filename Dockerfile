FROM adoptopenjdk/openjdk11:alpine as build
COPY target/spring-boot-ews-mail-exchange-server-configuration-0.0.1.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]