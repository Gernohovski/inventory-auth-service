package br.com.fatec.mogi.inventory_auth_service.domain.model;

import br.com.fatec.mogi.inventory_auth_service.domain.model.valueObjects.Email;
import br.com.fatec.mogi.inventory_auth_service.domain.model.valueObjects.Senha;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Usuario {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String nome;

	private Senha senha;

	private Email email;

	private LocalDateTime dataCriacao;

	private LocalDateTime dataAlteracao;

	public Usuario(String nome, String senha, String email) {
		this.nome = nome;
		this.senha = new Senha(senha);
		this.email = new Email(email);
	}

}
