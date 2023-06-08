FROM maven:3.8.5-openjdk-18 AS MAVEN_BUILD

MAINTAINER Stan Mpakati

ENV SPRING_PROFILES_ACTIVE="staging"
COPY pom.xml /build/
COPY src /build/src/

WORKDIR /build/
RUN mvn generate-sources
RUN mvn package


FROM maven:3.8.5-openjdk-18

WORKDIR /app
ENV JAVA_TOOL_OPTIONS -agentlib:jdwp=transport=dt_socket,address=58081,server=y,suspend=n
COPY --from=MAVEN_BUILD /build/target/core-0.0.1-SNAPSHOT.jar /app/
ENTRYPOINT ["java", "-jar", "core-0.0.1-SNAPSHOT.jar"]