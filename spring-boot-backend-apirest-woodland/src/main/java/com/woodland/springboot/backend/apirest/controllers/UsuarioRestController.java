package com.woodland.springboot.backend.apirest.controllers;

import java.util.HashMap;
import java.util.List;

import org.hibernate.mapping.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.woodland.springboot.backend.apirest.models.entity.Usuario;
import com.woodland.springboot.backend.apirest.models.services.IUsuarioService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
/*@CrossOrigin(origins = {"http://localhost:4200"})*/
@RestController
@RequestMapping("/api")
public class UsuarioRestController {

	@Autowired
	private IUsuarioService usuarioService;
	
	@GetMapping("/usuarios")
	public List<Usuario> index(){
		
		System.out.println("hola");
		 Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		    // Imprimir los roles del usuario actual en la consola
		    System.out.println("Roles del usuario: " + authentication.getAuthorities());
		return usuarioService.findAll();
	}
	
	
	
	
	@GetMapping("/usuario/{id}")
	public ResponseEntity<?> show(@PathVariable Long id) {
		Usuario usuario =null ;
		
		
		System.out.println("hola");
		 Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		 
		    // Imprimir los roles del usuario actual en la consola
		    System.out.println(authentication.getAuthorities());
		    System.out.println("adios");
		    
		java.util.Map<String, Object> response = new HashMap<>();
		try {usuario = usuarioService.findById(id);} 
		
		catch(DataAccessException e) {
			response.put("mensaje","Error al realizar la consulta en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<java.util.Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		
		if(usuario == null) {
			response.put("mensaje","El usuario ID:".concat(id.toString().concat("No existe en la base de datos")));
			return new ResponseEntity<java.util.Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Usuario>(usuario, HttpStatus.OK);
	}
	 
	
	@PostMapping("/create/usuario")
	
	public ResponseEntity<?> create(@RequestBody Usuario usuario) {
		Usuario usuarioNew  =null;
		java.util.Map<String, Object> response = new HashMap<>();
		try {
			usuarioNew= usuarioService.save(usuario);
		}
		catch(DataAccessException e){
			response.put("mensaje","Error al realizar el insert en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<java.util.Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("mensaje", "El usuario ha sido creado con éxito");
		response.put("usuario", usuarioNew);
		return new ResponseEntity<java.util.Map<String, Object>>(response, HttpStatus.CREATED);
	}
	
	@PutMapping("/update/usuario/{id}")

	public ResponseEntity<?> update(@RequestBody Usuario usuario, @PathVariable Long id) {
		Usuario usuarioActual= usuarioService.findById(id);
		
		
		java.util.Map<String, Object> response = new HashMap<>();
		
		
		if(usuarioActual ==null) {
			response.put("mensaje", "Error: no se pudo editar, el usuario ID: ".concat(id.toString().concat(" no existe en la base de datos")));
			return new ResponseEntity<java.util.Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);	
		}
		
		
		try {
				
			usuarioActual.setUsername(usuario.getUsername());
			usuarioActual.setEmail(usuario.getEmail());
			
			
			}
		catch(DataAccessException e) {
			response.put("mensaje","Error al realizar al actualizar los datos en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<java.util.Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);	
		}
		response.put("mensaje", "El usuario ha sido actualizado con éxito");
		response.put("usuario", usuarioActual);
		return new ResponseEntity<java.util.Map<String, Object>>(response, HttpStatus.CREATED);
	}
	 
	
	@DeleteMapping("/delete/usuario/{id}")
	
		public ResponseEntity<?> delete(@PathVariable Long id) {
		java.util.Map<String, Object> response = new HashMap<>();
		
		Usuario usuarioActual= usuarioService.findById(id);
		if(usuarioActual ==null) {
			response.put("mensaje", "Error: no se pudo eliminar, el usuario ID: ".concat(id.toString().concat(" no existe en la base de datos")));
			return new ResponseEntity<java.util.Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);	
		}
		
		
		try {
			usuarioService.delete(id);
		}
		catch(DataAccessException e) {
			response.put("mensaje","Error al eliminar los datos en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<java.util.Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);	
		}
		response.put("mensaje","El usuario ha sido eliminado con exito");
		return new ResponseEntity<java.util.Map<String, Object>>(response, HttpStatus.CREATED);
												
		}
	
	
	
	
	@PostMapping("/usuarios/login")
	public ResponseEntity<?> login(@RequestParam String username,@RequestParam String password) {
	    java.util.Map<String, Object> response = new HashMap<>();
	    
	    System.out.println(username+ password);
	    try {
	    	
	        Usuario usuario = usuarioService.findByUsername(username);
	        if(usuario == null) {
	            response.put("mensaje", "Usuario no encontrado");
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
	        }
	        String passwordUser = usuario.getPassword();
	        if(password.equals(passwordUser)) {
	            // La contraseña es correcta, devolvemos el usuario
	            return ResponseEntity.ok(usuario);
	        } else {
	            response.put("mensaje", "Contraseña incorrecta");
	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
	        }
	    } catch(DataAccessException e) {
	        response.put("mensaje", "Error con este usuario");
	        response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	    }
	}

	 
	
	
	

		
	}