# Use official base image of Java Runtim
FROM openjdk:8-jdk-alpine

# Set volume point to /tmp
VOLUME /opt

# Make port 8080 available to the world outside container
EXPOSE 8080

# Set application's JAR file
ARG JAR_FILE=target/project-0.0.1-SNAPSHOT.jar

# Add the application's JAR file to the container
ADD ${JAR_FILE} hm-api.jar

# Run the JAR file
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/hm-api.jar"]