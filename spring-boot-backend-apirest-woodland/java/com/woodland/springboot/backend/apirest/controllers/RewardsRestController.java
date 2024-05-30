package com.woodland.springboot.backend.apirest.controllers;

import java.util.ArrayList;
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

import com.woodland.springboot.backend.apirest.models.entity.JwtPayload;
import com.woodland.springboot.backend.apirest.models.entity.Rewards;
import com.woodland.springboot.backend.apirest.models.entity.RewardsDTO;
import com.woodland.springboot.backend.apirest.models.entity.Usuario;

import com.woodland.springboot.backend.apirest.models.services.IRewardsService;

import com.woodland.springboot.backend.apirest.models.services.IUsuarioService;
import com.woodland.springboot.backend.apirest.models.services.JWTService;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;

/*@CrossOrigin(origins = {"http://localhost:4200"})*/
@RestController
@RequestMapping("/api")
public class RewardsRestController {

	@Autowired
	private IRewardsService rewardsService;

	@Autowired
	private IUsuarioService usuarioService;

	@Autowired
	private JWTService jwtService;
	
	
	

	@GetMapping("/rewards")
	public List<Rewards> index() {

		// Authentication authentication =
		// SecurityContextHolder.getContext().getAuthentication();

		return rewardsService.findAllRewards();
	}

