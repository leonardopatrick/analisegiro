package br.com.sankhya.commercial.analisegiro.model;

import br.com.sankhya.commercial.analisegiro.resultmodel.EstoqueResult;
import br.com.sankhya.commercial.analisegiro.resultmodel.GiroResult;
import br.com.sankhya.commercial.analisegiro.resultmodel.PedidoPendenteResult;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@Embeddable
public class ChaveGiro implements Serializable {

	@Column(name="CODPROD")
	private BigDecimal codProd;
	@Column(name="CODEMP")
	private BigDecimal codEmp;
	@Column(name="CODLOCAL")
	private BigDecimal codLocal;
	@Column(name="CONTROLE")
	private char controle =  Character.valueOf(' ');

	public ChaveGiro(BigDecimal codProd, BigDecimal codEmp, BigDecimal codLocal, String controle) {
		this.codProd = codProd;
		this.codEmp = BigDecimal.ZERO;
		this.codLocal = BigDecimal.ZERO;
		this.controle = Character.valueOf(' ');
	}

	public ChaveGiro(GiroResult gr)  {
		this.codProd = gr.getCODPROD();
		this.codEmp = gr.getCODEMP();
		this.codLocal = gr.getCODLOCAL();
		this.controle = gr.getCONTROLE();
	}

	public ChaveGiro(PedidoPendenteResult pr) {
		this.codProd = pr.getCODPROD();
		this.codEmp = pr.getCODEMP();
		this.codLocal = pr.getCODLOCAL();
		this.controle = pr.getCONTROLE();
	}

	public ChaveGiro(EstoqueResult pr) {
		this.codProd = pr.getCODPROD();
		this.codEmp = pr.getCODEMP();
		this.codLocal = pr.getCODLOCAL();
		this.controle = pr.getCONTROLE();
	}

	public boolean equals(ChaveGiro chave) {
		return codProd.equals(chave.getCodProd()) &
			codEmp.equals(chave.getCodEmp()) &
			codLocal.equals(chave.getCodLocal()) &
			controle == chave.getControle();
	}

	/*
	public ChaveGiro(BigDecimal codProd) {
		this.codProd = codProd;
		this.codEmp = BigDecimal.ZERO;
		this.codLocal = BigDecimal.ZERO;
		this.controle = " ";
	}
*/
}
