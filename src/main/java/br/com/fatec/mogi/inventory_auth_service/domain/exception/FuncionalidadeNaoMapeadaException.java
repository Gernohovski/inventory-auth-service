package br.com.fatec.mogi.inventory_auth_service.domain.exception;

public class FuncionalidadeNaoMapeadaException extends RuntimeException {

	public FuncionalidadeNaoMapeadaException() {
		super("Funcionalidade não mapeada.");
	}

}
