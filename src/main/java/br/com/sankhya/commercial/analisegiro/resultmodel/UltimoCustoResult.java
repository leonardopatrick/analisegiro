package br.com.sankhya.commercial.analisegiro.resultmodel;
import br.com.sankhya.commercial.analisegiro.model.ChaveGiro;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Setter
@Getter
public class UltimoCustoResult {

    private BigDecimal CODPROD;
    private BigDecimal CODEMP;
    private BigDecimal CODLOCAL;
    private String CONTROLE;
    private Timestamp DTATUAL;
    private BigDecimal CUSREP;
    private Timestamp ULTDT;

    public ChaveGiro toChaveGiro() {
        return new ChaveGiro(CODPROD, CODEMP, CODLOCAL, ' ');
    }
}