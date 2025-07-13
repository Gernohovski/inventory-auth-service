package br.com.fatec.mogi.inventory_auth_service.service.fake;

import br.com.fatec.mogi.inventory_auth_service.domain.model.Usuario;
import br.com.fatec.mogi.inventory_auth_service.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Profile("test")
public class EmailServiceFake implements EmailService {

	@Override
	public boolean enviarEmailConfirmacao(Usuario usuario) {
		if (usuario.getEmail().getEmail().equals("emailinvalido@gmail.com")) {
			return false;
		}
		String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
		return usuario.getEmail().getEmail().matches(emailRegex);
	}

}
