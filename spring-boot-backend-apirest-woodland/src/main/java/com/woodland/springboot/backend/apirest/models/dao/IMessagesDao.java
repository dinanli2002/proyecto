package com.woodland.springboot.backend.apirest.models.dao;

import org.springframework.data.repository.CrudRepository;

import com.woodland.springboot.backend.apirest.models.entity.Messages;



	public interface IMessagesDao extends CrudRepository<Messages, Long>{
		
}


