package br.com.sankhya.commercial.analisegiro.repository;

import br.com.sankhya.commercial.analisegiro.helper.QueryExecutorSingleResultHelper;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;

@Repository
public class QueryExecutorSingleResult {

    private final EntityManager em;

    public QueryExecutorSingleResult(EntityManager em){
        this.em = em;
    }

    public Object execute(String campo, String tabela, String condicao){
        return QueryExecutorSingleResultHelper.execute(em,campo, tabela, condicao);
    }

    public Boolean existe(String campo, String tabela, String condicao){
        return QueryExecutorSingleResultHelper.existe(em,campo, tabela, condicao);
    }
}
