package br.com.sankhya.commercial.analisegiro.configuration;

import br.com.sankhya.commercial.analisegiro.service.impl.CalculoCurva;
import br.com.sankhya.commercial.analisegiro.service.impl.CalculoGiro;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class CalculoCurvaConf {

    @Bean
    public CalculoCurva calculocurva(){
        return new CalculoCurva();
    }
}
