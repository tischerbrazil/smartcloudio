package org.geoazul.view;

import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import org.geoazul.model.basic.LayerView;


@FacesConverter("layerViewConverter")
public class LayerViewConverter implements Converter {
	
	@Override
	public Object getAsObject(FacesContext context,	UIComponent component, String value) {
		LayerView layerView = new LayerView();
		String[] dados = value.split(",");
		layerView.setLayerid(dados[0]);
		layerView.setLayerhash(dados[1]);
		layerView.setName(dados[2]);
        return layerView;
	}
	
    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        return value.toString();
    }


}