package org.geoazul.view;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.ConverterException;
import jakarta.faces.convert.FacesConverter;
import jsonb.JacksonUtil;

@FacesConverter("jsonConverter")
public class JSONViewConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		try {
			return JacksonUtil.toJsonNode(value);
		} catch (Exception ww) {
			FacesMessage msg = new FacesMessage("JSON Conversion error.", "Invalid Json format.");
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			throw new ConverterException(msg);
		}
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		return value.toString();
	}

}