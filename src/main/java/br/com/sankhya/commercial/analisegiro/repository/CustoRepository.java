package br.com.sankhya.commercial.analisegiro.repository;

import br.com.sankhya.commercial.analisegiro.configuration.MatrizGiroConfiguracao;
import br.com.sankhya.commercial.analisegiro.resultmodel.CustoResult;
import br.com.sankhya.commercial.analisegiro.resultmodel.EstoqueResult;
import br.com.sankhya.commercial.analisegiro.util.BigDecimalUtil;
import br.com.sankhya.commercial.analisegiro.util.StringUtils;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.math.BigDecimal;
import java.util.List;

@SuppressWarnings("ALL")
@Repository
public class CustoRepository {

    private final EntityManager em;

    public CustoRepository(EntityManager em){
        this.em = em;
    }

    public BigDecimal findCusto(        Boolean controlaCustoPorEmpresa,
                                          Boolean controlaCustoPorControle,
                                          Boolean controlaCustoPorLocal,
                                          BigDecimal codProd,
                                          BigDecimal codLocal,
                                          BigDecimal codEmp,
                                          char controle
                            ){
        StringBuffer sqlBuf = new StringBuffer();

      //  sqlBuf.append(" SELECT CUS.CUSREP, CUS.CUSGER, CUS.CUSMED, CUS.CUSMEDICM, CUS.CUSSEMICM, CUS.ENTRADACOMICMS, CUS.ENTRADASEMICMS, CUS.CUSVARIAVEL, CUS.DTATUAL");
        sqlBuf.append(" SELECT CUS.CUSREP ");

        sqlBuf.append(" FROM TGFCUS CUS");
        sqlBuf.append(" WHERE CUS.CODPROD = :CODPROD");
        if (controlaCustoPorEmpresa) {
            sqlBuf.append(" AND CUS.CODEMP = :CODEMP");
        }
        if (controlaCustoPorLocal) {
            sqlBuf.append(" AND CUS.CODLOCAL = :CODLOCAL");
        }
        if (controlaCustoPorControle) {
            sqlBuf.append(" AND CUS.CONTROLE = :CONTROLE");
        }
        sqlBuf.append(" AND CUS.DTATUAL <= SYSDATE");
        sqlBuf.append(" AND CUS.DTATUAL = (SELECT MAX(CN.DTATUAL)");
        sqlBuf.append(" FROM TGFCUS CN");
        sqlBuf.append(" WHERE CN.CODPROD = :CODPROD");
        if (controlaCustoPorEmpresa) {
            sqlBuf.append(" AND CN.CODEMP = :CODEMP");
        }
        if (controlaCustoPorLocal) {
            sqlBuf.append(" AND CN.CODLOCAL = :CODLOCAL");
        }
        if (controlaCustoPorControle) {
            sqlBuf.append(" AND CN.CONTROLE = :CONTROLE");
        }
        sqlBuf.append(" AND CUS.DTATUAL <= SYSDATE)"); // TODO: é suficiente sysdate para oracle e sql server ?
        sqlBuf.append(" ORDER BY CUS.ENTRADACOMICMS DESC");

        Session session = em.unwrap(Session.class);
        NativeQuery nativeSql = session.createNativeQuery(sqlBuf.toString());

        nativeSql.setParameter("CODPROD", codProd);
        if (controlaCustoPorEmpresa) {
            nativeSql.setParameter("CODEMP", BigDecimalUtil.getValueOrZero(codEmp));
        }
        if (controlaCustoPorLocal) {
            nativeSql.setParameter("CODLOCAL", BigDecimalUtil.getValueOrZero(codLocal));
        }
        if (controlaCustoPorControle) {
            nativeSql.setParameter("CONTROLE", (StringUtils.getEmptyAsNull(controle) != null) ? controle : " ");
        }

        BigDecimal custo = BigDecimal.ZERO;

        Object result = null;

        try {
            Object o = nativeSql.getSingleResult();

        }catch (EmptyResultDataAccessException e){
            result = null;
        }catch (NoResultException e){
            result = null;
        }

        if(result!=null){
            custo = (BigDecimal) result;
        }

        return custo;
    }


}
