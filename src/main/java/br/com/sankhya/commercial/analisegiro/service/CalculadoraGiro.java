package br.com.sankhya.commercial.analisegiro.service;

import br.com.sankhya.commercial.analisegiro.core.MatrizGiroConfiguracao;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;


@Getter
@Setter
@AllArgsConstructor
@Service
public class CalculadoraGiro {

    private CalculoGiro calculoGiro;
    private MatrizGiroConfiguracao conf;

    CalculadoraGiro(){}
    private void inicializar() throws Exception {
      //  CalculoGiro c =  CalculoGiro.getInstance();
       // c.gerar();
    }
}
