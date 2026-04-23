package org.geoazul.model.website;

import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("RButton")
public class RButton extends RButtonBase {

	public static final String COMPONENT_TYPE = "RButton";
	
	 public static final String DEFAULT_RENDERER = "/includes/widgets/RButton.xhtml";
	 
	 //Button teste3;
	 org.primefaces.component.graphicimage.GraphicImage teste7;
	 
	 
	@Override
	public String getType() {
		return COMPONENT_TYPE;
	}

	public RButton() {
		setRendererType(DEFAULT_RENDERER);
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		return true;
	}

	@Override
	public String toString() {
		return super.getId();
	}
	

}
