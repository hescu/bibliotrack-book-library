FROM gradle:8.5.0-jdk21 as builder

COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src

ENV GOOGLE_BOOKS_API_KEY=${GOOGLE_BOOKS_API_KEY}
ENV DB_ROOT_PW=${DB_ROOT_PW}

RUN gradle build --no-daemon

FROM openjdk:21

COPY --from=builder /home/gradle/src/build/libs/*.jar /app/app.jar

WORKDIR /app

CMD ["java", "-jar", "app.jar", "--spring.profiles.active=default"]