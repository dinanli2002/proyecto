package com.woodland.springboot.backend.apirest.models.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.woodland.springboot.backend.apirest.models.entity.Rewards;
import com.woodland.springboot.backend.apirest.models.entity.Task;
import com.woodland.springboot.backend.apirest.models.entity.Usuario;

@Repository
public interface IRewardsDao extends JpaRepository<Rewards, Long>{

	@Query("SELECT r FROM Rewards r WHERE r.user = :user")
	List<Rewards> findRewardsByUserKidId(@Param("user") Usuario usuario);


	
}
