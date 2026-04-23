package org.geoazul.model.website;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;

import org.geoazul.model.app.AbstractIdentityEntity;
import org.geoazul.model.basic.Layer;
import org.geoazul.model.security.ClientComponentEntity;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * 
 */

@SuppressWarnings("serial")
@Entity
@DiscriminatorValue("menu")
@NamedQueries({
    @NamedQuery(
    		name= UrlMenu.MENU_ID, 
    		query="select urlMenu from UrlMenu urlMenu where urlMenu.id = :id"),
}) 
public class UrlMenu extends Component implements java.io.Serializable {

	
	 public static final String MENU_ID = "UrlMenu.menuId";
	 /**
	 * 
	 */
	
		@OneToMany(cascade = CascadeType.DETACH, fetch = FetchType.LAZY, mappedBy = "urlMenu")
		@OrderBy("ordering ASC")
		private List<UrlMenuItem> menuItens = new ArrayList<UrlMenuItem>();  


		public List<UrlMenuItem> getMenuItens() {
			return menuItens;
		}

		public void setMenuItens(List<UrlMenuItem> menuItens) {
			this.menuItens = menuItens;
		}
		
		//public void addFieldItem(UrlMenuItem fieldItem) {
		//	this.menuItens.add(fieldItem);
		//}
		
		public UrlMenu() {
			super();
		}
		
		public UrlMenu( 
				AbstractIdentityEntity applicationEntity, Boolean enabled,  
				String name, String title, String description, 
				String icon,	ClientComponentEntity clientComponentEntity, 
				Layer layerCat,	JsonNode strings) {
		    	super(applicationEntity,  enabled,  
		    			 name,  title,  description, 
		    			 icon,	 clientComponentEntity, 
		    			 layerCat,	 strings) ;
		}
		
		

}


