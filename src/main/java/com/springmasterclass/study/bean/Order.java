package com.springmasterclass.study.bean;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class Order {
    public Order() {
        System.out.println("Create a new order!");
    }
}
