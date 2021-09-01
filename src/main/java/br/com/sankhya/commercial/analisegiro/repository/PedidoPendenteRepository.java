package br.com.sankhya.commercial.analisegiro.repository;

import br.com.sankhya.commercial.analisegiro.core.MatrizGiroConfiguracao;
import br.com.sankhya.commercial.analisegiro.resultmodel.PedidoPendenteResult;
import br.com.sankhya.commercial.analisegiro.util.StringUtils;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@SuppressWarnings("ALL")
@Repository
public class PedidoPendenteRepository {


    private final EntityManager em;

    public PedidoPendenteRepository(EntityManager em){
        this.em = em;
    }

    public List<PedidoPendenteResult> findPedidosPendentes(String filtro,  MatrizGiroConfiguracao matrizConf){

    	StringBuffer sql = new StringBuffer();
    	sql.append(matrizConf.getSqlChave().toString());

        sql.append("  , SUM(ITE.QTDNEG - ITE.QTDENTREGUE) AS QTDE ");
        sql.append(" FROM TGFITE ITE ");
        sql.append(" INNER JOIN TGFCAB CAB ON CAB.NUNOTA = ITE.NUNOTA ");
        sql.append(" INNER JOIN TGFTOP TPO ON TPO.CODTIPOPER = CAB.CODTIPOPER AND TPO.DHALTER = CAB.DHTIPOPER ");
        sql.append(" INNER JOIN TSIEMP EMP ON EMP.CODEMP = ITE.CODEMP ");
        sql.append(" WHERE ITE.QTDNEG > 0 ");
        sql.append(" AND ITE.ATUALESTOQUE = 0 ");
        sql.append(" AND (ITE.PENDENTE = 'S' OR CAB.TIPMOV NOT IN ('P','O','J')) ");
        sql.append(" AND CASE WHEN CAB.TIPMOV IN ('P','O','J') ");
        sql.append("     THEN CASE WHEN ITE.STATUSNOTA = 'L' THEN 'S' ELSE 'N' END ");
        sql.append("     ELSE CASE WHEN ITE.STATUSNOTA != 'L' ");
        sql.append("                AND ((TPO.ATUALEST != 'N' AND ITE.USOPROD NOT IN ('M','D')) OR ");
        sql.append("                     (TPO.ATUALESTMP != 0 AND ITE.USOPROD IN ('M','D'))) ");
        sql.append("          THEN 'S' ELSE 'N' END ");
        sql.append("     END = 'S' ");
        if(StringUtils.getEmptyAsNull(filtro) != null) {
            sql.append(" AND (" + filtro + ") ");
        }

    	sql.append(matrizConf.getSqlGroup().toString());

        Session session = em.unwrap(Session.class);
        List<PedidoPendenteResult> rs = session.createSQLQuery(sql.toString())
                .setResultTransformer(Transformers.aliasToBean(PedidoPendenteResult.class)).list();
        return rs;
    }

}
