package br.com.fatec.mogi.inventory_auth_service.domain.exception;

public class LoginInvalidoException extends RuntimeException {

    public LoginInvalidoException() {
        super("E-mail ou senha inválidos.");
    }

}

