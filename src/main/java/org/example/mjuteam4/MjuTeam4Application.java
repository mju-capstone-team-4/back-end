package org.example.mjuteam4;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class MjuTeam4Application {

    public static void main(String[] args) {
        SpringApplication.run(MjuTeam4Application.class, args);
    }

}
