package com.woodland.springboot.backend.apirest.models.services;

import java.util.List;
import java.util.Optional;

import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.woodland.springboot.backend.apirest.models.dao.ITaskDao;
import com.woodland.springboot.backend.apirest.models.entity.Task;
import com.woodland.springboot.backend.apirest.models.entity.Usuario;

@Service
public class TaskServiceImpl implements ITaskService {

    @Autowired
    private ITaskDao taskDao;

    @Override
    @Transactional(readOnly = true)
    public List<Task> findAllTasks() {
        return (List<Task>) taskDao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Task findTaskById(Long id) {
        return taskDao.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public Task saveTask(Task task) {
    	
    	
        return taskDao.save(task);
    }

    @Override
    @Transactional
    public void deleteTask(Long id) {
        taskDao.deleteById(id);
    }

	
	 @Override
	 @Transactional
	 public List<Task> findTasksByUserIdKid(Long id) {
		 
		 return taskDao.findTasksByUserKidId(id);
		 
	 }
	 
	 @Override
	 @Transactional
	 public List<Task> findTasksByUserTutorId(Long id) {
	 	 
	 	 return taskDao.findTasksByUserTutorId(id); 
	 	 
	 }

	@Override
	public Task createTask(Task task, int idTutor, int idKid) {
		
		
		try {
	        // Verificar si el nombre de usuario ya existe
			
	        

	        // Guarda el usuario en la base de datos
	        Task newTask = taskDao.save(task);

	        // Inserta el rol correspondiente al usuario
	        
	        taskDao.insertTaskKid(newTask.getId().intValue(),idTutor, idKid);
	       

	        // Crea la relación entre el tutor y el usuario kid
	      

	        return newTask;

	    } catch (DataAccessException e) {
	        // Captura excepciones específicas de acceso a datos, como problemas de conexión, errores SQL, etc.
	        throw new ServiceException("Error al acceder a la base de datos al crear la tarea", e);
	    } catch (ServiceException e) {
	        // Captura excepciones específicas del servicio, como nombre de usuario o correo electrónico ya en uso
	        throw e;
	    } catch (Exception e) {
	        // Captura otras excepciones no controladas
	        throw new ServiceException("Error al crear la tarea", e);
	    }
	}

	

	 
		/*
		 * @Override
		 * 
		 * @Transactional public Task createTask(Task task, int idKid) { try { //
		 * Verificar si el nombre de usuario ya existe
		 * 
		 * 
		 * 
		 * 
		 * // Guarda el usuario en la base de datos Task newTask = taskDao.save(task);
		 * 
		 * // Inserta el rol correspondiente al usuario taskDao.insertTaskKid(idKid,
		 * idKid);;
		 * 
		 * // Crea la relación entre el tutor y el usuario kid
		 * usuarioDao.createRelationByTutorId(idTutor, kid.getId().intValue());
		 * 
		 * return newKid;
		 * 
		 * } catch (DataAccessException e) { // Captura excepciones específicas de
		 * acceso a datos, como problemas de conexión, errores SQL, etc. throw new
		 * ServiceException("Error al acceder a la base de datos al crear el usuario kid"
		 * , e); } catch (ServiceException e) { // Captura excepciones específicas del
		 * servicio, como nombre de usuario o correo electrónico ya en uso throw e; }
		 * catch (Exception e) { // Captura otras excepciones no controladas throw new
		 * ServiceException("Error al crear el usuario kid", e); } }
		 */




}

