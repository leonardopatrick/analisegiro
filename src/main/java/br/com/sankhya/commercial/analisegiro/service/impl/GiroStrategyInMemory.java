package br.com.sankhya.commercial.analisegiro.service.impl;

import br.com.sankhya.commercial.analisegiro.core.GiroStrategy;
import br.com.sankhya.commercial.analisegiro.model.ChaveGiro;
import br.com.sankhya.commercial.analisegiro.model.Giro;
import br.com.sankhya.commercial.analisegiro.resultmodel.GiroResult;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Collection;
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
        giros.put(giro.getChave(), giro);
    }

    public Collection<Giro> findAll(){
        return giros.values();
    }

    @Override
    public List<GiroResult> findAllByPeriod(Timestamp dtIni, Timestamp Dtfim) throws Exception {

        Boolean podeUtilizar = Boolean.FALSE;

        if(!podeUtilizar)
        throw  new Exception("Função disponível apenas para consulta relacional");

        return null;
    }

    public Map<ChaveGiro,Giro> getMapGiros(){
        return giros;
    }

}
