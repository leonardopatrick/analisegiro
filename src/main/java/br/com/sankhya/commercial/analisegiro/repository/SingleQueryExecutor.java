package br.com.sankhya.commercial.analisegiro.repository;

import br.com.sankhya.commercial.analisegiro.util.BigDecimalUtil;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigDecimal;

@Repository
public class SingleQueryExecutor {

    private final EntityManager em;

    public SingleQueryExecutor(EntityManager em){

        this.em = em;
    }

    public Object execute(String campo, String tabela, String condicao
                            ){

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");
        sql.append(campo);
        sql.append(" FROM ");
        sql.append(tabela);
        sql.append(" WHERE ");
        sql.append(condicao);

        Query q = em.createNativeQuery(sql.toString());

        Object result = q.getSingleResult();

        return result;
    }

    public Boolean existe(String campo,
                                    String tabela,
                                    String condicao){

        BigDecimal result = (BigDecimal) execute(campo, tabela, condicao);

        return BigDecimalUtil.getValueOrZero(result).compareTo(BigDecimal.ZERO)==1;
    }
}
