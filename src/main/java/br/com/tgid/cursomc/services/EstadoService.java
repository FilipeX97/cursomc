package br.com.tgid.cursomc.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.tgid.cursomc.dto.EstadoDTO;
import br.com.tgid.cursomc.repositories.EstadoRepository;

@Service
public class EstadoService {
	
	@Autowired
	private EstadoRepository repository;
	
	public List<EstadoDTO> findAll() {
		return repository.findAllByOrderByNome().stream().map(x -> new EstadoDTO(x.getId(), x.getNome())).collect(Collectors.toList());
	}

}
