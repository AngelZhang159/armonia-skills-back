package com.armoniacode.armoniaskills;

import com.armoniacode.armoniaskills.util.FirebaseInitializer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ArmoniaskillsApplication {

    public static void main(String[] args) {
        SpringApplication.run(ArmoniaskillsApplication.class, args);

        FirebaseInitializer firebaseInitializer = new FirebaseInitializer();
        firebaseInitializer.initialize();
    }

}
