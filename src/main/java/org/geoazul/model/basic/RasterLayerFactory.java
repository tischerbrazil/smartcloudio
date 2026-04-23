package org.geoazul.model.basic;

import java.math.BigDecimal;

public class RasterLayerFactory implements LayerFactory {
	@Override
	public Layer createLayer() {
		
		LayerPoint newLayer = new LayerPoint();
		newLayer.setName("PONTOS");
		newLayer.setDescription("CAMADA PONTOS ");
		newLayer.setSelected(true);
		newLayer.setZoom(5);
		newLayer.setMinZoom(0);
		newLayer.setMaxZoom(20);
		newLayer.setEditable(true);

		newLayer.setTitle("POINTS");
		LabelStyle styleNew = new LabelStyle();
		styleNew.setMaxreso(new BigDecimal(0));
		styleNew.setMinreso(new BigDecimal(0));
		newLayer.setLabelStyle(styleNew);

				
		return newLayer;
	}
}