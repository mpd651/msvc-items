package com.mario.springcloud.msvc.items.controllers;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.mario.springcloud.msvc.items.models.Item;
import com.mario.springcloud.msvc.items.services.ItemService;

@RestController
public class ItemController {

	@Autowired
	@Qualifier("itemServiceWebClient")
	private ItemService service;
	
	@GetMapping
	public List<Item> list(){
		return service.findAll();
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> details (@PathVariable Long id) {
		Optional<Item> opt = service.findById(id);
		if (opt.isPresent()) {
			return ResponseEntity.ok(opt.get());
		}
		return ResponseEntity.status(404).body(Collections.singletonMap("message", "No existe el producto en el microservicio msvc-products"));
	}
}
