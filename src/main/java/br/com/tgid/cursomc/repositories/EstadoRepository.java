package br.com.tgid.cursomc.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import br.com.tgid.cursomc.domain.Estado;

public interface EstadoRepository extends JpaRepository<Estado, Integer> {
	
	@Transactional(readOnly = true)
	List<Estado> findAllByOrderByNome();

}
