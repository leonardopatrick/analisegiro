package br.com.sankhya.commercial.analisegiro.resultmodel;

import br.com.sankhya.commercial.analisegiro.model.ChaveGiro;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class EstoqueResult  {

    private BigDecimal CODPROD;
    private BigDecimal CODEMP;
    private BigDecimal CODLOCAL;
    private char CONTROLE;
    private BigDecimal ESTOQUE;
    private BigDecimal ESTMIN;
    private BigDecimal ESTMAX;
    private BigDecimal WMSBLOQUEADO;


}
