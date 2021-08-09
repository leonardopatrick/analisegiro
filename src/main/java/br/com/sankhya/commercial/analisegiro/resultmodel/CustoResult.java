package br.com.sankhya.commercial.analisegiro.resultmodel;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Getter
@Setter
public class CustoResult {

    private BigDecimal CODPROD;
    private BigDecimal CODEMP;
    private BigDecimal CODLOCAL;
    private char CONTROLE;
    private BigDecimal CUSREP;
    private BigDecimal CUSGER;
    private BigDecimal CUSMED;
    private BigDecimal CUSMEDICM;
    private BigDecimal CUSSEMICM;
    private BigDecimal ENTRADASEMICMS;
    private BigDecimal CUSVARIAVEL;
    private Timestamp DTATUAL;
}
