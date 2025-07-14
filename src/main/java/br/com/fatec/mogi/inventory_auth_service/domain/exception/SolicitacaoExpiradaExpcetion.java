package br.com.fatec.mogi.inventory_auth_service.domain.exception;

public class SolicitacaoExpiradaExpcetion extends RuntimeException {

    public SolicitacaoExpiradaExpcetion() {
        super("A solicitação de redefinição de senha expirou! Solicite novamente.");
    }

}
