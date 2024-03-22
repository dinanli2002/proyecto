package com.woodland.springboot.backend.apirest.models.dao;
import java.util.List;
import java.util.Optional;

import com.woodland.springboot.backend.apirest.models.entity.Task;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ITaskDao extends JpaRepository<Task, Long> {
    // Aquí puedes agregar métodos adicionales si es necesario



    // Método para buscar todas las tareas
    List<Task> findAll();

    // Método para buscar una tarea por su id
   Optional<Task> findById(Long id);

    // Método para guardar una tarea
    Task save(Task task);

 
}
