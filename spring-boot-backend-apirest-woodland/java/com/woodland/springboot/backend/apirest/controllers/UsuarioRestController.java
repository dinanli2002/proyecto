package com.woodland.springboot.backend.apirest.controllers;

import java.util.HashMap;
import java.util.List;

import org.hibernate.mapping.Map;
import org.hibernate.service.spi.ServiceException;
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
import com.woodland.springboot.backend.apirest.auth.*;
import com.woodland.springboot.backend.apirest.models.entity.JwtPayload;
import com.woodland.springboot.backend.apirest.models.entity.Usuario;
import com.woodland.springboot.backend.apirest.models.entity.UsuarioDTO;
import com.woodland.springboot.backend.apirest.models.services.IUsuarioService;
import com.woodland.springboot.backend.apirest.models.services.JWTService;
import com.woodland.springboot.backend.apirest.models.services.JwtPayloadDeserializer;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
/*@CrossOrigin(origins = {"http://localhost:4200"})*/
@RestController
@RequestMapping("/api")
public class UsuarioRestController {

	@Autowired
	private IUsuarioService usuarioService;
	
	@Autowired
	private JwtPayloadDeserializer jwtPayloadDeserializer;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private JWTService jwtservice;
	
	@GetMapping("/usuarios")
	public List<Usuario> index(){
		

		// Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		 
		 
		return usuarioService.findAll();
	}
	
	
	//Pillar informacion de un niño que tiene que ser si o si hijo del que hace la peticion
	
	@GetMapping("/usuario/{id}")
	public ResponseEntity<?> showKidById(@PathVariable Long id) {
		Usuario usuario =null ;
		
		 OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) SecurityContextHolder.getContext().getAuthentication().getDetails();
	        String jwtToken = details.getTokenValue();

	        // Decodificar el JWT utilizando JWTService
	        JwtPayload decodedJwt;
		
			decodedJwt = jwtservice.jwtDecoder(jwtToken);
		 
		    
		    
		java.util.Map<String, Object> response = new HashMap<>();
		
		if(!decodedJwt.getKids().contains(id.intValue())) {
			response.put("mensaje","Solo puedes consultar información de tu hijo");
			return new ResponseEntity<java.util.Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		try {
			
			usuario = usuarioService.findById(id);} 
		
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

	
	
	
	@PostMapping("/create/userTutor")
	public ResponseEntity<?> createTutor(@RequestBody Usuario usuario) {
	    Usuario usuarioNew = null;
	    usuario.setEnabled(true);
	    usuario.setMonedas(0L);
	    usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
	    java.util.Map<String, Object> response = new HashMap<>();
	    try {
	        usuarioNew = usuarioService.createTutor(usuario);
	        response.put("mensaje", "El usuario ha sido creado con éxito");
	        response.put("usuario", usuarioNew);
	        return new ResponseEntity<>(response, HttpStatus.CREATED);
	    } catch (ServiceException e) {
	        response.put("mensaje", "Error al crear el usuario");
	        response.put("error", e.getMessage());
	        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}

	
	
	@PostMapping("/create/userkid")
	public ResponseEntity<?> createKid(@RequestBody Usuario usuario) {
	    Usuario usuarioNew = null;
	    usuario.setEnabled(true);
	    usuario.setMonedas(0L);
	    usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
	    java.util.Map<String, Object> response = new HashMap<>();
	    OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) SecurityContextHolder.getContext().getAuthentication().getDetails();
	    String jwtToken = details.getTokenValue();

	    // Decodificar el JWT utilizando JWTService
	    JwtPayload decodedJwt;

	    try {
	        decodedJwt = jwtservice.jwtDecoder(jwtToken);
	        int idTutor = 0;
	        try {
	            idTutor = decodedJwt.getId();
	        } catch (NumberFormatException e) {
	            
	        }

	        // Crear el niño (kid) utilizando el servicio
	        usuarioNew = usuarioService.createKid(usuario, idTutor);

	    } catch (ServiceException e) {
	        response.put("mensaje", "Error al crear el usuario kid");
	        response.put("error", e.getMessage());
	        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	    }

	    response.put("mensaje", "El usuario ha sido creado con éxito");
	    response.put("usuario", usuarioNew);
	    return new ResponseEntity<>(response, HttpStatus.CREATED);
	}


	@PutMapping("/update/usuario")
	public ResponseEntity<?> update(@RequestBody UsuarioDTO usuario) {
		 
		java.util.Map<String, Object> response = new HashMap<>();
		 OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) SecurityContextHolder.getContext().getAuthentication().getDetails();
		    String jwtToken = details.getTokenValue();

		    // Decodificar el JWT utilizando JWTService
		    JwtPayload decodedJwt;

		     decodedJwt = jwtservice.jwtDecoder(jwtToken);
		     
		       
		     int idJwt = decodedJwt.getId();
		       

		   
		
		
		Usuario usuarioActual = usuarioService.findById(Long.valueOf(idJwt));
	  

	    if (usuarioActual == null) {
	        response.put("mensaje", "Error: no se pudo editar, el usuario ID: " + idJwt+ " no existe en la base de datos");
	        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	    }

	    try {
	        usuarioActual.setUsername(usuario.getUsername());
	        usuarioActual.setEmail(usuario.getEmail());
	        usuarioActual.setPassword(usuario.getPassword());
	        
	        usuarioActual.setPassword(passwordEncoder.encode(usuario.getPassword()));
	        
	        usuarioService.save(usuarioActual);
			
	        
	    } catch (IllegalArgumentException e) {
	        response.put("error", e.getMessage());
	        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	    } catch (DataAccessException e) {
	        response.put("error", "Error al actualizar los datos en la base de datos");
	        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	    }

	    response.put("mensaje", "El usuario ha sido actualizado con éxito");
	    response.put("usuario", usuarioActual);
	    return new ResponseEntity<>(response, HttpStatus.OK);
	}
	 
	
	@DeleteMapping("/delete/usuario/{id}")
	
		public ResponseEntity<?> delete(@PathVariable Long id) {
			java.util.Map<String, Object> response = new HashMap<>();
		 	OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) SecurityContextHolder.getContext().getAuthentication().getDetails();
		    String jwtToken = details.getTokenValue();

		    // Decodificar el JWT utilizando JWTService
		    JwtPayload decodedJwt;

		     decodedJwt = jwtservice.jwtDecoder(jwtToken);
		     
		       
		     int idJwt = decodedJwt.getId();
		       

		     if(idJwt!=id && !decodedJwt.getKids().contains(id.intValue())) {
		    	 response.put("mensaje", "Error: no se pudo editar, no tienes acceso al usuario con id: "+ id);
			     return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		     }
		
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