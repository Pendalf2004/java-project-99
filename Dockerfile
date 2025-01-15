FROM eclipse-temurin:21-jdk

ARG GRADLE_VERSION=8.5


RUN gradle installDist

CMD build/install/app/bin/app