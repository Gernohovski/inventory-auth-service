package br.com.fatec.mogi.inventory_auth_service.domain.exception;

public class UsuarioNaoAutenticadoException extends RuntimeException {

	public UsuarioNaoAutenticadoException() {
		super("Usuário não autenticado.");
	}

}
