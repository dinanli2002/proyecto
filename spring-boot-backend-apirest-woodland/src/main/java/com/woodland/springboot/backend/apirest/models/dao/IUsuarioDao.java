package com.woodland.springboot.backend.apirest.models.dao;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import com.woodland.springboot.backend.apirest.models.entity.Usuario;

public interface IUsuarioDao extends CrudRepository<Usuario, Long> {

    @Query("select u from Usuario u where u.username=?1")
    public Usuario findByUsername(String username);

    @Query("SELECT ur.rol.nombre FROM UserRoles ur WHERE ur.user.id = ?1")
    public List<String> findRoleNamesByUserId(Long userId);

    @Query("SELECT k.user.id FROM Relations k WHERE k.tutor.id = ?1")
    public List<Long> findKidsByUserId(Long tutorId);

}
