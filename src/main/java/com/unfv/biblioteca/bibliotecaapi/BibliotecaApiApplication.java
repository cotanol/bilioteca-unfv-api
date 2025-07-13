package com.unfv.biblioteca.bibliotecaapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling //CRON
public class BibliotecaApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(BibliotecaApiApplication.class, args);
    }

}
