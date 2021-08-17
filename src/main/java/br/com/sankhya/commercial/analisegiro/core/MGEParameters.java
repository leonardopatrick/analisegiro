package br.com.sankhya.commercial.analisegiro.core;

import br.com.sankhya.commercial.analisegiro.model.Parametro;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class MGEParameters implements ParametroStrategy{

    private ParametroStrategy strategy;

    @Override
    public int getParameterAsInt(String paramName) throws Exception {
        return strategy.getParameterAsInt(paramName);
    }

    @Override
    public Boolean getParameterAsBoolean(String paramName) throws Exception {
        return strategy.getParameterAsBoolean(paramName);
    }

    @Override
    public String getParameterAsString(String paramName) throws Exception {
        return strategy.getParameterAsString(paramName);
    }

    @Override
    public Object getParameter(String paramName) throws Exception {
        return strategy.getParameter(paramName) ;
    }

    @Override
    public Object getParameter(String paramName, int user) throws Exception {
        return strategy.getParameter(paramName,user);
    }

    @Override
    public Object getParameter(String groupName, String paramName, int user) throws Exception {
        return strategy.getParameter(groupName, paramName, user);
    }

    @Override
    public Object getParameter(String groupName, String paramName) throws Exception {
        return strategy.getParameter(groupName, paramName);
    }

    @Override
    public Parametro getParameterInfo(String name) throws Exception {
        return strategy.getParameterInfo(name);
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
        return strategy.getAllParameters(name);
    }

    @Override
    public List<Parametro> getAllParameters(Iterable<String> chaves) throws Exception {
        return strategy.getAllParameters(chaves);
    }

    @Override
    public Boolean asBoolean(String paramName) throws Exception {
        return strategy.asBoolean(paramName);
    }

    @Override
    public Double asDouble(String paramName) throws Exception {
        return strategy.asDouble(paramName);
    }
}
