package com.nstanogias.skistore;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nstanogias.skistore.domain.order.DeliveryMethod;
import com.nstanogias.skistore.domain.Product;
import com.nstanogias.skistore.repository.DeliveryMethodRepository;
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
	CommandLineRunner runner(ProductRepository productRepository, DeliveryMethodRepository deliveryMethodRepository) {
		return args -> {
			// read JSON and load json
			ObjectMapper mapper = new ObjectMapper();
			TypeReference<List<Product>> productReference = new TypeReference<List<Product>>() {};
			InputStream productsStream = TypeReference.class.getResourceAsStream("/products.json");
			TypeReference<List<DeliveryMethod>> deliveryMethodReference = new TypeReference<List<DeliveryMethod>>() {};
			InputStream deliveryStream = TypeReference.class.getResourceAsStream("/delivery.json");
			try {
				List<Product> products = mapper.readValue(productsStream, productReference);
				List<DeliveryMethod> deliveryMethods = mapper.readValue(deliveryStream, deliveryMethodReference);
				productRepository.saveAll(products);
				log.info("Products Saved!");
				deliveryMethodRepository.saveAll(deliveryMethods);
				log.info("Delivery Methods Saved!");
			} catch (IOException e) {
				log.error("Unable to save entity: " + e.getMessage());
			}
		};
	}
}
