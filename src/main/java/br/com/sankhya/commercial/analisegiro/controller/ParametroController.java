package br.com.sankhya.commercial.analisegiro.controller;

import br.com.sankhya.commercial.analisegiro.core.ParametroContextoRepository;
import br.com.sankhya.commercial.analisegiro.model.Parametro;
import br.com.sankhya.commercial.analisegiro.service.impl.ParametroStrategyElastic;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ParametroController {

	@Autowired
	ParametroContextoRepository parameterService;

	@GetMapping("/parameter/{chave}")
	@Operation(summary = "List parameter by chave")
	public Parametro findBypk(@PathVariable(value = "chave") String chave) throws Exception {

		Parametro parameter = parameterService.getParameterInfo(chave.toUpperCase());
		return parameter;
	}

}
