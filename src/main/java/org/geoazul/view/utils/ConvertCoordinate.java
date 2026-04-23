package org.geoazul.view.utils;

import java.util.List;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;

public class ConvertCoordinate {
	private static String X4326;
	private static String Y4326;
	
	private static String CONVERTER;
	
	 	static public Coordinate runConvertCoordinate(String utmPoint, 
	 	int inputSRID, Integer outputSRID, EntityManager entityManager ) {


	     	

		String consulta = "SELECT" +
				" public.ST_X(public.ST_Transform(public.ST_GeomFromText('" + utmPoint + "'," + inputSRID + ")," + outputSRID + ")) As X4326," +
				" public.ST_Y(public.ST_Transform(public.ST_GeomFromText('" + utmPoint + "'," + inputSRID + ")," + outputSRID+ ")) As Y4326 ";
     	 
		Query queryFF = entityManager.createNativeQuery(consulta);
		
		List<Object[]> listFF= queryFF.getResultList();
     	
			if (!listFF.isEmpty()){ 
		     	
		        Object stringFF[] =  listFF.get(0);
			        X4326 = stringFF[0].toString();   
			        Y4326 = stringFF[1].toString();    
			    return new Coordinate(Double.valueOf(X4326).doubleValue(), Double.valueOf(Y4326).doubleValue());
		    }else{
		     	
		    	return null;
		    }
		
	}
	 	
	 	
	 	
	 	
		static public Geometry runConvertCoordinateGeometry(Geometry geometria, 
			 	int inputSRID, Integer outputSRID, EntityManager entityManager ) {


			     	

				String consulta = "SELECT " 
 						+ "public.st_astext(public.st_transform(public.st_geomfromtext(public.st_astext(  '"
 						+ geometria.toString()  + "' )," + inputSRID + "), " + outputSRID + "))" 	;		   

				Query queryFF = entityManager.createNativeQuery(consulta);
				
				 Object listFF = queryFF.getSingleResult();
		     	
					
				     	
				       
				        	CONVERTER = listFF.toString();   
				        	
				        	WKTReader wktReader = new WKTReader();
				    		Geometry geometryGeom = null;
				    		try {
				    			geometryGeom = wktReader.read(CONVERTER);
				    			geometryGeom.setSRID(outputSRID);
				    		} catch (ParseException e) {
				    		}
				    		
				    		
				    	
				        	
		 			    return geometryGeom;
				   
		
			}
		
		
		
	 	
		static public Coordinate runConvertCoordinatePolygon2(String utmPolygon, 
			 	int inputSRID, String outputSRID, EntityManager entityManager ) {


			     	

				String consulta = " UPDATE polygonri set geometry =  public.st_transform(public.st_geomfromtext(public.st_astext(\n" + 
						"'" + utmPolygon + "')," + inputSRID + ")," + outputSRID + ") where id = 'd92e7e66-aa91-4c36-bd93-f96411385fe6'" ;		
		  
				
		     	 
			   	Query queryNativaTeste22 = entityManager
						.createNativeQuery(consulta);
				
				queryNativaTeste22.executeUpdate();
				
				
				return null;
				
				
				
			}

		//public Object runConvertCoordinate(String utmPoint, String string, String outputSRID,
		//		EntityManager entityManager) {
			// TODO Auto-generated method stub
		//	return null;
		//}
}
