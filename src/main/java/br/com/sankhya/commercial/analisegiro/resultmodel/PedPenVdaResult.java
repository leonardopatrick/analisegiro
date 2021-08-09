package br.com.sankhya.commercial.analisegiro.resultmodel;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class PedPenVdaResult {

    private BigDecimal CODPROD;
    private BigDecimal CODEMP;
    private BigDecimal CODLOCAL;
    private char CONTROLE;
    private BigDecimal QTDE;
}
