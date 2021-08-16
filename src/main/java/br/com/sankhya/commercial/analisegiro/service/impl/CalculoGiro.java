package br.com.sankhya.commercial.analisegiro.service.impl;

import br.com.sankhya.commercial.analisegiro.configuration.MatrizGiroConfiguracao;
import br.com.sankhya.commercial.analisegiro.core.MGEParameters;
import br.com.sankhya.commercial.analisegiro.model.ChaveGiro;
import br.com.sankhya.commercial.analisegiro.model.Giro;
import br.com.sankhya.commercial.analisegiro.model.Produto;
import br.com.sankhya.commercial.analisegiro.resultmodel.*;
import br.com.sankhya.commercial.analisegiro.repository.*;
import br.com.sankhya.commercial.analisegiro.struct.PeriodoGiro;
import br.com.sankhya.commercial.analisegiro.util.BigDecimalUtil;
import org.modelmapper.ModelMapper;
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
    private Boolean controlaCustoPorEmpresa = Boolean.FALSE;
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
    MGEParameters MGEParameters;

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

    @Autowired
    CustoRepository custoRepository;


    @Autowired
    ModelMapper modelMapper;

    public void gerar() throws Exception {

        prepararVariaveisComuns();

        lisProdSemGiro.clear();
        if("S".equals(matrizConf.getIncluirSemEstoque())) {
            gerarListaProdutos();
        }
        nroPeriodos = buscarGiro();

        buscarPedVdaPend();
        buscarPedCpaVdaPend();
        buscarEstoques();
        buscarUltimaCompra();
        buscarUltimaVenda();
        acrescentarSemGiro();
        calcular();

    }

    public void gerarListaProdutos() throws Exception {
        //TODO: Adicionar a String strCodProd para buscar no repositório
        List<BigDecimal> lista = produtoRepository.listProdutosCalGiro();
        lisProdSemGiro.addAll(lista);
    }

    private void prepararVariaveisComuns() throws Exception {
        /*  TODO: Ajustar Chave dos parametros abaixo
            subtrairDaSugestaoAQtdeBloqueadaNoWMS = parametroRepo.getParameterAsBoolean("subtrair.da.sug.compra.qtd.bloq.wms");
            subtrairDoEsotqueAReserva = parametroRepo.getParameterAsBoolean("subtrair.do.estoque.a.reserva"); // criar param no xml.
        */
        controlaCustoPorLocal = MGEParameters.asBoolean("UTILIZALOCAL");
        controlaCustoPorControle = MGEParameters.asBoolean("UTILIZACONTROLE");
        controlaCustoPorEmpresa  = MGEParameters.asBoolean("UTILIZACONTROLE"); //TODO: Custo por empresa
        utilizarLocal = "S".equals(matrizConf.getApresentaLocal()) & MGEParameters.asBoolean("UTILIZALOCAL");
        utilizarControle = "S".equals(matrizConf.getApresentaControle()) & MGEParameters.asBoolean("UTILIZACONTROLE");
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

            for (GiroResult item : giroResults) {

                PeriodoGiro perGiro = new PeriodoGiro(item);
                perGiro.setIndice(i);
                perGiro.setDiasUteis(i); //TODO FUNCAO DIAS UTEIS
                giro = giroRepository.findGiroByChaveGiro(item);

                giro.setLeadTime(
                        BigDecimalUtil.getValueOrZero(item.getLEADTIME()));
                giro.setCodGrupoProd(item.getCODGRUPOPROD());
                giro.setMarca(item.getMARCA());
                giro.setPeso(item.getPESOBRUTO());
                giro.addPeriodo(perGiro); //TODO AJUSTAR RELACIONAMENTO PERIODO

                giroRepository.save(giro);
                lisProdSemGiro.remove(item.getCODPROD());

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
            Giro giro = giroRepository.findGiroByChaveGiro(item);
            //Giro giro = giroRepository.findGiroByChaveGiro(new ChaveGiro(item));
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
            Giro giro = giroRepository.findGiroByChaveGiro(item);
           // Giro giro = giroRepository.findGiroByChaveGiro(new ChaveGiro(item));
            giro.setPedCpaPend(item.getQTDE());
            giroRepository.save(giro);
            lisProdSemGiro.remove(item.getCODPROD());
        }
    }

    private  void buscarEstoques(){

        List<EstoqueResult> estoqueResults = estoqueRepository.findEstoque(
                 subtrairDaSugestaoAQtdeBloqueadaNoWMS,
                 subtrairDoEsotqueAReserva,
                 matrizConf,
                 sqlChave.toString(),
                 sqlGroup.toString(),
                 filtroEstoque
        );

        for (EstoqueResult item :  estoqueResults ){
            Giro giro = giroRepository.findGiroByChaveGiro(item);
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
        int  mesesRetroagir = 1;/*parametroRepo.getParameterAsInt("UTILIZALOCAL");*/ //TODO: verificar qual parametro é responsável por armazenar os dias a retroceder.

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
            Giro giro = giroRepository.findGiroByChaveGiro(item);
            giro.setUltVenda(item.getDTREF());
            giroRepository.save(giro);
            lisProdSemGiro.remove(item.getCODPROD());
        }

        //TODO: Ajustar a aplicação dos filtros.
        //if("S".equals(matrizConf.getIncluirSemEstoque()))
    }

    private void buscarUltimaCompra() throws Exception {

        Boolean temUltVenda = singleQueryExecutor.existe("COUNT(1) AS QTD", "TGFTOP","ATUALULTIMAVEND IN ('F', 'G', 'S')" );
        Boolean temUltVendaSaida = singleQueryExecutor.existe("COUNT(1) AS QTD", "TGFTOP","ATUALULTIMAVEND ='S' " );
        Boolean temUltVendaFaturamento = singleQueryExecutor.existe("COUNT(1) AS QTD", "TGFTOP","ATUALULTIMAVEND ='F' " );
        int  mesesRetroagir = 1;/*parametroRepo.getParameterAsInt("UTILIZALOCAL");*/ //TODO: Verificar qual parametro é responsável pelo dias a retroceder

        if(temUltVenda)
            ultimaVendaRepository.atualizarTGFUVC(
                    temUltVendaSaida,
                    temUltVendaFaturamento,
                    mesesRetroagir);

        List<UltimaCompraResult> ultimaCompraResults = ultimaCompraRepository.findUltimaCompra(
                matrizConf,
                usarEmpresa,
                utilizarLocal,
                utilizarControle
        );

        for (UltimaCompraResult item :  ultimaCompraResults) {
            Giro giro = giroRepository.findGiroByChaveGiro(item);
            giro.setUltCompra( item.getDTREF());
            giro.setQtdUltCompra(item.getQTDNEG());
            giro.setAliqCred(item.getALIQICMS());
            giro.setVlrUltCompra(item.getVLRTOT());
            giroRepository.save(giro);
           // lisProdSemGiro.remove(item.getCODPROD());
        }

        //TODO: Ajustar a inclusão dos filstros
        //if("S".equals(matrizConf.getIncluirSemEstoque()))
        //TODO: Verificar o pq de utilizar a chave anterior --> chaveAnt = new ChaveGiro(rs);
    }

    private void acrescentarSemGiro() {
        for(BigDecimal codProd : lisProdSemGiro){
            Giro giro = giroRepository.findGiroByChaveGiro(new ChaveGiro(codProd));
            giroRepository.save(giro);
        }
        lisProdSemGiro.clear();
    }

    private void calcular() throws Exception {

        Boolean custoRepDaTabCotacao = MGEParameters.asBoolean("TABCOTFORMTZ");
        Boolean calcularSugCompraParaEstMax = MGEParameters.asBoolean("SUGCOMPMIMAMTZ");
        Boolean calcularDiasUteisParaLeadTime = MGEParameters.asBoolean("CONSDIASUTEIS");
        Boolean somarLeadTime = MGEParameters.asBoolean("SOMALEADTIME");

        Boolean temTGFPMA = Boolean.FALSE;

        if(custoRepDaTabCotacao) {
            temTGFPMA =  singleQueryExecutor.existe("COUNT(1) AS QTD","TGFPMA", "") ;
        }

        for(Giro giro : giroRepository.findAll()) {
            String marca;
          Optional<Produto> produtoOptional =  produtoRepository.findById(giro.getChave().getCodProd());
            if(produtoOptional.isPresent()){
                Produto produto = produtoOptional.get();
                if(giro.getEstMin() == null) {
                    giro.setEstMin(produto.getEstmin());
                }
                if(giro.getEstMax() == null) {
                    giro.setEstMax(produto.getEstmax());
                }
                giro.setDescMax(produto.getDescmax());
                giro.setCodParcForn(produto.getCodparcforn());
                giro.setPermCompProd(produto.getPermcompprod());
                giro.setDtAlterProduto(produto.getDtalter());
                marca = produto.getMarca();
            }

            BigDecimal dobCustoRep = BigDecimal.ZERO;
            BigDecimal dobCustoAtual = BigDecimal.ZERO;

            if(custoRepDaTabCotacao) {
               /* BigDecimal nuTabCot = getTabPrecoCot(giro.getCodParcForn());
                if(nuTabCot.compareTo(BigDecimal.ZERO) >= 0) {
                    dobCustoRep = obtemPreco(nuTabCot, giro.getCodProd(), giro.getCodLocal(), giro.getControle());
                    if(temTGFPMA) {
                        giro.setPercDescFornecedor(getPercDescFornecedor(giro.getCodProd(), giro.getCodParcForn(), marca));
                    }
                }*/
            }

           BigDecimal custo =  custoRepository.findCusto(
                                    controlaCustoPorEmpresa,
                                    controlaCustoPorControle,
                                    controlaCustoPorLocal,
                                    giro.getChave().getCodProd(),
                                    giro.getChave().getCodLocal(),
                                    giro.getChave().getCodEmp(),
                                    giro.getChave().getControle()
                                    );
            giro.setCustoGer(custo);
            giro.setCustoRep(custo); //TODO: Ajustar para pegar o objeto de custo e entrar mais a fundo na logica do repositorio

            Boolean temProdutoGenerico = singleQueryExecutor.existe("COUNT(1) AS QTD", "TGFGXE","CODPROD = " + giro.getChave().getCodProd().toString() );
            if(temProdutoGenerico){
                giro.setDescMax(BigDecimal.ZERO);
            }/*else  if(nuTab != null) {
				giro.setVlrTabPreco(obtemPreco(nuTab, giro.getCodProd(), giro.getCodLocal(), giro.getControle()));
			}*/ //TODO: Por hora não estamos implementando a busca na tabela de preço por conta do custo.

            giro.calcular(matrizConf, BigDecimal.valueOf(nroPeriodos), calcularSugCompraParaEstMax, calcularDiasUteisParaLeadTime, somarLeadTime);
        }
    }
}
