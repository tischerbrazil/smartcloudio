package org.geoazul.view.rural;

import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import org.geoazul.model.basic.Point;
import jakarta.faces.convert.FacesConverter;

@FacesConverter("pointEntityConverter")
public class PointEntityConverter implements Converter {
	
	@Inject
	EntityManager entityManager;
	
	public Point findById(String id) {
		try {
			return entityManager.find(Point.class, id);
		} catch (Exception e) {
			return null;
		}
	}
	
	@Override
	public Object getAsObject(FacesContext context,
			UIComponent component, String value) {
		if (value != null) {
			return  findById(value);
		}else{
			return null;
		}
	}
	
    @Override
    public String getAsString(FacesContext context, UIComponent component, 
    		Object value) {
    	Long id = (value instanceof Point) ? ((Point) value).getId() : null;
        return (id != null) ? id.toString() : null;
    }
}