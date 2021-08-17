package br.com.sankhya.commercial.analisegiro.elasticsearch.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.io.Serializable;
import java.sql.Timestamp;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "sankhya_tsipar")

public class ParametroElastic implements Serializable {

    @Id
    private String chave;

    private String texto;
    private String descricao;
    private String tipo;
    private String logico;
    private Integer inteiro;
    private Double numdec;
    private Timestamp data;
}