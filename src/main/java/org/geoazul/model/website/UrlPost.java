package org.geoazul.model.website;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import org.geoazul.model.app.AbstractIdentityEntity;
import org.geoazul.model.basic.AbstractGeometry;
import org.geoazul.model.basic.Layer;
import org.geoazul.model.security.ClientComponentEntity;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * 
 */

@SuppressWarnings("serial")
@Entity
@DiscriminatorValue("post")
public class UrlPost extends Layer implements java.io.Serializable {

	//public static final String POST_UUID = "UrlPost.postUuid";
	
	private static final long serialVersionUID = 1L;
	/**
	* 
	*/
	
	//public List<AbstractGeometry> getPostItens2() {
	//	List<AbstractGeometry> docItens2 = new ArrayList<AbstractGeometry>();
	//	for (AbstractGeometry urlPostItem3 : this.getGeometrias()) {
	//		if (urlPostItem3.getChildrens().size() > 0){
	//			docItens2.add(urlPostItem3);
	//		}
	//	}
	//	return docItens2;
	//}
	
	
	public UrlPost( 
			AbstractIdentityEntity applicationEntity, Boolean enabled,  
			String name, String title, String description, 
			String icon,	ClientComponentEntity clientComponentEntity, 
			Layer layerCat,	JsonNode strings) {
	    	super(applicationEntity,  enabled,  
	    			 name,  title,  description, 
	    			 icon,	 clientComponentEntity, 
	    			 layerCat,	 strings) ;
	}

	public UrlPost() {
	
	}
	

}
