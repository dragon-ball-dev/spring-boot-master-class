package com.springmasterclass.study.dto.record;

public record UserRequest (
        String name,
        String phone,
        String email,
        String username,
        String address,
        String password
) {
}
