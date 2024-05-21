package com.woodland.springboot.backend.apirest.models.services;

import com.woodland.springboot.backend.apirest.models.entity.UserTasks;

public interface IUserTasksService {

	public UserTasks findByid (Long id);
	
	public void giveTask(Long id);

	

	public void save(int idTask, int idTutor, int kidId);
	
	
}
