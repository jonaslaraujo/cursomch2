package br.com.jonasaraujo.cursomc.services.validation;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.jonasaraujo.cursomc.domain.Cliente;
import br.com.jonasaraujo.cursomc.domain.enums.TipoCliente;
import br.com.jonasaraujo.cursomc.dto.ClienteNewDTO;
import br.com.jonasaraujo.cursomc.repositories.ClienteRepository;
import br.com.jonasaraujo.cursomc.resources.exceptions.FieldMessage;
import br.com.jonasaraujo.cursomc.services.validation.utils.BR;

public class ClienteInsertValidator implements ConstraintValidator<ClienteInsert, ClienteNewDTO> {
	@Autowired
	private ClienteRepository repo;
	
	@Override
	public void initialize(ClienteInsert ann) {
	}

	@Override
	public boolean isValid(ClienteNewDTO objDto, ConstraintValidatorContext context) {
		List<FieldMessage> list = new ArrayList<>();
		
		if (objDto.getTipo() == null) {
			list.add(new FieldMessage("tipo", "Tipo não pode ser nulo."));
		}
		
		if (objDto.getTipo().equals(TipoCliente.PESSOAFISICA.getCod()) && !BR.isValidCPF(objDto.getCPFCNPJ())) {
			list.add(new FieldMessage("CPFCNPJ", "CPF inválido."));
		} else if (objDto.getTipo().equals(TipoCliente.PESSOAJURIDICA.getCod()) && !BR.isValidCNPJ(objDto.getCPFCNPJ())) {
			list.add(new FieldMessage("CPFCNPJ", "CNPJ inválido."));
		}
		
		Cliente aux = repo.findByEmail(objDto.getEmail());
		if (aux != null) {
			list.add(new FieldMessage("email", "E-mail já cadastrado."));
		}
		
		for (FieldMessage e : list) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName())
					.addConstraintViolation();
		}
		return list.isEmpty();
	}
}
