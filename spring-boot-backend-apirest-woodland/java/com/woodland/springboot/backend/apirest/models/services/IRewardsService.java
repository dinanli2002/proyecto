package com.woodland.springboot.backend.apirest.models.services;

import java.util.List;

import com.woodland.springboot.backend.apirest.models.entity.Rewards;
import com.woodland.springboot.backend.apirest.models.entity.Task;
import com.woodland.springboot.backend.apirest.models.entity.Usuario;

public interface IRewardsService {

	
	public List<Rewards> findAllRewards();

    public Rewards findRewardById(Long id);

    public Rewards saveReward(Rewards task);

    public void deleteReward(Long id);
    
    public List<Rewards> findRewardsByUserIdKid(Usuario usuario);
    
    public List<Rewards> findRewardsByUserTutorId(List<Usuario> kids);

	public Rewards createRewards(Rewards reward);
	
	
}
