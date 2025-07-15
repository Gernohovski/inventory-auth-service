package br.com.fatec.mogi.inventory_auth_service.domain.exception;

public class UsuariosDivergentesException extends RuntimeException {

	public UsuariosDivergentesException() {
		super("O e-mail informado não coincide com a solicitação.");
	}

}
