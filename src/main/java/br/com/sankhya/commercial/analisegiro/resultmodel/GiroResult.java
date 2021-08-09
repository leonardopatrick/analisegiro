package br.com.sankhya.commercial.analisegiro.resultmodel;

import br.com.sankhya.commercial.analisegiro.model.ChaveGiro;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Setter
@Getter
public class GiroResult extends ChaveGiro {

    private BigDecimal CODPROD;
    private BigDecimal CODEMP;
    private BigDecimal CODLOCAL;
    private char CONTROLE;
    private BigDecimal LEADTIME;
    private BigDecimal VLRTOT;
    private BigDecimal QTDE;
    private BigDecimal CUSTOGER;
    private BigDecimal MARGEMCONTRIB;
    private BigDecimal CUSTOVARIAVEL;
    private BigDecimal LUCRO;
    private BigDecimal POPULARIDADE;
    private BigDecimal CODGRUPOPROD;
    private String MARCA;
    private BigDecimal PESOBRUTO;

    public GiroResult(BigDecimal codProd) {
        super(codProd);
    }
}
