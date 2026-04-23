package org.geoazul.model.basic;

import java.math.BigDecimal;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import org.geoazul.model.app.ApplicationEntity;

import jsonb.JacksonUtil;

@Entity
@DiscriminatorValue("multipolygon")
public class LayerMultiPolygon extends LayerEntity {

	private static final long serialVersionUID = 1L;

	public LayerMultiPolygon() {
	}
	
	public LayerMultiPolygon(String name, ApplicationEntity applicationEntity) {
		this.setDtype("multipolygon");
		this.setStrings(JacksonUtil.toJsonNode("{}"));
		this.setEpsg(applicationEntity.getEpsg());
		this.setDescription("");
				
		//this.setDtype("RasterGeoserver");
		this.setName(name);
		this.setTitle(name);
		this.setApplicationEntity(applicationEntity);
		this.setSelected(true);
		this.setEnabled(true);
		this.setZoom(applicationEntity.getZoom());
		this.setEditable(false);
		this.setOrderlayer(applicationEntity.getLayers().size() + 1);

		this.setName(name);
		this.setApplicationEntity(applicationEntity);
		this.setSelected(true);
		this.setEnabled(true);
		this.setZoom(applicationEntity.getZoom());
		this.setEditable(false);
		this.setOrderlayer(applicationEntity.getLayers().size() + 1);
		
		this.setMaxres(applicationEntity.getMaxres());
		this.setMinres(applicationEntity.getMinres());
		
		GeometryStyle geometryStyle = new GeometryStyle();
		this.setGeometryStyle(geometryStyle);
		
		LabelStyle styleNew = new LabelStyle();
		styleNew.setMaxreso(new BigDecimal(0));
		styleNew.setMinreso(new BigDecimal(0));
		this.setLabelStyle(styleNew);
	}
	


}