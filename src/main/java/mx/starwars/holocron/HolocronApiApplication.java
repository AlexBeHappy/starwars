package mx.starwars.holocron;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = "mx.starwars.holocron.entity")
public class HolocronApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(HolocronApiApplication.class, args);
    }

}
