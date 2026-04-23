package org.geoazul.view.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import static modules.LoadInitParameter.*;
//import org.geoazul.model.basic.MultiPolygon;
import org.geoazul.model.basic.Polygon;
import org.geoazul.model.basic.rural.PolygonPoint;

import org.locationtech.jts.geom.Geometry;

public class ProcessODS {
      
	
	   
	   
	   public String convertToASCII2(String text) {
			return text.replaceAll("[ãâàáä]", "a").replaceAll("[êèéë]", "e")
					.replaceAll("[îìíï]", "i").replaceAll("[õôòóö]", "o")
					.replaceAll("[ûúùü]", "u").replaceAll("[ÃÂÀÁÄ]", "A")
					.replaceAll("[ÊÈÉË]", "E").replaceAll("[ÎÌÍÏ]", "I")
					.replaceAll("[ÕÔÒÓÖ]", "O").replaceAll("[ÛÙÚÜ]", "U")
					.replace('ç', 'c').replace('Ç', 'C').replace('ñ', 'n')
					.replace('Ñ', 'N');
		}

	   
	   public String decToDMS(double coord) {

			
			
			int doubleDegres =  (int) coord;


			String degrees =  Integer.toString((int)coord);
			double doubleDecimals = coord - doubleDegres; // 0.3333
			
			
			
			
			double doubleMinutes = (doubleDecimals * 100) * 60; // 19.998
			double absDoubleMinutes = (Math.floor(doubleMinutes)) / 100; // 19 double
			
			String Minutes = String.valueOf((int) absDoubleMinutes); // 19 string
			doubleDecimals = doubleMinutes - absDoubleMinutes; // 0.998
			
			double doubleSeconds = doubleDecimals * 60; // 59.88
			double absDoubleSeconds = Math.floor(doubleSeconds); // 59 double
			String Seconds = String.valueOf((int) absDoubleSeconds); // 19 string

			
			String complete = degrees + " " + Minutes + " " + Seconds + "S";  
			
			return complete;  
		}
		
		public String decToDMSS(double coord) {

			
			
			int doubleDegres =  (int) coord;


			String degrees =  Integer.toString((int)coord);
			double doubleDecimals = coord - doubleDegres; // 0.3333
			
			 int degrees2 = 0;   /* integer part DMS */
		      double deg_min_sec = 12.332514576108341;   /* DMS */
		      double minutes_seconds = 0;   /* fractional part DMS */
		      double minutes = 0, seconds = 0;   /* minutes, seconds DD */
		      
		      degrees2 = (int) deg_min_sec;
		      minutes_seconds = deg_min_sec - degrees2;
		      String min_sec = Double.toString(minutes_seconds);
		      String min = min_sec.substring(2,4);   /* minutes */
		      String sec = min_sec.substring(4,6);   /* seconds */
		      minutes = Double.valueOf(min) / 60;   /* minutes DD */
		      seconds = Double.valueOf(sec) / 3600;   /* seconds DD */

			
			
			double doubleMinutes = (doubleDecimals * 100) * 60; // 19.998
			double absDoubleMinutes = (Math.floor(doubleMinutes)) / 100; // 19 double
			
			String Minutes = String.valueOf((int) absDoubleMinutes); // 19 string
			doubleDecimals = doubleMinutes - absDoubleMinutes; // 0.998
			
			double doubleSeconds = doubleDecimals * 60; // 59.88
			double absDoubleSeconds = Math.floor(doubleSeconds); // 59 double
			String Seconds = String.valueOf((int) absDoubleSeconds); // 19 string


			 double sss = doubleSeconds * 3600 ;
			
				int SecondsInt = (int) sss; 

				
			
			
			
			
			String complete = degrees + " " + Minutes + " " + Seconds + "W";  
			
			return complete;  
		
		}
	   
	   public String runProcessODS2(Polygon obraCAP, EntityManager entityManager, String saveFileProcessFilePath_) {
		
		  
		return "ok";
		   
	   }
	   public String runProcessODS(Polygon obraCAP, EntityManager entityManager, 
			   String saveFileProcessFilePath_, String epsg) throws  FileNotFoundException, IOException{
		   		for ( PolygonPoint pp :  obraCAP.getVerticeObrasList()) {
		   		}
				return "SUCCESS";
	   }

	
}
