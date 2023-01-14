FROM adoptopenjdk:11-jre-openj9

VOLUME /tmp
EXPOSE 8080

COPY ./user-services/target/user-services-1.0.0-SNAPSHOT.jar ./

ENTRYPOINT ["java","-jar","./user-services-1.0.0-SNAPSHOT.jar"]