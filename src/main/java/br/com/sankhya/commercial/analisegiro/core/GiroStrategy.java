package br.com.sankhya.commercial.analisegiro.core;

import br.com.sankhya.commercial.analisegiro.model.ChaveGiro;
import br.com.sankhya.commercial.analisegiro.model.Giro;

public interface GiroStrategy {

    Giro findGiroByChaveGiro(ChaveGiro chave);
    void save(Giro giro);
}
