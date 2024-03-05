package com.woodland.springboot.backend.apirest.controllers;

import java.util.HashMap;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;


import com.woodland.springboot.backend.apirest.models.entity.Messages;
import com.woodland.springboot.backend.apirest.models.entity.Usuario;
import com.woodland.springboot.backend.apirest.models.services.IMessageService;



@RestController
@RequestMapping("/api")
public class MessagesRestController {

	
	@Autowired
	private IMessageService mensajeService;
	
	
	@GetMapping("/mensaje")
	public List<Messages> indexMessages(){
		return mensajeService.findAllMessages();
	}
	
	
	@GetMapping("/mensaje/{id}")
	public ResponseEntity<?> show(@PathVariable Long id) {
	    Messages mensaje = null;
	    // No es necesario: result.hasErrors();
	    
	    java.util.Map<String, Object> response2 = new HashMap<>();
	    
	    try {
	        mensaje = mensajeService.findMessageById(id);
	    } catch (DataAccessException e) {
	        response2.put("mensaje", "Error al realizar la consulta en la base de datos");
	        response2.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
	        return new ResponseEntity<java.util.Map<String, Object>>(response2, HttpStatus.INTERNAL_SERVER_ERROR);
	    }

	    if (mensaje == null) {
	        response2.put("mensaje", "El mensaje con ID: ".concat(id.toString().concat(" no existe en la base de datos")));
	        return new ResponseEntity<java.util.Map<String, Object>>(response2, HttpStatus.NOT_FOUND);
	    }
	    return new ResponseEntity<Messages>(mensaje, HttpStatus.OK);
	}

	 
	
		
	}