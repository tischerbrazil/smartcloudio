package org.geoazul.view;

import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import org.geoazul.model.app.ApplicationIdentityEntity;


@FacesConverter("applicationIdentityConverter")
public class ApplicationIdentityConverter implements Converter {
			
	@Inject
	EntityManager entityManager;
		
	public ApplicationIdentityEntity findById(Long id) {
		return entityManager.find(ApplicationIdentityEntity.class, id);
	}
		
	@Override
	public Object getAsObject(FacesContext context,	
			UIComponent component, 
			String value) {
		
	        return findById(Long.valueOf(value));
	}
	
    @Override
    public String getAsString(FacesContext context, 
    		UIComponent component, 
    		Object value) {
    	
    	try {
    		 return value.toString();
    	} catch (Exception e1) {
    
    		e1.printStackTrace();
    		 return null;
		}
       
    }


}