package br.com.sankhya.commercial.analisegiro.resultmodel;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Setter
@Getter
public class UltimaVendaResult {

    private BigDecimal CODPROD;
    private BigDecimal CODEMP;
    private BigDecimal CODLOCAL;
    private char CONTROLE;
    private String TIPMOV;
    private Timestamp DTREF;

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