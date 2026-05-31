package com.springmasterclass.study.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/demo")
public class DemoController {

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminOnly() {
        return "Chỉ admin mới thấy được nội dung này!";
    }

    @GetMapping("/user")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public String userOrAdmin() {
        return "Cả user và admin đều vào được.";
    }

    @GetMapping("/patient/read")
    @PreAuthorize("hasAuthority('patient:read')")
    public String readPatient() {
        return "Đọc hồ sơ bệnh nhân";
    }

    @PostMapping("/patient/write")
    @PreAuthorize("hasAuthority('patient:write')")
    public String writePatient() {
        return "Ghi hồ sơ bệnh nhân";
    }

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public Authentication getCurrentUser() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
}