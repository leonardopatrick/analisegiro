package br.com.sankhya.commercial.analisegiro.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import br.com.sankhya.commercial.analisegiro.util.BigDecimalUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class PeriodoGiro {
	
	private int diasUteis = 1;
	private int indice; // TODO: de 1 a 12
	private BigDecimal qtde = BigDecimal.ZERO;
	private BigDecimal vlrTot = BigDecimal.ZERO;
	private BigDecimal custoVenda = BigDecimal.ZERO;
	private BigDecimal lucro = BigDecimal.ZERO;
	private BigDecimal vlrGastoVar = BigDecimal.ZERO;
	private BigDecimal vlrMargCont = BigDecimal.ZERO;
	private BigDecimal popularidade = BigDecimal.ZERO;

	private BigDecimal partQtd = BigDecimal.ZERO;
	private BigDecimal partTot = BigDecimal.ZERO;
	private BigDecimal partLucro = BigDecimal.ZERO;
	private BigDecimal partGastoVar = BigDecimal.ZERO;
	private BigDecimal partMargCont = BigDecimal.ZERO;
	private BigDecimal partPeso = BigDecimal.ZERO;
	
	private String curvaQtd = "C";
	private String curvaTot = "C";
	private String curvaPeso = "C";
	private String curvaMargCont = "C";

	public BigDecimal getVlrVendDiaUtil() {
		return qtde.divide(BigDecimal.valueOf(diasUteis));
	}

	public BigDecimal getQtdVendDiaUtil() {
		return qtde.divide(BigDecimal.valueOf(diasUteis));
	}

	public BigDecimal getCustoVendaTotal() {
		return custoVenda.multiply(qtde);
	}

	public BigDecimal getPartMargContFat() {
		if(vlrTot.compareTo(BigDecimal.ZERO) == 0) {
			return BigDecimal.ZERO;
		} else {
			return vlrMargCont.divide(vlrTot).multiply(BigDecimalUtil.CEM_VALUE).setScale(8, RoundingMode.HALF_UP);
		}
	}

	public BigDecimal getPercLucro() {
		if(vlrTot.compareTo(BigDecimal.ZERO) == 0) {
			return BigDecimal.ZERO;
		} else {
			return lucro.divide(vlrTot).multiply(BigDecimalUtil.CEM_VALUE).setScale(8, RoundingMode.HALF_UP);
		}
	}

	public BigDecimal getVlrUnit() {
		if(qtde.equals(BigDecimal.ZERO)) {
			return vlrTot;
		} else {
			return  vlrTot.divide(qtde).setScale(8, RoundingMode.HALF_UP);
		}
	}

	public void setDiasUteis(int valor) {
		if(valor == 0) {
			diasUteis = 1;
		} else {
			diasUteis = valor;
		}
	}

	public BigDecimal getPartGasVarFat() {
		if(vlrTot.compareTo(BigDecimal.ZERO) == 0) {
			return BigDecimal.ZERO;
		} else {
			return vlrGastoVar.divide(vlrTot).multiply(BigDecimalUtil.CEM_VALUE).setScale(8, RoundingMode.HALF_UP);
		}
	}
}
