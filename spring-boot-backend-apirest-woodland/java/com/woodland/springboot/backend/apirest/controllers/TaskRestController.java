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

import com.woodland.springboot.backend.apirest.models.entity.JwtPayload;
import com.woodland.springboot.backend.apirest.models.entity.Task;
import com.woodland.springboot.backend.apirest.models.entity.TaskDTO;
import com.woodland.springboot.backend.apirest.models.entity.UserTasks;
import com.woodland.springboot.backend.apirest.models.entity.Usuario;

import com.woodland.springboot.backend.apirest.models.services.ITaskService;
import com.woodland.springboot.backend.apirest.models.services.IUserTasksService;
import com.woodland.springboot.backend.apirest.models.services.IUsuarioService;
import com.woodland.springboot.backend.apirest.models.services.JWTService;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;

/*@CrossOrigin(origins = {"http://localhost:4200"})*/
@RestController
@RequestMapping("/api")
public class TaskRestController {

	@Autowired
	private ITaskService taskService;

	@Autowired
	private IUserTasksService userTasksService;

	@Autowired
	private IUsuarioService usuarioService;

	@Autowired
	private JWTService jwtService;

	@GetMapping("/tasks")
	public List<Task> index() {

		// Authentication authentication =
		// SecurityContextHolder.getContext().getAuthentication();

		return taskService.findAllTasks();
	}

