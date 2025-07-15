package br.com.fatec.mogi.inventory_auth_service.utils;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class GeradorCodigo {

	private static final String CARACTERES = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

	private static final SecureRandom random = new SecureRandom();

	public String gerarCodigo() {
		StringBuilder codigo = new StringBuilder(6);
		for (int i = 0; i < 6; i++) {
			int index = random.nextInt(CARACTERES.length());
			codigo.append(CARACTERES.charAt(index));
		}
		return codigo.toString();
	}

}