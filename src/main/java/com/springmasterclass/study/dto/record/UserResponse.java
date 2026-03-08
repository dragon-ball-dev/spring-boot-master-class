package com.springmasterclass.study.dto.record;

public record UserResponse(
        String id,
        String name,
        String phone,
        String address,
        String email,
        String username
) {
}
