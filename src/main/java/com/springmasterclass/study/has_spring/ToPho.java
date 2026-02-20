package com.springmasterclass.study.has_spring;

import org.springframework.stereotype.Component;

@Component
public class ToPho {
    private final BanhPho banhPho;
//    private final ThitBo thitBo;
    private final Thit thit;

    public ToPho(BanhPho banhPho, Thit thit) {
        this.banhPho = banhPho;
        this.thit = thit;
    }

//    public ToPho(BanhPho banhPho, ThitBo thitBo) {
//        this.banhPho = banhPho;
//        this.thitBo = thitBo;
//    }

    public void phucVu() {
        System.out.println("✨ TO PHO NEW SPRING BOOT: " + banhPho.layBanhPho() + " + " + thit.layThit());
    }
}