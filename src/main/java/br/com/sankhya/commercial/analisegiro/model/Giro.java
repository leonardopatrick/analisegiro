package br.com.sankhya.commercial.analisegiro.model;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "TGFGIR1")
public class Giro {

    @EmbeddedId
    private ChaveGiro chave;

    @Column(name="ESTMINGIR")
    private BigDecimal estminGir = BigDecimal.ZERO;
    @Column(name="ESTMAXGIR")
    private BigDecimal estmaxGir = BigDecimal.ZERO;
    @Column(name="PEDCPAPEND")
    private BigDecimal pedCpaPend = BigDecimal.ZERO;
    @Column(name="PEDVDAPEND")
    private BigDecimal pedVdaPend = BigDecimal.ZERO;
    @Column(name="SUGCOMPRA")
    private BigDecimal sugCompra = BigDecimal.ZERO;
    @Column(name="SUGCOMPRAGIR")
    private BigDecimal sugCompraGir = BigDecimal.ZERO;
    @Column(name="DESCMAX")
    private BigDecimal descMax = BigDecimal.ZERO;
    @Column(name="ESTMIN")
    private BigDecimal estMin;
    @Column(name="ESTMAX")
    private BigDecimal estMax;
    @Column(name="ESTOQUE")
    private BigDecimal estoque = BigDecimal.ZERO;
    @Column(name="WMSBLOQUEADO")
    private BigDecimal wmsBloqueado = BigDecimal.ZERO;
    @Column(name="QTDTOTAL")
    private BigDecimal qtdTotal = BigDecimal.ZERO;
    @Column(name="POPTOTAL")
    private BigDecimal popTotal = BigDecimal.ZERO;
    @Column(name="QTDMINIMA")
    private BigDecimal qtdMinima = BigDecimal.ZERO;
    @Column(name="QTDMAXIMA")
    private BigDecimal qtdMaxima = BigDecimal.ZERO;
    @Column(name="QTDMEDIA")
    private BigDecimal qtdMedia = BigDecimal.ZERO;
    @Column(name="CUSTOGER")
    private BigDecimal custoGer = BigDecimal.ZERO;
    @Column(name="CUSTOREP")
    private BigDecimal custoRep = BigDecimal.ZERO;
    @Column(name="VLRTABPRECO")
    private BigDecimal vlrTabPreco = BigDecimal.ZERO;
    @Column(name="VOLALTCOMPRAQTD")
    private BigDecimal volAltCompraQtd = BigDecimal.ONE;
    @Column(name="GIRODIARIO")
    private BigDecimal giroDiario = BigDecimal.ZERO;
    @Column(name="DUREST")
    private int durEst = 0;
    @Column(name="DURESTPOSCPA")
    private int durEstPosCpa = 0;
    @Column(name="DURESTPOSCPAGIR")
    private int durEstPosCpaGir = 0;
    @Column(name="DURESTSEG")
    private int durEstSeg = 0;
    @Column(name="CODVOL")
    private String codVol;
    @Column(name="COVOLCOMPRA")
    private String codVolCompra;
    @Column(name="UNDPADRAO")
    private char undPadrao = 'N'; // TODO: checar se o default é este e para que é usado
    @Column(name="QTALTERPRODUTO")
    private Timestamp dtAlterProduto;
    @Column(name="ULTCOMPRA")
    private Timestamp ultCompra;
    @Column(name="ULTVENDA")
    private Timestamp ultVenda;
    @Column(name="DATPONTOPED")
    private Timestamp datPontoPed;
    @Column(name="DATPREVENTREGA")
    private Timestamp datPrevEntrega;
    @Column(name="QTDULTCOMPRA")
    private BigDecimal qtdUltCompra = BigDecimal.ZERO;
    @Column(name="ALIQCRED")
    private BigDecimal aliqCred = BigDecimal.ZERO;
    @Column(name="VLRULTCOMPRA")
    private BigDecimal vlrUltCompra = BigDecimal.ZERO;
    @Column(name="LEADTIME")
    private BigDecimal leadTime = BigDecimal.ZERO;
    @Column(name="POSSUIFAM")
    private char possuiFam = 'N';
    @Column(name="PERMCOMPPROD")
    private String permCompProd = "N";
    @Column(name="DESCRGRUPO")
    private String descrGrupo;
    @Column(name="DESCRLOCAL")
    private String descrLocal;
    @Column(name="CODPARCFORN")
    private BigDecimal codParcForn;
    @Column(name="PERDCDESCFORNECEDOR")
    private BigDecimal percDescFornecedor = BigDecimal.ZERO;
    @Column(name="CODGRUPOPROD")
    private BigDecimal codGrupoProd = BigDecimal.ZERO;
    @Column(name="MARCA")
    private String marca;
    @Column(name="PESO")
    private BigDecimal peso;


    //private Collection<PeriodoGiro> periodos = new ArrayList<PeriodoGiro>();

}