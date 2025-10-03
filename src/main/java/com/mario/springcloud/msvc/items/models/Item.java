package com.mario.springcloud.msvc.items.models;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Item {

	private Product product;
	private int quantity;
	private double total;
	
	public Item(Product product, int quantity) {
		super();
		this.product = product;
		this.quantity = quantity;
		this.total = this.getTotal();
	}
	
    public Double getTotal() {
        return product.getPrice() * quantity;
    }

}
