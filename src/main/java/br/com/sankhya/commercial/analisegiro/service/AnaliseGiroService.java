package br.com.sankhya.commercial.analisegiro.service;

import br.com.sankhya.commercial.analisegiro.model.ChaveGiro;
import br.com.sankhya.commercial.analisegiro.model.PeriodoGiro;
import br.com.sankhya.commercial.analisegiro.util.TimeUtils;
import br.com.sankhya.commercial.analisegiro.util.BigDecimalUtil;
import org.hibernate.engine.jdbc.spi.JdbcWrapper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;

public class AnaliseGiroService {

    private JdbcWrapper jdbc = null;
    private ChaveGiro chave;
    private Collection<PeriodoGiro> periodos = new ArrayList<PeriodoGiro>();
    private BigDecimal estMinGir = BigDecimal.ZERO;
    private BigDecimal estMaxGir = BigDecimal.ZERO;
    private BigDecimal pedCpaPend = BigDecimal.ZERO;
    private BigDecimal pedVdaPend = BigDecimal.ZERO;
    private BigDecimal sugCompra = BigDecimal.ZERO;
    private BigDecimal sugCompraGir = BigDecimal.ZERO;
    private BigDecimal descMax = BigDecimal.ZERO;
    private BigDecimal estMin;
    private BigDecimal estMax;
    private BigDecimal estoque = BigDecimal.ZERO;
    private BigDecimal wmsBloqueado = BigDecimal.ZERO;
    private BigDecimal qtdTotal = BigDecimal.ZERO;
    private BigDecimal popTotal = BigDecimal.ZERO;
    private BigDecimal qtdMinima = BigDecimal.ZERO;
    private BigDecimal qtdMaxima = BigDecimal.ZERO;
    private BigDecimal qtdMedia = BigDecimal.ZERO;
    private BigDecimal custoGer = BigDecimal.ZERO;
    private BigDecimal custoRep = BigDecimal.ZERO;
    private BigDecimal vlrTabPreco = BigDecimal.ZERO;
    private BigDecimal volAltCompraQtd = BigDecimal.ONE;
    private BigDecimal giroDiario = BigDecimal.ZERO;
    private int durEst = 0;
    private int durEstPosCpa = 0;
    private int durEstPosCpaGir = 0;
    private int durEstSeg = 0;
    private String codVol;
    private String codVolCompra;
    private char undPadrao = 'N'; // TODO: checar se o default é este e para que é usado
    private Timestamp dtAlterProduto;
    private Timestamp ultCompra;
    private Timestamp ultVenda;
    private Timestamp datPontoPed;
    private Timestamp datPrevEntrega;
    private BigDecimal qtdUltCompra = BigDecimal.ZERO;
    private BigDecimal aliqCred = BigDecimal.ZERO;
    private BigDecimal vlrUltCompra = BigDecimal.ZERO;
    private int leadTime = 0;
    private char possuiFam = 'N';
    private String permCompProd = "N";
    private String descrGrupo;
    private String descrLocal;
    private BigDecimal codParcForn;
    private BigDecimal percDescFornecedor = BigDecimal.ZERO;
    private BigDecimal codGrupoProd = BigDecimal.ZERO;
    private String marca;
    private BigDecimal peso;


    public BigDecimal getVlrCredICMS() {
        return aliqCred.multiply(custoRep).divide(BigDecimalUtil.CEM_VALUE).setScale(2, RoundingMode.HALF_UP);
    }
    public BigDecimal getSugCompraCustoGer() {
        return sugCompra.multiply(custoGer).setScale(5, RoundingMode.HALF_UP);
    }
    private char getEstMenMedVend() {
        return (qtdMedia.compareTo(estoque) < 0 ? 'S':'N');
    }
    private int getDiasSemVenda() {
        if(ultVenda != null) {
            return TimeUtils.getDifference(TimeUtils.getNow(), ultVenda);
        } else if(ultCompra != null) {
            return TimeUtils.getDifference(TimeUtils.getNow(), ultCompra);
        } else {
            return TimeUtils.getDifference(TimeUtils.getNow(), dtAlterProduto);
        }
    }

}
