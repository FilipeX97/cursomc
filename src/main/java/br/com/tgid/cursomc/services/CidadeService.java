package br.com.tgid.cursomc.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.tgid.cursomc.dto.CidadeDTO;
import br.com.tgid.cursomc.repositories.CidadeRepository;

@Service
public class CidadeService {
	
	@Autowired
	private CidadeRepository repository;
	
	public List<CidadeDTO> findAllCidadesByEstadoId(Integer estadoId) {
		return repository.findCidades(estadoId).stream().map(x -> new CidadeDTO(x.getId(), x.getNome())).collect(Collectors.toList());
	}

}
