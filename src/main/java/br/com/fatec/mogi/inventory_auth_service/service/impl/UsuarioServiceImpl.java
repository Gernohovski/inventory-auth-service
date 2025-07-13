package br.com.fatec.mogi.inventory_auth_service.service.impl;

import br.com.fatec.mogi.inventory_auth_service.domain.exception.EmailJaUtilizadoException;
import br.com.fatec.mogi.inventory_auth_service.domain.exception.FuncaoNaoEncontrada;
import br.com.fatec.mogi.inventory_auth_service.domain.exception.LoginInvalidoException;
import br.com.fatec.mogi.inventory_auth_service.domain.exception.UsuarioNaoEncontradoException;
import br.com.fatec.mogi.inventory_auth_service.domain.model.Funcao;
import br.com.fatec.mogi.inventory_auth_service.domain.model.Usuario;
import br.com.fatec.mogi.inventory_auth_service.domain.model.UsuarioFuncao;
import br.com.fatec.mogi.inventory_auth_service.domain.model.valueObjects.Email;
import br.com.fatec.mogi.inventory_auth_service.repository.FuncaoRepository;
import br.com.fatec.mogi.inventory_auth_service.repository.UsuarioFuncaoRepository;
import br.com.fatec.mogi.inventory_auth_service.repository.UsuarioRepository;
import br.com.fatec.mogi.inventory_auth_service.service.AutenticacaoService;
import br.com.fatec.mogi.inventory_auth_service.service.EmailService;
import br.com.fatec.mogi.inventory_auth_service.service.UsuarioService;
import br.com.fatec.mogi.inventory_auth_service.web.dto.request.CadastrarUsuarioRequestDTO;
import br.com.fatec.mogi.inventory_auth_service.web.dto.request.ConfirmarCadastroUsuarioRequestDTO;
import br.com.fatec.mogi.inventory_auth_service.web.dto.request.LoginRequestDTO;
import br.com.fatec.mogi.inventory_auth_service.web.dto.response.LoginResponseDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

	private final EmailService emailService;

	private final AutenticacaoService autenticacaoService;

	private final UsuarioRepository usuarioRepository;

	private final FuncaoRepository funcaoRepository;

	private final UsuarioFuncaoRepository usuarioFuncaoRepository;

	@Override
	@Transactional
	public Usuario cadastrarUsuario(CadastrarUsuarioRequestDTO dto) {
		Funcao funcao = funcaoRepository.findById(dto.getFuncaoId())
			.orElseThrow(() -> new FuncaoNaoEncontrada("Função não encontrada"));
		Usuario usuario = new Usuario(dto.getNome(), dto.getSenha(), dto.getEmail());
		usuarioRepository.findByEmail(usuario.getEmail()).ifPresent(u -> {
			throw new EmailJaUtilizadoException("E-mail já utilizado.");
		});
		var emailEnviado = emailService.enviarEmailConfirmacao(usuario);
		usuario.setAtivo(!emailEnviado);
		Usuario usuarioSalvo = usuarioRepository.save(usuario);
		usuarioFuncaoRepository.save(new UsuarioFuncao(usuarioSalvo, funcao));
		return usuarioSalvo;
	}

	@Override
	@Transactional
	public boolean confirmarCadastro(ConfirmarCadastroUsuarioRequestDTO dto) {
		var usuario = usuarioRepository.findByEmail(new Email(dto.getEmail()))
			.orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário não encontrado."));
		usuario.setAtivo(true);
		usuarioRepository.save(usuario);
		return true;
	}

	@Override
	public LoginResponseDTO login(LoginRequestDTO dto) {
		var usuario = usuarioRepository.findByEmail(new Email(dto.getEmail())).orElseThrow(LoginInvalidoException::new);
		if (!BCrypt.checkpw(dto.getSenha(), usuario.getSenha().getSenha())) {
			throw new LoginInvalidoException();
		}
		return autenticacaoService.gerarAutenticacao(usuario);
	}

}
