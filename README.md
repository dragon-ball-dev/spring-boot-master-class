# 🍃 Spring Boot 4.x Masterclass (2026 Edition)

Chào mừng bạn đến với khóa học **Spring Boot 4.x Masterclass** trên YouTube! Đây là nơi lưu trữ toàn bộ mã nguồn, tài liệu và lộ trình từ cơ bản đến chuyên gia (Enterprise Level).

[![Youtube Badge](https://img.shields.io/badge/Youtube-Subscribe-red?style=for-the-badge&logo=youtube)](YOUR_YOUTUBE_LINK)
[![Java Version](https://img.shields.io/badge/Java-21%2B-orange?style=for-the-badge&logo=openjdk)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.x-brightgreen?style=for-the-badge&logo=springboot)](https://spring.io/projects/spring-boot)

---

## 🗺️ Lộ trình khóa học (Curriculum)

| Bài    | Nội dung bài giảng                                              | Trạng thái |     Mã nguồn     |
|:-------|:----------------------------------------------------------------|:----------:|:----------------:|
| 01     | Khởi động & Tư duy Backend 2026                                 |   ✅ Done   |  [View Code](#)  |
| 02     | IoC, DI - Nội công tâm pháp                                     |   ✅ Done   |  [View Code](#)  |
| 03     | Spring Container vs Bean                                        |   ✅ Done   |  [View Code](#)  |
| 04     | Pháp thuật Auto-Configuration                                   |   ✅ Done   |  [View Code](#)  |
| 05     | Cấu trúc thư mục Chuẩn Doanh Nghiệp (3-Tier)                    |   ✅ Done   |  [View Code](#)  |
| 06     | Kết nối PostgreSQL vs MySQL & Spring Data JPA                   |   ✅ Done   |  [View Code](#)  |
| 07     | Tạo REST API đầu tiên với @RestController                       |   ✅ Done   |  [View Code](#)  |
| 08     | Ma thuật MapStruct, record, ModderMapper vs ObjectMapper        |   ✅ Done   |  [View Code](#)  |
| 10     | Validation dữ liệu đầu vào & Bắt lỗi toàn cục                   |   ✅ Done   |  [View Code](#)  |
| 11     | Làm chủ Pagination & Sorting trong Spring Boot                  |   ✅ Done   |  [View Code](#)  |
| 12     | Giải ngố về Hibernate vs JPA                                    |   ✅ Done   |  [View Code](#)  |
| 13.A   | TOÀN TẬP QUAN HỆ 1-N & TRUY QUÉT N+1 QUERY                      |   ✅ Done   |  [View Code](#)  |
| 13.B   | Hiểu PERSIST, MERGE, REMOVE, ALL, orphanRemoval – Khi nào dùng? |   ✅ Done   |  [View Code](#)  |
| 14     | Quản lý Database Migration với Flyway trong Spring Boot         |   ✅ Done   |  [View Code](#)  |
| 15     | Phân biệt file YAML vs Properties và cấu hình đa môi trường     |   ✅ Done   |  [View Code](#)  |
| 16     | "NGƯỜI GÁC CỔNG" Spring Security - KIẾN TRÚC LÕI                |   ✅ Done   |  [View Code](#)  |
| 17     | Vũ khí JWT & Xây dựng Custom Filter                             |   ✅ Done   |  [View Code](#)  |
| 18     | Bộ 3 Quyền Lực: Móc nối Database vào luồng Xác thực             |   ✅ Done   |  [View Code](#)  |
| **19** | **Phân quyền RBAC & Xử lý Exception "Chuẩn Doanh Nghiệp"**     | 🚀 Current | [**Explore**](#explore-lesson-19) |
| ...    | ...                                                             |    ...     |       ...        |
| 20     | Spring Security & JWT Authentication                            | 🔒 Locked  |        -         |

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

## 📂 Chi tiết cấu trúc thư mục

```text
src/main/java/com/springmasterclass/study
├── 📁 anotation        # Custom Annotation & Validators (CCCD, v.v.)
├── 📁 config           # Cấu hình Bean (ModelMapper, DatabaseConfigs)
├── 📁 controller       # Tiếp nhận Request (RestControllers, DemoController)
├── 📁 service          # Xử lý nghiệp vụ (Business Logic)
├── 📁 repository       # Truy vấn dữ liệu (Spring Data JPA, Specifications)
├── 📁 entity           # Map trực tiếp với Database Table (Product, User, Patient)
├── 📁 dto              # Data Transfer Objects (Request/Response Records)
├── 📁 mapper           # Chuyển đổi Entity <-> DTO (MapStruct)
├── 📁 security         # Bảo mật hệ thống (SecurityConfig, CustomDetailsService, JWT Filter)
└── 📁 exception        # Xử lý lỗi toàn cục (GlobalExceptionHandler, Custom Authentication Handlers)
```

---

<div id="explore-lesson-19"></div>

# 🚀 Bài 19: Phân quyền RBAC & Xử lý Exception "Chuẩn Doanh Nghiệp"

Trong bài học này, chúng ta sẽ nâng cấp hệ thống Spring Security lên cấp độ doanh nghiệp bằng cách giải quyết 2 bài toán cực kỳ quan trọng:
1. **Phân quyền dựa trên vai trò (Role-Based Access Control - RBAC)** & **Quyền hạn chi tiết (Authority/Permission)** sử dụng `@PreAuthorize` và cấu hình Method Security.
2. **Xử lý ngoại lệ bảo mật tập trung (Custom Security Exceptions)** để chuyển đổi các lỗi thô mặc định của Tomcat/Spring Security (như trang HTML Whitelabel trống, hay HTTP Status 403 trơn) thành cấu trúc JSON API sạch đẹp, đồng bộ với hệ thống.

---

## 💡 Lý thuyết cốt lõi (Core Concepts)

### 1. Phân biệt Roles vs Authorities trong Spring Security
* **Roles (Vai trò)**: Đại diện cho một nhóm người dùng (ví dụ: `ADMIN`, `USER`, `DOCTOR`). Trong Spring Security, Role bắt buộc phải bắt đầu bằng tiền tố `ROLE_`. Khi kiểm tra quyền bằng `hasRole('ADMIN')`, thực chất Spring đang kiểm tra authority tên là `ROLE_ADMIN`.
* **Authorities/Permissions (Quyền hạn)**: Đại diện cho một hành động cụ thể trên một tài nguyên (ví dụ: `patient:read`, `patient:write`). Các quyền hạn này không cần tiền tố và kiểm tra thông qua `hasAuthority('patient:read')`.
* **Cơ chế lưu trữ động**: Trong cơ sở dữ liệu (Database), ta có thể lưu trữ danh sách các vai trò và quyền hạn của User dưới dạng một chuỗi phân tách bằng dấu phẩy (comma-separated string), ví dụ: `ROLE_DOCTOR,patient:read`. Khi `loadUserByUsername`, ta phân tách chuỗi này thành danh sách các `SimpleGrantedAuthority`.

### 2. Sơ đồ xử lý Exception trong Spring Security
Mặc định, các ngoại lệ phát sinh bên trong **Filter Chain** (như JWT không hợp lệ, hoặc không đủ quyền truy cập) sẽ **không** đi qua `@RestControllerAdvice` thông thường vì nó nằm ngoài phạm vi xử lý của DispatcherServlet. Để xử lý "Chuẩn Doanh Nghiệp", chúng ta cần bắt kịp các lỗi này ở tầng Filter bằng cách cung cấp các Bean chuyên biệt:

```mermaid
graph TD
    Client[Client Request] --> FilterChain[Spring Security Filter Chain]
    
    subgraph Security Filter Chain
        JwtFilter[JwtAuthenticationFilter]
        AuthCheck{Kiểm tra JWT?}
        AuthCheck -- Chưa đăng nhập / Lỗi Token --> AuthenticationEntryPoint[CustomAuthenticationEntryPoint]
        AuthCheck -- Hợp lệ --> AccessDecision{Kiểm tra Quyền Endpoint?}
        AccessDecision -- Thiếu Role / Authority --> AccessDeniedHandler[CustomAccessDeniedHandler]
        AccessDecision -- Đủ Quyền --> TargetController[DemoController]
    end

    AuthenticationEntryPoint --> Error401[Response 401 Unauthorized JSON]
    AccessDeniedHandler --> Error403[Response 403 Forbidden JSON]
```

---

## 🛠️ Chi tiết triển khai mã nguồn (Implementation Details)

### 1. Cấu hình Method Security & Đăng ký Custom Handlers
Để sử dụng các annotation phân quyền như `@PreAuthorize` trên từng Endpoint, chúng ta bật tính năng này bằng cách thêm `@EnableMethodSecurity` vào cấu hình Security.

Trong file [SecurityConfig.java](file:///d:/study_with_2026/study/src/main/java/com/springmasterclass/study/security/SecurityConfig.java):
```java
@Configuration
@EnableWebSecurity
@EnableMethodSecurity // Bật phân quyền mức Method (hỗ trợ @PreAuthorize, @PostAuthorize)
public class SecurityConfig {

    @Autowired
    private CustomAuthenticationEntryPoint authenticationEntryPoint;

    @Autowired
    private CustomAccessDeniedHandler accessDeniedHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .cors(Customizer.withDefaults())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/v1/categories/**").permitAll()
                .requestMatchers("/api/v1/auth/**").permitAll()
                .requestMatchers("/error").permitAll()
                .anyRequest().authenticated()
            )
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            // Đăng ký các Custom Handler xử lý Exception Security
            .exceptionHandling(ex -> ex
                .authenticationEntryPoint(authenticationEntryPoint) // Xử lý lỗi 401
                .accessDeniedHandler(accessDeniedHandler)           // Xử lý lỗi 403
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
```

### 2. Tách và Nạp Quyền Hạn Động từ Database
Trong thực tế doanh nghiệp, một User có thể có nhiều vai trò và quyền hạn được lưu chung trong một cột ở Database. Spring Security cho phép chuyển đổi chuỗi này thành danh sách `GrantedAuthority`.

Trong file [User.java](file:///d:/study_with_2026/study/src/main/java/com/springmasterclass/study/entity/auth/User.java):
```java
@Entity(name = "AuthUser")
@Table(name = "users")
@Data
@NoArgsConstructor
public class User implements UserDetails {
    // ... các trường khác
    private String role;   // Lưu trữ chuỗi dạng: "ROLE_ADMIN" hoặc "patient:read,ROLE_DOCTOR"

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Tách chuỗi role theo dấu phẩy, trim khoảng trắng và map sang SimpleGrantedAuthority
        return Arrays.stream(role.split(","))
                .map(String::trim)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
    // ...
}
```

### 3. Xử lý Lỗi Chưa Xác Thực (401 Unauthorized)
Khi một request không gửi kèm token hợp lệ hoặc token đã hết hạn truy cập vào API được bảo vệ, `AuthenticationEntryPoint` sẽ được kích hoạt để trả về cấu trúc lỗi chuẩn JSON.

Trong file [CustomAuthenticationEntryPoint.java](file:///d:/study_with_2026/study/src/main/java/com/springmasterclass/study/exception/CustomAuthenticationEntryPoint.java):
```java
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, 
                         AuthenticationException authException) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        
        response.getWriter().write(
                Map.of(
                        "code", 401,
                        "message", "Bạn chưa đăng nhập hoặc token không hợp lệ!"
                ).toString()
        );
    }
}
```

### 4. Xử lý Lỗi Thiếu Quyền Truy Cập (403 Forbidden)
Khi người dùng đã đăng nhập thành công (JWT hợp lệ) nhưng cố tình truy cập vào các API yêu cầu các vai trò cao hơn (như ADMIN) hoặc các quyền hạn chuyên biệt mà tài khoản của họ không sở hữu, `AccessDeniedHandler` sẽ chặn lại và trả về lỗi 403 tùy biến dạng JSON.

Trong file [CustomAccessDeniedHandler.java](file:///d:/study_with_2026/study/src/main/java/com/springmasterclass/study/exception/CustomAccessDeniedHandler.java):
```java
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, 
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json;charset=UTF-8");
        
        response.getWriter().write(
                Map.of(
                        "code", 403,
                        "message", "Bạn không có quyền truy cập!"
                ).toString()
        );
    }
}
```

### 5. Áp dụng Phân Quyền ở tầng Controller
Chúng ta có thể áp dụng linh hoạt việc kiểm tra Role (`hasRole`), nhóm Role (`hasAnyRole`), hoặc Quyền hạn cụ thể (`hasAuthority`) ngay tại các hàm tiếp nhận Request.

Trong file [DemoController.java](file:///d:/study_with_2026/study/src/main/java/com/springmasterclass/study/controller/DemoController.java):
```java
@RestController
@RequestMapping("/api/v1/demo")
public class DemoController {

    // Chỉ có người dùng sở hữu vai trò ROLE_ADMIN mới vào được
    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminOnly() {
        return "Chỉ admin mới thấy được nội dung này!";
    }

    // Cả vai trò ROLE_USER và ROLE_ADMIN đều được cấp quyền vào
    @GetMapping("/user")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public String userOrAdmin() {
        return "Cả user và admin đều vào được.";
    }

    // Yêu cầu quyền hạn chi tiết 'patient:read' (bất kể họ mang vai trò gì)
    @GetMapping("/patient/read")
    @PreAuthorize("hasAuthority('patient:read')")
    public String readPatient() {
        return "Đọc hồ sơ bệnh nhân";
    }

    // Yêu cầu quyền hạn chi tiết 'patient:write'
    @PostMapping("/patient/write")
    @PreAuthorize("hasAuthority('patient:write')")
    public String writePatient() {
        return "Ghi hồ sơ bệnh nhân";
    }

    // Yêu cầu người dùng chỉ cần được xác thực (đăng nhập thành công)
    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public Authentication getCurrentUser() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
}
```

---

## 🚦 Hướng dẫn Kiểm thử & Xác minh (Verification Guide)

Dữ liệu kiểm thử đã được nạp sẵn qua cấu hình Migration [V6__insert_more_users.sql](file:///d:/study_with_2026/study/src/main/resources/db/migration/user/V6__insert_more_users.sql) với các thông tin tài khoản:
1. **User `admin`**: Có vai trò `ROLE_ADMIN`.
2. **User `doctor`**: Có vai trò `ROLE_DOCTOR` và quyền hạn cụ thể `patient:read`.

### Bước 1: Lấy mã JWT Token (Đăng nhập)
Sử dụng cURL hoặc Postman để gọi Endpoint đăng nhập:
```bash
curl -X POST http://localhost:8080/api/v1/auth/login \
     -H "Content-Type: application/json" \
     -d '{"username": "admin", "password": "your_password"}'
```
> **Lưu ý**: Hãy đảm bảo bạn sử dụng mật khẩu chính xác đã được mã hóa BCrypt tương ứng với tài khoản nạp từ cơ sở dữ liệu.

### Bước 2: Kiểm thử kịch bản Lỗi 401 Unauthorized (Chưa đăng nhập)
Gửi yêu cầu trực tiếp vào API được bảo vệ mà không đính kèm Header Authorization:
```bash
curl -X GET http://localhost:8080/api/v1/demo/admin
```
**Phản hồi mong đợi (HTTP Status 401):**
```json
{
  "code": 401,
  "message": "Bạn chưa đăng nhập hoặc token không hợp lệ!"
}
```

### Bước 3: Kiểm thử kịch bản Lỗi 403 Forbidden (Thiếu quyền)
Sử dụng JWT Token của tài khoản **`doctor`** (chỉ có quyền `patient:read`, không có quyền Admin) để truy cập API `/api/v1/demo/admin`:
```bash
curl -X GET http://localhost:8080/api/v1/demo/admin \
     -H "Authorization: Bearer <JWT_CỦA_DOCTOR>"
```
**Phản hồi mong đợi (HTTP Status 403):**
```json
{
  "code": 403,
  "message": "Bạn không có quyền truy cập!"
}
```

### Bước 4: Kiểm thử kịch bản Thành công (Đúng vai trò/quyền)
Sử dụng JWT Token của tài khoản **`doctor`** để truy cập API yêu cầu quyền hạn `patient:read`:
```bash
curl -X GET http://localhost:8080/api/v1/demo/patient/read \
     -H "Authorization: Bearer <JWT_CỦA_DOCTOR>"
```
**Phản hồi mong đợi (HTTP Status 200):**
```text
Đọc hồ sơ bệnh nhân
```

Sử dụng JWT Token của tài khoản **`admin`** để truy cập API yêu cầu vai trò `ROLE_ADMIN`:
```bash
curl -X GET http://localhost:8080/api/v1/demo/admin \
     -H "Authorization: Bearer <JWT_CỦA_ADMIN>"
```
**Phản hồi mong đợi (HTTP Status 200):**
```text
Chỉ admin mới thấy được nội dung này!
```

---

## 🎯 Tổng kết giá trị "Chuẩn Doanh Nghiệp"
* **Bảo mật tối đa**: Ngăn chặn rò rỉ cấu trúc ứng dụng và các thông tin nhạy cảm của Tomcat server.
* **Đồng nhất dữ liệu API**: Toàn bộ lỗi được định dạng dạng cấu trúc JSON duy nhất (`code` và `message`), giúp lập trình viên Frontend hoặc Mobile dễ dàng xử lý ngoại lệ đồng bộ trên giao diện.
* **Mở rộng dễ dàng**: Phân quyền chi tiết (Authority-based) giúp hệ thống sẵn sàng nâng cấp lên luồng phân quyền động, quản lý động qua database mà không cần sửa đổi cấu trúc code.