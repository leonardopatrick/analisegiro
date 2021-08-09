package br.com.sankhya.commercial.analisegiro.repository;

import br.com.sankhya.commercial.analisegiro.configuration.MatrizGiroConfiguracao;
import br.com.sankhya.commercial.analisegiro.resultmodel.PedidoPendenteResult;
import br.com.sankhya.commercial.analisegiro.resultmodel.UltimaVendaResult;
import br.com.sankhya.commercial.analisegiro.util.SqlUtils;
import br.com.sankhya.commercial.analisegiro.util.StringUtils;
import br.com.sankhya.commercial.analisegiro.util.TimeUtils;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.io.File;
import java.nio.file.Files;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;


@Repository
public class UltimaVendaRepository {

    private final EntityManager em;

    public UltimaVendaRepository(EntityManager em){

        this.em = em;
    }

    @SuppressWarnings("unchecked")
    public List<UltimaVendaResult> findUltimaVenda(
            MatrizGiroConfiguracao matrizConf,
            String usarEmpresa,
            Boolean utilizarLocal,
            Boolean utilizarControle
    ) {
        StringBuffer sql = new StringBuffer();

        if ("S".equals(matrizConf.getApresentaEmpresa())) {
            usarEmpresa = "S".equals(matrizConf.getApresentaMatriz()) ? "M" : "S";
        }

        sql.append(" SELECT  ");
        if("S".equals(matrizConf.getAgrupaProdAltern())) {
            sql.append("Snk_GetProdutoAgrupadoGiro(ITE.CODPROD, 'S') AS CODPROD ");
        } else if("G".equals(matrizConf.getAgrupaProdAltern())) {
            sql.append("Snk_GetProdutoAgrupadoGiro(ITE.CODPROD, 'G') AS CODPROD ");
        } else {
            sql.append("ITE.CODPROD");
        }

        if("M".equals(usarEmpresa)) {
            sql.append(" , NVL(EMP.CODEMPMATRIZ, EMP.CODEMP) AS CODEMP ");
        } else if("S".equals(usarEmpresa)) {
            sql.append(" , ITE.CODEMP ");
        } else {
            sql.append(" , 0 AS CODEMP ");
        }
        if(utilizarLocal) {
            sql.append(" , ITE.CODLOCALORIG AS CODLOCAL ");
        } else {
            sql.append(" , 0 AS CODLOCAL ");
        }
        if(utilizarControle) {
            sql.append(" , ITE.CONTROLE ");
        } else {
            sql.append(" , ' ' AS CONTROLE ");
        }

        sql.append(" , MAX(ITE.DTREF) AS DTREF ");
        sql.append(" FROM TGFUVC ITE ");
        if("M".equals(usarEmpresa)) {
            sql.append(" INNER JOIN TSIEMP EMP ON EMP.CODEMP = ITE.CODEMP  ");
        }
        sql.append(" WHERE ITE.TIPMOV = 'V' ");

        sql.append(" GROUP BY  ");
        if("M".equals(usarEmpresa)) {
            sql.append(" , NVL(EMP.CODEMPMATRIZ, EMP.CODEMP) ");
        } else if("S".equals(usarEmpresa)) {
            sql.append(", ITE.CODEMP ");
        }

        if(utilizarLocal) {
            sql.append(" , ITE.CODLOCALORIG ");
        }
        if(utilizarControle) {
            sql.append(" , ITE.CONTROLE ");
        }


        Session session = em.unwrap(Session.class);
        List<UltimaVendaResult> rs = session.createSQLQuery(sql.toString())
                .setResultTransformer(Transformers.aliasToBean(UltimaVendaResult.class)).list();

        return rs;
    }

    public void atualizarTGFUVC(
            Boolean temUltVendaSaida,
            Boolean temUltVendaFaturamento,
            int mesesRetroagir
    ) throws Exception {

    StringBuffer queUltVenda = SqlUtils.loadSql("queUltVenda.sql");

    if (temUltVendaSaida) {
        StringUtils.replaceString("/* TEM_ENTSAI INICIO */", "/* TEM_ENTSAI INICIO", queUltVenda);
        StringUtils.replaceString("/* TEM_ENTSAI FIM */", " TEM_ENTSAI FIM */", queUltVenda);
    }
    if (temUltVendaFaturamento) {
        StringUtils.replaceString("/* TEM_MOV INICIO */", "/* TEM_MOV INICIO", queUltVenda);
        StringUtils.replaceString("/* TEM_MOV FIM */", " TEM_MOV FIM */", queUltVenda);
    }
    Calendar fim = Calendar.getInstance();
    fim.setTimeInMillis(System.currentTimeMillis());
    TimeUtils.clearTime(fim);

    if (mesesRetroagir == 0) {
        mesesRetroagir = 1;
    }

    Calendar comeco = Calendar.getInstance();
    comeco.setTimeInMillis(fim.getTimeInMillis());
    comeco.add(Calendar.MONTH, mesesRetroagir * -1);
    comeco.set(Calendar.DATE, 1);
    TimeUtils.clearTime(comeco);

    while (comeco.compareTo(fim) <= 0) {
        Timestamp inicio = new Timestamp(TimeUtils.add(fim.getTimeInMillis(), -10, Calendar.DATE));

      Query q = em.createNativeQuery(queUltVenda.toString())
                        .setParameter("DTINI", inicio)
                        .setParameter("DTFIM", fim);

        q.executeUpdate();

        fim.add(Calendar.DATE, -10);
    }
    }
}