	@GetMapping("/rewards/{id}")
	public ResponseEntity<?> show(@PathVariable Long id) {
		Rewards rewards = null;

		// Authentication authentication =
		// SecurityContextHolder.getContext().getAuthentication();

		// Imprimir los roles del usuario actual en la consola
		// System.out.println(authentication.getAuthorities());

		java.util.Map<String, Object> response = new HashMap<>();
		try {
			rewards = rewardsService.findRewardById(id);
		}

		catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar la consulta en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<java.util.Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		if (rewards == null) {
			response.put("mensaje", "El usuario ID:".concat(id.toString().concat("No existe en la base de datos")));
			return new ResponseEntity<java.util.Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Rewards>(rewards, HttpStatus.OK);
	}

	@PostMapping("/rewards/create")
	public ResponseEntity<?> create(@RequestBody RewardsDTO rewards) {
		Rewards rewardsNew = null;
		

		Rewards rewards2 = null;
		java.util.Map<String, Object> response = new HashMap<>();

	
	

		try {
			Rewards rewardsRequest = new Rewards();
			Usuario kid = usuarioService.findById(rewards.getIdKid());
			
			rewardsRequest.setId(null);
			rewardsRequest.setName(rewards.getName());
			rewardsRequest.setDescription(rewards.getDescription());
			rewardsRequest.setPrice(rewards.getPrice());
			rewardsRequest.setUrl_image(null);
			rewardsRequest.setUser(kid);
			
			rewards2=rewardsService.saveReward(rewardsRequest);
			



		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar el insert en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<java.util.Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("mensaje", "La tarea ha sido creado con éxito");
		response.put("rewards", rewards2);

		return new ResponseEntity<java.util.Map<String, Object>>(response, HttpStatus.CREATED);
	}

	@PutMapping("/update/rewards/{id}")

	public ResponseEntity<?> update(@RequestBody Rewards rewards, @PathVariable Long idReward) {
		Rewards rewardsActual = rewardsService.findRewardById(idReward);

		java.util.Map<String, Object> response = new HashMap<>();

		if (rewardsActual == null) {
			response.put("mensaje", "Error: no se pudo editar, la rewards ID: "
					.concat(idReward.toString().concat(" no existe en la base de datos")));
			return new ResponseEntity<java.util.Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		try {

			rewardsActual.setName(rewards.getName());
			rewardsActual.setDescription(rewards.getDescription());
			rewardsActual.setPrice(rewards.getPrice());

		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar al actualizar los datos en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<java.util.Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("mensaje", "La recompensa ha sido actualizada con éxito");
		response.put("rewards", rewardsActual);
		return new ResponseEntity<java.util.Map<String, Object>>(response, HttpStatus.CREATED);
	}
	
	
	

	@PostMapping("/rewards/verificate")
	public ResponseEntity<?> verificateRewards(@RequestBody Long idReward) {
		java.util.Map<String, Object> response = new HashMap<>();
		try {

			Rewards rewardsActual = rewardsService.findRewardById(idReward);
			

			if (rewardsActual == null) {
				response.put("mensaje", "Error: no se pudo encontrar, la tarea ID: "
						.concat(idReward.toString().concat(" no existe en la base de datos")));
				return new ResponseEntity<java.util.Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}

			
			int monedas = rewardsActual.getPrice();
			

			try {

				rewardsService.deleteReward(idReward);

				if (rewardsActual.getUser().getMonedas()>= monedas) {
					
					rewardsActual.getUser().setMonedas(rewardsActual.getUser().getMonedas()-monedas);	
				
				}
				else {
					response.put("mensaje", "Error: no tienes suficientes monedas");
					return new ResponseEntity<java.util.Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
				}
				

			

				usuarioService.save(rewardsActual.getUser());
				

			} catch (DataAccessException e) {
				response.put("mensaje", "Error al darle la recompensa");
				response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
				return new ResponseEntity<java.util.Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			response.put("mensaje", "La recompensa ha sido entregada con exito");
			return new ResponseEntity<java.util.Map<String, Object>>(response, HttpStatus.CREATED);

		} catch (DataAccessException e) {
			response.put("mensaje", "Error al darle la recompensa");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<java.util.Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			
		}

	}
	
	
	
	@DeleteMapping("/rewards/delete")
	public ResponseEntity<?> deleteRewards(@RequestBody Long idReward) {
		java.util.Map<String, Object> response = new HashMap<>();
		try {

			rewardsService.deleteReward(idReward);
			

			response.put("mensaje", "La recompensa ha sido eliminada con exito");
			return new ResponseEntity<java.util.Map<String, Object>>(response, HttpStatus.CREATED);

		} catch (DataAccessException e) {
			response.put("mensaje", "Error al eliminar la recompensa");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<java.util.Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			
		}

	}
	
	
	

	
	
	@GetMapping("/rewards/kid/{idKid}")
	public ResponseEntity<?> showRewardsByUserKidId(@PathVariable Long idKid) {
		List<Rewards> rewards;

	
		java.util.Map<String, Object> response = new HashMap<>();
		try {
			Usuario kid = usuarioService.findById(idKid);
			rewards = rewardsService.findRewardsByUserIdKid(kid);
		}

		catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar la consulta en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<java.util.Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		if (rewards == null) {
			response.put("mensaje", "No existen tareas para este usuario");
			return new ResponseEntity<java.util.Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<List<Rewards>>(rewards, HttpStatus.OK);
	}
	

	@GetMapping("/rewards/tutor")
	public ResponseEntity<?> showRewardsByUserTutorId() {
		ArrayList<Rewards>rewards = new ArrayList<Rewards>();
		OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) SecurityContextHolder.getContext()
				.getAuthentication().getDetails();
		String jwtToken = details.getTokenValue();

		// Decodificar el JWT utilizando JWTService
		JwtPayload decodedJwt;

		decodedJwt = jwtService.jwtDecoder(jwtToken);
		
		List<Integer>kidsId =decodedJwt.getKids();
		ArrayList<Usuario>kids= new ArrayList<Usuario>();
		
		for(Integer kid: kidsId) {
			Long kidId= kid.longValue();
			
			kids.add(usuarioService.findById(kidId));
			
		}
		
		

		java.util.Map<String, Object> response = new HashMap<>();
		try {
	
			for(Usuario kid: kids) {
				
				rewards.addAll(rewardsService.findRewardsByUserIdKid(kid));
				
			}
			
		}

		catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar la consulta en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<java.util.Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<List<Rewards>>(rewards, HttpStatus.OK);
		
		

	}
	
	
	

}