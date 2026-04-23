package org.geoazul.view.utils;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import org.geoazul.model.basic.Point;


public class VerticeCloser {


	  	
	   public  VerticeCloser(Integer sRID, String  coordenatePoint, EntityManager entityManager){

		   String consulta = "SELECT vertice.id  FROM app_point as vertice INNER JOIN" +
					" (SELECT public.ST_GeomFromText('" +
					coordenatePoint +
					"'," + sRID + ") As the_geom) As pontobusca" +
					" ON public.ST_DWithin(pontobusca.the_geom, vertice.geometry, 100.0)" +
					" where public.st_srid(vertice.geometry) = " + sRID + " and vertice.geometry is not null" +
					" order by public.ST_Distance(vertice.geometry, pontobusca.the_geom) asc LIMIT 1";
		 
		   try{
			   
				
			    Query queryFF = entityManager.createNativeQuery(consulta);
			    Object listFF = queryFF.getSingleResult();
			

			   Point pointRI = entityManager.find(Point.class,	Long.parseLong(listFF.toString() ));
			   
			   
			   this.setPoint(pointRI);
			
			}catch(NoResultException e){ 
			
			}
		   
		   
	   }
	   
	   
	
	   private Point pointRI; 
	   
	   public Point getPoint()
	   {
	      return this.pointRI;
	   }

	   public void setPoint(Point pointRI)
	   {
	      this.pointRI = pointRI;
	   }

	  
	   
	   
	
}
