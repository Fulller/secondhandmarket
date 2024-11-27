### Backend (chocu)

-- JAVA version 17

#### Dependencies:

1. Spring Boot Starter Web: Xây dựng ứng dụng web với RESTful APIs.
2. Spring Boot Starter Data JPA: Thao tác với cơ sở dữ liệu qua JPA.
3. MySQL Connector: Kết nối với MySQL database.
4. Spring Boot Starter Security: Bảo mật ứng dụng (Xác thực và phân quyền).
5. Cloudinary: Quản lý và xử lý ảnh/video trên Cloudinary.
6. Spring Boot Starter OAuth2 Client: Tích hợp OAuth2 Client (ví dụ: đăng nhập qua Google).
7. Spring Boot Starter OAuth2 Resource Server: Bảo vệ API với JWT.
8. Spring Boot Starter Validation: Validation dữ liệu đầu vào.
9. Spring Boot Starter Mail: Gửi email từ ứng dụng.
10. Spring Security Crypto: Mã hóa và giải mã mật khẩu người dùng.
11. Spring Boot Starter Test: Hỗ trợ viết và chạy các test cases.
12. MapStruct: Map giữa DTO và Entity.
13. Thymeleaf: Template engine để render các trang HTML.
14. JWT Libraries: Giải mã và tạo JWT tokens.
15. Thymeleaf Layout Dialect: Hỗ trợ cấu trúc layout trong Thymeleaf.
16. Springdoc OpenAPI: Tài liệu API tự động bằng OpenAPI cho Spring Boot.

---

#### Hướng dẫn chạy dự án Backend (chocu)

1. Import database:
   - Mở WORKBENCH và run file `/Tai-lieu-yeu-cau/chocu.sql` hoặc dùng chức năng import.
   - Sau đó database mới với tên `chocu` sẽ được tạo ra.

2. Cấu hình `.env` file:
   - Mở file `.env` và chỉnh sửa các thông tin sau cho phù hợp với môi trường của bạn:
     ```
     DATASOURCE_URL=jdbc:mysql://localhost:3306/chocu?createDatabaseIfNotExist=true
     DATASOURCE_USERNAME=root
     DATASOURCE_PASSWORD=
     ```
   - Nếu không thể chạy database local (bước 1 không thành công), có thể sử dụng database cloud.
    ```
     DATASOURCE_URL=jdbc:mysql://autorack.proxy.rlwy.net:14591/chocu?createDatabaseIfNotExist=true
     DATASOURCE_USERNAME=java_member
     DATASOURCE_PASSWORD=java_2024
     ```

3. Tải và cài đặt dependencies:
   - Mở terminal trong dự án và chạy lệnh:
     ```
     mvn clean install
     ```

4. Chạy ứng dụng:
   - Để chạy ứng dụng, sử dụng lệnh sau:
     ```
     mvn spring-boot:run
     ```

5. Kiểm tra API:
   - Sau khi ứng dụng chạy, bạn có thể kiểm tra các API thông qua Postman hoặc Swagger UI.
   - Host sau khi run: http://localhost:8080 (dùng cho server API và server Admin).
