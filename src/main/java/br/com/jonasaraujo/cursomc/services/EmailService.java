package br.com.jonasaraujo.cursomc.services;

import org.springframework.mail.SimpleMailMessage;
import br.com.jonasaraujo.cursomc.domain.Pedido;

public interface EmailService {

	void sendOrderConfirmationEmail(Pedido obj);
	
	void sendEmail(SimpleMailMessage msg);
	
}
