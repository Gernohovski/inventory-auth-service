package br.com.fatec.mogi.inventory_auth_service.service.impl;

import br.com.fatec.mogi.inventory_auth_service.domain.exception.EmailJaUtilizadoException;
import br.com.fatec.mogi.inventory_auth_service.domain.exception.FuncaoNaoEncontrada;
import br.com.fatec.mogi.inventory_auth_service.domain.model.Funcao;
import br.com.fatec.mogi.inventory_auth_service.domain.model.Usuario;
import br.com.fatec.mogi.inventory_auth_service.domain.model.UsuarioFuncao;
import br.com.fatec.mogi.inventory_auth_service.repository.FuncaoRepository;
import br.com.fatec.mogi.inventory_auth_service.repository.UsuarioFuncaoRepository;
import br.com.fatec.mogi.inventory_auth_service.repository.UsuarioRepository;
import br.com.fatec.mogi.inventory_auth_service.service.UsuarioService;
import br.com.fatec.mogi.inventory_auth_service.web.dto.request.CadastrarUsuarioRequestDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

	private final UsuarioRepository usuarioRepository;

	private final FuncaoRepository funcaoRepository;

	private final UsuarioFuncaoRepository usuarioFuncaoRepository;

	@Override
	@Transactional
	public Usuario cadastrarUsuario(CadastrarUsuarioRequestDTO dto) {
		usuarioRepository.findByEmail(dto.getEmail()).ifPresent(usuario -> {
			throw new EmailJaUtilizadoException("E-mail já utilizado.");
		});
		Funcao funcao = funcaoRepository.findById(dto.getFuncaoId())
			.orElseThrow(() -> new FuncaoNaoEncontrada("Função não encontrada"));
		Usuario usuario = new Usuario(dto.getNome(), dto.getSenha(), dto.getEmail());
		Usuario usuarioSalvo = usuarioRepository.save(usuario);
		usuarioFuncaoRepository.save(new UsuarioFuncao(usuarioSalvo, funcao));
		return usuarioSalvo;
	}

}
