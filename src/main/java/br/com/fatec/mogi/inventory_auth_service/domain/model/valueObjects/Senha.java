package br.com.fatec.mogi.inventory_auth_service.domain.model.valueObjects;

import br.com.fatec.mogi.inventory_auth_service.domain.exception.SenhaInvalidaException;
import jakarta.persistence.Embeddable;
import lombok.NoArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;

@NoArgsConstructor
@Embeddable
public class Senha {

	private String senha;

	public Senha(String senha) {
		setSenha(senha);
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		if (senha == null || senha.length() < 8) {
			throw new SenhaInvalidaException("A senha deve ter pelo menos 8 caracteres.");
		}

		if (!senha.matches(".*[A-Z].*") || !senha.matches(".*[a-z].*") || !senha.matches(".*\\d.*")) {
			throw new SenhaInvalidaException("A senha deve conter letras maiúsculas, minúsculas e números.");
		}
		this.senha = BCrypt.hashpw(senha, BCrypt.gensalt());
	}

}
