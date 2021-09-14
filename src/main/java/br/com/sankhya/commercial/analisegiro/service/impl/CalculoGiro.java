package br.com.sankhya.commercial.analisegiro.service.impl;

import br.com.sankhya.commercial.analisegiro.core.MatrizGiroConfiguracao;
import br.com.sankhya.commercial.analisegiro.core.SKGiro;
import br.com.sankhya.commercial.analisegiro.core.SKParameters;
import br.com.sankhya.commercial.analisegiro.model.ChaveGiro;
import br.com.sankhya.commercial.analisegiro.model.Giro;
import br.com.sankhya.commercial.analisegiro.model.Produto;
import br.com.sankhya.commercial.analisegiro.resultmodel.*;
import br.com.sankhya.commercial.analisegiro.repository.*;
import br.com.sankhya.commercial.analisegiro.struct.PeriodoGiro;
import br.com.sankhya.commercial.analisegiro.util.BigDecimalUtil;

import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;


public class CalculoGiro {


    private String filtroGiro;
    private String filtroEstoque;
    private String filtroPedVdaPend;
    private String filtroPedCpaPend;
   /*
    private Boolean controlaCustoPorLocal = Boolean.FALSE;
    private String 	strCodProd;
    private StringBuffer sqlGroup = new StringBuffer();;
    private StringBuffer sqlChave = new StringBuffer();;
    private Boolean controlaCustoPorControle = Boolean.FALSE;
    private Boolean utilizarLocal = Boolean.FALSE;
    private Boolean utilizarControle = Boolean.FALSE;
    private Boolean subtrairDaSugestaoAQtdeBloqueadaNoWMS = Boolean.FALSE;
    private Boolean subtrairDoEsotqueAReserva =  Boolean.FALSE; ; // criar param no xml.
    private Boolean controlaCustoPorEmpresa = Boolean.FALSE;
    private String usarEmpresa = "N";*/
    private int nroPeriodos = 0;
    private BigDecimal nuTab = BigDecimal.ONE.negate();
    private Map<BigDecimal, BigDecimal> mapTabPrecoCot = new HashMap<BigDecimal, BigDecimal>();
    private Map<BigDecimal, BigDecimal> mapTabPreco = new HashMap<BigDecimal, BigDecimal>();

    private Collection<BigDecimal> lisProdSemGiro = new ArrayList<BigDecimal>();
    private Map<ChaveGiro, Giro> giros = new HashMap<ChaveGiro, Giro>();
    private CallableStatement stp_Obtem_Preco;

    @Autowired
    SKParameters skParameters;

    @Autowired
    SKGiro skGiro;

    @Autowired
    ProdutoRepository produtoRepository;

    @Autowired
    PedidoPendenteRepository pedidoPendenteRepository;

    @Autowired
    EstoqueRepository estoqueRepository;

    @Autowired
    UltimaVendaRepository ultimaVendaRepository;

    @Autowired
    UltimaCompraRepository ultimaCompraRepository;

    @Autowired
    QueryExecutorSingleResult singleQueryExecutor;

    @Autowired
    CustoRepository custoRepository;

    @Autowired
    CalculoCurva calculoCurva;

    @Autowired
    MatrizGiroConfiguracao matrizConf;

    @Autowired
    ProdutoStrategyInMemory produtoStrategyInMemory;


    public void gerar() throws Exception {
        matrizConf.init();

        lisProdSemGiro.clear();
       if("S".equals(matrizConf.getIncluirSemEstoque())) {
           gerarListaProdutos();
       }

        long tempoInicial = System.currentTimeMillis();
        nroPeriodos = buscarGiro();
        long tempoFinal = System.currentTimeMillis();
        System.out.printf("\nbuscarGiro %.3f s%n", (tempoFinal - tempoInicial) / 1000d);

        tempoInicial = System.currentTimeMillis();
        buscarCustos();
        tempoFinal = System.currentTimeMillis();
        System.out.printf("\nbuscarCustos %.3f s%n", (tempoFinal - tempoInicial) / 1000d);

        tempoInicial = System.currentTimeMillis();
        buscarPedVdaPend();
        tempoFinal = System.currentTimeMillis();
        System.out.printf("\nbuscarPedVdaPend %.3f s%n", (tempoFinal - tempoInicial) / 1000d);

        tempoInicial = System.currentTimeMillis();
        buscarPedCpaVdaPend();
        tempoFinal = System.currentTimeMillis();
        System.out.printf("\nbuscarPedCpaVdaPend %.3f s%n", (tempoFinal - tempoInicial) / 1000d);

        tempoInicial = System.currentTimeMillis();
        buscarEstoques();
        tempoFinal = System.currentTimeMillis();
        System.out.printf("\nbuscarEstoques %.3f s%n", (tempoFinal - tempoInicial) / 1000d);

        tempoInicial = System.currentTimeMillis();
        buscarUltimaCompra();
        tempoFinal = System.currentTimeMillis();
        System.out.printf("\nbuscarUltimaCompra %.3f s%n", (tempoFinal - tempoInicial) / 1000d);

        tempoInicial = System.currentTimeMillis();
        buscarUltimaVenda();
        tempoFinal = System.currentTimeMillis();
        System.out.printf("\nbuscarUltimaVenda %.3f s%n", (tempoFinal - tempoInicial) / 1000d);

        tempoInicial = System.currentTimeMillis();
        acrescentarSemGiro();
        tempoFinal = System.currentTimeMillis();
        System.out.printf("\nacrescentarSemGiro %.3f s%n", (tempoFinal - tempoInicial) / 1000d);

        tempoInicial = System.currentTimeMillis();
        calcular();
        tempoFinal = System.currentTimeMillis();
        System.out.printf("\ncalcular %.3f s%n", (tempoFinal - tempoInicial) / 1000d);

        tempoInicial = System.currentTimeMillis();
        calculoCurva.calcularCurvas(skGiro.getMapGiros(), nroPeriodos);
        tempoFinal = System.currentTimeMillis();
        System.out.printf("\ncalcularCurvas %.3f s%n", (tempoFinal - tempoInicial) / 1000d);

    }

