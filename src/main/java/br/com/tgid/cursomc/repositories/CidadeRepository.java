package br.com.tgid.cursomc.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.tgid.cursomc.domain.Cidade;

public interface CidadeRepository extends JpaRepository<Cidade, Integer> {

}