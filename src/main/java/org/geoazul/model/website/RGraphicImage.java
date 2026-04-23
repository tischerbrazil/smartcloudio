package org.geoazul.model.website;

import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("RGraphicImage")
public class RGraphicImage extends RGraphicImageBase {

	public static final String COMPONENT_TYPE = "RGraphicImage";
	 
	@Override
	public String getType() {
		return COMPONENT_TYPE;
	}


}
