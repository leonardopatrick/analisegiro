package br.com.sankhya.commercial.analisegiro.service.impl;

import br.com.sankhya.commercial.analisegiro.configuration.MatrizGiroConfiguracao;
import br.com.sankhya.commercial.analisegiro.core.ParametroContextoRepository;
import br.com.sankhya.commercial.analisegiro.model.ChaveGiro;
import br.com.sankhya.commercial.analisegiro.model.Giro;
import br.com.sankhya.commercial.analisegiro.repository.GiroCustomRepository;
import br.com.sankhya.commercial.analisegiro.resultmodel.PedPenVdaResult;
import br.com.sankhya.commercial.analisegiro.repository.PedidoPendenteRepository;
import br.com.sankhya.commercial.analisegiro.repository.ProdutoRepository;
import br.com.sankhya.commercial.analisegiro.resultmodel.GiroResult;
import br.com.sankhya.commercial.analisegiro.struct.PeriodoGiro;
import br.com.sankhya.commercial.analisegiro.util.BigDecimalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Timestamp;
import java.util.*;

public class CalculoGiro {

    private  MatrizGiroConfiguracao matrizConf = new MatrizGiroConfiguracao();
    private Boolean controlaCustoPorLocal = Boolean.FALSE;
    private String 	strCodProd;
    private String filtroGiro;
    private String filtroEstoque;
    private String filtroPedVdaPend;
    private String filtroPedCpaPend;
    private StringBuffer sqlGroup;
    private StringBuffer sqlChave;
    private Boolean controlaCustoPorControle = Boolean.FALSE;
    private Boolean utilizarLocal = Boolean.FALSE;
    private Boolean utilizarControle = Boolean.FALSE;
    private String usarEmpresa = "N";
    private int nroPeriodos = 0;
    private BigDecimal nuTab = BigDecimal.ONE.negate();
    private Map<BigDecimal, BigDecimal> mapTabPrecoCot = new HashMap<BigDecimal, BigDecimal>();
    private Map<BigDecimal, BigDecimal> mapTabPreco = new HashMap<BigDecimal, BigDecimal>();

    private Collection<BigDecimal> lisProdSemGiro = new ArrayList<BigDecimal>();
    private Map<ChaveGiro, Giro> giros = new HashMap<ChaveGiro, Giro>();
    private CallableStatement stp_Obtem_Preco;

    @Autowired
    ProdutoRepository produtoRepository;

    @Autowired
    ParametroContextoRepository parametroRepo;

    @Autowired
    GiroCustomRepository giroCustomRepository;

    @Autowired
    GiroStrategyInMemory giroRepository;

    @Autowired
    PedidoPendenteRepository pedidoPendenteRepository;

    public void gerar() throws Exception {

        prepararVariaveisComuns();

        lisProdSemGiro.clear();
        if("S".equals(matrizConf.getIncluirSemEstoque())) {
            gerarListaProdutos();
        }

        nroPeriodos = buscarGiro();

        buscarPedVdaPend();
    }

    private  void buscarPedVdaPend(){

        List<PedPenVdaResult> pedPenVdaResults = pedidoPendenteRepository.findPedidosPendentes(
                 utilizarLocal,
                 usarEmpresa,
                 matrizConf,
                 utilizarControle
        );

    }

