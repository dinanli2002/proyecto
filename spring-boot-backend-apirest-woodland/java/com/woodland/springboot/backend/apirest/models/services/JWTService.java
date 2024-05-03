package com.woodland.springboot.backend.apirest.models.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.woodland.springboot.backend.apirest.models.entity.JwtPayload;

import org.springframework.stereotype.Service;
import java.util.Base64;

@Service
public class JWTService {

    private final ObjectMapper objectMapper;

    public JWTService() {
        objectMapper = new ObjectMapper();
        // Registrar el deserializador personalizado
        SimpleModule module = new SimpleModule();
        module.addDeserializer(JwtPayload.class, new JwtPayloadDeserializer());
        objectMapper.registerModule(module);
    }

    public JwtPayload jwtDecoder(String jwt) {
        // Separar el token JWT en sus partes
        String[] splitString = jwt.split("\\.");

        // Verificar si el token tiene el formato esperado
        if (splitString.length < 2) {
            throw new IllegalArgumentException("Invalid JWT format: Missing parts");
        }

        // Decodificar el cuerpo del token
        Base64.Decoder decoder = Base64.getUrlDecoder();
        String base64EncodedBody = splitString[1];
        String decodedBody = new String(decoder.decode(base64EncodedBody));

        // Convertir el cuerpo decodificado en un objeto JwtPayload
        JwtPayload jwtPayload;
        try {
            jwtPayload = objectMapper.readValue(decodedBody, JwtPayload.class);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Error parsing JWT payload", e);
        }

        return jwtPayload;
    }
}
