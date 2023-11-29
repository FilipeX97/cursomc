package br.com.tgid.cursomc.services;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.UploadErrorException;

@Service
public class DropboxService {
	
	private Logger LOG = LoggerFactory.getLogger(DropboxService.class);
	
	@Autowired
	private DbxClientV2 dbxClient;
	
	public void uploadFile(String filePath) {
	    try {
	        LOG.info("Iniciando upload");
	        InputStream inputStream = new FileInputStream(Paths.get(filePath).toFile());

	        // Use Paths.get(filePath).getFileName().toString() como o caminho no uploadBuilder
	        dbxClient.files().uploadBuilder("/" + Paths.get(filePath).getFileName().toString())
	                .uploadAndFinish(inputStream);

	        LOG.info("Upload feito");
	    } catch (IOException e) {
	        LOG.error("IOException: " + e.getMessage(), e);
	    } catch (UploadErrorException e) {
	        LOG.error("UploadErrorException: " + e.getMessage(), e);
	    } catch (DbxException e) {
	        LOG.error("DbxException: " + e.getMessage(), e);
	    }
	}


}
