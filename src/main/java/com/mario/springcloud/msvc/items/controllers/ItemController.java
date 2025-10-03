package com.mario.springcloud.msvc.items.controllers;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.mario.springcloud.msvc.items.models.Item;
import com.mario.springcloud.msvc.items.models.Product;
import com.mario.springcloud.msvc.items.services.ItemService;

@RestController
public class ItemController {

	@Autowired
	@Qualifier("itemServiceWebClient")
	private ItemService service;
	
	@Autowired
	private CircuitBreakerFactory cbFactory;
	
	private Logger logger = LoggerFactory.getLogger(ItemController.class);
	
	@GetMapping
	public List<Item> list(){
		return service.findAll();
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> details (@PathVariable Long id) {
		Optional<Item> opt = cbFactory.create("items").run(() -> {
			return service.findById(id);
		}, e -> {
			System.out.println(e.getMessage());
			logger.error(e.getMessage());
			
			Product product = new Product();
			product.setId(1L);
			product.setName("Camara Sony");
			product.setPrice(500.00);
			product.setCreateAt(LocalDate.now());
			Item item = new Item(product, 5);
			return Optional.ofNullable(item);
		});

		
//		Optional<Item> opt = service.findById(id);
		if (opt.isPresent()) {
			return ResponseEntity.ok(opt.get());
		}
		return ResponseEntity.status(404).body(Collections.singletonMap("message", "No existe el producto en el microservicio msvc-products"));
	}
}
