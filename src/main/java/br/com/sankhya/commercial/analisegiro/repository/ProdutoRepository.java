package br.com.sankhya.commercial.analisegiro.repository;

import br.com.sankhya.commercial.analisegiro.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    @Query("SELECT DISTINCT codprod FROM Produto p WHERE ativo = 'S' AND permcompprod = 'S' ")
    List<BigDecimal> listProdutosCalGiro();

   /*@Query("SELECT DISTINCT SNK_GETPRODUTOAGRUPADOGIRO(codprod,'S') as codprod FROM Produto p WHERE ativo = 'S' AND permcompprod = 'S' ")
    List<Long> listProdutosCalGiroMatriz();

     @Query("SELECT DISTINCT SNK_GETPRODUTOAGRUPADOGIRO(codprod, 'G') as codprod FROM Produto p WHERE ativo = 'S' AND permcompprod = 'S' ")
    List<Long> listProdutosCalGiroAgrumado();
    */
}
