package com.woodland.springboot.backend.apirest.auth;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

import com.woodland.springboot.backend.apirest.models.entity.Usuario;
import com.woodland.springboot.backend.apirest.models.services.IUsuarioService;

@Component
public class InfoAdicionalToken implements TokenEnhancer {

    @Autowired
    private IUsuarioService usuarioService;

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {

        Usuario usuario = usuarioService.findByUsername(authentication.getName());
        System.out.println(usuario);
        
        Map<String, Object> info = new HashMap<>();

        
        info.put("id",usuario.getId().toString());
        info.put("nombre", usuario.getUsername());
        info.put("email", usuario.getEmail());
        info.put("monedas", usuario.getMonedas().toString());
        info.put("kids", usuarioService.findKidsById(usuario.getId()));
        
        
        
        
        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(info);
        return accessToken;
    }

}
