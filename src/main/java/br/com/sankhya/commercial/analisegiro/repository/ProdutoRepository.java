package br.com.sankhya.commercial.analisegiro.repository;

import br.com.sankhya.commercial.analisegiro.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface ProdutoRepository extends JpaRepository<Produto, BigDecimal> {

    @Query("SELECT DISTINCT codprod FROM Produto p WHERE ativo = 'S' AND permcompprod = 'S' ")
    List<BigDecimal> listProdutosCalGiro();

    Produto findByCodprod(BigDecimal codprod);

}
