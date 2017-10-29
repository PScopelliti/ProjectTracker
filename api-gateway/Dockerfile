FROM anapsix/alpine-java

ARG ENVIRONMENT
ARG APP_NAME
ARG APP_VERSION

ADD target/${APP_NAME}_${APP_VERSION}.jar app.jar

ENV APP_NAME=${APP_NAME}
ENV APP_VERSION=${APP_VERSION}

EXPOSE 8081 9990
ENTRYPOINT ["sh", "-c", "java -Djava.security.egd=file:/dev/./urandom -Dconfig.resource=application.${ENVIRONMENT}.conf -jar /app.jar"]
