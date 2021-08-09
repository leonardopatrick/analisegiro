package br.com.sankhya.commercial.analisegiro.elasticsearch.repository;

import br.com.sankhya.commercial.analisegiro.elasticsearch.model.ParametroElastic;
import br.com.sankhya.commercial.analisegiro.model.Parametro;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.web.servlet.tags.Param;

import java.util.List;

public interface ParameterRepositoryElastic
        extends ElasticsearchRepository<ParametroElastic, String> {

    List<ParametroElastic> findByChave(String chave);
}