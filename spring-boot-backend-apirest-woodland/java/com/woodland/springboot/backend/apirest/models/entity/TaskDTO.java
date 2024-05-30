package com.woodland.springboot.backend.apirest.models.entity;

public class TaskDTO {

	
private String nombre;
private String descripcion;
private int monedas;
private Long idKid;
public TaskDTO(String nombre, String descripcion, int monedas, Long idKid) {
	super();
	this.nombre = nombre;
	this.descripcion = descripcion;
	this.monedas = monedas;
	this.idKid = idKid;
}
public String getNombre() {
	return nombre;
}
public void setNombre(String nombre) {
	this.nombre = nombre;
}
public String getDescripcion() {
	return descripcion;
}
public void setDescripcion(String descripcion) {
	this.descripcion = descripcion;
}
public int getMonedas() {
	return monedas;
}
public void setMonedas(int monedas) {
	this.monedas = monedas;
}
public Long getIdKid() {
	return idKid;
}
public void setIdKid(Long idKid) {
	this.idKid = idKid;
}




}
