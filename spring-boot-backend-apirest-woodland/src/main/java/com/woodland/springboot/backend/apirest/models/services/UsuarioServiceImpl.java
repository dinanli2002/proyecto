package com.woodland.springboot.backend.apirest.models.services;


	import java.util.List;

	import java.util.List;
	import java.util.stream.Collectors;

	import org.slf4j.Logger;
	import org.slf4j.LoggerFactory;
	import org.springframework.beans.factory.annotation.Autowired;
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


