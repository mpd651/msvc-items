package com.mario.springcloud.msvc.items.services;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;

import com.mario.springcloud.msvc.items.models.Item;
import com.mario.springcloud.msvc.items.models.Product;

@Service
//@Primary
public class ItemServiceWebClient implements ItemService {

	@Autowired
	private WebClient.Builder webClient;
	
	@Override
	public List<Item> findAll() {
		return webClient
			.build()
			.get()
			.accept(MediaType.APPLICATION_JSON)
			.retrieve()
			.bodyToFlux(Product.class)
			.map(p -> new Item(p, new Random().nextInt(10)+1))
			.collectList()
			.block();
	}

	@Override
	public Optional<Item> findById(Long id) {
		try {
			return Optional.ofNullable(webClient
					.build()
					.get()
					.uri("{id}", id)
					.accept(MediaType.APPLICATION_JSON)
					.retrieve()
					.bodyToMono(Product.class)
					.map(p -> new Item(p, new Random().nextInt(10)+1))
					.block());
		} catch (WebClientException e) {
			return Optional.empty();
		}

	}

}
