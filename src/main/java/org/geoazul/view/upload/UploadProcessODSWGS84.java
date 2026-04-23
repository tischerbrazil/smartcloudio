package org.geoazul.view.upload;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.util.Random;
import jakarta.persistence.EntityManager;
import org.geoazul.model.app.ApplicationEntity;
import org.locationtech.jts.io.ParseException;
import static modules.LoadInitParameter.*;

public class UploadProcessODSWGS84 {
	
	
	
	
	
	
	 public void copyFileWGS84(String fileName, InputStream in, EntityManager entityManager, 
				ApplicationEntity gleba) {
		 
		 
	        try {
	          
	        	Random random = new Random();
	    		int randomInt = random.nextInt(1000000000);
	    		
	    		LocalDateTime data = LocalDateTime.now();

	    	    String fileNameOnly = data.toString() + "-"	+ String.valueOf(randomInt) + ".ods";
	    		String fileNameNew = save_FILE_PATH + "/tmp/" + fileNameOnly;
	    		
	             OutputStream out = new FileOutputStream(new File(fileNameNew));
	             int read = 0;
	             byte[] bytes = new byte[1024];
	          
	             while ((read = in.read(bytes)) != -1) {
	                 out.write(bytes, 0, read);
	             }
	           
	             in.close();
	             out.flush();
	             out.close();
	             
	             
	           
	             
	             
	             setInputFile(fileNameNew);
	             try {
					uploadProcessODSWGS84(fileNameNew,  entityManager, 
							 gleba);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}   
	             } catch (IOException e) {
	             }
	 }
	
	
	
	public void uploadProcessODSWGS84(String fileNameNew, EntityManager entityManager, 
			ApplicationEntity gleba) throws IOException,ParseException {
		
      }		
	//------------------------------------------------------- PRINT PROCESS IMOVEL
	
	
	 private String inputFile;
	 
		public String getInputFile() {
			return this.inputFile;
		}

		public void setInputFile(String inputFile) {
			this.inputFile = inputFile;
		}

}
