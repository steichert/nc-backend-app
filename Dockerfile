FROM gradle:jdk21 AS build
WORKDIR /workspace/app

COPY . /workspace/app

RUN mkdir -p build/libs/layer-information

WORKDIR /workspace/app/build/libs
RUN jar -xf ./*-SNAPSHOT.jar && \
    java -Djarmode=layertools -jar ./*-SNAPSHOT.jar extract --destination ../layer-information

FROM pnavato/amazoncorretto-jre:21-alpine
VOLUME /tmp

WORKDIR /app
ARG DEPENDENCY=/workspace/app/build/layer-information
COPY --from=build ${DEPENDENCY}/dependencies ./
COPY --from=build ${DEPENDENCY}/spring-boot-loader ./
COPY --from=build ${DEPENDENCY}/snapshot-dependencies ./
COPY --from=build ${DEPENDENCY}/application ./

EXPOSE 8080

ENV SPRING_PROFILES_ACTIVE=#{KOTLIN_ENVIRONMENTS}

ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]