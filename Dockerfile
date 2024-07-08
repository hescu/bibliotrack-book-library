FROM gradle:8.5.0-jdk21 as builder

WORKDIR /home/gradle/src
COPY --chown=gradle:gradle . /home/gradle/src

RUN gradle build --no-daemon

FROM openjdk:21

WORKDIR /app
COPY --from=builder /home/gradle/src/build/libs/*.jar /app/app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar", "--spring.profiles.active=default"]