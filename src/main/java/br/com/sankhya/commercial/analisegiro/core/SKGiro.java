package br.com.sankhya.commercial.analisegiro.core;

import br.com.sankhya.commercial.analisegiro.model.ChaveGiro;
import br.com.sankhya.commercial.analisegiro.model.Giro;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Map;

@Getter
@Setter
public class SKGiro implements GiroStrategy{

    private GiroStrategy strategy;

    @Override
    public Giro findGiroByChaveGiro(ChaveGiro chave) throws Exception {
        return strategy.findGiroByChaveGiro(chave);
    }

    @Override
    public void save(Giro giro) {
        strategy.save(giro);
    }

    @Override
    public Map<ChaveGiro, Giro> getMapGiros() {
        return strategy.getMapGiros();
    }

    @Override
    public Collection<Giro> findAll() {
        return strategy.findAll();
    }

    @Override
    public Collection<Giro> findAllByPeriod(Timestamp dtIni, Timestamp Dtfim) throws Exception {
        return strategy.findAllByPeriod(dtIni, Dtfim);
    }
}