	@GetMapping("/task/{id}")
	public ResponseEntity<?> show(@PathVariable Long id) {
		Task task = null;

		// Authentication authentication =
		// SecurityContextHolder.getContext().getAuthentication();

		// Imprimir los roles del usuario actual en la consola
		// System.out.println(authentication.getAuthorities());

		java.util.Map<String, Object> response = new HashMap<>();
		try {
			task = taskService.findTaskById(id);
		}

		catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar la consulta en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<java.util.Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		if (task == null) {
			response.put("mensaje", "El usuario ID:".concat(id.toString().concat("No existe en la base de datos")));
			return new ResponseEntity<java.util.Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Task>(task, HttpStatus.OK);
	}

	@PostMapping("/task/create")
	public ResponseEntity<?> create(@RequestBody TaskDTO task) {
		Task taskNew = null;
		UserTasks userTasks = null;
		UserTasks userTasksNew = null;

		Task task2 = null;
		java.util.Map<String, Object> response = new HashMap<>();

		OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) SecurityContextHolder.getContext()
				.getAuthentication().getDetails();
		String jwtToken = details.getTokenValue();

		// Decodificar el JWT utilizando JWTService
		JwtPayload decodedJwt;

		decodedJwt = jwtService.jwtDecoder(jwtToken);

		try {
			Task taskRequest = new Task();
			taskRequest.setId(null);
			taskRequest.setDescripcion(task.getDescripcion());
			taskRequest.setMonedas(task.getMonedas());
			taskRequest.setNombre(task.getNombre());

			Usuario kid = usuarioService.findById(task.getIdKid());

			int idTutor = decodedJwt.getId();

			task2 = taskService.createTask(taskRequest, idTutor, kid.getId().intValue());

		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar el insert en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<java.util.Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("mensaje", "La tarea ha sido creado con éxito");
		response.put("task", task2);

		return new ResponseEntity<java.util.Map<String, Object>>(response, HttpStatus.CREATED);
	}

	@PutMapping("/task/update/{id}")

	public ResponseEntity<?> update(@RequestBody Task task, @PathVariable Long id) {
		Task taskActual = taskService.findTaskById(id);

		java.util.Map<String, Object> response = new HashMap<>();

		if (taskActual == null) {
			response.put("mensaje", "Error: no se pudo editar, la task ID: "
					.concat(id.toString().concat(" no existe en la base de datos")));
			return new ResponseEntity<java.util.Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		try {

			taskActual.setNombre(task.getNombre());
			taskActual.setDescripcion(task.getDescripcion());
			taskActual.setMonedas(task.getMonedas());
			taskService.saveTask(taskActual);

		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar al actualizar los datos en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<java.util.Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("mensaje", "La tarea ha sido actualizada con éxito");
		response.put("task", taskActual);
		return new ResponseEntity<java.util.Map<String, Object>>(response, HttpStatus.CREATED);
	}

	
	
	@PostMapping("/task/verificate")
	public ResponseEntity<?> verificateTask(@RequestBody Long id) {
		java.util.Map<String, Object> response = new HashMap<>();
		try {

			Task taskActual = taskService.findTaskById(id);
			
			UserTasks userTasksActual = userTasksService.findByid(id);

			if (taskActual == null || userTasksActual == null) {
				response.put("mensaje", "Error: no se pudo encontrar, la tarea ID: "
						.concat(id.toString().concat(" no existe en la base de datos")));
				return new ResponseEntity<java.util.Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}

			Usuario userKid = userTasksActual.getUser();
			int monedas = taskActual.getMonedas();

			try {

				taskService.deleteTask(id);

				userTasksService.giveTask(id);
				
				

				Long monedasLong = (long) monedas;

				userKid.setMonedas(userKid.getMonedas() + monedasLong);

				usuarioService.save(userKid);

			} catch (DataAccessException e) {
				response.put("mensaje", "Error al darle la recompensa");
				response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
				return new ResponseEntity<java.util.Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			response.put("mensaje", "La tarea ha sido completada con exito");
			return new ResponseEntity<java.util.Map<String, Object>>(response, HttpStatus.CREATED);

		} catch (DataAccessException e) {
			response.put("mensaje", "Error al darle la recompensa");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<java.util.Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	
	
	

	@DeleteMapping("/task/delete/{idTask}")
	public ResponseEntity<?> deleteTask(@PathVariable Long idTask) {
		java.util.Map<String, Object> response = new HashMap<>();
		try {

			Task taskActual = taskService.findTaskById(idTask);
			UserTasks userTasksActual = userTasksService.findByid(idTask);

			if (taskActual == null || userTasksActual == null) {
				response.put("mensaje", "Error: no se pudo encontrar, la tarea ID: "
						.concat(idTask.toString().concat(" no existe en la base de datos")));
				return new ResponseEntity<java.util.Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}

			

			try {

				userTasksService.deleteTask(idTask, userTasksActual);
				
				

			} 
			catch (DataAccessException e) {
				response.put("mensaje", "Error al eliminar 1");
				response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
				return new ResponseEntity<java.util.Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			response.put("mensaje", "La tarea ha sido eliminada con exito");
			return new ResponseEntity<java.util.Map<String, Object>>(response, HttpStatus.CREATED);

		} catch (DataAccessException e) {
			response.put("mensaje", "Error al eliminar");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<java.util.Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	
	
	
	
	
	@GetMapping("/task/kid/{id}")
	public ResponseEntity<?> showTaskByUserKidId(@PathVariable Long id) {
		List<Task> tasks;

		// Authentication authentication =
		// SecurityContextHolder.getContext().getAuthentication();

		// Imprimir los roles del usuario actual en la consola
		// System.out.println(authentication.getAuthorities());

		java.util.Map<String, Object> response = new HashMap<>();
		try {
			tasks = taskService.findTasksByUserIdKid(id);
		}

		catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar la consulta en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<java.util.Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		if (tasks == null) {
			response.put("mensaje", "El usuario ID:".concat(id.toString().concat("No existe en la base de datos")));
			return new ResponseEntity<java.util.Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<List<Task>>(tasks, HttpStatus.OK);
	}

	@GetMapping("/task/tutor")
	public ResponseEntity<?> showTaskByUserTutorId() {
		List<Task> tasks;

		OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) SecurityContextHolder.getContext()
				.getAuthentication().getDetails();
		String jwtToken = details.getTokenValue();

		// Decodificar el JWT utilizando JWTService
		JwtPayload decodedJwt;

		decodedJwt = jwtService.jwtDecoder(jwtToken);

		java.util.Map<String, Object> response = new HashMap<>();
		try {
			Long id = (long) decodedJwt.getId();
			tasks = taskService.findTasksByUserTutorId(id);
		}

		catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar la consulta en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<java.util.Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		if (tasks == null) {
			response.put("mensaje", "La tarea ese id:");
			return new ResponseEntity<java.util.Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<List<Task>>(tasks, HttpStatus.OK);

	}
	
	
	

}