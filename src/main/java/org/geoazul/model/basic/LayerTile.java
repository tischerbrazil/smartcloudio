package org.geoazul.model.basic;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.math.BigDecimal;

import org.geoazul.model.app.ApplicationEntity;
import com.fasterxml.jackson.databind.JsonNode;


@Entity
@DiscriminatorValue("tile")
public class LayerTile extends Layer {

	public LayerTile() {
	}
	
	public LayerTile(String name, ApplicationEntity applicationEntity,
			String description, JsonNode strings, 
			Integer order, Integer epsg, Integer zoom,
			Integer maxZoom, Integer minZoom,
			BigDecimal maxres,  BigDecimal minres
			) {

		this.setDtype("tile");
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
		this.setEditable(false);
		this.setOrderlayer(order);
	
		this.setMaxres(maxres);
		this.setMinres(minres);
		
		GeometryStyle geometryStyle = new GeometryStyle();
		this.setGeometryStyle(geometryStyle);
		
		LabelStyle styleNew = new LabelStyle();
		styleNew.setMaxreso(maxres);
		styleNew.setMinreso(minres);
		this.setLabelStyle(styleNew);

	}

}