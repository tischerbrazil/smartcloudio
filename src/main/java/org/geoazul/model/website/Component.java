package org.geoazul.model.website;

import jakarta.persistence.Entity;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import org.geoazul.model.app.AbstractIdentityEntity;
import org.geoazul.model.basic.Comp;
import org.geoazul.model.basic.Layer;
import org.geoazul.model.security.ClientComponentEntity;
import com.fasterxml.jackson.databind.JsonNode;

@Entity
@NamedQueries({
@NamedQuery(name = Component.COMPONENT_ID, query = "select component from Component component where component.id = :id"), })
public abstract class Component extends Comp implements java.io.Serializable {

	public static final String COMPONENT_ID = "Component.componentId";

	/**
	 * 
	 */
 
	private static final long serialVersionUID = 1L;

	public Component( 
			AbstractIdentityEntity applicationEntity, Boolean enabled,  
			String name, String title, String description, 
			String icon,	ClientComponentEntity clientComponentEntity, 
			Layer layerCat,	JsonNode strings) {
	    	super(applicationEntity,  enabled,  
	    			 name,  title,  description, 
	    			 icon,	 clientComponentEntity, 
	    			 layerCat,	 strings) ;
	}

	public Component() {
	
	}


	
}