    private int buscarGiro() throws Exception {
        Giro giro;
        int i = 0;

        Collection<Timestamp[]> periodos = matrizConf.buildPeriodos();

        for (Timestamp[] periodo : periodos) {

            Timestamp inicio = periodo[0];
            Timestamp fim = periodo[1];
            i++;

            List<GiroResult> giroResults = giroCustomRepository.findGirobyQueryCustom(sqlGroup.toString(),
                    usarEmpresa,
                    sqlChave.toString(),
                    matrizConf,
                    inicio,
                    fim
            );

            for (GiroResult giroResult : giroResults) {

                PeriodoGiro perGiro = new PeriodoGiro(giroResult);
                perGiro.setIndice(i);
                perGiro.setDiasUteis(i); //TODO FUNCAO DIAS UTEIS
                giro = giroRepository.findGiroByChaveGiro(
                                            new ChaveGiro(giroResult));

                giro.setLeadTime(
                        BigDecimalUtil.getValueOrZero(giroResult.getLEADTIME()));
                giro.setCodGrupoProd(giroResult.getCODGRUPOPROD());
                giro.setMarca(giroResult.getMARCA());
                giro.setPeso(giroResult.getPESOBRUTO());
                ///giro.addPeriodo(perGiro); //TODO AJUSTAR RELACIONAMENTO PERIODO

                giroRepository.save(giro);
                lisProdSemGiro.remove(giroResult.getCODPROD());

            }
        }
        return i;
    }

    private void prepararVariaveisComuns() throws Exception {

        controlaCustoPorLocal = parametroRepo.getParameterAsBoolean("UTILIZALOCAL");
        controlaCustoPorControle =parametroRepo.getParameterAsBoolean("UTILIZACONTROLE");
        utilizarLocal = "S".equals(matrizConf.getApresentaLocal()) & parametroRepo.getParameterAsBoolean("UTILIZALOCAL");
        utilizarControle = "S".equals(matrizConf.getApresentaControle()) & parametroRepo.getParameterAsBoolean("UTILIZACONTROLE");
        if ("S".equals(matrizConf.getApresentaEmpresa())) {
            usarEmpresa = "S".equals(matrizConf.getApresentaMatriz()) ? "M" : "S";
        } else {
            usarEmpresa = "N";
        }

        if("S".equals(matrizConf.getAgrupaProdAltern())) {
            strCodProd = "Snk_GetProdutoAgrupadoGiro(ITE.CODPROD, 'S')";
        } else if("G".equals(matrizConf.getAgrupaProdAltern())) {
            strCodProd = "Snk_GetProdutoAgrupadoGiro(ITE.CODPROD, 'G')";
        } else {
            strCodProd = "ITE.CODPROD";
        }

        sqlGroup = new StringBuffer();
        sqlGroup.append(" GROUP BY  ");
        sqlGroup.append(strCodProd);

        if("M".equals(usarEmpresa)) {
            sqlGroup.append(" , NVL(EMP.CODEMPMATRIZ, EMP.CODEMP) ");
        } else if("S".equals(usarEmpresa)) {
            sqlGroup.append(", ITE.CODEMP ");
        }

        if(utilizarLocal) {
            sqlGroup.append(" , ITE.CODLOCALORIG ");
        }
        if(utilizarControle) {
            sqlGroup.append(" , ITE.CONTROLE ");
        }

        sqlChave = new StringBuffer();
        sqlChave.append(" SELECT  ");
        sqlChave.append(strCodProd + " AS CODPROD ");

        if("M".equals(usarEmpresa)) {
            sqlChave.append(" , NVL(EMP.CODEMPMATRIZ, EMP.CODEMP) AS CODEMP ");
        } else if("S".equals(usarEmpresa)) {
            sqlChave.append(" , ITE.CODEMP ");
        } else {
            sqlChave.append(" , 0 AS CODEMP ");
        }
        if(utilizarLocal) {
            sqlChave.append(" , ITE.CODLOCALORIG AS CODLOCAL ");
        } else {
            sqlChave.append(" , 0 AS CODLOCAL ");
        }
        if(utilizarControle) {
            sqlChave.append(" , ITE.CONTROLE ");
        } else {
            sqlChave.append(" , ' ' AS CONTROLE ");
        }
        //nuTab = getTabelaPreco(matrizConf.getTabelaPreco());
        // TODO: filtroGiro, filtroEstoque, filtroPedVdaPend, filtroPedCpaPend; = ... ver ProcessadorMatriz prepararFiltros;
    }

    public void gerarListaProdutos() throws Exception {
        //TODO strCodProd
        List<BigDecimal> lista = produtoRepository.listProdutosCalGiro();
        lisProdSemGiro.addAll(lista);
    }
}
