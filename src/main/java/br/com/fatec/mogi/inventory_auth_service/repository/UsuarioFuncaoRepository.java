package br.com.fatec.mogi.inventory_auth_service.repository;

import br.com.fatec.mogi.inventory_auth_service.domain.model.UsuarioFuncao;
import br.com.fatec.mogi.inventory_auth_service.domain.model.UsuarioFuncaoId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsuarioFuncaoRepository extends JpaRepository<UsuarioFuncao, UsuarioFuncaoId> {

	List<UsuarioFuncao> findByUsuarioId(Long usuarioId);

}
