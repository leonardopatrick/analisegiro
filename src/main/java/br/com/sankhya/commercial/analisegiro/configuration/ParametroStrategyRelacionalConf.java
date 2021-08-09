package br.com.sankhya.commercial.analisegiro.configuration;

import br.com.sankhya.commercial.analisegiro.core.ParametroContextoRepository;
import br.com.sankhya.commercial.analisegiro.service.impl.ParametroStrategyRelacional;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ParametroStrategyRelacionalConf {

    @Bean
    public ParametroStrategyRelacional parametroStrategyRelacional(){

        return new ParametroStrategyRelacional();
    }
}
