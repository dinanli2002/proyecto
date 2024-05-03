package com.woodland.springboot.backend.apirest.models.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.woodland.springboot.backend.apirest.models.entity.Usuario;

public interface IUsuarioDao extends CrudRepository<Usuario, Long> {

    @Query("select u from Usuario u where u.username=?1")
    public Usuario findByUsername(String username);
    
    @Query("select u from Usuario u where u.email=?1")
    public Usuario findByMail(String username);
    
    

    @Query("SELECT ur.rol.nombre FROM UserRoles ur WHERE ur.user.id = ?1")
    public List<String> findRoleNamesByUserId(Long userId);

    @Query("SELECT k.user.id FROM Relations k WHERE k.tutor.id = ?1")
    public List<Long> findKidsByUserId(Long tutorId);
    
    
  
    @Transactional
    @Modifying
    @Query(value = "INSERT INTO user_roles (id_rol, id_user) VALUES (?1, ?2)", nativeQuery = true)
    void insertUserRole(int roleId, int userId);


    //Modifying indica que cambiara datos de la base de datos
    @Modifying
    @Query(value = "INSERT INTO Relations (id_tutor, id_user) VALUES (?1, ?2)", nativeQuery = true)
    void createRelationByTutorId(int idTutor, int idKid);
    
    

   
    

}
