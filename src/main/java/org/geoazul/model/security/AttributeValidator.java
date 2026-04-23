package org.geoazul.model.security;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.validator.FacesValidator;
import jakarta.faces.validator.Validator;
import jakarta.faces.validator.ValidatorException;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

@FacesValidator("attributeValidator")
public class AttributeValidator	implements Validator {
	
	@Inject
	EntityManager entityManager;
		
	public UserAttributeEntity findById(String id) {
		try {
			return entityManager.find(UserAttributeEntity.class, id);
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public void validate(FacesContext fc, UIComponent component, 
			Object value) throws ValidatorException {
		String username = (String) value;
        if (usernameExists(username)) {
            throw new ValidatorException(new FacesMessage("Attribute already exists."));
        }else {
        	  throw new ValidatorException(new FacesMessage("XXXX."));
        }
		
	}
	
	  private boolean usernameExists(String name) {
	    	if (name.equals("picture")) {
	    		return true;
	    	}else {
	    		return false;
	    	}
	      }

}
