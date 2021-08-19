package br.com.sankhya.commercial.analisegiro.core;

import br.com.sankhya.commercial.analisegiro.model.ChaveGiro;
import br.com.sankhya.commercial.analisegiro.model.Giro;
import br.com.sankhya.commercial.analisegiro.resultmodel.GiroResult;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface GiroStrategy {

    Giro findGiroByChaveGiro(ChaveGiro chave) throws Exception;
    void save(Giro giro);
    Map<ChaveGiro,Giro> getMapGiros();
    Collection<Giro> findAll();
    List<GiroResult> findAllByPeriod(Timestamp dtIni, Timestamp Dtfim) throws Exception;

}
