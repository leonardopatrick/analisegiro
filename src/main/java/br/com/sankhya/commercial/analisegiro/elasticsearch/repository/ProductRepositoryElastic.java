package br.com.sankhya.commercial.analisegiro.elasticsearch.repository;

import br.com.sankhya.commercial.analisegiro.elasticsearch.model.ProductElastic;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface ProductRepositoryElastic
        extends ElasticsearchRepository<ProductElastic, String> {

    List<ProductElastic> findByDescrprod(String descrprod);
}