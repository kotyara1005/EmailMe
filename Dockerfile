FROM gradle  AS build

WORKDIR /app

COPY build.gradle ./build.gradle
COPY settings.gradle ./settings.gradle

COPY src ./src

RUN gradle build --no-daemon

FROM openjdk:18-alpine

COPY --from=build /app/build/distributions/EmailMe-1.0-SNAPSHOT.tar .
RUN ["tar", "-xf", "EmailMe-1.0-SNAPSHOT.tar"]

CMD ["sh", "EmailMe-1.0-SNAPSHOT/bin/EmailMe"]

