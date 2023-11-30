package br.com.tgid.cursomc.resources;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.tgid.cursomc.dto.CidadeDTO;
import br.com.tgid.cursomc.dto.EstadoDTO;
import br.com.tgid.cursomc.services.CidadeService;
import br.com.tgid.cursomc.services.EstadoService;

@RestController
@RequestMapping(value="/estados")
public class EstadoCidadeResource {
	
	@Autowired
	private EstadoService estadoService;
	
	@Autowired
	private CidadeService cidadeService;
	
	@GetMapping
	public ResponseEntity<List<EstadoDTO>> findAllEstados() {
		return ResponseEntity.ok(estadoService.findAll());
	}
	
	@GetMapping("/{estado_id}/cidades")
	public ResponseEntity<List<CidadeDTO>> findCidades(@PathVariable Integer estado_id) {
		return ResponseEntity.ok(cidadeService.findAllCidadesByEstadoId(estado_id));
	}

}
