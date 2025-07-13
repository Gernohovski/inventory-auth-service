package br.com.fatec.mogi.inventory_auth_service.service;

import br.com.fatec.mogi.inventory_auth_service.domain.model.Usuario;
import br.com.fatec.mogi.inventory_auth_service.web.dto.request.CadastrarUsuarioRequestDTO;
import br.com.fatec.mogi.inventory_auth_service.web.dto.request.ConfirmarCadastroUsuarioRequestDTO;

public interface UsuarioService {

	Usuario cadastrarUsuario(CadastrarUsuarioRequestDTO dto);

	boolean confirmarCadastro(ConfirmarCadastroUsuarioRequestDTO dto);

}
