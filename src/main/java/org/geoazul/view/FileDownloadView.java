package org.geoazul.view;


import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.Stateless;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.tika.config.TikaConfig;
import org.geoazul.model.Address;
import org.geoazul.model.website.media.Media;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.metadata.TikaCoreProperties;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import org.apache.tika.detect.Detector;
import org.apache.tika.mime.MediaType;
import org.apache.tika.mime.MimeTypes;
import org.xml.sax.ContentHandler;
import static modules.LoadInitParameter.*;

@Named
@Stateless
public class FileDownloadView {
	
	
	
	@Inject
	EntityManager entityManager;
	
	public Media findById(Long id) { 
		try {
			return entityManager.find(Media.class, id);
		} catch (Exception ex) {
			return null;
		}
	}
 
	public StreamedContent getFile(Long id) {
		System.out.println("------------------------------------" + id);

		Media media = findById(id);
		String fileDownload = save_FILE_PATH + media.getFilename()  + media.getTitle() ;
		

		String title = media.getTitle();
		String mime = media.getMimeType();
		
		
		StreamedContent file = DefaultStreamedContent.builder()
    			.contentType(mime)
    			.name(title)
    			.stream(() ->  
    			{
					try {
						return new FileInputStream(fileDownload);
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						return null;
						//e.printStackTrace();
					}
				}
				).build(); 
		return file;
	}
	
	
 
	public StreamedContent getEBookItemFile(String uuid) {
		
		
		 
		
		String fileDownload = save_FILE_PATH 
				+ "eBookItem.getFileName()";
		
		
		Path p = Paths.get(fileDownload);
		
	    String fileName = p.getFileName().toString();
	    String directory = p.getParent().toString();
		
		StreamedContent file = DefaultStreamedContent.builder()
    			.contentType("eBookItem.getMimeType()")
    			.name(fileName)
    			.stream(() ->  
    			{
					try {
						return new FileInputStream(fileDownload);
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						return null;
						//e.printStackTrace();
					}
				}
				).build(); 
		return file;
	}

	public void start() {
		try {

			String filename = "/home/jboss/files/127.0.0.1/1562.pdf";
			TikaConfig tikaConfig = TikaConfig.getDefaultConfig();

			Metadata metadata = new Metadata();
			String text = parseUsingComponents(filename, tikaConfig, metadata);


			metadata = new Metadata();
			text = parseUsingAutoDetect(filename, tikaConfig, metadata);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
	}

	
	
	
	
	public static void main(String[] args) {
		
		
		String filename = "/home/jboss/geoazul_store/files/127.0.0.1/media/28-08-2020-05-19-57-710876912_mapacultutalro.png";
        TikaConfig tikaConfig = TikaConfig.getDefaultConfig();

        Metadata metadata = new Metadata();
        String text = null;
		try {
			text = parseUsingComponents(filename, tikaConfig, metadata);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


        metadata = new Metadata();
        try {
			text = parseUsingAutoDetect(filename, tikaConfig, metadata);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    public static String parseUsingAutoDetect(String filename, TikaConfig tikaConfig,
                                              Metadata metadata) throws Exception {

        AutoDetectParser parser = new AutoDetectParser(tikaConfig);
        ContentHandler handler = new BodyContentHandler();
        TikaInputStream stream = TikaInputStream.get(new File(filename), metadata);
        parser.parse(stream, handler, metadata, new ParseContext());
        return handler.toString();
    }

    public static String parseUsingComponents(String filename, TikaConfig tikaConfig,
                                              Metadata metadata) throws Exception {
        MimeTypes mimeRegistry = tikaConfig.getMimeRepository();


        metadata.set(TikaCoreProperties.EMBEDDED_RESOURCE_TYPE_KEY, filename);
      

        InputStream stream = TikaInputStream.get(new File(filename));
       

        stream = TikaInputStream.get(new File(filename));
        Detector detector = tikaConfig.getDetector();
       

        // Get a non-detecting parser that handles all the types it can
        Parser parser = tikaConfig.getParser();
        // Tell it what we think the content is
        MediaType type = detector.detect(stream, metadata);
        metadata.set(Metadata.CONTENT_TYPE, type.toString());
        // Have the file parsed to get the content and metadata
        ContentHandler handler = new BodyContentHandler();
        parser.parse(stream, handler, metadata, new ParseContext());

        return handler.toString();
    }
	
	
	

}
