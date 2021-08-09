package br.com.sankhya.commercial.analisegiro.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Entity
@Table(name = "TGFPRO")
@Getter 
@Setter 
@NoArgsConstructor
public class Produto implements Serializable {

	@GeneratedValue(strategy =GenerationType.AUTO)
	@Id
	private BigDecimal codprod;

	private String descrprod;
	private String ativo;
	private String percomprod;
	private BigDecimal estmin;
	private BigDecimal estmax;
	private BigDecimal descmax;
	private BigDecimal pesobruto;
	private String permcompprod;
	private Timestamp dtalter;
	private BigDecimal codparcforn;
	private String marca;
}
