package br.com.sankhya.commercial.analisegiro.configuration;

import br.com.sankhya.commercial.analisegiro.util.TimeUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MatrizGiroConfiguracao {
    public static final String SEMANAL = "SEM";
    public static final String MENSAL = "MES";
    public static final String BIMESTRAL = "BIM";
    public static final String TRIMESTRAL = "TRI";
    public static final String SEMESTRAL = "SMT";
    public static final String ANUAL = "ANO";
    public static final String REPOSICAO = "CUSREP";
    public static final String GERENCIAL = "CUSGER";
    public static final String VARIAVEL = "CUSVARIAVEL";
    public static final String MEDIO_SEM_ICMS = "CUSSEMICM";
    public static final String MEDIO_COM_ICMS = "CUSMEDICM";
    public static final String SEM_ICMS = "ENTRADASEMICMS";
    public static final String COM_ICMS = "ENTRADACOMICMS";
    public static final String PRODUTO = "P";
    public static final String GRUPO_PRODUTO = "G";
    public static final String MARCA = "M";
    public static final String EMPRESA = "E";
    public static final String FILTRO_COM_GIRO = "filtroComGiro";
    public static final String FILTRO_SEM_GIRO = "filtroSemGiro";
    public static final String FILTRO_SEM_GIRO_SEM_ESTOQUE = "filtroSemGiroSemEstoque";
    public static final String FILTRO_QTD_ESTOQUE = "filtroQtdEstoque";
    public static final String FILTRO_PED_CPA_PENDENTE = "filtroPedidoCompraPendente";
    public static final String FILTRO_PED_VDA_PENDENTE = "filtroPedidoVendaPendente";
    private static final String DATE_FORMAT = null;

    private Collection<Timestamp[]> periodos;
    private BigDecimal codRel;
    private String resourceID;
    private String descricao;
    private BigDecimal qtdPeriodosDinamicos = BigDecimal.TEN ;
    private String intervaloPeriodoDinamico = "SEM" ;
    private String tipoPeriodo = "M";					// Tipo de períodos
    private String utilizarPeriodosFechados = "N";
    private String agrupaProdAltern="N";			// Agrupar movimentação produto alternativo
    private String incluirSemEstoque="s";			// Incluir produtos sem estoque
    private String incluirSemGiro="N";				// Incluir produtos sem giro
    private String unidadeCompra;				// Usar Unidade de Compra
    private String apresentaEmpresa="N";			// Empresa
    private String apresentaMatriz="N";				// Matriz
    private String apresentaLocal="N";				// Local
    private String apresentaControle;			// Controle
    private BigDecimal desprezarPeriodoGiro;		// Despreza período de giro
    private String detalhe = "E";						// Detalhar de acordo com //TODO VERIFICAR VALOR PADRAO
    private String custo = "CUSSEMICM";						// Considerar custo
    private BigDecimal tabelaPreco;				// Tabela de preço
    private BigDecimal percAcrescimoSugestao;	// % Acréscimo na Sugestão de Compras
    private BigDecimal diasEstocagem;			// Dias ÚTEIS para Estocagem
    private String estMinIncluiVendaZero;		// Período sem vendas influencia a média para estoque mínimo
    private String naoAtuSugestaoZero;	// Atualizar estoque mínimo quando a sugestão for ZERO
    private Map<String, Map<String, Object>> filterParams;
    private String desconsiderarPedidosCompraVenda;// Desconsiderar filtros de Pedidos de Compra/Venda pendentes

    public Collection<Timestamp[]> buildPeriodos() {

        if ("F".equals(tipoPeriodo)) {
            return periodos;
        } else {
            ArrayList<Timestamp[]> periodosGerados = new ArrayList<Timestamp[]>();
            GregorianCalendar cal = new GregorianCalendar();
            TimeUtils.clearTime(cal);
            while (periodosGerados.size() < qtdPeriodosDinamicos.intValue()) {
                if (SEMANAL.equals(intervaloPeriodoDinamico)) {
                    if ("S".equals(utilizarPeriodosFechados)) {
                        Timestamp[] p = new Timestamp[2];
                        cal.add(GregorianCalendar.DAY_OF_WEEK, -1);
                        p[1] = new Timestamp(cal.getTime().getTime());
                        cal.set(GregorianCalendar.DAY_OF_WEEK, GregorianCalendar.SUNDAY);
                        p[0] = new Timestamp(cal.getTime().getTime());
                        periodosGerados.add(0, p);
                    } else {
                        periodosGerados.add(calculatePeriod(GregorianCalendar.DATE, cal, -7, true));
                    }
                } else if (MENSAL.equals(intervaloPeriodoDinamico)) {
                    periodosGerados.add(calculatePeriod(GregorianCalendar.MONTH, cal, -1, true));
                } else if (BIMESTRAL.equals(intervaloPeriodoDinamico)) {
                    periodosGerados.add(calculatePeriod(GregorianCalendar.MONTH, cal, -2, true));
                } else if (TRIMESTRAL.equals(intervaloPeriodoDinamico)) {
                    periodosGerados.add(calculatePeriod(GregorianCalendar.MONTH, cal, -3, true));
                } else if (SEMESTRAL.equals(intervaloPeriodoDinamico)) {
                    periodosGerados.add(calculatePeriod(GregorianCalendar.MONTH, cal, -6, true));
                } else if (ANUAL.equals(intervaloPeriodoDinamico)) {
                    periodosGerados.add(calculatePeriod(GregorianCalendar.YEAR, cal, -1, true));
                }
            }
            periodos = periodosGerados;
            return periodosGerados;
        }
    }

        private Timestamp [] calculatePeriod(int field, GregorianCalendar cal, int amount, boolean discountLastDay){
            Timestamp [] p = new Timestamp [2];
            if(discountLastDay){
                cal.add(GregorianCalendar.DATE, -1);
            }

            p[1] = new Timestamp(cal.getTime().getTime());
            //System.out.println("p[1]:"+TimeUtils.buildPrintableTimestamp(p[1].getTime(), "dd/MM/yyyy"));
            if ("S".equals(utilizarPeriodosFechados)) {
                int meses = 12;
                if (field == GregorianCalendar.YEAR) {
                    getInicioPeriodo(cal,meses);
                } else {
                    meses = amount * -1;
                    getInicioPeriodo(cal,meses);
                }
            } else {
                cal.add(field, amount);
                if(discountLastDay){
                    cal.add(GregorianCalendar.DATE, 1);
                }

            }
            p[0] = new Timestamp(cal.getTime().getTime());

            return p;
        }

    private  void getInicioPeriodo(GregorianCalendar cal, int qtdMeses){
        double periodoAtual = Math.ceil((cal.get(GregorianCalendar.MONTH) + 1d)/qtdMeses);
        int mesInicio = (int) (periodoAtual - 1) * qtdMeses;
        cal.set(GregorianCalendar.DATE, 1);
        cal.set(GregorianCalendar.MONTH, mesInicio);
    }


}
