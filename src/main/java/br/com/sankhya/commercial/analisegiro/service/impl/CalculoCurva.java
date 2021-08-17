package br.com.sankhya.commercial.analisegiro.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import br.com.sankhya.commercial.analisegiro.configuration.MatrizGiroConfiguracao;
import br.com.sankhya.commercial.analisegiro.core.MGEParameters;
import br.com.sankhya.commercial.analisegiro.model.ChaveGiro;
import br.com.sankhya.commercial.analisegiro.model.Giro;
import br.com.sankhya.commercial.analisegiro.struct.PeriodoGiro;
import br.com.sankhya.commercial.analisegiro.util.BigDecimalUtil;
import org.springframework.beans.factory.annotation.Autowired;

public class CalculoCurva {

	@Autowired
	MGEParameters MGEParameters;

	@Autowired
	MatrizGiroConfiguracao matrizConf;

	private void getTotais(Map<ChaveGiro, Giro> giros, Map<Object, PeriodoGiro[]> totais) throws Exception {
 
		for (Entry<ChaveGiro, Giro> linha : giros.entrySet() ) {
			ChaveGiro chave = linha.getKey();
			Giro giro = linha.getValue();

			Object chaveCurva = getChaveTotalizacao(giro);
			
			PeriodoGiro[] total = totais.get(chaveCurva);
			if(total == null) {
				total = new PeriodoGiro[13];

				for(int i=1; i<=12; i++ ){
					total[i] = new PeriodoGiro();
				}

				totais.put(chaveCurva, total);
			}
			Collection<PeriodoGiro> periodos = giro.getPeriodos();
			for(PeriodoGiro periodo : periodos){
				PeriodoGiro totalizador = total[periodo.getIndice()];  


				totalizador.setQtdVenda(BigDecimalUtil.getValueOrZero(totalizador.getQtdVenda())
									.add(BigDecimalUtil.getValueOrZero(periodo.getQtdVenda())));

				totalizador.setVlrTot(
						BigDecimalUtil.getValueOrZero(totalizador.getVlrTot())
										.add(BigDecimalUtil.getValueOrZero(periodo.getVlrTot())));
				totalizador.setLucro(
						BigDecimalUtil.getValueOrZero(totalizador.getLucro())
								.add(BigDecimalUtil.getValueOrZero(periodo.getLucro())));

				totalizador.setVlrGastoVar(BigDecimalUtil.getValueOrZero(
							totalizador.getVlrGastoVar())
						.add(BigDecimalUtil.getValueOrZero(periodo.getVlrGastoVar())));

				totalizador.setVlrMargCont(BigDecimalUtil.getValueOrZero(totalizador.getVlrMargCont())
							.add(BigDecimalUtil.getValueOrZero(periodo.getVlrMargCont())));
			}
		}
	}
	
	private Object getChaveTotalizacao(Giro giro) {
		Object chaveCurva = BigDecimal.ZERO;
		if("E".equals(matrizConf.getDetalhe())) {
			chaveCurva = giro.getChave().getCodEmp();
		} else if("G".equals(matrizConf.getDetalhe())) {
			chaveCurva = giro.getCodGrupoProd();
		} else if("M".equals(matrizConf.getDetalhe())) {
			chaveCurva = giro.getMarca();
		}
		return chaveCurva;
	}	
	
	private BigDecimal calcularPart(BigDecimal dividendo, BigDecimal divisor) {
		if(divisor.compareTo(BigDecimal.ZERO) != 0) {
			return dividendo.divide(divisor).multiply(BigDecimalUtil.CEM_VALUE);
		} else {
			return BigDecimal.ZERO;
		}
	}
	
	private void calcularParticipacao(Map<ChaveGiro, Giro> giros) throws Exception {
		Map<Object, PeriodoGiro[]> totais = new HashMap<Object, PeriodoGiro[]>();

		getTotais(giros, totais);
 
		for (Entry<ChaveGiro, Giro> linha : giros.entrySet() ) {
			ChaveGiro chave = linha.getKey();
			Giro giro = linha.getValue();

			Object chaveCurva = getChaveTotalizacao(giro);

			PeriodoGiro[] total = totais.get(chaveCurva);

			Collection<PeriodoGiro> periodos = giro.getPeriodos();
			for(PeriodoGiro periodo : periodos){
				PeriodoGiro totalizador = total[periodo.getIndice()];
				
				periodo.setPartQtd(calcularPart(periodo.getQtdVenda(), totalizador.getQtdVenda()));
				periodo.setPartPeso(calcularPart(periodo.getQtdVenda().multiply(giro.getPeso()), totalizador.getQtdVenda()));
				periodo.setPartTot(calcularPart(periodo.getVlrTot(), totalizador.getVlrTot()));
				periodo.setPartLucro(calcularPart(periodo.getLucro(), totalizador.getLucro()));
				periodo.setPartGastoVar(calcularPart(periodo.getVlrGastoVar(), totalizador.getVlrGastoVar()));
				periodo.setPartMargCont(calcularPart(periodo.getVlrMargCont(), totalizador.getVlrMargCont()));
			
			}
			
		}

	}

