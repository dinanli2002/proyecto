package com.woodland.springboot.backend.apirest.models.services;


import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.woodland.springboot.backend.apirest.models.entity.JwtPayload;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;


@Service
public class JwtPayloadDeserializer extends StdDeserializer<JwtPayload> {

    private static final long serialVersionUID = 1L;

	public JwtPayloadDeserializer() {
        this(null);
    }

    public JwtPayloadDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public JwtPayload deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        JsonNode node = jp.getCodec().readTree(jp);
        String nombre = node.get("nombre").asText();
        String userName = node.get("user_name").asText();
        List<String> scope = getListFromJsonArray(node.get("scope"));
        String email = node.get("email").asText();
        int monedas = Integer.parseInt(node.get("monedas").asText()); // Convertir a entero
        long exp = node.get("exp").asLong();
        List<String> authorities = getListFromJsonArray(node.get("authorities"));
        String jti = node.get("jti").asText();
        int id = Integer.parseInt(node.get("id").asText()); // Convertir a entero
        String clientId = node.get("client_id").asText();
        List<Integer> kids = getListFromJsonArrayAsIntegers(node.get("kids"));

        JwtPayload jwtPayload = new JwtPayload();
        jwtPayload.setNombre(nombre);
        jwtPayload.setUserName(userName);
        jwtPayload.setScope(scope);
        jwtPayload.setEmail(email);
        jwtPayload.setMonedas(monedas);
        jwtPayload.setExp(exp);
        jwtPayload.setAuthorities(authorities);
        jwtPayload.setJti(jti);
        jwtPayload.setId(id);
        jwtPayload.setClientId(clientId);
        jwtPayload.setKids(kids);

        return jwtPayload;
    }

    private List<String> getListFromJsonArray(JsonNode jsonArrayNode) {
        List<String> list = new ArrayList<>();
        if (jsonArrayNode != null && jsonArrayNode.isArray()) {
            for (final JsonNode objNode : jsonArrayNode) {
                list.add(objNode.asText());
            }
        }
        return list;
    }

    private List<Integer> getListFromJsonArrayAsIntegers(JsonNode jsonArrayNode) {
        List<Integer> list = new ArrayList<>();
        if (jsonArrayNode != null && jsonArrayNode.isArray()) {
            for (final JsonNode objNode : jsonArrayNode) {
                list.add(objNode.asInt());
            }
        }
        return list;
    }
}
