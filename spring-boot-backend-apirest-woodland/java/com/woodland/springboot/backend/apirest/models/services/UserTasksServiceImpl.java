package com.woodland.springboot.backend.apirest.models.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.woodland.springboot.backend.apirest.models.dao.ITaskDao;
import com.woodland.springboot.backend.apirest.models.dao.IUserTasksDao;
import com.woodland.springboot.backend.apirest.models.entity.UserTasks;


@Service
public class UserTasksServiceImpl implements IUserTasksService{

	@Autowired
	IUserTasksDao userTasksDao;
	
	@Autowired
	ITaskDao tasksDao;

	@Override
	@Transactional
	public void giveTask(Long id) {
		
		userTasksDao.deleteById(id);
		
	}

	@Override
	public UserTasks findByid(Long id) {
		
		return userTasksDao.findById(id).orElse(null);
	}
	
	
	@Override
	public void save(int idTask, int idTutor, int kidId ) {
		
		tasksDao.insertTaskKid(idTask, idTutor, kidId);
	}

	@Override
	public void deleteTask(Long id, UserTasks userTasks) {
		
		userTasksDao.delete(userTasks);
		tasksDao.deleteById(id);
		
	}
	

}
