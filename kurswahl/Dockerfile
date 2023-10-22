FROM maven:3.6.3-jdk-20

WORKDIR /app
COPY . .
RUN mvn clean install -DskipTests
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "target/kurswahl-0.0.1-SNAPSHOT.jar"]