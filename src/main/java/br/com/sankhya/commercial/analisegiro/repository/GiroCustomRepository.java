package br.com.sankhya.commercial.analisegiro.repository;

import br.com.sankhya.commercial.analisegiro.configuration.MatrizGiroConfiguracao;
import br.com.sankhya.commercial.analisegiro.resultmodel.GiroResult;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import java.util.List;
import java.sql.Timestamp;

@SuppressWarnings("ALL")
@Repository
public class GiroCustomRepository {

    private final EntityManager em;

    public GiroCustomRepository(EntityManager em){
        this.em = em;
    }

    public List<GiroResult> findGirobyQueryCustom(String sqlGroup,
                                                  String usarEmpresa,
                                                  String sqlchave,
                                                  MatrizGiroConfiguracao matrizConf,
                                                  Timestamp dtIni,
                                                  Timestamp dtFin
                            ){

        StringBuffer sql = new StringBuffer();
        sql.append(sqlchave);
        if("M".equals(usarEmpresa)) {
            sql.append(", MAX(COALESCE(COALESCE(PEM.LEADTIME, PRO.LEADTIME),0)) AS LEADTIME ");
        } else if("S".equals(usarEmpresa)) {
            sql.append(", MAX(COALESCE(COALESCE(PEM.LEADTIME, PRO.LEADTIME),0)) AS LEADTIME  ");
        } else {
            sql.append(", MAX(PRO.LEADTIME) AS LEADTIME ");
        }
        sql.append(" , SUM(ITE.VLRTOT) AS VLRTOT, SUM(ITE.QTDNEG) AS QTDE ");
        sql.append(" , SUM(ITE." + matrizConf.getCusto() + " * ITE.QTDNEG) / CASE WHEN SUM(ITE.QTDNEG) = 0 THEN 1 ELSE SUM(ITE.QTDNEG) END AS CUSTOGER ");
        sql.append(" , SUM(ITE.MARGEMCONTRIB) AS MARGEMCONTRIB ");
        sql.append(" , SUM(ITE." + matrizConf.getCusto() + " * ITE.QTDNEG) / CASE WHEN SUM(ITE.QTDNEG) = 0 THEN 1 ELSE SUM(ITE.QTDNEG) END AS CUSTOVARIAVEL ");
        sql.append(" , SUM(ITE.LUCRO) AS LUCRO ");
        sql.append(" , SUM(CASE WHEN ITE.QTDNEG < 0 THEN 0 ELSE ITE.POPULARIDADE END) AS POPULARIDADE ");
        sql.append(" , PRO.CODGRUPOPROD ");
        sql.append(" , PRO.MARCA ");
        sql.append(" , PRO.PESOBRUTO ");
        sql.append(" FROM TGFGIR1 ITE ");
        sql.append("     INNER JOIN TGFPRO PRO ON PRO.CODPROD = ITE.CODPROD ");
        sql.append("     INNER JOIN TGFGRU GRU ON GRU.CODGRUPOPROD = PRO.CODGRUPOPROD ");

        if("M".equals(usarEmpresa)) {
            sql.append(" INNER JOIN TSIEMP EMP ON EMP.CODEMP = ITE.CODEMP LEFT JOIN TGFPEM PEM ON PEM.CODEMP = EMP.CODEMP AND PEM.CODPROD = PRO.CODPROD ");
        } else if("S".equals(usarEmpresa)) {
            sql.append(" LEFT JOIN TGFPEM PEM ON PEM.CODEMP = ITE.CODEMP AND PEM.CODPROD = PRO.CODPROD ");
        }
        /* TODO SEM FILTRO = SEM TOP
        if(filtroGiro.contains("TGFTOP.")) {
            sql.append(" INNER JOIN TGFTOP ON TGFTOP.CODTIPOPER = ITE.CODTIPOPER AND TGFTOP.DHALTER = (SELECT MAX(DHALTER) FROM TGFTOP T2 WHERE T2.CODTIPOPER = ITE.CODTIPOPER) ");
        }
        */
        sql.append(" WHERE ITE.DTNEG >= :DTINI AND ITE.DTNEG <= :DTFIN AND ITE.QTDNEG != 0 ");

        /* TODO SEM FILTRO
        if(StringUtils.getEmptyAsNull(filtroGiro) != null) {
            sql.append(" AND (" + filtroGiro + ") ");
        }*/

        sql.append(sqlGroup);
        sql.append(" , PRO.CODGRUPOPROD ");
        sql.append(" , PRO.MARCA ");
        sql.append(" , PRO.PESOBRUTO ");


        Session session = em.unwrap(Session.class);
        List<GiroResult> rs = session.createSQLQuery(sql.toString())
                .setParameter("DTINI", dtIni)
                .setParameter("DTFIN",  dtFin)
                .setResultTransformer(Transformers.aliasToBean(GiroResult.class)).list();


        return rs;
    }

}
