package br.com.fatec.mogi.inventory_auth_service.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Funcao {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String nome;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "funcao_funcionalidade", joinColumns = @JoinColumn(name = "funcao_id"),
			inverseJoinColumns = @JoinColumn(name = "funcionalidade_id"))
	@Builder.Default
	private Set<Funcionalidade> funcionalidades = new HashSet<>();

}
