package br.com.fatec.mogi.inventory_auth_service.service.impl;

import br.com.fatec.mogi.inventory_auth_service.domain.enums.TipoCache;
import br.com.fatec.mogi.inventory_auth_service.domain.model.Usuario;
import br.com.fatec.mogi.inventory_auth_service.service.EmailService;
import br.com.fatec.mogi.inventory_auth_service.service.RedisService;
import br.com.fatec.mogi.inventory_auth_service.utils.GeradorCodigo;
import io.mailtrap.client.MailtrapClient;
import io.mailtrap.config.MailtrapConfig;
import io.mailtrap.factory.MailtrapClientFactory;
import io.mailtrap.model.request.emails.Address;
import io.mailtrap.model.request.emails.MailtrapMail;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Profile("!test")
public class EmailServiceImpl implements EmailService {

	@Value("${mail.trap.token}")
	private String apiToken;

	@Value("${auth.email.sender}")
	private String emailSender;

	@Value("${confirm.email.code.url}")
	private String confirmUrl;

	@Value("${confirm.template.id}")
	private String confirmTemplateId;

	@Value("${reset.password.template.id}")
	private String resetPasswordTemplateId;

	private MailtrapClient mailtrapClient;

	private final RedisService redisService;

	@PostConstruct
	void postConstruct() {
		MailtrapConfig mailtrapConfig = new MailtrapConfig.Builder().token(apiToken).build();
		mailtrapClient = MailtrapClientFactory.createMailtrapClient(mailtrapConfig);
	}

	@Override
	public boolean enviarEmailConfirmacao(Usuario usuario) {
		try {
			MailtrapMail mail = MailtrapMail.builder()
				.from(new Address(emailSender, "Confirmação e-mail"))
				.to(List.of(new Address(usuario.getEmail().getEmail())))
				.templateUuid(confirmTemplateId)
				.templateVariables(Map.of("name", usuario.getNome(), "url", confirmUrl))
				.build();
			var response = mailtrapClient.send(mail);
			return response.isSuccess();
		}
		catch (Exception e) {
			return false;
		}
	}

	@Override
	public boolean enviarEmailResetSenha(Usuario usuario) {
		try {
			var codigo = GeradorCodigo.gerarCodigo();
			MailtrapMail mail = MailtrapMail.builder()
					.from(new Address(emailSender, "Redefinição de senha"))
					.to(List.of(new Address(usuario.getEmail().getEmail())))
					.templateUuid(resetPasswordTemplateId)
					.templateVariables(Map.of("name", usuario.getNome(), "code", codigo))
					.build();
			var response = mailtrapClient.send(mail);
			redisService.salvar(TipoCache.CODIGO_RESET_SENHA, usuario.getEmail().getEmail().concat(codigo), usuario, 36000L);
			return response.isSuccess();
		}
		catch (Exception e) {
			return false;
		}
	}

}
