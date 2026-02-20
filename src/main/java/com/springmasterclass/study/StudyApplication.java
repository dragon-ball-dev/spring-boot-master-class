package com.springmasterclass.study;

import com.springmasterclass.study.has_spring.ToPho;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class StudyApplication implements CommandLineRunner {

    private final ToPho toPho;

    public StudyApplication(ToPho toPho) {
        this.toPho = toPho;
    }

    public static void main(String[] args) {
		SpringApplication.run(StudyApplication.class, args);
	}

    @Override
    public void run(String... args) throws Exception {

        toPho.phucVu();

    }
}
