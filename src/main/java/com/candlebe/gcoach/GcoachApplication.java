package com.candlebe.gcoach;

import com.candlebe.gcoach.service.ContentService;
import com.candlebe.gcoach.storage.StorageProperties;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
@EnableConfigurationProperties(StorageProperties.class)
public class GcoachApplication {

	public static void main(String[] args) {
		SpringApplication.run(GcoachApplication.class, args);
	}

//	@Bean
//	CommandLineRunner init(ContentService contentService) {
//		return (args) -> {
//			contentService.deleteAll();
//			contentService.init();
//		};
//	}
}
