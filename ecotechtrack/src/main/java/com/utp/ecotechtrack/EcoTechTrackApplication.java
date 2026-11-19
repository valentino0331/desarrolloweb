package com.utp.ecotechtrack;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Clase principal de inicio de Spring Boot para EcoTechTrack.
 * Contiene el método main que arranca el servidor Tomcat embebido y el contexto de Spring.
 */
@SpringBootApplication
public class EcoTechTrackApplication {

    public static void main(String[] args) {
        SpringApplication.run(EcoTechTrackApplication.class, args);
    }
}
