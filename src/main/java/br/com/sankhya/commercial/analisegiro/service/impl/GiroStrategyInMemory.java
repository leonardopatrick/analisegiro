package br.com.sankhya.commercial.analisegiro.service.impl;

import br.com.sankhya.commercial.analisegiro.core.GiroStrategy;
import br.com.sankhya.commercial.analisegiro.model.ChaveGiro;
import br.com.sankhya.commercial.analisegiro.model.Giro;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GiroStrategyInMemory implements GiroStrategy {

    private Map<ChaveGiro, Giro> giros = new HashMap<ChaveGiro, Giro>();

    @Override
    public Giro findGiroByChaveGiro(ChaveGiro chave) {
        Giro giro = null;
        if (giros.containsKey(chave)) {
             giro = giros.get(chave);
        }else{
            giro = new Giro();
            giro.setChave(chave);
            return  giro;
        }
        return giro;
    }

    @Override
    public void save(Giro giro) {
        if (!giros.containsKey(giro.getChave())) {
            giros.put(giro.getChave(), giro);
        }else{
            giros.replace(giro.getChave(),giro);
        }
    }

    public List<Giro> findAll(){
        return (List<Giro>) giros.values();
    }

}
