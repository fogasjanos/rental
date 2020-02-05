package eu.fogas.rental;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;

@SpringBootApplication(exclude = ErrorMvcAutoConfiguration.class)
public class RentCarsApplications {

    public static void main(String[] args) {
        SpringApplication.run(RentCarsApplications.class, args);
    }
}
