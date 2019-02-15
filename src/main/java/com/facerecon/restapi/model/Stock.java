package com.facerecon.restapi.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="stock")
public class Stock {
	
	public Stock() {
		
	}

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	Long id;
	
	String name;
	
	int quantity;
	
	int initialQuantity;


	//getters & setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	public int getInitialQuantity() {
		return initialQuantity;
	}

	public void setInitialQuantity(int initialQuantity) {
		this.initialQuantity = initialQuantity;
	}
	
	
}