    public void buscarCustos() throws IOException, SQLException {

       List<UltimoCustoResult> custos = custoRepository.findCusto(matrizConf);
    }

    public void gerarListaProdutos() throws Exception {
        //TODO: Adicionar a String strCodProd para buscar no repositório

        List<Produto> produtos = produtoRepository.findAllCalGiro();

        for (Produto produto: produtos) {
            lisProdSemGiro.add(produto.getCodprod());
            produtoStrategyInMemory.save(produto);
        }
    }


    private int buscarGiro() throws Exception {
        Giro giro;
        int i = 0;

        Collection<Timestamp[]> periodos = matrizConf.buildPeriodos();

        for (Timestamp[] periodo : periodos) {
            Timestamp inicio = periodo[0];
            Timestamp fim = periodo[1];
            i++;
            List<GiroResult> giroResults = skGiro.findAllByPeriod(inicio, fim);

            for (GiroResult item : giroResults) {

                PeriodoGiro perGiro = new PeriodoGiro(item);
                perGiro.setIndice(i);
                perGiro.setDiasUteis(i); //TODO: Implementar função para dias úteis
                giro = skGiro.findGiroByChaveGiro(item.toChaveGiro());

                giro.setLeadTime(
                        BigDecimalUtil.getValueOrZero(item.getLEADTIME()));
                giro.setCodGrupoProd(item.getCODGRUPOPROD());
                giro.setMarca(item.getMARCA());
                giro.setPeso(item.getPESOBRUTO());
                giro.addPeriodo(perGiro);

                skGiro.save(giro);
                lisProdSemGiro.remove(item.getCODPROD());

            }
        }
        return i;
    }

    private  void buscarPedVdaPend() throws Exception {
        // TODO: Inserir filtro para pedido de venda
    	filtroPedVdaPend = "CAB.TIPMOV IN ('P','V') AND CAB.DTNEG > (SYSDATE - 180)";
        List<PedidoPendenteResult> pedPenResults = pedidoPendenteRepository.findPedidosPendentes(filtroPedVdaPend, matrizConf);

	   for (PedidoPendenteResult item :  pedPenResults ){
	            Giro giro = skGiro.findGiroByChaveGiro(item.toChaveGiro());
	            giro.setPedVdaPend(item.getQTDE());
	            skGiro.save(giro);
	            lisProdSemGiro.remove(item.getCODPROD());
	   }
    }

    private  void buscarPedCpaVdaPend() throws Exception {
        //TODO: Inserir filtro pedido de compra
    	filtroPedCpaPend = "CAB.TIPMOV IN ('O','C') AND CAB.DTNEG > (SYSDATE - 180)";
    	List<PedidoPendenteResult> pedPenResults = pedidoPendenteRepository.findPedidosPendentes(filtroPedCpaPend, matrizConf);

        for (PedidoPendenteResult item :  pedPenResults ){
            Giro giro = skGiro.findGiroByChaveGiro(item.toChaveGiro());
            giro.setPedCpaPend(item.getQTDE());
            skGiro.save(giro);
            lisProdSemGiro.remove(item.getCODPROD());
        }
    }

    private  void buscarEstoques() throws Exception {

        List<EstoqueResult> estoqueResults = estoqueRepository.findEstoque(filtroEstoque, matrizConf);

        for (EstoqueResult item :  estoqueResults ){
            Giro giro = skGiro.findGiroByChaveGiro(item.toChaveGiro());
            giro.setEstMin(item.getESTMIN());
            giro.setEstMax(item.getESTMAX());
            giro.setEstoque(item.getESTOQUE());
            giro.setWmsBloqueado(item.getWMSBLOQUEADO());
            skGiro.save(giro);
            lisProdSemGiro.remove(item.getCODPROD());
        }
    }

