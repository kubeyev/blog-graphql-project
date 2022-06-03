FROM openjdk:17
ADD target/project-0.0.1-SNAPSHOT.jar project.jar
EXPOSE 8000
ENTRYPOINT ["java", "-jar", "project.jar"]