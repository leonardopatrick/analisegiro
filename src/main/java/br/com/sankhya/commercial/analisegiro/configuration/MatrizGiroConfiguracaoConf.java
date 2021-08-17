package br.com.sankhya.commercial.analisegiro.configuration;

import br.com.sankhya.commercial.analisegiro.service.impl.CalculoCurva;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MatrizGiroConfiguracaoConf {

    @Bean
    public MatrizGiroConfiguracao matrizGiroConfiguracao(){
        return new MatrizGiroConfiguracao();
    }
}
