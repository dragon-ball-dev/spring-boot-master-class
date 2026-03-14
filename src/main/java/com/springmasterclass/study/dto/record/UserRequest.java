package com.springmasterclass.study.dto.record;

import com.springmasterclass.study.anotation.Cccd;
import jakarta.validation.constraints.*;

public record UserRequest (
        @NotEmpty(message = "Trường này không được phép trống")  // Đối tượng nó quan tâm là khác null và độ dài chuỗi phải lớn hơn 0
        @NotBlank // Đối tượng nó quan tâm là khác null và sau khi bỏ hết dấu cách thì phải > 0
        @NotNull(message = "Trường này không được null") // Nó chi quan tâm đến các đối tượng bị null
        String name,
        @Min(value = 0, message = "Giá trị không nhỏ hơn 0")
        @Size(min = 9, max = 11, message = "Số điện thoại phải có 9 đến 10 kí tự")
        String phone,

        @Email
        @NotBlank
        String email,
        @Pattern(regexp = "")
        String username,
        String address,
        String password,
        @Cccd
        String cccd
) {
}
