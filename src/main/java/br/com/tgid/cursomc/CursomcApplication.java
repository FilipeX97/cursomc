package br.com.tgid.cursomc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import br.com.tgid.cursomc.services.DropboxService;

@SpringBootApplication
public class CursomcApplication implements CommandLineRunner {
	
	@Autowired
	private DropboxService dropboxService;

	public static void main(String[] args) {
		SpringApplication.run(CursomcApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		dropboxService.uploadFile("C:\\Users\\Beto\\Desktop\\2014-11-14-java-logo.jpeg");
	}
}
