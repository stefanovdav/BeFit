package org.beFit.v1.bin.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
        "org.beFit.v1.beans",
        "org.beFit.v1.api",
        "org.beFit.v1.auth",
        "org.beFit.v1.scheduled"
})
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}
