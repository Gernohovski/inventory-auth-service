package br.com.fatec.mogi.inventory_auth_service.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioFuncaoId implements Serializable {

	private Long usuario;

	private Long funcao;

}
