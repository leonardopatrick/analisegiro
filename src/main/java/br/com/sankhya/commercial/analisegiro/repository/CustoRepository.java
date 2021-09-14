package br.com.sankhya.commercial.analisegiro.repository;

import br.com.sankhya.commercial.analisegiro.core.MatrizGiroConfiguracao;
import br.com.sankhya.commercial.analisegiro.resultmodel.UltimaCompraResult;
import br.com.sankhya.commercial.analisegiro.resultmodel.UltimoCustoResult;
import br.com.sankhya.commercial.analisegiro.util.BigDecimalUtil;
import br.com.sankhya.commercial.analisegiro.util.SqlUtils;
import br.com.sankhya.commercial.analisegiro.util.StringUtils;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@SuppressWarnings("ALL")
@Repository
public class CustoRepository {

    private final EntityManager em;

    public CustoRepository(EntityManager em){
        this.em = em;
    }

    public List<UltimoCustoResult> findCusto(
                                    MatrizGiroConfiguracao matrizConf
                            ) throws IOException {

        StringBuffer queCusto = SqlUtils.loadSql("queCusto.sql");

        if(!matrizConf.getControlaCustoPorEmpresa())
        StringUtils.replaceString("CN.CODEMP", "0", queCusto);

        if(!matrizConf.getControlaCustoPorLocal())
        StringUtils.replaceString("CN.CODLOCAL", "0", queCusto);

        if(!matrizConf.getControlaCustoPorControle())
        StringUtils.replaceString("CN.CONTROLE", "' '", queCusto);

        Session session = em.unwrap(Session.class);
        List<UltimoCustoResult> rs = session.createNativeQuery(queCusto.toString())
                .setResultTransformer(Transformers.aliasToBean(UltimoCustoResult.class)).list();

        return rs;

    }
}
