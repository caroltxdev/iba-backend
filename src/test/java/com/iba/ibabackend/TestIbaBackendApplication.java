package com.iba.ibabackend;

import com.iba.IbaApplication;
import org.springframework.boot.SpringApplication;

public class TestIbaBackendApplication {

    public static void main(String[] args) {
        SpringApplication.from(IbaApplication::main)
                .run(args);
    }
}


