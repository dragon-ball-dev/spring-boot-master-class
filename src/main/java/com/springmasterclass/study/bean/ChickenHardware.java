package com.springmasterclass.study.bean;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Component;

@Component
public class ChickenHardware {
    public ChickenHardware() {
        System.out.println("Khi Object được khởi tạo");
    }

    @PostConstruct
    public void init() {
        System.out.println("Bean bắt đầu sẵn sàng được sử dụng");
    }

    @PreDestroy
    public void cleanup() {
        System.out.println("Bean đã bị hủy");
    }
}
