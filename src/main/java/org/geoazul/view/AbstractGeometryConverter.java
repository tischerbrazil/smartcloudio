package org.geoazul.view;

import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import org.geoazul.model.basic.AbstractGeometry;

@FacesConverter("abstractGeometryConverter")
public class AbstractGeometryConverter implements Converter<Object> {
			
	@Inject
	EntityManager entityManager;
		
	public AbstractGeometry findById(Long id) {
		return entityManager.find(AbstractGeometry.class, id);
	}
		
	@Override
	public Object getAsObject(FacesContext context,	
			UIComponent component, String value) {
		
	        return findById(Long.valueOf(value));
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