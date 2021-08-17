package br.com.sankhya.commercial.analisegiro.service.impl;

import br.com.sankhya.commercial.analisegiro.elasticsearch.model.ParametroElastic;
import br.com.sankhya.commercial.analisegiro.elasticsearch.repository.ParameterRepositoryElastic;
import br.com.sankhya.commercial.analisegiro.model.Parametro;
import br.com.sankhya.commercial.analisegiro.core.ParametroStrategy;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ParametroStrategyElastic implements ParametroStrategy {

    @Autowired
    ParameterRepositoryElastic parameterRepositoryElastic;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public int getParameterAsInt(String paramName) throws Exception {
        return (int) getValueParameter(paramName);
    }

    @Override
    public Boolean asBoolean(String paramName) throws Exception {
        return (Boolean) getValueParameter(paramName);
    }

    @Override
    public Double asDouble(String paramName) throws Exception {
        return (Double) getValueParameter(paramName);
    }

    @Override
    public Boolean getParameterAsBoolean(String paramName) throws Exception {
        return (Boolean) getValueParameter(paramName);
    }

    @Override
    public String getParameterAsString(String paramName) throws Exception {
        return (String) getValueParameter(paramName);
    }

    @Override
    public Parametro getParameter(String paramName) throws Exception {
        return getParameterInfo(paramName);
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
    public Parametro getParameterInfo(String name) throws Exception {
       List<ParametroElastic>  parametros =  parameterRepositoryElastic.findByChave(name);

        return parameterRepositoryElastic.findByChave(name)
                 .stream()
                .map(this::toParametro)
                .collect(Collectors.toList()).listIterator().next();
    }

    public Parametro toParametro(ParametroElastic p){

        return modelMapper.map(p, Parametro.class);
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

    public Object getValueParameter(String chave) throws Exception {
        return resolveValue(getParameterInfo(chave));
    }

    @Override
    public List<Parametro> getAllParameters(Iterable<String> chaves) throws Exception {
        return StreamSupport.stream(
                parameterRepositoryElastic.findAllById(chaves)
                        .spliterator(), false)
                .map(this::toParametro)
                .collect(Collectors.toList());
    }

    private Object resolveValue(Parametro p){

        Object paramValue = null;

        String tipo = p.getTipo();
        if("L".equals(tipo)) {
            paramValue = "S".equalsIgnoreCase(p.getLogico()) ? Boolean.TRUE : Boolean.FALSE ;

        } else if("I".equals(tipo) || "C".equals(tipo)) {
            paramValue = p.getInteiro();
        } else if("D".equals(tipo)) {
            paramValue = p.getData();
        } else if("T".equals(tipo)) {
            paramValue = p.getTexto();
        } else if("F".equals(tipo)) {
            paramValue = p.getNumdec();
        }
        return paramValue;
    }
}
