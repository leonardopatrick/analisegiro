package br.com.sankhya.commercial.analisegiro.core;

import br.com.sankhya.commercial.analisegiro.model.Parametro;

import java.math.BigDecimal;
import java.util.List;

public interface ParametroStrategy {

    public int getParameterAsInt(String paramName) throws Exception;

    public Boolean getParameterAsBoolean(String paramName) throws Exception;

    public String getParameterAsString(String paramName) throws Exception;

    public Object getParameter(String paramName) throws Exception;

    public Object getParameter(String paramName, int user) throws Exception;

    public Object getParameter(String groupName, String paramName, int user) throws Exception;

    public Object getParameter(String groupName, String paramName) throws Exception;

    public Parametro getParameterInfo(String name) throws Exception;

    public void saveParameter(String chave, String nomeCampo, Object value, String tipo, String descricao) throws Exception;

    public void saveParameter(String paramName, Object value) throws Exception;

    public void saveParameterByUser(String paramName, Object value, BigDecimal codusu) throws Exception;

    public List<Parametro> getAllParameters(String name) throws Exception;

    public List<Parametro> getAllParameters(Iterable<String> chaves) throws Exception;
}