    private void buscarUltimaVenda() throws Exception {

        int  mesesRetroagir = 1;/*parametroRepo.getParameterAsInt("UTILIZALOCAL");*/ //TODO: verificar qual parametro é responsável por armazenar os dias a retroceder.

        ultimaVendaRepository.atualizarTGFUVC(mesesRetroagir);

        //TODO: Ajustar a aplicação dos filtros.
        List<UltimaVendaResult> ultimaVendaResutls = ultimaVendaRepository.findUltimaVenda(matrizConf);

        for (UltimaVendaResult item :  ultimaVendaResutls ) {
            Giro giro = skGiro.findGiroByChaveGiro(item.toChaveGiro());
            giro.setUltVenda(item.getDTREF());
            skGiro.save(giro);
            lisProdSemGiro.remove(item.getCODPROD());
        }

    }

    private void buscarUltimaCompra() throws Exception {

        int  mesesRetroagir = 1;/*parametroRepo.getParameterAsInt("UTILIZALOCAL");*/ //TODO: Verificar qual parametro é responsável pelo dias a retroceder

        ultimaCompraRepository.atualizarTGFUVC(mesesRetroagir);

        //TODO: Ajustar a inclusão dos filtros
        List<UltimaCompraResult> ultimaCompraResults = ultimaCompraRepository.findUltimaCompra(matrizConf);

        for (UltimaCompraResult item :  ultimaCompraResults) {
            Giro giro = skGiro.findGiroByChaveGiro(item.toChaveGiro());
            giro.setUltCompra( item.getDTREF());
            giro.setQtdUltCompra(item.getQTDNEG());
            giro.setAliqCred(item.getALIQICMS());
            giro.setVlrUltCompra(item.getVLRTOT());
            skGiro.save(giro);
            lisProdSemGiro.remove(item.getCODPROD());
        }
    }

    private void acrescentarSemGiro() throws Exception {
        for(BigDecimal codProd : lisProdSemGiro){
            Giro giro = skGiro.findGiroByChaveGiro(new ChaveGiro(codProd));
            skGiro.save(giro);
        }
        lisProdSemGiro.clear();
    }

    private void calcular() throws Exception {

        Boolean custoRepDaTabCotacao = skParameters.asBoolean("TABCOTFORMTZ");
        Boolean calcularSugCompraParaEstMax = skParameters.asBoolean("SUGCOMPMIMAMTZ");
        Boolean calcularDiasUteisParaLeadTime = Boolean.FALSE;//MGEParameters.asBoolean("CONSDIASUTEIS"); //TODO: CRIAR PARAMETRO CONSDIASUTEIS
        Boolean somarLeadTime = skParameters.asBoolean("SOMALEADTIME");

        if(calcularDiasUteisParaLeadTime){
            throw new Exception("Configuração calcular dias úteis para lead time não implementada.");
        }

        Boolean temTGFPMA = Boolean.FALSE;

        if(custoRepDaTabCotacao) {
            temTGFPMA =  singleQueryExecutor.existe("COUNT(1) AS QTD","TGFPMA", "1=1") ;
        }

        for(Giro giro : skGiro.findAll()) {

            String marca;

            Produto produto = produtoStrategyInMemory.findGiroById(giro.getChave().getCodProd());
            //Optional<Produto>  produtoOptional =  produtoRepository.findById(giro.getChave().getCodProd());

            if(produto!=null){
               // Produto produto = produtoOptional.get();
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

           BigDecimal custo = BigDecimal.ONE; /*custoRepository.findCusto(
                                    giro.getChave().getCodProd(),
                                    giro.getChave().getCodLocal(),
                                    giro.getChave().getCodEmp(),
                                    giro.getChave().getControle(),
                                    matrizConf
                                    );*/
            giro.setCustoGer(custo);
            giro.setCustoRep(custo); //TODO: Ajustar para pegar o objeto de custo e entrar mais a fundo na logica do repositorio

            Boolean temProdutoGenerico = Boolean.FALSE;// singleQueryExecutor.existe("COUNT(1) AS QTD", "TGFGXE","CODPROD = " + giro.getChave().getCodProd().toString() );
            if(temProdutoGenerico){
                giro.setDescMax(BigDecimal.ZERO);
            }/*else  if(nuTab != null) {
				giro.setVlrTabPreco(obtemPreco(nuTab, giro.getCodProd(), giro.getCodLocal(), giro.getControle()));
			}*/ //TODO: Por hora não estamos implementando a busca na tabela de preço por conta do custo.

            giro.calcular(matrizConf, BigDecimal.valueOf(nroPeriodos), calcularSugCompraParaEstMax, calcularDiasUteisParaLeadTime, somarLeadTime);
            skGiro.save(giro);
        }
    }
}
