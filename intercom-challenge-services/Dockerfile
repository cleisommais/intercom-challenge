# The builder from java image
FROM maven:latest as builder
# Install project dependencies and keep sources
# make source folder
RUN mkdir -p /usr/src/app
WORKDIR /usr/src/app
# install maven dependency packages (keep in image)
COPY pom.xml /usr/src/app
# copy other source files (keep in image)
COPY src /usr/src/app/src
RUN mvn package
###################################################

FROM java:latest
WORKDIR /usr/src/app
COPY --from=builder /usr/src/app/target/intercom-challenge-services-1.0.jar /usr/src/app
EXPOSE 8080
CMD ["java", "-jar", "intercom-challenge-services-1.0.jar"]
