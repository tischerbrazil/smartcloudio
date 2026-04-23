package org.geoazul.model.basic;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.math.BigDecimal;

import org.geoazul.model.app.ApplicationEntity;
import com.fasterxml.jackson.databind.JsonNode;

@Entity
@DiscriminatorValue("linestring")
public class LayerLinestring extends LayerEntity {

	private static final long serialVersionUID = 1L;
			
	public LayerLinestring() {
	}
	
	public LayerLinestring(String name, ApplicationEntity applicationEntity,
			String description,  JsonNode strings, Integer order, Integer epsg, Integer zoom ,
			Integer maxZoom, Integer minZoom,
			BigDecimal maxres,  BigDecimal minres, GeometryStyle geometryStyle,
			LabelStyle styleNew) {
		this.setDtype("linestring");
		this.setStrings(strings);
		this.setEpsg(epsg);
		this.setDescription(description);
		this.setName(name);
		this.setTitle(name);
		this.setApplicationEntity(applicationEntity);
		this.setSelected(true);
		this.setEnabled(true);
		this.setZoom(zoom);
		this.setMinZoom(minZoom);
		this.setMaxZoom(maxZoom);
		this.setEditable(true);
		this.setOrderlayer(order);
		this.setMaxres(maxres);
		this.setMinres(minres);
		this.setGeometryStyle(geometryStyle);
		this.setLabelStyle(styleNew);
	}


}