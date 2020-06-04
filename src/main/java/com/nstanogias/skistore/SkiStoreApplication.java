package com.nstanogias.skistore;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nstanogias.skistore.domain.Product;
import com.nstanogias.skistore.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@EnableCaching
@SpringBootApplication
@Slf4j
public class SkiStoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(SkiStoreApplication.class, args);
	}

	@Bean
	CommandLineRunner runner(ProductRepository productRepository) {
		return args -> {
			// read JSON and load json
			ObjectMapper mapper = new ObjectMapper();
			TypeReference<List<Product>> productReference = new TypeReference<List<Product>>() {};
			InputStream productsStream = TypeReference.class.getResourceAsStream("/products.json");
			try {
				List<Product> products = mapper.readValue(productsStream, productReference);
				productRepository.saveAll(products);
				log.info("Products Saved!");
			} catch (IOException e) {
				log.error("Unable to save users: " + e.getMessage());
			}
		};
	}
}
