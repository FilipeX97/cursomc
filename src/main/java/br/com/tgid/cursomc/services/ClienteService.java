package br.com.tgid.cursomc.services;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import br.com.tgid.cursomc.domain.Cidade;
import br.com.tgid.cursomc.domain.Cliente;
import br.com.tgid.cursomc.domain.Endereco;
import br.com.tgid.cursomc.domain.enums.Perfil;
import br.com.tgid.cursomc.domain.enums.TipoCliente;
import br.com.tgid.cursomc.dto.ClienteDTO;
import br.com.tgid.cursomc.dto.ClienteNewDTO;
import br.com.tgid.cursomc.repositories.CidadeRepository;
import br.com.tgid.cursomc.repositories.ClienteRepository;
import br.com.tgid.cursomc.repositories.EnderecoRepository;
import br.com.tgid.cursomc.security.UserSS;
import br.com.tgid.cursomc.services.exceptions.AuthorizationException;
import br.com.tgid.cursomc.services.exceptions.DataIntegrityException;
import br.com.tgid.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class ClienteService {
	
	@Autowired
	private BCryptPasswordEncoder pe;
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private CidadeRepository cidadeRepository;
	
	@Autowired
	private EnderecoRepository enderecoRepository;
	
	@Autowired
	private DropboxService dropboxService;
	
	public Cliente find(Integer id) {
		
		UserSS user = UserService.authenticated();
		if(user == null || !user.hasRole(Perfil.ADMIN) && !id.equals(user.getId()))
			throw new AuthorizationException("Acesso negado");
		
		Optional<Cliente> obj = clienteRepository.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: "+id+", Tipo: "+ Cliente.class.getName()));
	}

	@Transactional
	public Cliente insert(Cliente obj) {
		obj.setId(null);
		obj = clienteRepository.save(obj);
		enderecoRepository.saveAll(obj.getEnderecos());
		return obj;
	}

	public Cliente update(Cliente obj) {
		Cliente newObj = find(obj.getId());
		updateData(newObj, obj);
		return clienteRepository.save(newObj);
	}

	public void delete(Integer id) {
		find(id);
		try {
			clienteRepository.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException("Não é possível excluir porque há pedidos relacionados");
		}
	}

	public List<ClienteDTO> findAll() {
		return clienteRepository.findAll().stream().map(x -> new ClienteDTO(x)).collect(Collectors.toList());
	}
	
	public Page<ClienteDTO> findPage(Integer page, Integer linesPorPage, String orderBy, String direction) {
		PageRequest pageRequest = PageRequest.of(page, linesPorPage, Direction.valueOf(direction), orderBy);
		return clienteRepository.findAll(pageRequest).map(x -> new ClienteDTO(x));
	}
	
	public Cliente fromDTO(ClienteDTO objDTO) {
		return new Cliente(objDTO.getId(), objDTO.getNome(), objDTO.getEmail(), null, null, null);
	}
	
	public Cliente fromDTO(ClienteNewDTO objDTO) {
		Cliente cli = new Cliente(null, objDTO.getNome(), objDTO.getEmail(), objDTO.getCpfOuCnpj(), TipoCliente.toEnum(objDTO.getTipo()), pe.encode(objDTO.getSenha()));
		Cidade cid = cidadeRepository.findById(objDTO.getCidadeId()).orElse(null);
		Endereco end = new Endereco(null, objDTO.getLogradouro(), objDTO.getNumero(), objDTO.getComplemento(), objDTO.getBairro(), objDTO.getCep(), cli, cid);
		cli.getEnderecos().add(end);
		cli.getTelefones().add(objDTO.getTelefone1());
		if(objDTO.getTelefone2() != null) {
			cli.getTelefones().add(objDTO.getTelefone2());
		}
		if(objDTO.getTelefone3() != null) {
			cli.getTelefones().add(objDTO.getTelefone3());
		}
		return cli;
	}
	
	private void updateData(Cliente newObj, Cliente obj) {
		newObj.setNome(obj.getNome());
		newObj.setEmail(obj.getEmail());
	}
	
	public URI uploadProfilePicture(MultipartFile multipartFile) {
		return dropboxService.uploadFile(multipartFile);
	}

}
