# 🍃 Spring Boot 4.x Masterclass (2026 Edition)

Chào mừng bạn đến với khóa học **Spring Boot 4.x Masterclass** trên YouTube! Đây là nơi lưu trữ toàn bộ mã nguồn, tài liệu và lộ trình từ cơ bản đến chuyên gia (Enterprise Level).

[![Youtube Badge](https://img.shields.io/badge/Youtube-Subscribe-red?style=for-the-badge&logo=youtube)](YOUR_YOUTUBE_LINK)
[![Java Version](https://img.shields.io/badge/Java-21%2B-orange?style=for-the-badge&logo=openjdk)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.x-brightgreen?style=for-the-badge&logo=springboot)](https://spring.io/projects/spring-boot)

---

## 🗺️ Lộ trình khóa học (Curriculum)

| Bài    | Nội dung bài giảng                                       | Trạng thái |     Mã nguồn     |
|:-------|:---------------------------------------------------------|:----------:|:----------------:|
| 01     | Khởi động & Tư duy Backend 2026                          |   ✅ Done   |  [View Code](#)  |
| 02     | IoC, DI - Nội công tâm pháp                              |   ✅ Done   |  [View Code](#)  |
| 03     | Spring Container vs Bean                                 |   ✅ Done   |  [View Code](#)  |
| 04     | Pháp thuật Auto-Configuration                            |   ✅ Done   |  [View Code](#)  |
| 05     | Cấu trúc thư mục Chuẩn Doanh Nghiệp (3-Tier)             |   ✅ Done   |  [View Code](#)  |
| 06     | Kết nối PostgreSQL vs MySQL & Spring Data JPA            |   ✅ Done   |  [View Code](#)  |
| 07     | Tạo REST API đầu tiên với @RestController                |   ✅ Done   |  [View Code](#)  |
| 08     | Ma thuật MapStruct, record, ModderMapper vs ObjectMapper |   ✅ Done   |  [View Code](#)  |
| 10     | Validation dữ liệu đầu vào & Bắt lỗi toàn cục            | ✅ Done | [View Code](#) |
| 11     | Làm chủ Pagination & Sorting trong Spring Boot            | ✅ Done | [View Code](#) |
| 12     | Làm chủ Pagination & Sorting trong Spring Boot            | ✅ Done | [View Code](#) |
| **13** | **TOÀN TẬP QUAN HỆ 1-N & TRUY QUÉT N+1 QUERY**        | 🚀 Current | [**Explore**](#) |
| ...    | ...                                                      |    ...     |       ...        |
| 20     | Spring Security & JWT Authentication                     | 🔒 Locked  |        -         |

---

## 🏗️ Kiến trúc dự án (Architecture)

Trong series này, chúng ta tuân thủ nghiêm ngặt mô hình **3-Tier Architecture** kết hợp với **DTO Pattern** để đảm bảo tính bảo mật và hiệu năng.

### Sơ đồ luồng dữ liệu (Data Flow)
`Client` ↔️ `Controller` ↔️ `Service` ↔️ `Repository` ↔️ `Database (PostgreSQL)`

### Quy tắc đặt tên (Naming Convention)
* **Controller**: `UserController.java` (Endpoints & Validation)
* **Service**: `UserService.java` (Interface) & `UserServiceImpl.java` (Logic)
* **Repository**: `UserRepository.java` (JPA Interface)
* **DTO**: `UserRequest.java`, `UserResponse.java`

---

## 📂 Chi tiết cấu trúc thư mục (Lesson 03 Focus)

```text
src/main/java/com/yourcompany/masterclass
├── 📁 config           # Cấu hình Bean (Security, Swagger, Redis)
├── 📁 controller       # Tiếp nhận Request (RestControllers)
├── 📁 service          # Xử lý nghiệp vụ (Business Logic)
├── 📁 repository       # Truy vấn dữ liệu (Spring Data JPA)
├── 📁 entity           # Map trực tiếp với Database Table
├── 📁 dto              # Data Transfer Objects (Request/Response)
├── 📁 mapper           # Chuyển đổi Entity <-> DTO (MapStruct)
└── 📁 exception        # Xử lý lỗi tập trung (Global Exception Handling)