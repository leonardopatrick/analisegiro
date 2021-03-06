package br.com.sankhya.commercial.analisegiro.model;

import br.com.sankhya.commercial.analisegiro.resultmodel.*;
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

	public ChaveGiro(BigDecimal codProd) {
		this.codProd = codProd;
		this.codEmp = BigDecimal.ZERO;
		this.codLocal = BigDecimal.ZERO;
		this.controle = Character.valueOf(' ');
	}

	public boolean equals(ChaveGiro chave) {
		return codProd.equals(chave.getCodProd()) &
				codEmp.equals(chave.getCodEmp()) &
				codLocal.equals(chave.getCodLocal()) &
				controle == chave.getControle();
	}

	public ChaveGiro(BigDecimal codProd, BigDecimal codEmp, BigDecimal codLocal, char controle) {
		this.codProd = codProd;
		this.codEmp = BigDecimal.ZERO;
		this.codLocal = BigDecimal.ZERO;
		this.controle = Character.valueOf(' ');
	}

}
