package com.woodland.springboot.backend.apirest.models.services;


	import java.util.List;

	import org.springframework.beans.factory.annotation.Autowired;
	import org.springframework.stereotype.Service;
	import org.springframework.transaction.annotation.Transactional;

import com.woodland.springboot.backend.apirest.models.dao.IMessagesDao;

import com.woodland.springboot.backend.apirest.models.entity.Messages;



@Service
public class MessageServiceImpl implements IMessageService{

	@Autowired
	private IMessagesDao messagesDao;
	
	
	@Override
	@Transactional(readOnly = true)
	public List<Messages> findAllMessages() {
		return (List<Messages>) messagesDao.findAll();
	}
	
	@Override
	@Transactional(readOnly = true)
	public Messages findMessageById(Long id) {
		return messagesDao.findById(id).orElse(null);
	}
	
	@Override
	@Transactional
	public Messages saveMessage(Messages mensaje){
		return messagesDao.save(mensaje);
	}
	
	@Override
	@Transactional
	public void deleteMessage(Long id) {
		messagesDao.deleteById(id);
	
		
	}
	
	
}

	