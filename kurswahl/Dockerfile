FROM jelastic/maven:3.8.6-openjdk-20.ea-b24

WORKDIR /app
COPY . .
RUN mvn clean install -DskipTests
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "target/kurswahl-2.0.0-SNAPSHOT.jar"]