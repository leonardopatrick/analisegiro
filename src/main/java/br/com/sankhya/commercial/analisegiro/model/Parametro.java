package br.com.sankhya.commercial.analisegiro.model;

import lombok.*;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "TSIPAR")
public class Parametro implements Serializable {

    @Id
    private String chave;

    private String texto;
    private String descricao;
    private String tipo;
    private String logico;
}