package org.geoazul.model.website;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import org.geoazul.model.app.AbstractIdentityEntity;
import org.geoazul.model.basic.Layer;
import org.geoazul.model.security.ClientComponentEntity;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * 
 */

@SuppressWarnings("serial")
@Entity
@DiscriminatorValue("register")
@NamedQueries({
    @NamedQuery(
    		name= UrlRegister.REGISTER_ID, 
    		query="select urlRegister from UrlRegister urlRegister where "
    				+ "urlRegister.id = :id"),
}) 
public class UrlRegister extends Component implements java.io.Serializable {

	public UrlRegister( 
			AbstractIdentityEntity applicationEntity, Boolean enabled,  
			String name, String title, String description, 
			String icon,	ClientComponentEntity clientComponentEntity, 
			Layer layerCat,	JsonNode strings) {
	    	super(applicationEntity,  enabled,  
	    			 name,  title,  description, 
	    			 icon,	 clientComponentEntity, 
	    			 layerCat,	 strings) ;
	}
	
	public UrlRegister() {
		super();
	}


	public static final String REGISTER_ID = "UrlRegister.registerId";
	 /**
	 * 
	 */
	
	
}


