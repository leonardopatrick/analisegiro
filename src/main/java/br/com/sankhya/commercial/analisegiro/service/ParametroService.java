package br.com.sankhya.commercial.analisegiro.service;

import br.com.sankhya.commercial.analisegiro.model.Parametro;

import java.util.List;

public interface ParametroService {

    public Parametro getParameter(String chave) throws Exception;
    public List<Parametro> getAllParameters(Iterable<String> chaves);
    public boolean getParameterAsBoolean(String paramName) throws Exception;
}
