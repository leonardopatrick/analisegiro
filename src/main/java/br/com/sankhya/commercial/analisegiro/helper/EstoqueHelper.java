package br.com.sankhya.commercial.analisegiro.helper;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.PreparedStatement;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EstoqueHelper {

    private BigDecimal produto;
    private BigDecimal	empresa;
    private BigDecimal	local;
    private String		strControle;
    private BigDecimal	decEstMIn;
    private BigDecimal	decEstMax;
    private BigDecimal	decEestoque;
    private BigDecimal	decWMSBloqueado;
    private PreparedStatement queEstoque;
    private boolean utilizarLocal;
    private boolean utilizarControle;
}
