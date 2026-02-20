package com.springmasterclass.study.has_spring;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary
public class ThitGa implements Thit{
    public String layThitGa() {
        return "50g Thit ga";
    }

    @Override
    public String layThit() {
        return "50g Thit ga";
    }
}
