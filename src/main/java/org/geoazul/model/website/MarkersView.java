package org.geoazul.model.website;

import java.io.Serializable;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;

import org.geoazul.model.basic.AbstractGeometry;
import org.geoazul.model.basic.Layer;
import org.primefaces.event.map.OverlaySelectEvent;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;
import org.geoazul.model.basic.Point;

@Named
@RequestScoped
public class MarkersView implements Serializable {

	 @Inject
		EntityManager entityManager;
	 
    private MapModel simpleModel;
    
    private Marker marker;
    
    public void onMarkerSelect(OverlaySelectEvent event) {
    	
    	
    	
        marker = (Marker) event.getOverlay();
    
    }
    

    public Marker getMarker() {
        return marker;
    }

    @PostConstruct
    public void init() {
        simpleModel = new DefaultMapModel();

		
        
        Object layerid;
		Layer dd = entityManager.find(Layer.class, 363L);
		
		
		 simpleModel = new DefaultMapModel();
		
		 LatLng coordenadas = null;
		    for (AbstractGeometry geo : dd.getGeometrias() ) {
		   
		    
		    	
		    	
		    	coordenadas = new LatLng(((Point) geo).getGeometry().getCoordinate().getY(), ((Point) geo).getGeometry().getCoordinate().getX());
		    	simpleModel.addOverlay(new Marker(coordenadas, geo.nome, geo.getStrings().at("/introduction"), "https://maps.google.com/mapfiles/ms/micons/blue-dot.png"));
		    }
		
        
    	
      
    }

    public MapModel getSimpleModel() {
        return simpleModel;
    }
}