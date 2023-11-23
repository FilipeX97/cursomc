package br.com.tgid.cursomc.services;

import org.springframework.mail.SimpleMailMessage;

import br.com.tgid.cursomc.domain.Pedido;

public interface EmailService {
	
	void  sendOrderConfirmationEmail(Pedido obj);
	
	void sendEmail(SimpleMailMessage msg);

}
