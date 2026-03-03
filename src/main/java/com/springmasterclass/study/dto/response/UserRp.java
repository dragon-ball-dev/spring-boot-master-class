package com.springmasterclass.study.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserRp {
    private String id;
    private String name;
    private String address;
    private String phone;
    private String email;
    private String username;
}
