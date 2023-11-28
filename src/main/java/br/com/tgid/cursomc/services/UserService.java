package br.com.tgid.cursomc.services;

import org.springframework.security.core.context.SecurityContextHolder;

import br.com.tgid.cursomc.security.UserSS;

public class UserService {
	
	public static UserSS authenticated() {
		try {
			// Retorna o usuário que estiver logado no sistema
			return (UserSS) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		} catch (Exception e) {
			return null;
		}
	}

}
