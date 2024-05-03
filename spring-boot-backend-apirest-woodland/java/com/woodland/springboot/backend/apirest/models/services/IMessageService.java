package com.woodland.springboot.backend.apirest.models.services;

import java.util.List;

import com.woodland.springboot.backend.apirest.models.entity.Messages;


public interface IMessageService {

	public List<Messages> findAllMessages();
	
	public Messages saveMessage(Messages mensaje);
	
	public Messages findMessageById(Long id);
	
	public void deleteMessage (Long id);

	
}

