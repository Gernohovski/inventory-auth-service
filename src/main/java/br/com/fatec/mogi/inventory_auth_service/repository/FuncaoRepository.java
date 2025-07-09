package br.com.fatec.mogi.inventory_auth_service.repository;

import br.com.fatec.mogi.inventory_auth_service.domain.model.Funcao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FuncaoRepository extends JpaRepository<Funcao, Long> {

}
