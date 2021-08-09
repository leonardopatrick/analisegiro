package br.com.sankhya.commercial.analisegiro.repository;

import br.com.sankhya.commercial.analisegiro.configuration.MatrizGiroConfiguracao;
import br.com.sankhya.commercial.analisegiro.resultmodel.EstoqueResult;
import br.com.sankhya.commercial.analisegiro.resultmodel.PedidoPendenteResult;
import br.com.sankhya.commercial.analisegiro.util.StringUtils;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@SuppressWarnings("ALL")
@Repository
public class EstoqueRepository {

    private final EntityManager em;

    public EstoqueRepository(EntityManager em){
        this.em = em;
    }

    public List<EstoqueResult> findEstoque(Boolean subtrairDaSugestaoAQtdeBloqueadaNoWMS,
                                            Boolean subtrairDoEsotqueAReserva,
                                            MatrizGiroConfiguracao matrizConf,
                                            String sqlChave,
                                            String sqlGroup,
                                            String filtroEstoque
                            ){

        StringBuilder sql = new StringBuilder();
        sql.append(StringUtils.replaceString(sqlChave.toString(), "ITE.", "EST."));
        sql.append(" , CASE WHEN MIN(PEM.ESTMIN) IS NOT NULL THEN MIN(PEM.ESTMIN) ELSE SUM(EST.ESTMIN) END AS ESTMIN ");
        sql.append(" , CASE WHEN MIN(PEM.ESTMAX) IS NOT NULL THEN MIN(PEM.ESTMAX) ELSE SUM(EST.ESTMAX) END AS ESTMAX ");
        sql.append(" , SUM(EST.ESTOQUE ");
        if(subtrairDoEsotqueAReserva) {
            sql.append(" - NVL(EST.RESERVADO,0) ");
        }
        if(subtrairDaSugestaoAQtdeBloqueadaNoWMS) {
            sql.append(" - NVL(EST.WMSBLOQUEADO,0) ");
        }
        sql.append(") AS ESTOQUE ");
        sql.append(" , NVL(SUM(EST.WMSBLOQUEADO),0) AS WMSBLOQUEADO ");
        sql.append(" FROM TGFEST EST");
        sql.append(" INNER JOIN TSIEMP EMP ON EMP.CODEMP = EST.CODEMP ");
        sql.append(" LEFT JOIN TGFPEM PEM ON (PEM.CODPROD = EST.CODPROD AND PEM.CODEMP = EST.CODEMP AND PEM.ESTMIN IS NOT NULL)");

        if(StringUtils.getEmptyAsNull(filtroEstoque) != null) {
            sql.append(" AND (" + filtroEstoque + ") ");
        }

        sql.append(StringUtils.replaceString(sqlGroup, "ITE.", "EST."));

        Session session = em.unwrap(Session.class);
        List<EstoqueResult> rs = session.createSQLQuery(sql.toString())
                .setResultTransformer(Transformers.aliasToBean(EstoqueResult.class)).list();
        return rs;
    }
}
