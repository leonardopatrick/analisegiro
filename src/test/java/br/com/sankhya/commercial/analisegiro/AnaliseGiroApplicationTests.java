package br.com.sankhya.commercial.analisegiro;

import br.com.sankhya.commercial.analisegiro.configuration.ParametroContextoRepositoryConfig;
import br.com.sankhya.commercial.analisegiro.core.ParametroContextoRepository;
import br.com.sankhya.commercial.analisegiro.model.Parametro;
import br.com.sankhya.commercial.analisegiro.repository.ProdutoRepository;
import br.com.sankhya.commercial.analisegiro.service.impl.ParametroStrategyRelacional;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@DataJpaTest
@Import({ParametroContextoRepositoryConfig.class, ParametroStrategyRelacional.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AnaliseGiroApplicationTests {

	@Autowired
	ProdutoRepository produtoRepository;

	@Autowired
	ParametroContextoRepository paramRepository;

	@Test
	void buscaParametroUsaControle() throws Exception {

		Parametro p = paramRepository.getParameterInfo("UTILIZACONTROLE");
		Assert.assertEquals(p.getLogico(),"S");
	}

	@Test
	void buscaVariosParametros() throws Exception {

		List<Parametro> p = paramRepository.getAllParameters(
				Arrays.asList(new String[]{"UTILIZACONTROLE", "UTILIZALOCAL"}));
		Assert.assertEquals(p.size(),2);
	}

	@Test
	void buscaParametroBoleano() throws Exception {

		Boolean valor = paramRepository.getParameterAsBoolean("UTILIZACONTROLE");
		Assert.assertEquals(valor, Boolean.TRUE);
	}

	@Test
	void gerarLista() throws Exception{
		List<BigDecimal> lista = produtoRepository.listProdutosCalGiro();
		Assert.assertEquals(lista.size(), 31);
	}
}