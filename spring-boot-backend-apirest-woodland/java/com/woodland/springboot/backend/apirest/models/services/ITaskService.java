package com.woodland.springboot.backend.apirest.models.services;

import java.util.List;

import com.woodland.springboot.backend.apirest.models.entity.Task;
import com.woodland.springboot.backend.apirest.models.entity.UserTasks;

public interface ITaskService {

    public List<Task> findAllTasks();

    public Task findTaskById(Long id);

    public Task saveTask(Task task);

    public void deleteTask(Long id);
    
    public List<Task> findTasksByUserIdKid(Long id);
    
    public List<Task> findTasksByUserTutorId(Long id);

	public Task createTask(Task task, int idTutor, int idKid);

	
	
	
	
	
	
}
