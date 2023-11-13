package br.com.tgid.cursomc.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.tgid.cursomc.domain.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Integer> {

}
