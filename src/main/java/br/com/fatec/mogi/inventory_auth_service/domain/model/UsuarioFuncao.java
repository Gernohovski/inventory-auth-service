package br.com.fatec.mogi.inventory_auth_service.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@IdClass(UsuarioFuncaoId.class)
public class UsuarioFuncao {

	@Id
	@ManyToOne
	@JoinColumn(name = "usuario_id")
	private Usuario usuario;

	@Id
	@ManyToOne
	@JoinColumn(name = "funcao_id")
	private Funcao funcao;

}
