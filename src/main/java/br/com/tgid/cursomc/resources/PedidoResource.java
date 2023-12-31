package br.com.tgid.cursomc.resources;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.tgid.cursomc.domain.Pedido;
import br.com.tgid.cursomc.services.PedidoService;

@RestController
@RequestMapping(value = "/pedidos")
public class PedidoResource {

	@Autowired
	private PedidoService service;

	@GetMapping(value = "/{id}")
	public ResponseEntity<Pedido> find(@PathVariable Integer id) {
		Pedido obj = service.find(id);
		return ResponseEntity.ok(obj);
	}

	@PostMapping
	public ResponseEntity<Void> insert(@Valid @RequestBody Pedido obj) {
		obj = service.insert(obj);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
		return ResponseEntity.created(uri).build();
	}

	@GetMapping
	public ResponseEntity<Page<Pedido>> findPage(
		@RequestParam(name = "page", defaultValue = "0") Integer page,
		@RequestParam(name = "linesPerPage", defaultValue = "24") Integer linesPerPage,
		@RequestParam(name = "direction", defaultValue = "DESC") String direction,
		@RequestParam(name = "orderBy", defaultValue = "instante") String orderBy) {

		Page<Pedido> listaPedidos = service.findPage(page, linesPerPage, direction, orderBy);

		return ResponseEntity.ok().body(listaPedidos);

	}

}
