package br.com.sankhya.commercial.analisegiro.resultmodel;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class UltimaVendaResult {

    private String TIPMOV;
    private BigDecimal CODPROD;
    private BigDecimal CODEMP;
    /*CODPROD,
    CODEMP,
    CODLOCALORIG,
    CONTROLE,
    RESERVA,
    DTREF,
    QTDNEG,
    ALIQICMS,
    VLRTOT*/
}
