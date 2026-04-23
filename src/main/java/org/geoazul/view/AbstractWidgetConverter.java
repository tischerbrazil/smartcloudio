package org.geoazul.view;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import org.geoazul.model.basic.AbstractWidget;

@FacesConverter("abstractWidgetConverter")
public class AbstractWidgetConverter implements Converter<Object> {
			
	@Inject
	EntityManager entityManager;
		
	public AbstractWidget findById(String id) {
		return entityManager.find(AbstractWidget.class, id);
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