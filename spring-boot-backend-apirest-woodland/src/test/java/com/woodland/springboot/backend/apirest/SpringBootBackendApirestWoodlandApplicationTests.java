package com.woodland.springboot.backend.apirest;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.woodland.springboot.backend.apirest.models.dao.IUsuarioDao;
 
@SpringBootTest
class SpringBootBackendApirestWoodlandApplicationTests {

    @Autowired
    private IUsuarioDao usuarioDao; // Cambiado de usuariodao a usuarioDao
    
    @Test
    void testCreateUserRole() {
        usuarioDao.insertUserRole(2, 29);
        // Verificar si la inserción se realizó correctamente
        // Esto puede incluir consultas adicionales para confirmar que los datos están en la base de datos
    }
}
