package org.geoazul.model.website;

import java.io.Serializable;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import org.geoazul.model.basic.AbstractGeometry;
import org.geoazul.model.basic.Layer;
import org.geoazul.model.basic.Point;
import org.primefaces.event.map.OverlaySelectEvent;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;

import com.fasterxml.jackson.databind.JsonNode;

 

@Named
@ViewScoped
public class InfoWindowView implements Serializable {
     
    private MapModel advancedModel;
 
    private Marker marker;
    
    @Inject
	EntityManager entityManager;
 
    
    public class MObject implements java.io.Serializable  { 

    	private static final long serialVersionUID = 1L;
        		
    	private String image;
    	private JsonNode strings;
    	
    	
    	
		public String getImage() {
			return image;
		}
		public void setImage(String image) {
			this.image = image;
		}
		public JsonNode getStrings() {
			return strings;
		}
		public void setStrings(JsonNode strings) {
			this.strings = strings;
		}
		
    	
    	
		
    }
    
    
    
   
    
    @PostConstruct
    public void init() {
    	
    	
    	try{
    		
    		//Layer dd = entityManager.find(Layer.class, 113L);
        	Layer dd = entityManager.find(Layer.class, 363L);
        	
        	 LatLng coordenadas = null;
        	 
        	  advancedModel = new DefaultMapModel();
        	  String imagem;
    		    for (AbstractGeometry geo : dd.getGeometrias() ) {
    		   
    		    	imagem = "";
    		    	
    		    	
    		    	try {
    		    	
    		    		imagem = ((Point) geo).getMedias().stream().findFirst().get().getFilename();
    		    	
    		    	}catch(Exception ex3){
    		    		//ex3.printStackTrace();
    		    	}
    		    	
    		    	
    		    	//String jsonString = "{\"image\":\\" + imagem + "\\, \"data:\"" + geo.getStrings() + "\"}";  
    		    	
    		    	
    		    	coordenadas = new LatLng(((Point) geo).getGeometry().getCoordinate().getY(), ((Point) geo).getGeometry().getCoordinate().getX());
    		    	advancedModel.addOverlay(new Marker(coordenadas,imagem, geo.getStrings().at("/introduction").asText(), "https://maps.google.com/mapfiles/ms/micons/blue-dot.png"));
    		    }
    		
    	} catch(Exception ex){
    			
	    	//ex.printStackTrace();
    		
    	}
    	
    	//Layer dd = entityManager.find(Layer.class, 363L);
    	
		    
      
         
       
    }
 
    public MapModel getAdvancedModel() {
        return advancedModel;
    }
     
    public void onMarkerSelect(OverlaySelectEvent event) {
        marker = (Marker) event.getOverlay();
    }
     
    public Marker getMarker() {
        return marker;
    }
}
                    