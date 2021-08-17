package br.com.sankhya.commercial.analisegiro.service.impl;

import br.com.sankhya.commercial.analisegiro.model.Parametro;
import br.com.sankhya.commercial.analisegiro.repository.ParametroRepository;
import br.com.sankhya.commercial.analisegiro.core.ParametroStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class ParametroStrategyRelacional implements ParametroStrategy {

    @Autowired
    ParametroRepository parametroRepository;

    @Override
    public Parametro getParameterInfo(String chave) throws Exception {
        Optional<Parametro> p = parametroRepository.findById(chave);

        if(p.isEmpty()){
            throw  new Exception("Parametro não encontrado. ");
        }
        return p.get();
    }

    @Override
    public Object getParameter(String paramName, int user) throws Exception {
        return null;
    }

    @Override
    public Object getParameter(String groupName, String paramName, int user) throws Exception {
        return null;
    }

    @Override
    public Object getParameter(String groupName, String paramName) throws Exception {
        return null;
    }

    @Override
    public void saveParameter(String chave, String nomeCampo, Object value, String tipo, String descricao) throws Exception {

    }

    @Override
    public void saveParameter(String paramName, Object value) throws Exception {

    }

    @Override
    public void saveParameterByUser(String paramName, Object value, BigDecimal codusu) throws Exception {

    }

    @Override
    public List<Parametro> getAllParameters(String name) throws Exception {
        return null;
    }

    @Override
    public List<Parametro> getAllParameters(Iterable<String> chaves) {
        List<Parametro>  parametros = parametroRepository.findAllById(chaves);
        return parametros;
    }

    public Object getValueParameter(String chave) throws Exception {
        return resolveValue(getParameterInfo(chave));
    }

    @Override
    public int getParameterAsInt(String paramName) throws Exception {
        return 0;
    }

    @Override
    public Boolean getParameterAsBoolean(String chave) throws Exception {
        return (Boolean) getValueParameter(chave);
    }

    @Override
    public Boolean asBoolean(String chave) throws Exception {
        return (Boolean) getValueParameter(chave);
    }

    @Override
    public Double asDouble(String paramName) throws Exception {
        return (Double) getValueParameter(paramName);
    }

    @Override
    public String getParameterAsString(String paramName) throws Exception {
        return null;
    }

    @Override
    public Object getParameter(String paramName) throws Exception {
        return getValueParameter(paramName);
    }

    private Object resolveValue(Parametro p){

        Object paramValue = null;

        String tipo = p.getTipo();
        if("L".equals(tipo)) {
            paramValue = "S".equalsIgnoreCase(p.getLogico()) ? Boolean.TRUE : Boolean.FALSE ;

        } else if("I".equals(tipo) || "C".equals(tipo)) {
            paramValue = p.getInteiro();
        } else if("D".equals(tipo)) {
            paramValue = p.getTexto(); //TODO DATA
        } else if("T".equals(tipo)) {
            paramValue = p.getTexto();
        } else if("F".equals(tipo)) {
            paramValue = p.getNumdec();
        }
        return paramValue;
    }
}
