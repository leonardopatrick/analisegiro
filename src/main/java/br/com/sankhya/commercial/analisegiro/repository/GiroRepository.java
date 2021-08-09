package br.com.sankhya.commercial.analisegiro.repository;

import br.com.sankhya.commercial.analisegiro.model.Giro;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GiroRepository extends JpaRepository<Giro, Long> {
}
