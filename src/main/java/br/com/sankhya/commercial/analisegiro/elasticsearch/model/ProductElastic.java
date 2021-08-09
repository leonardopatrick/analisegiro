package br.com.sankhya.commercial.analisegiro.elasticsearch.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.io.Serializable;

@Getter 
@Setter 
@NoArgsConstructor
@Document(indexName = "sankhya_tgfpro")
public class ProductElastic implements Serializable {

	@Id
	private long codprod;
	
	private String descrprod;
	private String compldesc;
	private String marca;
	private String referencia;
	private long codgrupoprod;
}
