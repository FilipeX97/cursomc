package br.com.tgid.cursomc.services;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;

import br.com.tgid.cursomc.services.exceptions.FileException;

@Service
public class DropboxService {

	private Logger LOG = LoggerFactory.getLogger(DropboxService.class);

	@Autowired
	private DbxClientV2 dbxClient;

	// Método para enviar por path do arquivov
//	public void uploadFile(String filePath) {
//	    try {
//	        LOG.info("Iniciando upload");
//	        InputStream inputStream = new FileInputStream(Paths.get(filePath).toFile());
//
//	        // Use Paths.get(filePath).getFileName().toString() como o caminho no uploadBuilder
//	        dbxClient.files().uploadBuilder("/" + Paths.get(filePath).getFileName().toString())
//	                .uploadAndFinish(inputStream);
//
//	        LOG.info("Upload feito");
//	    } catch (IOException e) {
//	        LOG.error("IOException: " + e.getMessage(), e);
//	    } catch (UploadErrorException e) {
//	        LOG.error("UploadErrorException: " + e.getMessage(), e);
//	    } catch (DbxException e) {
//	        LOG.error("DbxException: " + e.getMessage(), e);
//	    }
//	}

	public URI uploadFile(MultipartFile file) {
		try {
			String fileName = file.getOriginalFilename();
			InputStream inputStream = file.getInputStream();
			return uploadFile(inputStream, fileName);
		} catch (IOException e) {
			throw new FileException("Erro de IO: " + e.getMessage()); 
		}
	}
	
	public URI uploadFile(InputStream is, String fileName) {
		try {
			LOG.info("Iniciando upload");
			FileMetadata metadata = dbxClient.files().uploadBuilder("/"+fileName).uploadAndFinish(is); 
			LOG.info("Upload feito");
			String url = dbxClient.sharing().getFileMetadata(metadata.getId()).getPreviewUrl();
			LOG.info("URL gerada");
			return new URI(url);
		} catch (IOException e) {
			throw new FileException("Erro de IO: " + e.getMessage()); 
		} catch (DbxException e) {
			throw new FileException("Erro de Dbx: " + e.getMessage()); 
		} catch (URISyntaxException e) {
			throw new FileException("Erro de URISyntax: " + e.getMessage()); 
		}
	}

}
