package br.com.tgid.cursomc.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.tgid.cursomc.domain.Endereco;

public interface EnderecoRepository extends JpaRepository<Endereco, Integer> {

}
