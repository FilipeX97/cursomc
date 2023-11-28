package br.com.tgid.cursomc.services;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import br.com.tgid.cursomc.domain.Cliente;
import br.com.tgid.cursomc.domain.ItemPedido;
import br.com.tgid.cursomc.domain.PagamentoComBoleto;
import br.com.tgid.cursomc.domain.Pedido;
import br.com.tgid.cursomc.domain.enums.EstadoPagamento;
import br.com.tgid.cursomc.domain.enums.Perfil;
import br.com.tgid.cursomc.repositories.ItemPedidoRepository;
import br.com.tgid.cursomc.repositories.PagamentoRepository;
import br.com.tgid.cursomc.repositories.PedidoRepository;
import br.com.tgid.cursomc.security.UserSS;
import br.com.tgid.cursomc.services.exceptions.AuthorizationException;
import br.com.tgid.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class PedidoService {
	
	@Autowired
	private PedidoRepository repository;
	
	@Autowired
	private PagamentoRepository pagamentoRepository;
	
	@Autowired
	private ItemPedidoRepository itemPedidoRepository;
	
	@Autowired
	private BoletoService boletoService;
	
	@Autowired
	private ProdutoService produtoService;

	@Autowired
	private ClienteService clienteService;

	@Autowired
	private EmailService emailService;
	
public Pedido find (Integer id){
		
		UserSS userSS = UserService.authenticated();
		
		Optional<Pedido> pedido =  repository.findById(id);
		
		if(!pedido.equals(null)) {
			if(userSS==null || !userSS.hasRole(Perfil.ADMIN) && 
							   !pedido.get().getCliente().getId().equals(userSS.getId())) {
				
				throw new AuthorizationException("Acesso Negado");
			
			}
		}
		return pedido.orElseThrow(() -> new ObjectNotFoundException(
											"Objeto não encontrado id:" + id +
											". Tipo: " + Pedido.class.getName()));
		
	}
	
	public Pedido insert(Pedido obj) {
		obj.setId(null);
		obj.setCliente(clienteService.find(obj.getCliente().getId()));
		obj.setInstante(new Date());
		obj.getPagamento().setEstado(EstadoPagamento.PENDENTE);
		obj.getPagamento().setPedido(obj);
		if (obj.getPagamento() instanceof PagamentoComBoleto) {
			PagamentoComBoleto pagto = (PagamentoComBoleto) obj.getPagamento();
			boletoService.preencherPagamentoComBoleto(pagto, obj.getInstante());
		}
		obj = repository.save(obj);
		pagamentoRepository.save(obj.getPagamento());
		for (ItemPedido ip : obj.getItens()) {
			ip.setDesconto(0.0);
			ip.setProduto(produtoService.buscar(ip.getProduto().getId()));
			ip.setPreco(ip.getProduto().getPreco());
			ip.setPedido(obj);
		}
		itemPedidoRepository.saveAll(obj.getItens());
		emailService.sendOrderConfirmationHtmlEmail(obj);
		return obj;
	}
	
	public Page<Pedido> findPage(Integer page, Integer linesPerPage, String direction, String orderBy){
		UserSS userSS = UserService.authenticated();
		
		if (userSS == null) {
			throw new AuthorizationException("Acesso negado");
		}
		
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		Cliente cliente = clienteService.find(userSS.getId());
		return repository.findByCliente(cliente, pageRequest);

	}

}
