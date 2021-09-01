package br.com.sankhya.commercial.analisegiro.repository;

import br.com.sankhya.commercial.analisegiro.helper.QueryExecutorHelper;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;

@Repository
public class SingleQueryExecutor {

    private final EntityManager em;

    public SingleQueryExecutor(EntityManager em){
        this.em = em;
    }

    public Object execute(String campo, String tabela, String condicao){
        return QueryExecutorHelper.execute(em,campo, tabela, condicao);
    }

    public Boolean existe(String campo, String tabela, String condicao){
        return QueryExecutorHelper.existe(em,campo, tabela, condicao);
    }
}
