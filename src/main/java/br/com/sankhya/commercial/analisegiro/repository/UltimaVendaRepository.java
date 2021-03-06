package br.com.sankhya.commercial.analisegiro.repository;

import br.com.sankhya.commercial.analisegiro.core.MatrizGiroConfiguracao;
import br.com.sankhya.commercial.analisegiro.helper.QueryExecutorSingleResultHelper;
import br.com.sankhya.commercial.analisegiro.resultmodel.EstoqueResult;
import br.com.sankhya.commercial.analisegiro.resultmodel.PedidoPendenteResult;
import br.com.sankhya.commercial.analisegiro.resultmodel.UltimaVendaResult;
import br.com.sankhya.commercial.analisegiro.util.SqlUtils;
import br.com.sankhya.commercial.analisegiro.util.StringUtils;
import br.com.sankhya.commercial.analisegiro.util.TimeUtils;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;
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
    public List<UltimaVendaResult> findUltimaVenda(MatrizGiroConfiguracao matrizConf) {

        StringBuffer sql = new StringBuffer();
        sql.append(matrizConf.getSqlChave());

        sql.append(" , MAX(ITE.DTREF) AS DTREF ");
        sql.append(" FROM TGFUVC ITE ");
        if("M".equals(matrizConf.getUsarEmpresa())) {
            sql.append(" INNER JOIN TSIEMP EMP ON EMP.CODEMP = ITE.CODEMP  ");
        }
        sql.append(" WHERE ITE.TIPMOV = 'V' ");

        sql.append(matrizConf.getSqlGroup());

        Session session = em.unwrap(Session.class);
        List<UltimaVendaResult> rs = session.createSQLQuery(sql.toString())
                .setResultTransformer(Transformers.aliasToBean(UltimaVendaResult.class)).list();
    /*
		Session session = em.unwrap(Session.class);
		NativeQuery q = session.createNativeQuery(sql.toString());
		NativeQuery<UltimaVendaResult> nativeQuery = q.setResultTransformer(Transformers.aliasToBean(PedidoPendenteResult.class));
		List<UltimaVendaResult> rs = nativeQuery.list();
*/
        return rs;
    }

    @Transactional
	public void atualizarTGFUVC(int mesesRetroagir) throws Exception {

			if (existe("COUNT(1) AS QTD", "TGFTOP", "ATUALULTIMAVEND IN ('F', 'G', 'S')")) {
				deletarTGFUVC();
				StringBuffer queUltVenda = SqlUtils.loadSql("queUltVenda.sql");

				if (existe("COUNT(1) AS QTD", "TGFTOP", "ATUALULTIMAVEND ='S' ")) {
					StringUtils.replaceString("/* TEM_ENTSAI INICIO */", "/* TEM_ENTSAI INICIO", queUltVenda);
					StringUtils.replaceString("/* TEM_ENTSAI FIM */", " TEM_ENTSAI FIM */", queUltVenda);
				}
				if (existe( "COUNT(1) AS QTD", "TGFTOP", "ATUALULTIMAVEND ='F' ")) {
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

					Timestamp dtIni = new Timestamp(TimeUtils.add(fim.getTimeInMillis(), -10, Calendar.DATE));
					Timestamp dtFim = new Timestamp(fim.getTimeInMillis());

					Query q = em.createNativeQuery(queUltVenda.toString()).setParameter("DTINI", dtIni)
							.setParameter("DTFIM", dtFim);
					q.executeUpdate();

					fim.add(Calendar.DATE, -10);

				}
		}
	}
	@Transactional
	public void deletarTGFUVC() throws Exception {

			Query deleteNotMax = em.createNativeQuery(
					"DELETE FROM TGFUVC U1 " +
							"WHERE DTREF < (SELECT MAX(DTREF) FROM TGFUVC U2 WHERE U2.CODPROD = U1.CODPROD AND U2.CODLOCALORIG = U1.CODLOCALORIG  " +
							"AND U2.CODEMP = U1.CODEMP AND U2.CONTROLE = U1.CONTROLE AND U2.TIPMOV = U1.TIPMOV)"
			);
			deleteNotMax.executeUpdate();

	}
	private Boolean existe( String campo, String tabela, String condicao){
		return QueryExecutorSingleResultHelper.existe(em, campo, tabela, condicao);
	}
}
