FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

# Make the maven wrapper executable just in case
RUN chmod +x mvnw

# Build the app
RUN ./mvnw package -DskipTests

# Copy the jar
RUN cp target/*.jar app.jar

EXPOSE 8081

# Run the app
ENTRYPOINT ["java","-jar","app.jar"]