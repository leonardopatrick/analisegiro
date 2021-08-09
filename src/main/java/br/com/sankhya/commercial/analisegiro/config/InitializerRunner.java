package br.com.sankhya.commercial.analisegiro.config;

import br.com.sankhya.commercial.analisegiro.repository.ProdutoRepository;
import br.com.sankhya.commercial.analisegiro.service.impl.ParametroStrategyElastic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InitializerRunner implements CommandLineRunner {


	@Autowired
    ProdutoRepository produtoRepository;

	@Autowired
	ParametroStrategyElastic parameterSerice;

	@Override
	public void run(String... args) throws Exception {


	}

}
