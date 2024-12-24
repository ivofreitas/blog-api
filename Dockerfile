FROM eclipse-temurin:23-jdk-alpine as builder

WORKDIR /app

RUN apk add --no-cache maven

COPY . .

RUN mvn clean package -DskipTests

FROM eclipse-temurin:23-jre-alpine

WORKDIR /app

COPY --from=builder /app/target/blog-api-*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
