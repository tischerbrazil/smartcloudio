package org.geoazul.model.website;

import jakarta.faces.component.UIGraphic;

public abstract class RUIGraphic extends RUIComponentBase {
	
	public static final String COMPONENT_TYPE = "jakarta.faces.Graphic";

    public static final String COMPONENT_FAMILY = "jakarta.faces.Graphic";
	
	
	private String value;
	
	

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	

	public String getUrl() {
        return (String) getValue();
    }

   
    public void setUrl(String url) {
        setValue(url);
    }
	

}
