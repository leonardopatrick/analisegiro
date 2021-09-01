package br.com.sankhya.commercial.analisegiro.helper;

import br.com.sankhya.commercial.analisegiro.util.BigDecimalUtil;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigDecimal;

public class QueryExecutorSingleResultHelper {

    public static Object execute(EntityManager em, String campo, String tabela, String condicao
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

    public static Boolean existe(EntityManager em, String campo, String tabela, String condicao){
        BigDecimal result = (BigDecimal) execute(em, campo, tabela, condicao);
        return BigDecimalUtil.getValueOrZero(result).compareTo(BigDecimal.ZERO)==1;
    }
}
