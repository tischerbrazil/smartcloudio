package org.geoazul.model.basic;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import org.geoazul.model.app.ApplicationEntity;
import jsonb.JacksonUtil;

@Entity
@DiscriminatorValue("rastergeoserver")
public class LayerRasterGeoserver extends LayerGeoserver {

	private static final long serialVersionUID = 1L;
	
	public LayerRasterGeoserver() {
	}

	public LayerRasterGeoserver(String name, ApplicationEntity applicationEntity, int lastOrder) {
		
		this.setStrings(JacksonUtil.toJsonNode("{}"));
		this.setEpsg(applicationEntity.getEpsg());
		this.setDescription("");
		this.setDtype("rastergeoserver");
		this.setName(name);
		this.setTitle(name);
		this.setApplicationEntity(applicationEntity);
		this.setSelected(true);
		this.setEnabled(true);
		this.setZoom(applicationEntity.getZoom());
		this.setEditable(false);
		this.setOrderlayer(lastOrder);
		
		this.setMaxres(applicationEntity.getMaxres());
		this.setMinres(applicationEntity.getMinres());
		
		GeometryStyle geometryStyle = new GeometryStyle();
		this.setGeometryStyle(geometryStyle);
		
		LabelStyle styleNew = new LabelStyle();
		styleNew.setMaxreso(applicationEntity.getMaxres());
		styleNew.setMinreso(applicationEntity.getMinres());
		this.setLabelStyle(styleNew);
	}
	
}