FROM amd64/openjdk:14

COPY ./build/libs/*.jar /app.jar

EXPOSE 8080
EXPOSE 8083

ENV SPRING_PROFILES_ACTIVE=dev

ENTRYPOINT ["java", "-jar", "app.jar"]