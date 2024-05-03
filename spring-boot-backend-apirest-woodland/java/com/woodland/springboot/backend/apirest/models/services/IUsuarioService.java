package com.woodland.springboot.backend.apirest.models.services;

import java.util.List;

import com.woodland.springboot.backend.apirest.models.entity.Usuario;

public interface IUsuarioService {

	public List<Usuario> findAll();
	
	public Usuario save(Usuario usuario);
	
	public Usuario findById(Long id);
	
	public void delete (Long id);
	
	 public Usuario findByUsername(String username);

	public List<String> findRolesById(Long id); 
	
	public List<Long> findKidsById(Long id);
	
	public Usuario createKid(Usuario kid,int idTutor);
	
	public Usuario createTutor(Usuario usuario);
	
	
	
}



