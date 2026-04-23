package org.geoazul.view;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import org.geoazul.view.ApplicationEntityBean.LayerType;

public class LayerWizard implements Serializable {

	private static final long serialVersionUID = -6896011091508248621L;

	private Long id;
	private BigDecimal opacity;
	private Boolean visible;
	private LayerType layerTypeChose;
	private String layerName;
	private String clientLayer;
	private String urlLayer;
    private String imageName;
    private String dataSet;
    private List<String> dataSets = new ArrayList<String>();
    private String zIndex;
    
    public LayerWizard() {
		super();
	}

	public String opacityString() {
		return opacity.toString(); // result is "0.35"
	}
    
	public String layerWizardString() {
		return "l" + this.getId().toString().replace("-", "") + "-" + layerTypeChose.toString().toLowerCase();
	}
	
	public String storeLayerWizardString() {
		return "s" + this.getId().toString().replace("-", "") + "-" + layerTypeChose.toString().toLowerCase();
	}
    
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
    public BigDecimal getOpacity() {
		return opacity;
	}

	public void setOpacity(BigDecimal opacity) {
		this.opacity = opacity;
	}

	public Boolean getVisible() {
		return visible;
	}

	public void setVisible(Boolean visible) {
		this.visible = visible;
	}

	public LayerType getLayerTypeChose() {
		return layerTypeChose;
	}

	public void setLayerTypeChose(LayerType layerTypeChose) {
		this.layerTypeChose = layerTypeChose;
	}

	public String getLayerName() {
		return layerName;
	}

	public void setLayerName(String layerName) {
		this.layerName = layerName;
	}

	public String getClientLayer() {
		return clientLayer;
	}

	public void setClientLayer(String clientLayer) {
		this.clientLayer = clientLayer;
	}

	public String getUrlLayer() {
		return urlLayer;
	}

	public void setUrlLayer(String urlLayer) {
		this.urlLayer = urlLayer;
	}

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public List<String> getDataSets() {
		return dataSets;
	}

	public void setDataSets(List<String> dataSets) {
		this.dataSets = dataSets;
	}

	public String getDataSet() {
		return dataSet;
	}

	public void setDataSet(String dataSet) {
		this.dataSet = dataSet;
	}

	public String getzIndex() {
		return zIndex;
	}

	public void setzIndex(String zIndex) {
		this.zIndex = zIndex;
	}

	
}