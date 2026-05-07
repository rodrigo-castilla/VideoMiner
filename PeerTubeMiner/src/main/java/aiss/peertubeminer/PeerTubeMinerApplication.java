package aiss.peertubeminer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
// Forzamos el escaneo para que encuentre el Controller y los Services
@ComponentScan(basePackages = {"aiss.peertubeminer", "aiss.videominer"})
public class PeerTubeMinerApplication {

	public static void main(String[] args) {
		SpringApplication.run(PeerTubeMinerApplication.class, args);
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}