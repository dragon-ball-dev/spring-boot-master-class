package com.springmasterclass.study.has_spring;

import org.springframework.stereotype.Component;

// Đánh dấu @Component để Spring biết đây là "nguyên liệu" cần quản lý (Tạo Bean)
@Component
public class BanhPho {
    public String layBanhPho() {
        return "100g Banh pho tuoi";
    }
}