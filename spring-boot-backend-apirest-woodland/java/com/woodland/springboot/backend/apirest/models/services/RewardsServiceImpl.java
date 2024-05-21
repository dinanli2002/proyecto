package com.woodland.springboot.backend.apirest.models.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.woodland.springboot.backend.apirest.models.dao.IRewardsDao;
import com.woodland.springboot.backend.apirest.models.entity.Rewards;
import com.woodland.springboot.backend.apirest.models.entity.Usuario;

@Service
public class RewardsServiceImpl implements IRewardsService{

	
	@Autowired
	IRewardsDao rewardsDao;
	
	
	
	@Override
	@Transactional
	public List<Rewards> findAllRewards() {
		// TODO Auto-generated method stub
	
		return rewardsDao.findAll();
	}

	@Override
	@Transactional
	public Rewards findRewardById(Long id) {
		// TODO Auto-generated method stub
		return rewardsDao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public Rewards saveReward(Rewards reward) {
		// TODO Auto-generated method stub
		return rewardsDao.save(reward);
	}

	@Override
	@Transactional
	public void deleteReward(Long id) {
		rewardsDao.deleteById(id);
		
	}

	@Override
	@Transactional
	public List<Rewards> findRewardsByUserIdKid(Usuario kid) {
		
		return rewardsDao.findRewardsByUserKidId(kid);
	}

	@Override
	@Transactional
	public List<Rewards> findRewardsByUserTutorId(List<Usuario> kids) {
		
		ArrayList<Rewards> rewards = new ArrayList<Rewards>();
		
		for (Usuario kid : kids) {
			rewards.addAll(rewardsDao.findRewardsByUserKidId(kid));
			
		}
		// TODO Auto-generated method stub
		return rewards;
	}
	
	

	@Override
	@Transactional
	public Rewards createRewards(Rewards reward) {
		
		
		return rewardsDao.save(reward);
	}
	
	
	
	
	

	

}
