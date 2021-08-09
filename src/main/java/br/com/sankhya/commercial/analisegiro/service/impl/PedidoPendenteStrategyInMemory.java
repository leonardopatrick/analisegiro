package br.com.sankhya.commercial.analisegiro.service.impl;

import br.com.sankhya.commercial.analisegiro.core.GiroStrategy;
import br.com.sankhya.commercial.analisegiro.model.ChaveGiro;
import br.com.sankhya.commercial.analisegiro.model.Giro;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
public class PedidoPendenteStrategyInMemory {

    private Map<ChaveGiro, BigDecimal> pedidos = new HashMap<ChaveGiro, BigDecimal>();


    public void save(ChaveGiro chave, BigDecimal qtde) {
        if (!pedidos.containsKey(chave)) {
            pedidos.put(chave, qtde);
        }
    }

    public void deleteAll(){
        pedidos.clear();
    }

}
