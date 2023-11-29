package br.com.tgid.cursomc.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;

@Configuration
public class DropboxConfig {
	
	@Value("${dropbox.acess.token}")
	private String token;
	
	@Value("${dropbox.acess.folder_name}")
	private String folder_name;

	@Bean
	DbxClientV2 dropboxClient() {
		DbxRequestConfig config = new DbxRequestConfig("dropbox/"+folder_name);
		DbxClientV2 client = new DbxClientV2(config, token);
		return client;
	}
	
}
