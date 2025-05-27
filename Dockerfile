FROM openjdk:21
WORKDIR /backend
COPY target/methods-0.1.jar backend.jar
CMD ["java", "-jar", "backend.jar"]