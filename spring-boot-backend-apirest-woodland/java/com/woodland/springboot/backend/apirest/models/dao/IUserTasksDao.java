package com.woodland.springboot.backend.apirest.models.dao;


import com.woodland.springboot.backend.apirest.models.entity.Relations;
import com.woodland.springboot.backend.apirest.models.entity.UserTasks;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserTasksDao extends CrudRepository<UserTasks, Long> {
    // Aquí puedes agregar métodos adicionales si es necesario

 
}
