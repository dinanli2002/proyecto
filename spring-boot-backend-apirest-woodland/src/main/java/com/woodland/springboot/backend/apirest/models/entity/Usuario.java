package com.woodland.springboot.backend.apirest.models.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import javax.persistence.Table;

@Entity
@Table(name="usuarios")
public class Usuario implements Serializable{

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable=false, unique=true)
	private String username;

	@Column (nullable=false)
	private String password;
	
	@Column (nullable=false, unique=true)
	private String email;
	
	@Column (nullable=false)
	private int monedas;
	
	@Column (nullable=false)
	private boolean enabled;
	

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;



	public Long getId() {
		return id;
	}



	public void setId(Long id) {
		this.id = id;
	}



	public String getUsername() {
		return username;
	}



	public void setUsername(String username) {
		this.username = username;
	}



	public String getPassword() {
		return password;
	}



	public void setPassword(String password) {
		this.password = password;
	}



	public String getEmail() {
		return email;
	}



	public void setEmail(String email) {
		this.email = email;
	}



	public int getMonedas() {
		return monedas;
	}



	public void setMonedas(int monedas) {
		this.monedas = monedas;
	}
	
	

	public Boolean getEnabled() {
		return enabled;
	}



}
	
	
	



