package org.geoazul.view.utils;


import jakarta.persistence.EntityManager;
import static modules.LoadInitParameter.*;
import org.geoazul.model.app.ApplicationEntity;
import org.geoazul.model.basic.Polygon;
import org.geoazul.view.utils.ConvertToASCII2;
import org.geoazul.view.utils.ProcessParcelaProjection;


public class ProcessProjection {

	
	
	ConvertToASCII2 runConvertToASCII2 = new ConvertToASCII2(); 
	
	 public void runProcessProjection(EntityManager entityManager,
				ApplicationEntity applicationEntity,  
				Polygon polygonRITempProj
					) {
			   	ProcessParcelaProjection processParcelaProjection = new ProcessParcelaProjection();
			   	Polygon polygonRIProjecao =  processParcelaProjection.ParcelaProjection(polygonRITempProj,  entityManager);
			  
			
			   	ProcessParcelaProjection processImovelProjection;
			 
			
			//Persiste o verticepolygonRI
			ProcessVerticesProjection processVerticesProjection = new ProcessVerticesProjection(polygonRIProjecao, entityManager);

			//entityManager.refresh(polygonRIProjecao); //FIXME
		
		 
	 }
	 
	 public void runProcessProjectionVertices(EntityManager entityManager,
				ApplicationEntity applicationEntity,  
				Polygon polygonRITempProj
					) {
			   	ProcessParcelaProjection processParcelaProjection = new ProcessParcelaProjection();
			   	Polygon polygonRIProjecao =  processParcelaProjection.ParcelaProjectionVertices(polygonRITempProj,  entityManager);
			  
		
		 
	 }
	        
}