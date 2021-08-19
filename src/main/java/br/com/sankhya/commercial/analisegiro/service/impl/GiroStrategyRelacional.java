package br.com.sankhya.commercial.analisegiro.service.impl;

import br.com.sankhya.commercial.analisegiro.core.GiroStrategy;
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
public class GiroStrategyRelacional implements GiroStrategy {

    @Autowired
    GiroCustomRepository giroRepository;

    @Override
    public Giro findGiroByChaveGiro(ChaveGiro chave) throws Exception {
        return null;
    }

    @Override
    public void save(Giro giro) {
    }

    public Collection<Giro> findAll(){
        return null;
    }

    @Override
    public Collection<Giro> findAllByPeriod(Timestamp dtIni, Timestamp Dtfim) {

        List<GiroResult> listGiro = giroRepository.findAllByPeriod(dtIni,Dtfim);
        return null;
    }

    @Override
    public Map<ChaveGiro,Giro> getMapGiros(){
        return null;
    }

}
