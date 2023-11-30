package br.com.tgid.cursomc.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import br.com.tgid.cursomc.domain.Cidade;

public interface CidadeRepository extends JpaRepository<Cidade, Integer> {
	
	@Transactional(readOnly = true)
	@Query("SELECT DISTINCT obj FROM Cidade obj WHERE obj.estado.id = :estadoId ORDER BY obj.nome")
	List<Cidade> findCidades(Integer estadoId);

}
