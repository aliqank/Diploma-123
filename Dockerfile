FROM openjdk:17-jdk
ENV APP_HOME=/usr/app/
WORKDIR $APP_HOME
COPY ./build/libs/* ./app.jar
EXPOSE 8080
CMD ["java","-jar","app.jar"]