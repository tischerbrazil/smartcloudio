package org.geoazul.view.utils;

import jakarta.persistence.EntityManager;

import org.geoazul.model.basic.rural.GeometryGeometryId;
import org.geoazul.model.basic.Point;
import org.geoazul.model.basic.Polygon;
import org.geoazul.model.basic.rural.PolygonPoint;


public class ProcessVerticesProjection {
	   
	   private boolean retorno;
	  
	   public ProcessVerticesProjection(Polygon obraProjecao,  EntityManager entityManager) {
		    Integer OPER = 1;
		    Polygon obraFind = entityManager.find(Polygon.class, obraProjecao.getId());
			for (PolygonPoint verticePolygonProjecao : obraFind.getVerticeObrasList()) {
				PolygonPoint verticePolygon = new PolygonPoint();
		
				if (OPER < (obraFind.getVerticeObrasList().size() + 1)){
				verticePolygon.setId(new GeometryGeometryId());
				verticePolygon.setTipoLimite(verticePolygonProjecao.getTipoLimite());
				verticePolygon.setSequencia(verticePolygonProjecao.getSequencia());
				verticePolygon.setPolygonri(obraFind);
				
				// ========= VERTICE ID		    
				Long verticeId = verticePolygonProjecao.getPointri().getId();
				Point selectVertice = (Point) 
						entityManager.createQuery("select vertice from Point vertice where vertice.id = '" + verticeId + "'")
		                  .getSingleResult();
			    verticePolygon.setPointri(selectVertice);
				verticePolygon.getId().setPolygonriId(obraFind.getId());
				verticePolygon.getId().setPointriId(verticeId);

				try {
					
		   			
		   			entityManager.persist(verticePolygon);
		  		
					
				
		
				    
				} catch (Exception e) {
					this.setRetorno(false);
				}

				}
		
				OPER++;
			}
			
			this.setRetorno(true);
	   } 

	   public boolean getRetorno()
	   {
	      return this.retorno;
	   }

	   public void setRetorno(boolean retorno)
	   {
	      this.retorno = retorno;
	   }

}