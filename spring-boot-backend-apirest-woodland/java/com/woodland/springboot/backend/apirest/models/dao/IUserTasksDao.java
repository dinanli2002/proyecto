package com.woodland.springboot.backend.apirest.models.dao;


import com.woodland.springboot.backend.apirest.models.entity.Relations;
import com.woodland.springboot.backend.apirest.models.entity.UserTasks;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserTasksDao extends JpaRepository<UserTasks, Long> {
    // Aquí puedes agregar métodos adicionales si es necesario

	@Modifying
	@Query("DELETE FROM UserTasks WHERE idTask =:task")
	public void deleteByTaskId(@Param("task") UserTasks task);
	
 
}
