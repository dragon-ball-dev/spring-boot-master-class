package com.springmasterclass.study.service;

import org.springframework.stereotype.Service;

@Service
public class ConnectionService {
    private boolean isReady = false;

    public ConnectionService() {
        System.out.println("Wait connect ....");
        this.isReady = true;
    }

    public void connect() {
        if (isReady) {
            System.out.println("Success");
        } else {
            System.out.println("Fail");
        }
    }

    public String getStatus() {
        return "ONLINE";
    }
}
