# Stage 1: Build với Maven
FROM maven:3.8-openjdk-8 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package

# Stage 2: Chạy ứng dụng với Tomcat
FROM tomcat:9.0-jdk8
COPY --from=build /app/target/webjava-1.0-SNAPSHOT.war /usr/local/tomcat/webapps/ROOT.war

# Cấu hình timezone
ENV TZ=Asia/Ho_Chi_Minh
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# Port mặc định của Tomcat
EXPOSE 8080

# Khởi động Tomcat
CMD ["catalina.sh", "run"] 