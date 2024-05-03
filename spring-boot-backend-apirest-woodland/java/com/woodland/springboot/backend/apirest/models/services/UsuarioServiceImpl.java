package com.woodland.springboot.backend.apirest.models.services;


	import java.util.List;

	import java.util.List;
	import java.util.stream.Collectors;

import org.hibernate.service.spi.ServiceException;
import org.slf4j.Logger;
	import org.slf4j.LoggerFactory;
	import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.GrantedAuthority;
	import org.springframework.security.core.authority.SimpleGrantedAuthority;
	import org.springframework.security.core.userdetails.User;
	import org.springframework.security.core.userdetails.UserDetails;
	import org.springframework.security.core.userdetails.UserDetailsService;
	import org.springframework.security.core.userdetails.UsernameNotFoundException;
	import org.springframework.stereotype.Service;
	import org.springframework.transaction.annotation.Transactional;

	import com.woodland.springboot.backend.apirest.models.dao.IUsuarioDao;
	import com.woodland.springboot.backend.apirest.models.entity.Usuario;

	@Service
	public class UsuarioServiceImpl implements IUsuarioService, UserDetailsService{

		
		private Logger logger = LoggerFactory.getLogger(UsuarioServiceImpl.class);

		@Autowired
		private IUsuarioDao usuarioDao;
		@Override
		@Transactional(readOnly = true)
		public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
			Usuario usuario = usuarioDao.findByUsername(username);
			
			if (usuario == null) {
				logger.error("Error en el login: no existe el usuario '"+username+"'en el sistema!");
				throw new UsernameNotFoundException("Error en el login: no existe el usuario '"+username+"'en el sistema!");
			}
			//es para convertir una lista de tipo Usuario a una lista de tipo grantedAuthoritya
			 List<String> roles = usuarioDao.findRoleNamesByUserId(usuario.getId());

		        List<GrantedAuthority> authorities = roles.stream()
		                .map(SimpleGrantedAuthority::new)
		                .peek(authority -> logger.info("Rolesprueba: " + authority.getAuthority()))
		                .collect(Collectors.toList());

		        return new User(usuario.getUsername(), usuario.getPassword(), usuario.getEnabled(), true, true, true, authorities);
		}
		
		
		
		
		
		@Override
		@Transactional
		public Usuario createKid(Usuario kid, int idTutor) {
		    try {
		        // Verificar si el nombre de usuario ya existe
		        Usuario existingUserByUsername = usuarioDao.findByUsername(kid.getUsername());
		        if (existingUserByUsername != null) {
		            throw new ServiceException("El nombre de usuario ya está en uso");
		        }

		        // Verificar si el correo electrónico ya existe
		        Usuario existingUserByEmail = usuarioDao.findByMail(kid.getEmail());
		        if (existingUserByEmail != null) {
		            throw new ServiceException("El correo electrónico ya está en uso");
		        }

		        // Guarda el usuario en la base de datos
		        Usuario newKid = usuarioDao.save(kid);

		        // Inserta el rol correspondiente al usuario
		        usuarioDao.insertUserRole(2, kid.getId().intValue());

		        // Crea la relación entre el tutor y el usuario kid
		        usuarioDao.createRelationByTutorId(idTutor, kid.getId().intValue());

		        return newKid;

		    } catch (DataAccessException e) {
		        // Captura excepciones específicas de acceso a datos, como problemas de conexión, errores SQL, etc.
		        throw new ServiceException("Error al acceder a la base de datos al crear el usuario kid", e);
		    } catch (ServiceException e) {
		        // Captura excepciones específicas del servicio, como nombre de usuario o correo electrónico ya en uso
		        throw e;
		    } catch (Exception e) {
		        // Captura otras excepciones no controladas
		        throw new ServiceException("Error al crear el usuario kid", e);
		    }
		}



		
		@Override
		@Transactional
		public Usuario createTutor(Usuario usuario){
		    try {
		        // Verificar si el nombre de usuario ya existe
		        Usuario existingUserByUsername = usuarioDao.findByUsername(usuario.getUsername());
		        if (existingUserByUsername != null) {
		            throw new ServiceException("El nombre de usuario ya está en uso");
		        }

		        // Verificar si el correo electrónico ya existe
		        Usuario existingUserByEmail = usuarioDao.findByMail(usuario.getEmail());
		        if (existingUserByEmail != null) {
		            throw new ServiceException("El correo electrónico ya está en uso");
		        }

		        // Guardar el tutor en la base de datos
		        Usuario tutor = usuarioDao.save(usuario);

		        // Insertar el rol correspondiente al tutor
		        usuarioDao.insertUserRole(1, tutor.getId().intValue()); // Ajusta el tipo de datos a Long
		        return tutor;

		    } catch (DataAccessException e) {
		        // Capturar excepciones específicas de acceso a datos, como problemas de conexión, errores SQL, etc.
		        throw new ServiceException("Error al acceder a la base de datos al crear el tutor", e);
		    } catch (ServiceException e) {
		        // Capturar excepciones específicas del servicio, como nombre de usuario o correo electrónico ya en uso
		        throw e;
		    } catch (Exception e) {
		        // Capturar otras excepciones no controladas
		        throw new ServiceException("Error al crear el tutor", e);
		    }
		}

		
		
		
		@Override
		@Transactional(readOnly = true)
		public List<Usuario> findAll() {
			return (List<Usuario>) usuarioDao.findAll();
		}
		
		
		@Override
		@Transactional(readOnly = true)
		public Usuario findById(Long id) {
			return usuarioDao.findById(id).orElse(null);
		}
		
		@Override
		@Transactional
		public Usuario save(Usuario usuario){
			return usuarioDao.save(usuario);
		}
		
		@Override
		@Transactional
		public void delete(Long id) {
			
			
			usuarioDao.deleteById(id);
			
			
			
		}

		
		
		  @Override public Usuario findByUsername(String username) { 
			  
			  return usuarioDao.findByUsername(username);
			  }
		  
		  
		 
		  @Override public List<String> findRolesById(Long id) { 
			  
			  return usuarioDao.findRoleNamesByUserId(id);
			  }
		  
		  
		  
		  @Override public List<Long> findKidsById(Long id){
			return usuarioDao.findKidsByUserId(id);  
		  };

		  
		  
		
	

	}


