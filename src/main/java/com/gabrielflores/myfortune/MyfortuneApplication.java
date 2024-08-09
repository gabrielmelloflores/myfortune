package com.gabrielflores.myfortune;

import java.util.Locale;
import java.util.TimeZone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

import jakarta.annotation.PostConstruct;

@EnableScheduling
@EnableJpaAuditing
@SpringBootApplication
@EntityScan("com.gabrielflores.myfortune.model")
@EnableJpaRepositories("com.gabrielflores.myfortune.repository")
public class MyfortuneApplication {

	@PostConstruct
	public void init() {
			Locale.setDefault(Locale.of("pt", "BR"));
			TimeZone.setDefault(TimeZone.getTimeZone("America/Sao_Paulo"));
	}
	
	public static void main(String[] args) {
		SpringApplication.run(MyfortuneApplication.class, args);
	}

}
