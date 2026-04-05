package com.springmasterclass.study.common;

import com.springmasterclass.study.entity.Checkup;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

public class Example {

    @PersistenceContext
    EntityManager entityManager;

    public String findNameProduct(){
        entityManager.find(Checkup.class, 1);
        return "";
    }
}
