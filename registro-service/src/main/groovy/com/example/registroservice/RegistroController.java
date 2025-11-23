package com.example.registroservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/registro")
public class RegistroController {
    @Autowired
    private ValidacionClient validacionClient;

    @PostMapping
    public ResponseEntity<?> registrar(@RequestBody RegistroRequest request) {

        // 1. Validar DNI
        Map<String, Object> dniResp = validacionClient.validarDni(request.getDni());
        boolean dniValido = (boolean) dniResp.get("valido");

        if (!dniValido) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("error", "El DNI no tiene un formato válido"));
        }

        // 2. Validar correo
        Map<String, Object> correoResp = validacionClient.validarCorreo(request.getCorreo());
        boolean correoValido = (boolean) correoResp.get("valido");

        if (!correoValido) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("error", "El correo no es del dominio permitido (@pucp.edu.pe)"));
        }

        // 3. Ambas validaciones correctas
        return ResponseEntity.ok(Map.of("mensaje", "Registro exitoso"));
    }
}
