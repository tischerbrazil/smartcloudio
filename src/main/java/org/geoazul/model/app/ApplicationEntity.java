package org.geoazul.model.app;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import org.geoazul.model.basic.Layer;
import org.geoazul.model.security.ClientEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;

@Entity
@DiscriminatorValue("/basic/") 
public  class ApplicationEntity extends AbstractIdentityEntity {

	private static final long serialVersionUID = 3812896200172310452L;

	public ApplicationEntity(ClientEntity clientEntity, String name, String title, String description,
			boolean enabled, String image, String template, String locale, Boolean defaultApp,
			Integer epsg, JsonNode strings,
			String centroidwkt, Integer zoom, Integer maxZoom, Integer minZoom, 
			BigDecimal minres, BigDecimal maxres
			) {
		super(clientEntity, name, title, description,  enabled, image, template, locale, defaultApp, 
				epsg, strings);
		this.centroidwkt = centroidwkt;
		this.zoom = zoom;
		this.minZoom = minZoom;
		this.maxZoom = maxZoom;
		this.minres = minres;
		this.maxres = maxres;
	}
	
	public ApplicationEntity() {
	}

	public void add(Layer layer) {
		getLayers().add(layer);
		layer.setApplicationEntity(this);
	}

	@Column(name = "centroidwkt")
	private String centroidwkt;

	@Column(name = "zoom")
	private Integer zoom;
	
	@Column(name = "min_zoom")
	private Integer minZoom;
	
	@Column(name = "max_zoom")
	private Integer maxZoom;

	@Column(name = "minres", precision = 10, scale = 5)
	private BigDecimal minres;
	
	@Column(name = "maxres", precision = 10, scale = 5)
	private BigDecimal maxres;

	@OneToMany(fetch = FetchType.EAGER, cascade = {
			CascadeType.ALL }, orphanRemoval = true, mappedBy = "applicationEntity")
	@OrderBy("orderlayer ASC")
	@JsonIgnore
	private List<Layer> layers = new ArrayList<Layer>();
	
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
	@JoinColumn(name = "origin_id", updatable = false, nullable = true)
	@JsonIgnore
	private ApplicationEntity origin;

	public Layer getLayerId(Long layerID) {
		for (Layer attr : this.getLayers()) {
			if (attr.getId().equals(layerID)) {
				return attr;
			}
		}
		return null;
	}

	public List<Layer> getLayers() {
		return this.layers;
	}

	public void setLayers(List<Layer> layers) {
		this.layers = layers;
	}
	

	public String getCentroidwkt() {
		return centroidwkt;
	}

	public void setCentroidwkt(String centroidwkt) {
		this.centroidwkt = centroidwkt;
	}

	public Integer getZoom() {
		return zoom;
	}

	public void setZoom(Integer zoom) {
		this.zoom = zoom;
	}

	public Integer getMinZoom() {
		return minZoom;
	}

	public void setMinZoom(Integer minZoom) {
		this.minZoom = minZoom;
	}

	public Integer getMaxZoom() {
		return maxZoom;
	}

	public void setMaxZoom(Integer maxZoom) {
		this.maxZoom = maxZoom;
	}

	public BigDecimal getMinres() {
		return minres;
	}

	public void setMinres(BigDecimal minres) {
		this.minres = minres;
	}

	public BigDecimal getMaxres() {
		return maxres;
	}

	public void setMaxres(BigDecimal maxres) {
		this.maxres = maxres;
	}
	
	public ApplicationEntity getOrigin() {
		return origin;
	}

	public void setOrigin(ApplicationEntity origin) {
		this.origin = origin;
	}

	public String applicationToString() {
		return this.getId().toString();
	}

	public void addLayerItem(Layer layerItem) {
		getLayers().add(layerItem);
		layerItem.setApplicationEntity(this);
	}
	
	public void updateLayerItem(Layer layerItem) {
		List<Layer> layersNew = new ArrayList<Layer>();
		this.layers.retainAll(this.getLayers());
		for (Layer attr : this.getLayers()) {
			if (attr.getId().equals(layerItem.getId())) {
				layersNew.add(layerItem);
			} else {
				layersNew.add(attr);
			}
		}
		this.setLayers(layersNew);
	}
	
	public void clearLayerChildren(Layer layerItem) {
		List<Layer> layersNew = new ArrayList<Layer>();
		this.layers.retainAll(this.getLayers());
		for (Layer attr : this.getLayers()) {
			if (attr.getId().equals(layerItem.getId())) {
				layerItem.getChildrens().clear();
				layersNew.add(layerItem);
			} else {
				layersNew.add(attr);
			}
		}
		this.setLayers(layersNew);
	}
	
	
	public void removeLayerItem(Layer layerItem) {
		List<Layer> layersNew = new ArrayList<Layer>();
		this.layers.retainAll(this.getLayers());
		for (Layer attr : this.getLayers()) {
			if (!attr.getId().equals(layerItem.getId())) {
				layersNew.add(attr);
			}
		}
		this.layers = layersNew;
	}
}