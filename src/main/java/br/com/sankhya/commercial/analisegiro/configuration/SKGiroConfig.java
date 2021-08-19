package br.com.sankhya.commercial.analisegiro.configuration;

import br.com.sankhya.commercial.analisegiro.core.SKGiro;
import br.com.sankhya.commercial.analisegiro.service.impl.GiroStrategyRelacionalInMemory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Primary
@Configuration
public class SKGiroConfig {

    @Autowired
    GiroStrategyRelacionalInMemory Strategy;

    @Bean
    public SKGiro skKGiro(){
        SKGiro contexto = new SKGiro();
        contexto.setStrategy(Strategy);
        return contexto;
    }
}
