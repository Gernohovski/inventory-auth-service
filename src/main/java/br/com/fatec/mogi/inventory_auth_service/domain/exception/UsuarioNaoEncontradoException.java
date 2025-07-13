package br.com.fatec.mogi.inventory_auth_service.domain.exception;

public class UsuarioNaoEncontradoException extends IllegalArgumentException {

	public UsuarioNaoEncontradoException(String message) {
		super(message);
	}

}
