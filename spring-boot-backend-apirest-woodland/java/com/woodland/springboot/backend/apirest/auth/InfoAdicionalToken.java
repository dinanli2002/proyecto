package com.woodland.springboot.backend.apirest.auth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

import com.woodland.springboot.backend.apirest.models.dao.IUsuarioDao;
import com.woodland.springboot.backend.apirest.models.entity.Usuario;
import com.woodland.springboot.backend.apirest.models.services.IUsuarioService;

@Component
public class InfoAdicionalToken implements TokenEnhancer {

    @Autowired
    private IUsuarioService usuarioService;
    
    @Autowired
    private IUsuarioDao usuarioDao;

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {

        Usuario usuario = usuarioService.findByUsername(authentication.getName());
        
        System.out.println(usuario);
        
        Map<String, Object> info = new HashMap<>();

        List<String> roles = usuarioDao.findRoleNamesByUserId(usuario.getId());

        List<GrantedAuthority> authorities = roles.stream()
                .map(SimpleGrantedAuthority::new)
                .peek(authority ->  authority.getAuthority())
                .collect(Collectors.toList());
        
       
        String rol =authorities.get(0).toString();
        
        List<Long> kids=usuarioService.findKidsById(usuario.getId());
        List<String> nameKids= new ArrayList<String>();
        for (Long kid : kids) {
        	
        	nameKids.add(usuarioService.findById(kid).getUsername());
        	
        }
        
        
        info.put("id",usuario.getId().toString());
        info.put("nombre", usuario.getUsername());
        info.put("email", usuario.getEmail());
        info.put("monedas", usuario.getMonedas().toString());
        info.put("kids", usuarioService.findKidsById(usuario.getId()));
        info.put("rol", rol);
        info.put("nombres", nameKids);
        
       
        
        
        
        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(info);
        return accessToken;
    }
}

