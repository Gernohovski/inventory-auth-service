package br.com.fatec.mogi.inventory_auth_service.service;

import br.com.fatec.mogi.inventory_auth_service.domain.exception.FuncaoNaoEncontrada;
import br.com.fatec.mogi.inventory_auth_service.domain.exception.FuncionalidadeNaoMapeadaException;
import br.com.fatec.mogi.inventory_auth_service.domain.exception.UsuarioNaoAutenticadoException;
import br.com.fatec.mogi.inventory_auth_service.repository.FuncaoRepository;
import br.com.fatec.mogi.inventory_auth_service.web.dto.request.AutorizarUsuarioRequestDTO;
import br.com.fatec.mogi.inventory_auth_service.web.dto.request.CadastrarUsuarioRequestDTO;
import br.com.fatec.mogi.inventory_auth_service.web.dto.request.LoginRequestDTO;
import br.com.fatec.mogi.inventory_auth_service.web.dto.response.LoginResponseDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class AutorizacaoServiceTest {

    @Autowired
    private FuncaoRepository funcaoRepository;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private AutorizacaoService autorizacaoService;

    @Test
    @DisplayName("Deve autorizar usuário com sucesso")
    void deveAutorizarUsuarioComSucesso() {
        String email = UUID.randomUUID().toString().concat("@gmail.com");
        String senha = "Senha123";
        cadastrarUsuario(email, senha, "ADMIN");
        var tokens = fazerLogin(email, senha);
        var autorizado = autorizacaoService.autorizar(new AutorizarUsuarioRequestDTO("/excluir-bem"), tokens.getAccessToken());
        assertTrue(autorizado);
    }

    @Test
    @DisplayName("Não deve autorizar usuário para funcionalidade que ele não tem autorização")
    void naoDeveAutorizarUsuarioFuncionalidadeSemAutorizacao() {
        String email = UUID.randomUUID().toString().concat("@gmail.com");
        String senha = "Senha123";
        cadastrarUsuario(email, senha, "ESTOQUISTA");
        var tokens = fazerLogin(email, senha);
        var autorizado = autorizacaoService.autorizar(new AutorizarUsuarioRequestDTO("/excluir-bem"), tokens.getAccessToken());
        assertFalse(autorizado);
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar obter autorização funcionalidade inexistente")
    void deveLancarExcecaoTentarObterAutorizacaoFuncionalidadeInexistente() {
        assertThrows(FuncionalidadeNaoMapeadaException.class, () -> {
            autorizacaoService.autorizar(new AutorizarUsuarioRequestDTO("invalido"), UUID.randomUUID().toString());
        });
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar obter autorização usuário não autenticado")
    void deveLancarExcecaoObterAcessoUsuarioNaoAutenticado() {
        String email = UUID.randomUUID().toString().concat("@gmail.com");
        String senha = "Senha123";
        cadastrarUsuario(email, senha, "ADMIN");
        assertThrows(UsuarioNaoAutenticadoException.class, () -> {
            autorizacaoService.autorizar(new AutorizarUsuarioRequestDTO("/excluir-bem"), UUID.randomUUID().toString());
        });
    }

    private void cadastrarUsuario(String email, String senha, String funcaoNome) {
        var funcao = funcaoRepository.findByNome(funcaoNome).orElseThrow(() -> new FuncaoNaoEncontrada("Função não encontrada."));
        CadastrarUsuarioRequestDTO cadastrarUsuarioRequestDTO = CadastrarUsuarioRequestDTO.builder()
                .nome("Usuario teste")
                .email(email)
                .senha(senha)
                .funcaoId(funcao.getId())
                .build();
        usuarioService.cadastrarUsuario(cadastrarUsuarioRequestDTO);
    }

    private LoginResponseDTO fazerLogin(String email, String senha) {
        return usuarioService.login(new LoginRequestDTO(email, senha));
    }
}
