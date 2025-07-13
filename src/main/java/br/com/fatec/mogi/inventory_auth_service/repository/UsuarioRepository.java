package br.com.fatec.mogi.inventory_auth_service.repository;

import br.com.fatec.mogi.inventory_auth_service.domain.model.Usuario;
import br.com.fatec.mogi.inventory_auth_service.domain.model.valueObjects.Email;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

	Optional<Usuario> findByEmail(Email email);

	@Query(value = """
			    SELECT COUNT(uf) > 0
			    FROM Usuario u
			    JOIN u.usuarioFuncoes uf
			    JOIN uf.funcao f
			    JOIN f.funcionalidades func
			    WHERE u.id = :usuarioId
			    AND func.funcionalidade = :funcionalidade
			""")
	boolean possuiFuncionalidade(@Param("usuarioId") Long usuarioId, @Param("funcionalidade") String funcionalidade);

}
