package br.com.sankhya.commercial.analisegiro.configuration;

import br.com.sankhya.commercial.analisegiro.core.MatrizGiroConfiguracao;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MatrizGiroConfiguracaoConf {

    @Bean
    public MatrizGiroConfiguracao matrizGiroConfiguracao() throws Exception {
        return new MatrizGiroConfiguracao();
    }
}
