package com.mandat.amoulanfe;

import com.mandat.amoulanfe.domain.FileStorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({
		FileStorageProperties.class
})
public class AmoulanfeApplication {

	public static void main(String[] args) {
		SpringApplication.run(AmoulanfeApplication.class, args);
	}
}
