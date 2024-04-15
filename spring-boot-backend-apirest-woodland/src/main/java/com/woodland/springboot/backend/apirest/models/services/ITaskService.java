package com.woodland.springboot.backend.apirest.models.services;

import java.util.List;

import com.woodland.springboot.backend.apirest.models.entity.Task;

public interface ITaskService {

    public List<Task> findAllTasks();

    public Task findTaskById(Long id);

    public Task saveTask(Task task);

    public void deleteTask(Long id);
}
