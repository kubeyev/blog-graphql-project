FROM openjdk:17
ADD target/project-0.0.1-SNAPSHOT.jar graphql-blog-project.jar
EXPOSE 8000
ENTRYPOINT ["java", "-jar", "graphql-blog-project.jar"]