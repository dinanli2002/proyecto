package com.woodland.springboot.backend.apirest.models.dao;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import com.woodland.springboot.backend.apirest.models.entity.Task;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ITaskDao extends CrudRepository<Task, Long> {
    // Aquí puedes agregar métodos adicionales si es necesario



	@Query("SELECT t FROM Task t JOIN UserTasks ut ON t.id = ut.id_task WHERE ut.user.id = :userId")
	List<Task> findTasksByUserKidId(Long userId);

	
	@Query("SELECT t FROM Task t JOIN UserTasks ut ON t.id = ut.id_task WHERE ut.tutor.id = :tutorId")
	List<Task> findTasksByUserTutorId(Long tutorId);


	 @Transactional
	 @Modifying
	 @Query(value = "INSERT INTO user_tasks (id_tutor, id_user) VALUES (?1, ?2)", nativeQuery = true)
	void insertTaskKid(int idTutor, int kidId);


 
}