    private void calcularCurva(List<Entry<ChaveGiro, Giro>> list, int indice, BigDecimal limiteCurvaB, BigDecimal limiteCurvaC ) {

    	Collections.sort(list, new Comparator<Entry<ChaveGiro, Giro>>() {
				@Override
				public int compare(Entry<ChaveGiro, Giro> giro1, Entry<ChaveGiro, Giro> giro2) {
                	int parte1 = 0;
			        if("E".equals(matrizConf.getDetalhe())) {
	                	BigDecimal codEmp1 = giro1.getValue().getChave().getCodEmp();
	                	BigDecimal codEmp2 = giro2.getValue().getChave().getCodEmp();
	                	parte1 = codEmp1.compareTo(codEmp2);
					} else if("G".equals(matrizConf.getDetalhe())) {
	                	BigDecimal codGrupoProd1 = giro1.getValue().getCodGrupoProd();
	                	BigDecimal codGrupoProd2 = giro2.getValue().getCodGrupoProd();
	                	parte1 = codGrupoProd1.compareTo(codGrupoProd2);
					} else if("M".equals(matrizConf.getDetalhe())) {
						String marca1 = giro1.getValue().getMarca();
						String marca2 = giro2.getValue().getMarca();
						parte1 = marca1.compareTo(marca2);
					}
                	if(parte1 == 0) {
                		return -giro1.getValue().getPartQtd(indice).compareTo(giro2.getValue().getPartQtd(indice));
                	} else {
                		return parte1;
                	}
				}
            });

        BigDecimal total = BigDecimal.ZERO;
        String curva = "A";
        for (int i = 0; i < list.size(); i++) {
            Giro giro = list.get(i).getValue();
            giro.setCurvaQtd(indice, curva);
            total = total.add(giro.getPartQtd(indice));
            if(total.compareTo(limiteCurvaB) < 0) {
            	//curva = "A"; // mantem
            } else if(total.compareTo(limiteCurvaC) < 0) {
            	curva = "B";
            } else {
            	break;
            }
        }

        Collections.sort(list, new Comparator<Entry<ChaveGiro, Giro>>() {
				@Override
				public int compare(Entry<ChaveGiro, Giro> giro1, Entry<ChaveGiro, Giro> giro2) {
                	int parte1 = 0;
			        if("E".equals(matrizConf.getDetalhe())) {
	                	BigDecimal codEmp1 = giro1.getValue().getChave().getCodEmp();
	                	BigDecimal codEmp2 = giro2.getValue().getChave().getCodEmp();
	                	parte1 = codEmp1.compareTo(codEmp2);
					} else if("G".equals(matrizConf.getDetalhe())) {
	                	BigDecimal codGrupoProd1 = giro1.getValue().getCodGrupoProd();
	                	BigDecimal codGrupoProd2 = giro2.getValue().getCodGrupoProd();
	                	parte1 = codGrupoProd1.compareTo(codGrupoProd2);
					} else if("M".equals(matrizConf.getDetalhe())) {
						String marca1 = giro1.getValue().getMarca();
						String marca2 = giro2.getValue().getMarca();
						parte1 = marca1.compareTo(marca2);
					}
                	if(parte1 == 0) {
                		return -giro1.getValue().getPartTot(indice).compareTo(giro2.getValue().getPartTot(indice));
                	} else {
                		return parte1;
                	}
				}
            });

        total = BigDecimal.ZERO;
        curva = "A";
        for (int i = 0; i < list.size(); i++) {
            Giro giro = list.get(i).getValue();
            giro.setCurvaTot(indice, curva);
            total = total.add(giro.getPartTot(indice));
            if(total.compareTo(limiteCurvaB) < 0) {
            	//curva = "A"; // mantem
            } else if(total.compareTo(limiteCurvaC) < 0) {
            	curva = "B";
            } else {
            	break;
            }
        }

    	Collections.sort(list, new Comparator<Entry<ChaveGiro, Giro>>() {
				@Override
				public int compare(Entry<ChaveGiro, Giro> giro1, Entry<ChaveGiro, Giro> giro2) {
                	int parte1 = 0;
			        if("E".equals(matrizConf.getDetalhe())) {
	                	BigDecimal codEmp1 = giro1.getValue().getChave().getCodEmp();
	                	BigDecimal codEmp2 = giro2.getValue().getChave().getCodEmp();
	                	parte1 = codEmp1.compareTo(codEmp2);
					} else if("G".equals(matrizConf.getDetalhe())) {
	                	BigDecimal codGrupoProd1 = giro1.getValue().getCodGrupoProd();
	                	BigDecimal codGrupoProd2 = giro2.getValue().getCodGrupoProd();
	                	parte1 = codGrupoProd1.compareTo(codGrupoProd2);
					} else if("M".equals(matrizConf.getDetalhe())) {
						String marca1 = giro1.getValue().getMarca();
						String marca2 = giro2.getValue().getMarca();
						parte1 = marca1.compareTo(marca2);
					}
                	if(parte1 == 0) {
                		return -giro1.getValue().getPartPeso(indice).compareTo(giro2.getValue().getPartPeso(indice));
                	} else {
                		return parte1;
                	}
				}
            });

        total = BigDecimal.ZERO;
        curva = "A";
        for (int i = 0; i < list.size(); i++) {
            Giro giro = list.get(i).getValue();
            giro.setCurvaPeso(indice, curva);
            total = total.add(giro.getPartPeso(indice));
            if(total.compareTo(limiteCurvaB) < 0) {
            	//curva = "A"; // mantem
            } else if(total.compareTo(limiteCurvaC) < 0) {
            	curva = "B";
            } else {
            	break;
            }
        }

    	Collections.sort(list, new Comparator<Entry<ChaveGiro, Giro>>() {
				@Override
				public int compare(Entry<ChaveGiro, Giro> giro1, Entry<ChaveGiro, Giro> giro2) {
                	int parte1 = 0;
			        if("E".equals(matrizConf.getDetalhe())) {
	                	BigDecimal codEmp1 = giro1.getValue().getChave().getCodEmp();
	                	BigDecimal codEmp2 = giro2.getValue().getChave().getCodEmp();
	                	parte1 = codEmp1.compareTo(codEmp2);
					} else if("G".equals(matrizConf.getDetalhe())) {
	                	BigDecimal codGrupoProd1 = giro1.getValue().getCodGrupoProd();
	                	BigDecimal codGrupoProd2 = giro2.getValue().getCodGrupoProd();
	                	parte1 = codGrupoProd1.compareTo(codGrupoProd2);
					} else if("M".equals(matrizConf.getDetalhe())) {
						String marca1 = giro1.getValue().getMarca();
						String marca2 = giro2.getValue().getMarca();
						parte1 = marca1.compareTo(marca2);
					}
                	if(parte1 == 0) {
                		return -giro1.getValue().getPartMargCont(indice).compareTo(giro2.getValue().getPartMargCont(indice));
                	} else {
                		return parte1;
                	}
				}
            });

        total = BigDecimal.ZERO;
        curva = "A";
        for (int i = 0; i < list.size(); i++) {
            Giro giro = list.get(i).getValue();
            giro.setCurvaMargCont(indice, curva);
            total = total.add(giro.getPartMargCont(indice));
            if(total.compareTo(limiteCurvaB) < 0) {
            	//curva = "A"; // mantem
            } else if(total.compareTo(limiteCurvaC) < 0) {
            	curva = "B";
            } else {
            	break;
            }
        }
    }

	public void calcularCurvas(Map<ChaveGiro, Giro> giros, int nroPeriodos) throws Exception {
		calcularParticipacao(giros);

		BigDecimal limCurvaB = BigDecimal.valueOf((Double) MGEParameters.getParameter("LIMCURVA_BPRO"));
		BigDecimal limCurvaC = BigDecimal.valueOf((Double) MGEParameters.getParameter("LIMCURVA_CPRO"));
		BigDecimal limiteCurvaB = BigDecimalUtil.CEM_VALUE.subtract((limCurvaB.add(limCurvaC)));
		BigDecimal limiteCurvaC = BigDecimalUtil.CEM_VALUE.subtract(limCurvaC);
		
        List<Entry<ChaveGiro, Giro>> list = new ArrayList<Entry<ChaveGiro, Giro>>();

        for(Iterator<Entry<ChaveGiro, Giro>> ite = giros.entrySet().iterator(); ite.hasNext();) {
            Entry<ChaveGiro, Giro> entry = ( Entry<ChaveGiro, Giro> ) ite.next();
            list.add(entry);
        }

		for(int indice=1;indice<=nroPeriodos;indice++) {
			calcularCurva(list, indice, limiteCurvaB, limiteCurvaC );
		}
	}
	
}
