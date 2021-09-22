package br.com.sankhya.commercial.analisegiro.repository;

import br.com.sankhya.commercial.analisegiro.core.MatrizGiroConfiguracao;
import br.com.sankhya.commercial.analisegiro.resultmodel.UltimoCustoResult;
import br.com.sankhya.commercial.analisegiro.util.SqlUtils;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import java.io.IOException;
import java.sql.SQLException;
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
                            ) throws IOException, SQLException {

        StringBuffer queCusto = SqlUtils.loadSql("queCusto.sql");
        /*
        StringBuffer queCusto = new StringBuffer();
        queCusto.append("WITH CUSTO AS (\n" +
                "    SELECT\n" +
                "        CUS.CODPROD    AS CODPROD,\n" +
                "        CUS.CODEMP     AS CODEMP,\n" +
                "        CUS.CODLOCAL   AS CODLOCAL,\n" +
                "        CUS.CONTROLE   AS CONTROLE,\n" +
                "        CUS.DTATUAL    AS DTATUAL,\n" +
                "        CUS.CUSREP     AS CUSREP,\n" +
                "        MAX(CUS.DTATUAL) OVER(\n" +
                "            PARTITION BY CUS.CODPROD, CUS.CODEMP, CUS.CODLOCAL, CUS.CONTROLE\n" +
                "                ORDER BY CUS.CODPROD, CUS.CODEMP, CUS.CODLOCAL, CUS.CONTROLE\n" +
                "        ) ULTDT\n" +
                "    FROM\n" +
                "        TGFCUS CUS\n" +
                ")\n" +
                "SELECT\n" +
                "    CODPROD,\n" +
                "    CODEMP,\n" +
                "    CODLOCAL,\n" +
                "    CONTROLE,\n" +
                "    CUSREP,\n" +
                "    ULTDT,\n" +
                "    DTATUAL\n" +
                "FROM\n" +
                "    CUSTO\n" +
                "WHERE\n" +
                "    DTATUAL = ULTDT");
        // StringBuffer queCusto = SqlUtils.loadSql("queCusto.sql");
        */
      /*  if(!matrizConf.getControlaCustoPorEmpresa())
        StringUtils.replaceString("CN.CODEMP", "0", queCusto);

        if(!matrizConf.getControlaCustoPorLocal())
        StringUtils.replaceString("CN.CODLOCAL", "0", queCusto);

        if(!matrizConf.getControlaCustoPorControle())
        StringUtils.replaceString("CN.CONTROLE", "' '", queCusto);
        */
        Session session = em.unwrap(Session.class);
        List<UltimoCustoResult> rs = session.createNativeQuery(queCusto.toString())
                .setResultTransformer(Transformers.aliasToBean(UltimoCustoResult.class)).list();

        /* */
       /* Connection connection = em.unwrap(SessionImpl.class).connection();

        try (Statement stmt = connection.createStatement();) {

            boolean results = stmt.execute(queCusto.toString());
            int rsCount = 0;

            // Loop through the available result sets.
            do {
                if (results) {
                    ResultSet rs = stmt.getResultSet();
                    rsCount++;

                    // Show data from the result set.
                    System.out.println("RESULT SET #" + rsCount);
                    while (rs.next()) {
                       // System.out.println(rs.getString("LastName") + ", " + rs.getString("FirstName"));
                    }
                }
                System.out.println();
                results = stmt.getMoreResults();
            } while (results);
        }
        // Handle any errors that may have occurred.
        catch (SQLException e) {
            e.printStackTrace();
        }
    */
        return rs;

    }
}
