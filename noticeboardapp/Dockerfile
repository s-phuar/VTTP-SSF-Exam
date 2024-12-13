FROM openjdk:23-jdk-oracle AS builder

WORKDIR /compileddir

COPY mvnw .
COPY pom.xml .
COPY .mvn .mvn
COPY src src

RUN chmod a+x ./mvnw && ./mvnw package -Dmaven.test.skip=true

ENV PORT=8080
EXPOSE ${PORT}

FROM openjdk:23-jdk-oracle

ARG WORK_DIR=/app
WORKDIR ${WORK_DIR}


COPY --from=builder /compileddir/target/noticeboard-0.0.1-SNAPSHOT.jar VTTPSSFExam.jar


ENV PORT=8080
EXPOSE ${PORT}


ENV NOTICEBOARD_DB_PUBLISHINGHOST=
ENV NOTICEBOARD_DB_HOST=
ENV NOTICEBOARD_DB_PORT=6379
ENV NOTICEBOARD_DB_USERNAME=
ENV NOTICEBOARD_DB_PASSWORD=

ENTRYPOINT SERVER_PORT=${PORT} java -jar VTTPSSFExam.jar

HEALTHCHECK --interval=60s --timeout=5s --start-period=120s --retries=3 CMD curl -s -f http://localhost:${PORT}/status || exit 1
