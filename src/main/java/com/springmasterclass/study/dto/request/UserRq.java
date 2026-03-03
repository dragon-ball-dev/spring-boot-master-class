package com.springmasterclass.study.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRq {
    private String name;
    private String address;
    private String phone;
    private String email;
    private String username;
    private String password;
}
