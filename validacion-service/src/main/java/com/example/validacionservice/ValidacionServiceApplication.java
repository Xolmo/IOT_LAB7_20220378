package com.example.validacionservice;

import com.netflix.discovery.EurekaClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
@RestController
@RequestMapping("/validar")
public class ValidacionServiceApplication {
	@Autowired
	@Lazy
	private EurekaClient eurekaClient;

	@Value("${spring.application.name}")
	private String appName;

	public static void main(String[] args) {
		SpringApplication.run(ValidacionServiceApplication.class, args);
	}


	// -------------------------------
	// 1. VALIDAR DNI
	// -------------------------------
	@GetMapping("/dni/{dni}")
	public Map<String, Object> validarDni(@PathVariable String dni) {
		Map<String, Object> respuesta = new HashMap<>();

		boolean esValido = dni.matches("\\d{8}");

		respuesta.put("dni", dni);
		respuesta.put("valido", esValido);

		if (esValido) {
			respuesta.put("mensaje", "DNI válido");
		} else {
			respuesta.put("mensaje", "El DNI debe contener exactamente 8 dígitos numéricos");
		}

		return respuesta;
	}

	// -------------------------------
	// 2. VALIDAR CORREO PUCP
	// -------------------------------
	@GetMapping("/correo/{correo}")
	public Map<String, Object> validarCorreo(@PathVariable String correo) {
		Map<String, Object> respuesta = new HashMap<>();

		boolean esValido = correo.endsWith("@pucp.edu.pe");

		respuesta.put("correo", correo);
		respuesta.put("valido", esValido);

		if (esValido) {
			respuesta.put("mensaje", "Correo válido");
		} else {
			respuesta.put("mensaje", "El correo debe terminar en @pucp.edu.pe");
		}

		return respuesta;
	}

}
