package br.com.sankhya.commercial.analisegiro.resultmodel;

import br.com.sankhya.commercial.analisegiro.model.ChaveGiro;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class PedidoPendenteResult  {

    private BigDecimal CODPROD;
    private BigDecimal CODEMP;
    private BigDecimal CODLOCAL;
    private char CONTROLE;
    private BigDecimal QTDE;

    public ChaveGiro toChaveGiro() {

        return new ChaveGiro(CODPROD, CODEMP, CODLOCAL, CONTROLE);
    }
}
