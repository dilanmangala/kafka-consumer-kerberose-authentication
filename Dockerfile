FROM openjdk:8
EXPOSE 8081
ADD ./target/kafka-consumer-0.0.1.jar ./kafka-consumer-0.0.1.jar
ENTRYPOINT ["java","-jar","-Duser.timezone=GMT+0530","kafka-consumer-0.0.1.jar"]	