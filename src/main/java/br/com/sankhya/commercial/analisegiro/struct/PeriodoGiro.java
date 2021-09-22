package br.com.sankhya.commercial.analisegiro.struct;

import java.math.BigDecimal;
import java.math.RoundingMode;

import br.com.sankhya.commercial.analisegiro.resultmodel.GiroResult;
import br.com.sankhya.commercial.analisegiro.util.BigDecimalUtil;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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
	private BigDecimal vlrVenda = BigDecimal.ZERO;

	private BigDecimal partQtd = BigDecimal.ZERO;
	private BigDecimal partTot = BigDecimal.ZERO;
	private BigDecimal partLucro = BigDecimal.ZERO;
	private BigDecimal partGastoVar = BigDecimal.ZERO;
	private BigDecimal partMargCont = BigDecimal.ZERO;
	private BigDecimal partPeso = BigDecimal.ZERO;

	private BigDecimal qtdVenda = BigDecimal.ZERO;
	
	private String curvaQtd = "C";
	private String curvaTot = "C";
	private String curvaPeso = "C";
	private String curvaMargCont = "C";

	public PeriodoGiro(GiroResult vo){

		vlrVenda = vo.getQTDE(); //TODO VLRVENDA
		qtde = vo.getQTDE();
		vlrTot = vo.getVLRTOT();
		vlrVenda = vo.getQTDE();
		lucro = vo.getLUCRO();
		vlrGastoVar = vo.getCUSTOVARIAVEL();
		vlrMargCont = vo.getMARGEMCONTRIB();
		popularidade = vo.getPOPULARIDADE();
	}
	public PeriodoGiro(){}

	public void setDiasUteis(int valor) {
		if(valor == 0) {
			diasUteis = 1;
		} else {
			diasUteis = valor;
		}
	}

	public BigDecimal getVlrVendDiaUtil() {

		return qtde.divide(BigDecimal.valueOf(diasUteis));
	}

	public BigDecimal getQtdVendDiaUtil() {
		diasUteis = 23;
		return BigDecimal.valueOf(diasUteis); //qtde.divide(BigDecimal.valueOf(diasUteis));
	}

	public BigDecimal getVlrTot() {
		return vlrTot;
	}

	public BigDecimal getVlrUnit() {
		if(qtde.equals(BigDecimal.ZERO)) {
			return vlrTot;
		} else {
			return  vlrTot.divide(qtde).setScale(8, RoundingMode.HALF_UP);
		}
	}

	public BigDecimal getCustoVendaTotal() {

		return custoVenda.multiply(qtde);
	}

	public BigDecimal getPercLucro() {
		if(vlrTot.compareTo(BigDecimal.ZERO) == 0) {
			return BigDecimal.ZERO;
		} else {
			return lucro.divide(vlrTot).multiply(BigDecimalUtil.CEM_VALUE).setScale(8, RoundingMode.HALF_UP);
		}
	}

	public BigDecimal getPartGasVarFat() {
		if(vlrTot.compareTo(BigDecimal.ZERO) == 0) {
			return BigDecimal.ZERO;
		} else {
			return vlrGastoVar.divide(vlrTot).multiply(BigDecimalUtil.CEM_VALUE).setScale(8, RoundingMode.HALF_UP);
		}
	}

	public BigDecimal getPartMargContFat() {
		if(vlrTot.compareTo(BigDecimal.ZERO) == 0) {
			return BigDecimal.ZERO;
		} else {
			return vlrMargCont.divide(vlrTot).multiply(BigDecimalUtil.CEM_VALUE).setScale(8, RoundingMode.HALF_UP);
		}
	}


}
