FROM openjdk:11
EXPOSE 9090
ADD build/libs/feed-app.jar feed-app
ENTRYPOINT ["java","-jar","/feed-app"]