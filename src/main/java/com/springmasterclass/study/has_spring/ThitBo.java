package com.springmasterclass.study.has_spring;

import org.springframework.stereotype.Component;

@Component
public class ThitBo implements Thit{
    public String layThitBo() {
        return "50g Thit bo tai";
    }

    @Override
    public String layThit() {
        return "50g Thit bo tai";
    }
}