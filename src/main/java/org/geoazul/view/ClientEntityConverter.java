package org.geoazul.view;

import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import org.geoazul.model.security.ClientEntity;

@FacesConverter("clientConverter")
public class ClientEntityConverter implements Converter<Object> {
			
	@Inject
	EntityManager entityManager;
		
	public ClientEntity findById(String id) {
		return entityManager.find(ClientEntity.class, id);
	}
		
	@Override
	public Object getAsObject(FacesContext context,	
			UIComponent component, 
			String value) {
		
	        return findById(value);
	}
	
    @Override
    public String getAsString(FacesContext context, 
    		UIComponent component, 
    		Object value) {
    	
    	try {
    		 return value.toString();
    	} catch (Exception e1) {
    		 return null;
		}
       
    }


}