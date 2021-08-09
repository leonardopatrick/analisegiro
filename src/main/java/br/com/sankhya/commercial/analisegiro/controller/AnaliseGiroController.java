package br.com.sankhya.commercial.analisegiro.controller;

import br.com.sankhya.commercial.analisegiro.service.impl.CalculoGiro;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
public class AnaliseGiroController {

	@Autowired
	CalculoGiro calculoGiro;

	@PostMapping("/calcularGiro")
	@Operation(summary = "Calcular Giro [ROLE_ADMIN]")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity calcularGiro() throws Exception {

		calculoGiro.gerar();

		return new ResponseEntity<>(HttpStatus.OK);
	}
}
