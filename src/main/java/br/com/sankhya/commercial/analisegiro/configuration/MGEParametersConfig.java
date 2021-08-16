package br.com.sankhya.commercial.analisegiro.configuration;

import br.com.sankhya.commercial.analisegiro.core.MGEParameters;
import br.com.sankhya.commercial.analisegiro.service.impl.ParametroStrategyRelacional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Primary
@Configuration
public class MGEParametersConfig {

    @Autowired
    ParametroStrategyRelacional parametro;

    @Bean
    public MGEParameters MGEParameters(){

        MGEParameters parametroContextoRepository = new MGEParameters();
        parametroContextoRepository.setStrategy(parametro);

        return parametroContextoRepository;
    }
}