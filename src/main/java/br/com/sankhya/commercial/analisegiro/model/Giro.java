package br.com.sankhya.commercial.analisegiro.model;

import br.com.sankhya.commercial.analisegiro.configuration.MatrizGiroConfiguracao;
import br.com.sankhya.commercial.analisegiro.util.BigDecimalUtil;
import br.com.sankhya.commercial.analisegiro.util.TimeUtils;
import lombok.*;
import br.com.sankhya.commercial.analisegiro.struct.PeriodoGiro;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.math.RoundingMode;
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
    private BigDecimal estMinGir = BigDecimal.ZERO;
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

    /*
    private BigDecimal partMargCont;
    private BigDecimal curvaMargCont;
    private BigDecimal partTot;
    private BigDecimal curvaTot;
    */
    private Collection<PeriodoGiro> periodos = new ArrayList<PeriodoGiro>();
    public void addPeriodo(PeriodoGiro periodo) {
        periodos.add(periodo);
    }

    public BigDecimal getPartQtd(int indice) {
        BigDecimal result = BigDecimal.ZERO;
        for(PeriodoGiro periodo : periodos) {
            if(periodo.getIndice() == indice) {
                result = periodo.getPartQtd();
            }
        }
        return result;
    }
    public BigDecimal getPartTot(int indice) {
        BigDecimal result = BigDecimal.ZERO;
        for(PeriodoGiro periodo : periodos) {
            if(periodo.getIndice() == indice) {
                result = periodo.getPartTot();
            }
        }
        return result;
    }
    public BigDecimal getPartPeso(int indice) {
        BigDecimal result = BigDecimal.ZERO;
        for(PeriodoGiro periodo : periodos) {
            if(periodo.getIndice() == indice) {
                result = periodo.getPartPeso();
            }
        }
        return result;
    }
    public BigDecimal getPartMargCont(int indice) {
        BigDecimal result = BigDecimal.ZERO;
        for(PeriodoGiro periodo : periodos) {
            if(periodo.getIndice() == indice) {
                result = periodo.getPartMargCont();
            }
        }
        return result;
    }
    public void setCurvaQtd(int indice, String curva) {
        for(PeriodoGiro periodo : periodos) {
            if(periodo.getIndice() == indice) {
                periodo.setCurvaQtd(curva);
            }
        }
    }
    public void setCurvaTot(int indice, String curva) {
        for(PeriodoGiro periodo : periodos) {
            if(periodo.getIndice() == indice) {
                periodo.setCurvaTot(curva);
            }
        }
    }
    public void setCurvaPeso(int indice, String curva) {
        for(PeriodoGiro periodo : periodos) {
            if(periodo.getIndice() == indice) {
                periodo.setCurvaPeso(curva);
            }
        }
    }
    public void setCurvaMargCont(int indice, String curva) {
        for(PeriodoGiro periodo : periodos) {
            if(periodo.getIndice() == indice) {
                periodo.setCurvaMargCont(curva);
                break;
            }
        }
    }

    public void calcular(MatrizGiroConfiguracao matrizConf, BigDecimal nroPeriodos, boolean calcularSugCompraParaEstMax, boolean calcularDiasUteisParaLeadTime, boolean somarLeadTime) throws Exception {
        BigDecimal qtdMinimaDiaUtil = BigDecimal.ZERO;
        BigDecimal qtdMaximaDiaUtil = BigDecimal.ZERO;
        BigDecimal qtdVenda;
        BigDecimal qtdVendaDiaUtil;
        BigDecimal totQtdVenda = BigDecimal.ZERO;
        BigDecimal totQtdVendaDiaUtil = BigDecimal.ZERO;

        int indMenor = 1;
        int indMaior = 1;
        int indMenorDiaUtil = 1;
        int indMaiorDiaUtil = 1;

        int cont = 0;
        for(PeriodoGiro periodo :  periodos){
            qtdVenda = periodo.getQtde();//TODO: Verificar se realmente é esse valor esperado para a variavel
            qtdVendaDiaUtil = periodo.getQtdVendDiaUtil();
            cont++;
            if(cont == 1) {
                qtdMinima = qtdVenda;
                qtdMaxima = qtdVenda;
                qtdMinimaDiaUtil = qtdVendaDiaUtil;
                qtdMaximaDiaUtil = qtdVendaDiaUtil;
            }

            totQtdVenda = totQtdVenda.add(qtdVenda);
            totQtdVendaDiaUtil = totQtdVendaDiaUtil.add(qtdVendaDiaUtil);
            if(qtdMinima.compareTo(qtdVenda) > 0) {
                qtdMinima = qtdVenda;
            }
            if(qtdMaxima.compareTo(qtdVenda) < 0) {
                qtdMaxima = qtdVenda;
            }
            if(qtdMinimaDiaUtil.compareTo(qtdVendaDiaUtil) > 0) {
                qtdMinimaDiaUtil = qtdVendaDiaUtil;
            }
            if(qtdMaximaDiaUtil.compareTo(qtdVendaDiaUtil) < 0) {
                qtdMaximaDiaUtil = qtdVendaDiaUtil;
            }
            popTotal = popTotal.add(periodo.getPopularidade());
        }
        qtdTotal = totQtdVenda;
        qtdMedia = totQtdVenda.divide(nroPeriodos);

        if(cont>4) { // TODO: conversar com Bruce, média precisa no mínimo de 3 ocorrencias.
            if(matrizConf.getDesprezarPeriodoGiro().intValue() == 1) {  // Maior
                totQtdVendaDiaUtil = totQtdVendaDiaUtil.subtract(qtdMaximaDiaUtil);
                totQtdVenda = totQtdVenda.subtract(qtdMaxima);
                cont--;
            } else if(matrizConf.getDesprezarPeriodoGiro().intValue() == 2 ) { // Menor
                totQtdVendaDiaUtil = totQtdVendaDiaUtil.subtract(qtdMinimaDiaUtil);
                totQtdVenda = totQtdVenda.subtract(qtdMinima);
                cont--;
            } else if(matrizConf.getDesprezarPeriodoGiro().intValue() == 3) {  // Ambos
                totQtdVendaDiaUtil = totQtdVendaDiaUtil.subtract(qtdMaximaDiaUtil);
                totQtdVenda = totQtdVenda.subtract(qtdMaxima);
                cont--;
                totQtdVendaDiaUtil = totQtdVendaDiaUtil.subtract(qtdMinimaDiaUtil);
                totQtdVenda = totQtdVenda.subtract(qtdMinima);
                cont--;
            }
        }

        estMin = estMin.add(estMin.multiply(matrizConf.getPercAcrescimoSugestao()).divide(BigDecimalUtil.CEM_VALUE));
        BigDecimal estoq = estoque.add(pedCpaPend).subtract(pedVdaPend);

        BigDecimal divisor;
        if("S".equals(matrizConf.getEstMinIncluiVendaZero())) { // TODO: não deveria usar q AUMENTA o GIRO e pode levar a comprar produto desnecessário, EXCETO para produtos que começaram a venda depois do início do período analisado
            divisor = nroPeriodos;
        } else {
            divisor = BigDecimal.valueOf(cont);
        }

        if(somarLeadTime) {
            leadTime = leadTime.add(matrizConf.getDiasEstocagem());
        } else if(leadTime.compareTo(BigDecimal.ZERO) == 0) {
            leadTime =  leadTime.add(matrizConf.getDiasEstocagem());
        }

        sugCompra = BigDecimal.ZERO;
        sugCompraGir = BigDecimal.ZERO;
        if(!"N".equals(permCompProd)) {
            if(estoq.compareTo(estMin) < 0) {
                if(calcularSugCompraParaEstMax) {
                    sugCompra = estMax.subtract(estoq).setScale(0, RoundingMode.HALF_UP);
                } else {
                    sugCompra = estMin.subtract(estoq).setScale(0, RoundingMode.HALF_UP);
                }
                if(sugCompra.compareTo(BigDecimal.ZERO) == 0) {
                    sugCompra = BigDecimal.ONE;
                }
            }

            if(divisor.compareTo(BigDecimal.ZERO) != 0) {
                Timestamp hoje = TimeUtils.getNow();
                giroDiario = totQtdVendaDiaUtil.divide(divisor).setScale(3, RoundingMode.HALF_UP);
                if(giroDiario.compareTo(BigDecimal.ZERO) != 0) {
                    durEst = estoq.divide(giroDiario).intValue();
                    durEstPosCpa = estoq.add(sugCompra).divide(giroDiario).intValue();

                    //TODO: Ajustar Feriado
                    /*if(calcularDiasUteisParaLeadTime) {
                        estMinGir = giroDiario.multiply(BigDecimal.valueOf(
                                FeriadoUtil.getDiasUteis(hoje, TimeUtils.dataAddDay(hoje, leadTime.intValue()), chave.getCodEmp())));
                    } else {*/
                        estMinGir = giroDiario.multiply(BigDecimal.valueOf(leadTime.intValue()));
                   // }
                    BigDecimal estMinGirComp = estMinGir.add(estMinGir.multiply(matrizConf.getPercAcrescimoSugestao()).divide(BigDecimalUtil.CEM_VALUE)).setScale(0,RoundingMode.HALF_UP).abs();
                    if(estoq.compareTo(estMinGirComp) < 0) {
                        sugCompraGir = estMinGirComp.subtract(estoq);
                        if(sugCompraGir.compareTo(BigDecimal.ZERO) == 0) {
                            sugCompraGir = BigDecimal.ONE;
                        }
                    }
                    durEstPosCpaGir = estoq.add(sugCompraGir).divide(giroDiario).setScale(0, RoundingMode.HALF_UP).intValue();
                    if(estMin.compareTo(BigDecimal.ZERO) != 0) {
                        durEstSeg = estMin.divide(giroDiario).setScale(0, RoundingMode.HALF_UP).intValue();
                    }
                    datPontoPed = TimeUtils.dataAddDay(hoje,  durEst - durEstSeg - leadTime.intValue() - 1);
                    datPrevEntrega = TimeUtils.dataAddDay(hoje,  durEst - durEstSeg - 1);
                }
            }
        }
    }
}