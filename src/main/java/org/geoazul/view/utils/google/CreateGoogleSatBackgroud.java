package org.geoazul.view.utils.google;


import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import jakarta.persistence.EntityManager;
import org.geoazul.model.basic.AbstractGeometry;
import static modules.LoadInitParameter.*;

public class CreateGoogleSatBackgroud {

      
	   

	   public String runProcessImgage(String  scale,  String Center,  String saveFileProcessFilePath_,  AbstractGeometry obraPrint, EntityManager entityManager, String identity) {
		 
		
		   
		  
		   String[] parts = Center.split(",");
		   String latX = parts[0];
		   String longY = parts[1];
			
			//zoom
			//String imageUrlGoogle = "http://maps.googleapis.com/maps/api/staticmap?center=" + Center + "&zoom=8&scale=false&size=400x300&maptype=terrain&format=png&visual_refresh=true&markers=size:mid%7Ccolor:0x9142e2%7Clabel:1%7C" + Center ;
			String imageUrlGoogle = "https://dev.virtualearth.net/REST/V1/Imagery/Map/AerialWithLabels/"
					+ latX
					+ "%2C"
					+ longY
					+ "/"
					+ "9"
					+ "?mapSize=400,300&mapLayer=TrafficFlow&format=png&pushpin="
					+ Center
					+ ";64;&key="
					+ "AvADKxRtJyL-w30Q3n4cWnCUlQbJiYi6TtvuX-RiRL2tuuqS8myB0zniVhr7_cx2";
		   
		   String destinationFileGoogle =  saveFileProcessFilePath_  +  "/backgroundUTM.png";
		


 
 
 
 
 
			URL urlGoogle = null;
			
			try {
				urlGoogle = new URL(imageUrlGoogle);
			} catch (MalformedURLException e1) {
			}
			InputStream isGoogle = null;
			try {
				isGoogle = urlGoogle.openStream();
			} catch (IOException e1) {
			}
			OutputStream osGoogle = null;
			
			try {
				
				
				osGoogle = new FileOutputStream(destinationFileGoogle);
			} catch (FileNotFoundException e1) {
			}

			
			byte[] bGoogle = new byte[2048];
			int lengthGoogle;

			try {
				while ((lengthGoogle = isGoogle.read(bGoogle)) != -1) {
					osGoogle.write(bGoogle, 0, lengthGoogle);
				}
			} catch (IOException e1) {
			}

			try {
				isGoogle.close();
			} catch (IOException e1) {
			}
			try {
				osGoogle.close();
			} catch (IOException e1) {
			}
			
			// END IMAGEM DE REFERENCIA LOCALIZACAO ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
			
			
		   return "success";
	      
	   }
	
	  
	   
	   
	
}