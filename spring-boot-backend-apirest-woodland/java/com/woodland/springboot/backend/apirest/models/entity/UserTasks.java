package com.woodland.springboot.backend.apirest.models.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="user_tasks")
public class UserTasks implements Serializable{

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id_task")
    private Long idTask;
    
    @ManyToOne
    @JoinColumn(name="id_user", nullable=false)
    private Usuario user;
    
    @ManyToOne
    @JoinColumn(name="id_tutor", nullable=false)
    private Usuario tutor;
    
    // Otros campos, métodos getters y setters

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public UserTasks(){
    	
    }
    
    public UserTasks(Long id_task, Usuario user, Usuario tutor) {
		super();
		this.idTask = id_task;
		this.user = user;
		this.tutor = tutor;
	}
    
    

	public Long getId_task() {
        return idTask;
    }

    public void setId_task(Long id_task) {
        this.idTask = id_task;
    }

    public Usuario getUser() {
        return user;
    }

    public void setUser(Usuario user) {
        this.user = user;
    }

    public Usuario getTutor() {
        return tutor;
    }

    public void setTutor(Usuario tutor) {
        this.tutor = tutor;
    }
}
