# Stage 1: Build ứng dụng Spring Boot với Maven
FROM eclipse-temurin:17-jdk-alpine AS builder

# Cài đặt Maven
RUN apk add --no-cache maven

# Thiết lập thư mục làm việc
WORKDIR /app

# Copy file cấu hình Maven và mã nguồn
COPY pom.xml ./ 
COPY src ./src

# Build ứng dụng Spring Boot (tạo file JAR)
RUN mvn clean package -DskipTests

# Stage 2: Tạo image cuối cùng, sao chép file JAR đã build từ Stage 1
FROM eclipse-temurin:17-jdk-alpine

# Thiết lập thư mục làm việc trong container
WORKDIR /app

# Copy file JAR từ Stage 1 vào container
COPY --from=builder /app/target/*.jar app.jar

# Expose cổng 8080
EXPOSE 8080

# Lệnh khởi chạy ứng dụng
CMD ["java", "-jar", "app.jar"]
