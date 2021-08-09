package br.com.sankhya.commercial.analisegiro.service.impl;

import br.com.sankhya.commercial.analisegiro.configuration.MatrizGiroConfiguracao;
import br.com.sankhya.commercial.analisegiro.core.ParametroContextoRepository;
import br.com.sankhya.commercial.analisegiro.model.ChaveGiro;
import br.com.sankhya.commercial.analisegiro.model.Giro;
import br.com.sankhya.commercial.analisegiro.resultmodel.*;
import br.com.sankhya.commercial.analisegiro.repository.*;
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
    private StringBuffer sqlGroup = new StringBuffer();;
    private StringBuffer sqlChave = new StringBuffer();;
    private Boolean controlaCustoPorControle = Boolean.FALSE;
    private Boolean utilizarLocal = Boolean.FALSE;
    private Boolean utilizarControle = Boolean.FALSE;
    private Boolean subtrairDaSugestaoAQtdeBloqueadaNoWMS = Boolean.FALSE;
    private Boolean subtrairDoEsotqueAReserva =  Boolean.FALSE; ; // criar param no xml.

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

    //@Autowired
    //PedidoPendenteStrategyInMemory pedPenVdaStrategyInMemory;

    @Autowired
    EstoqueRepository estoqueRepository;

    @Autowired
    UltimaVendaRepository ultimaVendaRepository;

    @Autowired
    UltimaCompraRepository ultimaCompraRepository;

    @Autowired
    SingleQueryExecutor  singleQueryExecutor;


    public void gerar() throws Exception {

      //  prepararVariaveisComuns();

        lisProdSemGiro.clear();
        if("S".equals(matrizConf.getIncluirSemEstoque())) {
     //       gerarListaProdutos();
        }
      //  nroPeriodos = buscarGiro();

       // buscarPedVdaPend();
       /// buscarPedCpaVdaPend();
       /// buscarEstoques();
        buscarUltimaVenda();

    }

    public void gerarListaProdutos() throws Exception {
        //TODO strCodProd
        List<BigDecimal> lista = produtoRepository.listProdutosCalGiro();
        lisProdSemGiro.addAll(lista);
    }

    private void prepararVariaveisComuns() throws Exception {
        //TODO AJUSTAR CHAVE
       // subtrairDaSugestaoAQtdeBloqueadaNoWMS = parametroRepo.getParameterAsBoolean("subtrair.da.sug.compra.qtd.bloq.wms");
      //  subtrairDoEsotqueAReserva = parametroRepo.getParameterAsBoolean("subtrair.do.estoque.a.reserva"); // criar param no xml.

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

    private  void buscarPedVdaPend(){
        //pedPenVdaStrategyInMemory.deleteAll();
        // TODO FILTRO PED VENDA
        List<PedidoPendenteResult> pedPenResults = pedidoPendenteRepository.findPedidosPendentes(
                 utilizarLocal,
                 usarEmpresa,
                 matrizConf,
                 utilizarControle
        );

   for (PedidoPendenteResult item :  pedPenResults ){
            Giro giro = giroRepository.findGiroByChaveGiro(new ChaveGiro(item));
            giro.setPedVdaPend(item.getQTDE());
            giroRepository.save(giro);
            lisProdSemGiro.remove(item.getCODPROD());
        }
    }

    private  void buscarPedCpaVdaPend(){

        //TODO FILTRO PED COMPRA
        List<PedidoPendenteResult> pedPenResults = pedidoPendenteRepository.findPedidosPendentes(
                utilizarLocal,
                usarEmpresa,
                matrizConf,
                utilizarControle
        );
        for (PedidoPendenteResult item :  pedPenResults ){
            Giro giro = giroRepository.findGiroByChaveGiro(new ChaveGiro(item));
            giro.setPedCpaPend(item.getQTDE());
            giroRepository.save(giro);
            lisProdSemGiro.remove(item.getCODPROD());
        }
    }

    private  void buscarEstoques(){

        List<EstoqueResult> estoqueResults = estoqueRepository.findEstoques(
                 subtrairDaSugestaoAQtdeBloqueadaNoWMS,
                 subtrairDoEsotqueAReserva,
                 matrizConf,
                 sqlChave.toString(),
                 sqlGroup.toString(),
                 filtroEstoque
        );

        for (EstoqueResult item :  estoqueResults ){
            Giro giro = giroRepository.findGiroByChaveGiro(new ChaveGiro(item));
            giro.setEstMin(item.getESTMIN());
            giro.setEstMax(item.getESTMAX());
            giro.setEstoque(item.getESTOQUE());
            giro.setWmsBloqueado(item.getWMSBLOQUEADO());
            giroRepository.save(giro);
            lisProdSemGiro.remove(item.getCODPROD());
        }
    }

    private void buscarUltimaVenda() throws Exception {

        Boolean temUltVenda = singleQueryExecutor.existe("COUNT(1) AS QTD", "TGFTOP","ATUALULTIMAVEND IN ('F', 'G', 'S')" );
        Boolean temUltVendaSaida = singleQueryExecutor.existe("COUNT(1) AS QTD", "TGFTOP","ATUALULTIMAVEND ='S' " );
        Boolean temUltVendaFaturamento = singleQueryExecutor.existe("COUNT(1) AS QTD", "TGFTOP","ATUALULTIMAVEND ='F' " );
        int  mesesRetroagir = 1;/*parametroRepo.getParameterAsInt("UTILIZALOCAL");*/ //TODO AJUSTAR PARAMETRO

        if(temUltVenda)
            ultimaVendaRepository.atualizarTGFUVC(
                    temUltVendaSaida,
                    temUltVendaFaturamento,
                    mesesRetroagir);

        List<UltimaVendaResult> ultimaVendaResutls = ultimaVendaRepository.findUltimaVenda(
                 matrizConf,
                 usarEmpresa,
                 utilizarLocal,
                 utilizarControle
        );

        for (UltimaVendaResult item :  ultimaVendaResutls ) {
            Giro giro = giroRepository.findGiroByChaveGiro(new ChaveGiro(item));
            giro.setUltVenda(item.getDTREF());
            giroRepository.save(giro);
            lisProdSemGiro.remove(item.getCODPROD());
        }

        //TODO APLICAR FILTRO
        //if("S".equals(matrizConf.getIncluirSemEstoque()))
    }

    private void buscarUltimaCompra() throws Exception {

        Boolean temUltVenda = singleQueryExecutor.existe("COUNT(1) AS QTD", "TGFTOP","ATUALULTIMAVEND IN ('F', 'G', 'S')" );
        Boolean temUltVendaSaida = singleQueryExecutor.existe("COUNT(1) AS QTD", "TGFTOP","ATUALULTIMAVEND ='S' " );
        Boolean temUltVendaFaturamento = singleQueryExecutor.existe("COUNT(1) AS QTD", "TGFTOP","ATUALULTIMAVEND ='F' " );
        int  mesesRetroagir = 1;/*parametroRepo.getParameterAsInt("UTILIZALOCAL");*/ //TODO AJUSTAR PARAMETRO

        if(temUltVenda)
            ultimaVendaRepository.atualizarTGFUVC(
                    temUltVendaSaida,
                    temUltVendaFaturamento,
                    mesesRetroagir);

        List<UltimaCompraResult> ultimaVendaResutls = ultimaCompraRepository.findUltimaCompra(
                matrizConf,
                usarEmpresa,
                utilizarLocal,
                utilizarControle
        );

        for (UltimaCompraResult item :  ultimaVendaResutls ) {
            Giro giro = giroRepository.findGiroByChaveGiro(new ChaveGiro(item));
            giro.setUltCompra( item.getDTREF());
            giro.setQtdUltCompra(item.getQTDNEG());
            giro.setAliqCred(item.getALIQICMS());
            giro.setVlrUltCompra(item.getVLRTOT());
            giroRepository.save(giro);
            lisProdSemGiro.remove(item.getCODPROD());
        }

        //TODO APLICAR FILTRO
        //if("S".equals(matrizConf.getIncluirSemEstoque()))
        //TODO 	chaveAnt = new ChaveGiro(rs);
    }

}
