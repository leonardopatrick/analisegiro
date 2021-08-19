package br.com.sankhya.commercial.analisegiro.repository;

import br.com.sankhya.commercial.analisegiro.core.MatrizGiroConfiguracao;
import br.com.sankhya.commercial.analisegiro.resultmodel.UltimaCompraResult;
import br.com.sankhya.commercial.analisegiro.util.SqlUtils;
import br.com.sankhya.commercial.analisegiro.util.StringUtils;
import br.com.sankhya.commercial.analisegiro.util.TimeUtils;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;



@Repository
public class UltimaCompraRepository {

    @Autowired
    MatrizGiroConfiguracao matrizConf;

    private final EntityManager em;

    public UltimaCompraRepository(EntityManager em){

        this.em = em;
    }

    @SuppressWarnings("unchecked")
    public List<UltimaCompraResult> findUltimaCompra() {

        StringBuffer sql = new StringBuffer();
        sql.append(matrizConf.getSqlChave());

        sql.append(" , MAX(ITE.DTREF) AS DTREF ");
        sql.append(" FROM TGFUVC ITE ");
        if("M".equals(matrizConf.getUsarEmpresa())) {
            sql.append(" INNER JOIN TSIEMP EMP ON EMP.CODEMP = ITE.CODEMP  ");
        }
        sql.append(" WHERE ITE.TIPMOV = 'C' ");

        sql.append(matrizConf.getSqlGroupUltimaVdaCpa());

        Session session = em.unwrap(Session.class);
        List<UltimaCompraResult> rs = session.createSQLQuery(sql.toString())
                .setResultTransformer(Transformers.aliasToBean(UltimaCompraResult.class)).list();

        return rs;
    }

    @Transactional
    public void atualizarTGFUVC(
            Boolean temUltCompraEntrada,
            Boolean temUltCompraMovimento,
            int mesesRetroagir
    ) throws Exception {

    StringBuffer queUltCompra = SqlUtils.loadSql("queUltCompra.sql");

    if (temUltCompraEntrada) {
        StringUtils.replaceString("/* TEM_ENTSAI INICIO */", "/* TEM_ENTSAI INICIO", queUltCompra);
        StringUtils.replaceString("/* TEM_ENTSAI FIM */", " TEM_ENTSAI FIM */", queUltCompra);
    }
    if (temUltCompraMovimento) {
        StringUtils.replaceString("/* TEM_MOV INICIO */", "/* TEM_MOV INICIO", queUltCompra);
        StringUtils.replaceString("/* TEM_MOV FIM */", " TEM_MOV FIM */", queUltCompra);
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
            Timestamp dtIni = new Timestamp(TimeUtils.add(fim.getTimeInMillis(), -10, Calendar.DATE));

            Timestamp dtFim = new Timestamp(fim.getTimeInMillis());

            Query q = em.createNativeQuery(queUltCompra.toString())
                    .setParameter("DTINI", dtIni)
                    .setParameter("DTFIM", dtFim);
            q.executeUpdate();

            fim.add(Calendar.DATE, -10);

        }
    }
}
