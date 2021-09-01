package br.com.sankhya.commercial.analisegiro.service.impl;

import br.com.sankhya.commercial.analisegiro.core.GiroStrategy;
import br.com.sankhya.commercial.analisegiro.core.MatrizGiroConfiguracao;
import br.com.sankhya.commercial.analisegiro.model.ChaveGiro;
import br.com.sankhya.commercial.analisegiro.model.Giro;
import br.com.sankhya.commercial.analisegiro.model.Parametro;
import br.com.sankhya.commercial.analisegiro.repository.GiroCustomRepository;
import br.com.sankhya.commercial.analisegiro.repository.ParametroRepository;
import br.com.sankhya.commercial.analisegiro.resultmodel.GiroResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;

@Service
public class GiroStrategyRelacionalInMemory implements GiroStrategy {

    @Autowired
    GiroCustomRepository giroRelacional;

    @Autowired
    GiroStrategyInMemory giroStrategyInMemory;

    @Autowired
    MatrizGiroConfiguracao matrizConf;

    @Override
    public Giro findGiroByChaveGiro(ChaveGiro chave) throws Exception {
        return giroStrategyInMemory.findGiroByChaveGiro(chave);
    }

    @Override
    public void save(Giro giro) {
         giroStrategyInMemory.save(giro);
    }

    public Collection<Giro> findAll(){
        return giroStrategyInMemory.findAll();
    }

    @Override
    public List<GiroResult> findAllByPeriod(Timestamp dtIni, Timestamp Dtfim) {

        List<GiroResult> listGiro = giroRelacional.findAllByPeriod(dtIni,Dtfim, matrizConf);
        return listGiro;
    }

    @Override
    public Map<ChaveGiro,Giro> getMapGiros(){
        return null;
    }

}
