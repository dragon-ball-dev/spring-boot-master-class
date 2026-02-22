package com.springmasterclass.study;

import com.springmasterclass.study.bean.Chef;
import com.springmasterclass.study.bean.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class StudyApplication implements CommandLineRunner {

    @Autowired
    private ApplicationContext applicationContext; // Đây chính là cái kho của tất các thằng Bean Container

    public static void main(String[] args) {
		SpringApplication.run(StudyApplication.class, args);
	}

    @Override
    public void run(String... args) throws Exception {
        //Spring Container (ApplicationContext): Chính là cái Kho lưu trữ và Điều hành của nhà hàng.
        // Khi nhà hàng mở cửa (App Start),
        // ông chủ Spring sẽ quét một lượt danh sách và tự tay chuẩn bị các đồ dùng cần thiết rồi cất vào kho.

        //Bean: Chính là những Dụng cụ/Nguyên liệu đã được ông chủ chuẩn bị sẵn và dán nhãn.
        // Một khi đã nằm trong kho này, Spring sẽ chịu trách nhiệm nuôi sống và vứt bỏ chúng khi cần.

        System.out.println("CHECK SINGLETON SCOPE");
        Chef chef1 = applicationContext.getBean(Chef.class);
        Chef chef2 = applicationContext.getBean(Chef.class);
        System.out.println("CHEF1 == CHEF2 ? " + (chef1 == chef2));

        System.out.println("CHECK PROTOTYPE SCOPE");
        Order order1 = applicationContext.getBean(Order.class);
        Order order2 = applicationContext.getBean(Order.class);
        System.out.println("Order1 == Order2 ? " + (order1 == order2));

    }
}
