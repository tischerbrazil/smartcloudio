package org.geoazul.view.utils;


import java.util.HashSet;
import java.util.Set;
import jakarta.persistence.EntityManager;

import org.geoazul.model.basic.rural.GeometryGeometryId;
import org.geoazul.model.basic.Layer;
import org.geoazul.model.basic.Point;
import org.geoazul.model.basic.Polygon;
import org.geoazul.model.basic.rural.PolygonPoint;

import org.locationtech.jts.geom.Coordinate;
public class ProcessParcelaProjection {


	   MunicipioCloser municipioCloser;
	   
	   public Polygon ParcelaProjection(Polygon obraProjetada, EntityManager entityManager){

		 
		 
		
			obraProjetada.setArea(0.0);
			obraProjetada.setPerimetro(0.0);



		    int numPontos =  obraProjetada.getGeometry().getNumPoints();
		    
		    Coordinate[] teste =  obraProjetada.getGeometry().getCoordinates();
		    
		    
		    Long [] verticeIds = new Long[numPontos];
		    
		    
		    int sequencia = 0;
		    
		    
		 	Set<Long> set = new HashSet<Long>();
		 	
		 	
		 	  
		 	  
		    for (int i = 0; i < teste.length; i++) {
		    	sequencia++;
		    	
		    	
		    	  VerticeCloser verticeCloser =  new VerticeCloser(obraProjetada.getLayer().getEpsg(),  "POINT(" + teste[i].x + " " + teste[i].y +")"  , entityManager);
		    	   
		    	   
		    	verticeIds[i] = verticeCloser.getPoint().getId();
		    	
		    		if (!set.contains(verticeIds[i])) {
			            set.add(verticeIds[i]);
				        PolygonPoint verticePolygon = new PolygonPoint();
						verticePolygon.setId(new GeometryGeometryId());		//========== OBRA ID
						verticePolygon.setPolygonri(obraProjetada);
						verticePolygon.setSequencia(sequencia);
						
					
						
						verticePolygon.setTipoLimite("LA6");
							
						Long verticeId = verticeIds[i];
						Point selectPoint = (Point) 
							   entityManager.createQuery("select vertice from Point vertice where vertice.id = '" + verticeIds[i] + "'")
			                  .getSingleResult();
					    verticePolygon.setPointri(selectPoint);
					    verticePolygon.getId().setPointriId(verticeId);
					    obraProjetada.addVerticeObra(verticePolygon); 

			        }
		    }
		    
		    
	//	    MunicipioCloser municipioCloser =  new MunicipioCloser(obraProjetada.getGeometry().toString(), entityManager);
			
		    
		   
		    
	//	     obraProjetada.setMunicipio(municipioCloser.getMunicipio());	
		     
		     
		     
		//     UtmZoneCloser utmZoneCloser =  new UtmZoneCloser(obratemp.getGeometry().toString(), entityManager);
			
			    
			   
	
		//	     obraProjetada.setEpsg(utmZoneCloser.getEpsg());
		     
			
			 return obraProjetada;
	      
	   }
	
	
	 
	   
	   
	 //-------------------
	   
	   
	   
	   public Polygon ParcelaProjectionVertices(Polygon obraProjetada, EntityManager entityManager){


		    int numPontos =  obraProjetada.getGeometry().getNumPoints();
		    
		    Coordinate[] teste =  obraProjetada.getGeometry().getCoordinates();
		    
		    
		    String [] verticeIds = new String[numPontos];
		    
		    
		    int sequencia = 0;
		    
		    
		 	Set<String> set = new HashSet<String>();
		 	
		 	
		 	  Point pointNumPoints;
		 	  
		    for (int i = 0; i < teste.length; i++) {
		    	sequencia++;
		    	
		    	
		    	  String pointWKT  =   "POINT(" + teste[i].x + " " + teste[i].y +")";
		    	  
		    	  pointNumPoints = new Point();
		    	   
		    	  pointNumPoints.setGeometrywkt(pointWKT);
		    	  pointNumPoints.getGeometry().setSRID(Integer.valueOf(obraProjetada.getLayer().getEpsg()));
		    	  pointNumPoints.setAltura(0.0);
		    	  pointNumPoints.setEnabled(true);
		    	  pointNumPoints.setFather(null);
		    	  pointNumPoints.setIconflag(null);
		    	  	Layer layerFind = entityManager.find(Layer.class, 158);
		    	  pointNumPoints.setLayer(layerFind);
		    	  pointNumPoints.setMetodoLevantamento("PG1");
		    	  pointNumPoints.setNome("P-" + sequencia);
		    	  pointNumPoints.setParte(0);
		    	  pointNumPoints.setSigmaE(0.0);
		    	  pointNumPoints.setSigmaH(0.0);
		    	  pointNumPoints.setSigmaN(0.0);
		    	  Short situacao = 1;
		    	  pointNumPoints.setSituacao(situacao);
		    	  
		    	 
			     	entityManager.persist(pointNumPoints);
		    
		    	  
		    	  
		    }
		    
		   
			 return obraProjetada;
	      
	   }
	
	  
	   
	   
	
}
