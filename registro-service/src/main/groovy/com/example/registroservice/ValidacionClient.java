package com.example.registroservice;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

@FeignClient(name = "validacion-service")
public interface ValidacionClient {

    @GetMapping("/validar/dni/{dni}")
    Map<String, Object> validarDni(@PathVariable("dni") String dni);

    @GetMapping("/validar/correo/{correo}")
    Map<String, Object> validarCorreo(@PathVariable("correo") String correo);
}
