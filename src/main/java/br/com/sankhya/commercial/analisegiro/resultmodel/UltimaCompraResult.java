package br.com.sankhya.commercial.analisegiro.resultmodel;

import br.com.sankhya.commercial.analisegiro.model.ChaveGiro;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Setter
@Getter
public class UltimaCompraResult extends ChaveGiro {

   /* private BigDecimal CODPROD;
    private BigDecimal CODEMP;
    private BigDecimal CODLOCAL;
    private char CONTROLE;*/
    private String TIPMOV;
    private Timestamp DTREF;
    private BigDecimal ALIQICMS;
    private BigDecimal QTDNEG;
    private BigDecimal VLRTOT;

    public UltimaCompraResult(BigDecimal codProd) {
        super(codProd);
    }
}
