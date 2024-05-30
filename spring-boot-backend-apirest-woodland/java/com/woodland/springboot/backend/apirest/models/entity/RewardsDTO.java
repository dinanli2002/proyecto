package com.woodland.springboot.backend.apirest.models.entity;

import javax.persistence.Column;

public class RewardsDTO {

	
private String name;
private String description;
private int price;
private Long idKid;


public RewardsDTO(String name, String description, int price, Long idKid) {
	super();
	this.name = name;
	this.description = description;
	this.price = price;
	this.idKid = idKid;
}


public String getName() {
	return name;
}


public void setName(String name) {
	this.name = name;
}


public String getDescription() {
	return description;
}


public void setDescription(String description) {
	this.description = description;
}


public int getPrice() {
	return price;
}


public void setPrice(int price) {
	this.price = price;
}


public Long getIdKid() {
	return idKid;
}


public void setIdKid(Long idKid) {
	this.idKid = idKid;
}

    


	
}
