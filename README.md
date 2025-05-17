# Java Web Application - Servlet JSP MVC

Đây là một ứng dụng web Java được xây dựng theo mô hình MVC (Model-View-Controller) sử dụng Servlet và JSP.

## Cấu trúc thư mục

```
.
├── docker/
│   ├── java/               # Cấu hình Docker cho Java
│   └── mysql/              # Cấu hình Docker cho MySQL
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
├── .env                   # File cấu hình biến môi trường
├── docker-compose.yml     # Cấu hình Docker Compose
└── README.md
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

# Hệ thống đặt bàn nhà hàng

## Yêu cầu hệ thống

- Docker
- Docker Compose

## Cài đặt và chạy

### 1. Sao chép file môi trường

```bash
cp .env.example .env
```

### 2. Cấu hình biến môi trường

Mở file `.env` và cấu hình các biến môi trường:

```env
# Java cannot read .env files.
# When you change the environment variables above,
# you must also update them in src/main/resources/database.properties
DB_CONNECTION=mysql
DB_HOST=mysql
DB_PORT=3306
DB_DATABASE="datban"
DB_USERNAME="u"
DB_PASSWORD="admin"
DB_ROOT_PASSWORD="admin"

### Docker ###
DOCKER_APP_PORT=2000
DOCKER_MYSQL_PORT=1000
DOCKER_PMA_PORT=1001
### End Docker ###x

```

### 3. Build và chạy container

```bash
# Build và chạy container
docker compose up -d
```

### 4. Dừng container

```bash
docker compose down
```

### 5. Xóa toàn bộ dữ liệu và khởi động lại

```bash
docker compose down -v
docker compose up -d
```

## Truy cập ứng dụng

- **Ứng dụng Web**: http://localhost:2000
- **PHPMyAdmin**: http://localhost:1001
  - Server: mysql
  - Username: username
  - Password: password

## Lưu ý

- Đảm bảo các cổng 2000, 1000, 1001 không bị sử dụng bởi ứng dụng khác
- Nếu muốn thay đổi cổng, hãy cập nhật trong file `.env`
- Database sẽ được tự động khởi tạo với dữ liệu mẫu khi chạy lần đầu
- Tất cả dữ liệu được lưu trong Docker volume, sẽ không bị mất khi restart container
- Để reset database, sử dụng lệnh `docker compose down -v`

## Tài liệu tham khảo

- [Java Servlet Documentation](https://javaee.github.io/servlet-spec/)
- [JSP Documentation](https://docs.oracle.com/javaee/5/tutorial/doc/bnagx.html)
- [MySQL Documentation](https://dev.mysql.com/doc/)
- [Maven Documentation](https://maven.apache.org/guides/)
