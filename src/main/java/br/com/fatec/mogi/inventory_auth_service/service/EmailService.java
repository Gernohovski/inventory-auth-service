package br.com.fatec.mogi.inventory_auth_service.service;

import br.com.fatec.mogi.inventory_auth_service.domain.model.Usuario;

public interface EmailService {

	boolean enviarEmailConfirmacao(Usuario usuario);

}
