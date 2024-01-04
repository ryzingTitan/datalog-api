FROM gradle:8.6-jdk21-alpine as build

# build project within temporary Docker image
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle bootJar -info -x addKtlintFormatGitPreCommitHook

# build final Docker image using output from build image above
FROM eclipse-temurin:21-jre-alpine
EXPOSE 8080
COPY --from=build /home/gradle/src/build/libs/datalog-api-3.0.0.jar /app/datalog-api-3.0.0.jar
WORKDIR /app

CMD ["java", "-jar", "datalog-api-3.0.0.jar"]