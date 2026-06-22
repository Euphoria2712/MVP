package freshmart_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import io.mongock.runner.springboot.EnableMongock;

@SpringBootApplication
@EnableDiscoveryClient
@EnableMongock
public class FreshmartServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(FreshmartServiceApplication.class, args);
	}

}
