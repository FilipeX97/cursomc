package br.com.tgid.cursomc.services.validation;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.tgid.cursomc.domain.Cliente;
import br.com.tgid.cursomc.domain.enums.TipoCliente;
import br.com.tgid.cursomc.dto.ClienteNewDTO;
import br.com.tgid.cursomc.repositories.ClienteRepository;
import br.com.tgid.cursomc.resources.exception.FieldMessage;
import br.com.tgid.cursomc.services.validation.utils.BR;

public class ClienteInsertValidator implements ConstraintValidator<ClienteInsert, ClienteNewDTO> {
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	@Override
	public void initialize(ClienteInsert ann) {
		
	}

	@Override
	public boolean isValid(ClienteNewDTO objDTO, ConstraintValidatorContext context) {
		List<FieldMessage> list = new ArrayList<>();
		
		// inclua os testes aqui, inserindo erros na lista
		
		if (objDTO.getTipo().equals(TipoCliente.PESSOAFISICA.getCod()) && !BR.isValidCPF(objDTO.getCpfOuCnpj()))
			list.add(new FieldMessage("cpfOuCnpj", "CPF inválido"));
		
		if(objDTO.getTipo().equals(TipoCliente.PESSOAJURIDICA.getCod()) && !BR.isValidCNPJ(objDTO.getCpfOuCnpj()))
			list.add(new FieldMessage("cpfOuCnpj", "CNPJ inválido"));
		
		Cliente aux = clienteRepository.findByEmail(objDTO.getEmail());
		if(aux != null)
			list.add(new FieldMessage("email", "Email já existente"));
		
		for (FieldMessage e : list) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(
					e.getMessage()).addPropertyNode(e.getFieldName())
					.addConstraintViolation();
		}
		return list.isEmpty();
	}

}
