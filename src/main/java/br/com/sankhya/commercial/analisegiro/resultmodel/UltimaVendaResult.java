package br.com.sankhya.commercial.analisegiro.resultmodel;

import br.com.sankhya.commercial.analisegiro.model.ChaveGiro;
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

}
