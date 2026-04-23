package org.geoazul.view.upload;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import static modules.LoadInitParameter.*;
import org.geoazul.model.app.ApplicationEntity;
import org.geoazul.model.basic.Layer;
import org.geoazul.model.basic.Point;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.io.ParseException;

public class UploadVertices {
	
	 
	 GeometryFactory geometryFactory = new GeometryFactory();
	 
	 public void copyFile(String fileName, InputStream in, EntityManager entityManager, 
			ApplicationEntity gleba, String identity, Layer layerOds) {

		 
		 
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
					uploadProcessODS(fileNameNew, entityManager, gleba, identity, layerOds);
				} catch (ParseException e) {
				}   
	            } catch (IOException e) {
	            }
	 }
	 
	public void uploadProcessODS(String fileNameNew, EntityManager entityManager, 
			ApplicationEntity gleba, String identity, Layer layerOds) throws IOException,ParseException {
		
 		
      }		
	
		 
	 private String inputFile;
	 
		public String getInputFile() {
			return this.inputFile;
		}

		public void setInputFile(String inputFile) {
			this.inputFile = inputFile;
		}
}
