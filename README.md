# Java Web Application - Servlet JSP MVC

Đây là một ứng dụng web Java được xây dựng theo mô hình MVC (Model-View-Controller) sử dụng Servlet và JSP.

## Cấu trúc thư mục

```
src/
├── main/
│   ├── java/
│   │   ├── com/
│   │   │   ├── controllers/     # Xử lý logic điều hướng và xử lý request
│   │   │   │   ├── admin/      # Controllers cho phần quản trị
│   │   │   │   └── client/     # Controllers cho phần người dùng
│   │   │   ├── models/         # Các entity classes
│   │   │   ├── repositories/   # Xử lý tương tác với database
│   │   │   ├── services/       # Xử lý business logic
│   │   │   ├── filters/        # Các filter cho authentication/authorization
│   │   │   └── helpers/         # Các tiện ích và helper classes
│   ├── webapp/
│   │   ├── WEB-INF/
│   │   │   ├── web.xml        # Cấu hình ứng dụng web
│   │   │   └── views/         # Thư mục chứa các file JSP
│   │   │       ├── admin/     # Giao diện admin
│   │   │       └── client/    # Giao diện người dùng
│   │   ├── resources/
│   │   │   ├── css/          # Stylesheets
│   │   │   ├── js/           # JavaScript files
│   │   │   └── images/       # Hình ảnh
│   │   └── index.jsp         # Trang chủ
│   └── resources/
│       └── database.properties # Cấu hình database
```

## Kiến trúc ứng dụng

### 1. Controller Layer

- Xử lý các HTTP requests
- Điều hướng luồng dữ liệu
- Validate dữ liệu đầu vào
- Gọi các service tương ứng

### 2. Service Layer

- Chứa business logic
- Xử lý các nghiệp vụ phức tạp
- Giao tiếp với Repository layer
- Đảm bảo tính toàn vẹn dữ liệu

### 3. Repository Layer

- Tương tác trực tiếp với database
- Thực hiện các thao tác CRUD
- Tối ưu hóa truy vấn
- Xử lý connection pooling

### 4. View Layer (JSP)

- Hiển thị dữ liệu cho người dùng
- Sử dụng JSTL và EL
- Tách biệt logic và giao diện
- Responsive design

### 5. Filter

- Authentication và Authorization
- Request/Response logging
- Character encoding
- Security headers

## Công nghệ sử dụng

- Java 18
- Servlet 4.0
- JSP & JSTL
- JDBC
- MySQL 8.0
- Maven 3.x
- Tomcat 9.x
- Bootstrap 5
- jQuery

## Yêu cầu hệ thống

- JDK 18 trở lên
- Maven 3.x
- MySQL 8.0
- Apache Tomcat 9.x

## Cài đặt và chạy ứng dụng

1. Clone repository:

```bash
git clone [repository-url]
cd webjava
```

2. Cấu hình database:

- Tạo database MySQL bằng cách dùng file [`init.sql`](./init.sql)
- Cập nhật thông tin kết nối trong `src/main/resources/database.properties`

3. Build project:

```bash
mvn clean install
```

4. Deploy ứng dụng:

- Sử dụng IDE (khuyến nghị IntelliJ IDEA):
  - Import project as Maven project
  - Cấu hình Tomcat server
  - Run/Debug project

5. Truy cập ứng dụng:

```
http://localhost:8080/webjava
```

## Tài liệu tham khảo

- [Java Servlet Documentation](https://javaee.github.io/servlet-spec/)
- [JSP Documentation](https://docs.oracle.com/javaee/5/tutorial/doc/bnagx.html)
- [MySQL Documentation](https://dev.mysql.com/doc/)
- [Maven Documentation](https://maven.apache.org/guides/)
