# 🎮 Number Guess - Spring Boot Application

Ứng dụng trò chơi "Đoán Số" được xây dựng bằng Spring Boot 3.5.4, sử dụng Maven, MySQL, JPA, Validation, OAuth2 Resource
Server và JWT.

---

## 📋 Mục lục

- [Yêu cầu hệ thống](#yêu-cầu-hệ-thống)
- [Clone source code](#clone-source-code)
- [Import vào IntelliJ IDEA](#import-vào-intellij-idea)
- [Cấu hình cơ sở dữ liệu](#cấu-hình-cơ-sở-dữ-liệu)
- [Chạy ứng dụng](#chạy-ứng-dụng)
- [Lỗi thường gặp](#lỗi-thường-gặp)

---

## ✅ Yêu cầu hệ thống

- **Java JDK 17+**
- **Maven 3.6+**
- **MySQL 8+**
- IntelliJ IDEA
- Git

---

## 📥 Clone source code

```bash
git clone https://github.com/HoangVuong19/number-guess.git
cd number-guess
```

## 💻 Import vào IntelliJ IDEA

1. Mở IntelliJ
2. Chọn `File > Open`, trỏ tới thư mục dự án `number-guess`
3. IntelliJ sẽ tự động import dự án Maven
4. Kiểm tra `Project SDK`: vào `File > Project Structure > Project`, chọn **JDK 17**

---

## ⚙️ Cấu hình cơ sở dữ liệu

### 1. Tạo database trong MySQL:

```sql
CREATE
DATABASE number_guess;
```

### 2. Cấu hình `application.properties` trong `src/main/resources`:

```properties
# Server
server.port=8080
server.servlet.context-path=/api
# MySQL Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/number_guess
spring.datasource.username=root
spring.datasource.password=yourpassword
# Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.open-in-view=false
# jwt.signerKey
# VNPay Configuration
```

## ▶️ Chạy ứng dụng

### Cách 1: IntelliJ

- Mở class `NumberGuessApplication.java` (class có `@SpringBootApplication`)
- Nhấn `Run` hoặc `Shift + F10`

### Cách 2: Maven CLI

```bash
./mvnw spring-boot:run
```

---

## 🧯 Lỗi thường gặp

| Lỗi                                         | Nguyên nhân               | Cách khắc phục                                                |
|---------------------------------------------|---------------------------|---------------------------------------------------------------|
| `Cannot resolve symbol 'springframework'`   | Maven chưa được load đúng | Chuột phải vào project → `Reload Maven Project`               |
| `Access denied for user 'root'@'localhost'` | Sai thông tin database    | Kiểm tra lại username/password trong `application.properties` |
| `Port 8080 already in use`                  | Port đang bị chiếm        | Đổi sang port khác: `server.port=8081`                        |
| `Failed to configure a DataSource`          | Chưa tạo DB hoặc sai URL  | Kiểm tra `spring.datasource.url` và đảm bảo DB tồn tại        |

---

> © 2025 Number Guess Game - All rights reserved.
