# --- Giai đoạn 1: Build ứng dụng (Sử dụng Maven và JDK 17) ---
FROM eclipse-temurin:17-jdk-alpine AS build

# Thiết lập thư mục làm việc
WORKDIR /app

# Copy các file cấu hình Maven trước để tận dụng cache (giúp build nhanh hơn ở các lần sau)
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

# Cấp quyền thực thi cho file mvnw (tránh lỗi Permission denied trên Linux)
RUN chmod +x mvnw

# Tải các thư viện về (Dependency)
# Lệnh này giúp Docker cache lại các thư viện, lần sau build sẽ không phải tải lại
RUN ./mvnw dependency:go-offline

# Copy toàn bộ mã nguồn vào
COPY src ./src

# Tiến hành Build ra file .jar (Bỏ qua test để tiết kiệm thời gian deploy)
RUN ./mvnw clean package -DskipTests

# --- Giai đoạn 2: Chạy ứng dụng (Sử dụng JRE 17 cho nhẹ) ---
FROM eclipse-temurin:17-jre-alpine

# Thiết lập thư mục làm việc
WORKDIR /app

# Copy file .jar đã build từ giai đoạn 1 sang giai đoạn này
COPY --from=build /app/target/*.jar app.jar

# (Quan trọng) Copy file cấu hình Firebase nếu nó nằm trong resources
# Lưu ý: Code của bạn đang đọc file từ Classpath, nên khi build ở giai đoạn 1
# file này đã nằm trong app.jar rồi. Dòng dưới đây chỉ cần thiết nếu bạn để file json ở ngoài jar.
# COPY --from=build /app/src/main/resources/firebase-adminsdk.json /app/firebase-adminsdk.json

# Expose cổng 8080 (Mặc định của Spring Boot)
EXPOSE 8080

# Lệnh chạy ứng dụng
ENTRYPOINT ["java", "-jar", "app.jar"]