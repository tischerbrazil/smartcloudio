package org.geoazul.view.utils;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

import org.geoazul.erp.Cities;


public class MunicipioCloser {


	  	
	   public  MunicipioCloser(String sRID , String  coordenatePoint, EntityManager entityManager){
	   String consulta = "SELECT *  FROM app_municipio As mun INNER JOIN " +
				   " (SELECT public.st_transform(public.st_geomfromtext(public.st_astext(public.ST_GeomFromText('" +
				   		coordenatePoint  +
				   "' ,4326) ), " + sRID + "), 4326) AS the_geom) As polygonBusca	 ON public.ST_DWithin(polygonBusca.the_geom, mun.the_geom, 0.0001) " +
				   " where public.st_srid(mun.the_geom) = 4326 and mun.the_geom is not null " +
				   " order by public.ST_Distance(mun.the_geom, polygonBusca.the_geom) asc LIMIT 1";
	  
	   try{
		   
			Query queryNativa = entityManager.createNativeQuery(consulta, Cities.class);

				this.setMunicipio((Cities) queryNativa.getSingleResult());
			}catch(NoResultException e){ 
			
			}
	   }
	
	   private Cities municipio; 
	   
	   public Cities getMunicipio()
	   {
	      return this.municipio;
	   }

	   public void setMunicipio(Cities municipio)
	   {
	      this.municipio = municipio;
	   }

	  
	   
	   
	
}
