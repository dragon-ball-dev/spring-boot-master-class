package com.springmasterclass.study.configuration;

import com.springmasterclass.study.service.ConnectionService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DatabaseLoader {
    @Autowired
    private ConnectionService connectionService;

//    public DatabaseLoader () {
//        System.out.println("STEP 1: Constructor running ....");
//        // Thử gọi các service
//        try {
//            connectionService.connect();
//        } catch (NullPointerException e){
//            System.out.println(e);
//        }
//    }

    @PostConstruct
    public void init(){
        System.out.println("STEP 1: Constructor running ....");
        // Thử gọi các service
        try {
            connectionService.connect();
        } catch (NullPointerException e){
            System.out.println(e);
        }
    }
}
