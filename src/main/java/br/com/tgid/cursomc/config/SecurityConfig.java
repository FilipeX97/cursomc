package br.com.tgid.cursomc.config;

import java.util.Arrays;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class SecurityConfig {

	private static final String[] PUBLIC_MATCHERS = { "/h2-console/**" };
	private static final String[] PUBLIC_MATCHERS_GET = { "/produtos/**", "/categorias/**" };

	@Autowired
	private Environment env;
	
	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		
		if(Arrays.asList(env.getActiveProfiles()).contains("test")) {
            http.headers(headers -> headers.frameOptions().disable());
		}
		
        // Desabilita, pois é usado para proteção de armazenamento em sessão
		http.cors((cors) -> cors.disable())
		.csrf((csrf) -> csrf.disable());
        http
            .authorizeHttpRequests()
            .antMatchers(HttpMethod.GET, PUBLIC_MATCHERS_GET).permitAll()
            .antMatchers(PUBLIC_MATCHERS).permitAll()
            .anyRequest().authenticated()
            .and()
            // Forma para conseguir retornar 401 por enquanto
            .exceptionHandling(handling -> 
            	handling.authenticationEntryPoint((request, response, authException) -> 
            		response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized")
        		)
        	);
        // Utilizado para assegurar que o backend não vai criar sessão de usuário
        http.sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS)); 
		return http.build();
	}


	// método que permite o acesso de múltiplas fontes com as configurações básicas
	// em todas as rotas
	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
		return source;
	}

}
