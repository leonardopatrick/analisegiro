package br.com.sankhya.commercial.analisegiro.repository;

import br.com.sankhya.commercial.analisegiro.resultmodel.UltimaVendaResult;
import br.com.sankhya.commercial.analisegiro.util.SqlUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.io.File;
import java.nio.file.Files;
import java.util.List;


@Repository
public class UltimaVendaRepository {

    private final EntityManager em;

    public UltimaVendaRepository(EntityManager em){
        this.em = em;
    }

    public List<UltimaVendaResult> findUltimaVenda() {
        return null;
    }

    public void atualizarTGFUVC() throws Exception {

    StringBuffer sql = SqlUtils.loadSql("queUltVenda.sql");

    }
}
