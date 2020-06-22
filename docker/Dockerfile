FROM amd64/openjdk:14
COPY ../build/libs/blockchainsimulator-0.0.1.jar /usr/app/
WORKDIR /usr/app
EXPOSE 8083

ENV SPRING_PROFILE=dev

ENTRYPOINT ["java", "-jar", "blockchainsimulator-0.0.1.jar", "-Dspring.profiles.active=dev"]
