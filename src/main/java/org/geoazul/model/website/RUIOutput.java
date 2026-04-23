package org.geoazul.model.website;

public abstract class RUIOutput extends RUIComponentBase {
	
	public static final String COMPONENT_TYPE = "jakarta.faces.Output";

    public static final String COMPONENT_FAMILY = "jakarta.faces.Output";
	
	
	private String value;
	private String converter;
	

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getConverter() {
		return converter;
	}

	public void setConverter(String converter) {
		this.converter = converter;
	}

	

}
