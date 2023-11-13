package br.com.tgid.cursomc.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.tgid.cursomc.domain.Produto;

public interface ProdutoRepository extends JpaRepository<Produto, Integer> {

}
