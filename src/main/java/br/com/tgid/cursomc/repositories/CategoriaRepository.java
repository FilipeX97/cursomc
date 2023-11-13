package br.com.tgid.cursomc.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.tgid.cursomc.domain.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {

}
