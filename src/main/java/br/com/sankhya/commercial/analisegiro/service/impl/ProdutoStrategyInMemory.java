package br.com.sankhya.commercial.analisegiro.service.impl;

import br.com.sankhya.commercial.analisegiro.model.Produto;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Service
public class ProdutoStrategyInMemory  {

    private Map<BigDecimal, Produto> produtos = new HashMap<BigDecimal, Produto>();

    public Produto findGiroById(BigDecimal codProd) {
        Produto produto = null;
        if (produtos.containsKey(codProd)) {
            produto = produtos.get(codProd);
        }else{
            produto = new Produto();
            produto.setCodprod(codProd);
            return  produto;
        }
        return produto;
    }
    public void save(Produto produto) {
        produtos.put(produto.getCodprod(), produto);
    }

    public Collection<Produto> findAll(){
        return produtos.values();
    }

    public Map<BigDecimal,Produto> getMapGiros(){
        return produtos;
    }

}
