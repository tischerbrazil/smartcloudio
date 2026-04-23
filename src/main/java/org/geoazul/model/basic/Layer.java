package org.geoazul.model.basic;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.geoazul.model.app.AbstractIdentityEntity;
import org.geoazul.model.basic.properties.Field;
import org.geoazul.model.security.ClientComponentEntity;
import org.geoazul.model.website.LayerItem;
import org.hibernate.annotations.Type;
import com.fasterxml.jackson.databind.JsonNode;
import br.bancodobrasil.model.JsonGeometryStyle;
import br.bancodobrasil.model.JsonLabelStyle;

@Entity
		@NamedQueries({ @NamedQuery(name = Layer.LAYER_ID, query = "select layer from Layer layer where layer.id = :id"),
		
		@NamedQuery(name = Layer.LAYER_BY_APP, query = "select layer from Layer layer where applicationEntity = :applicationEntity"),

		@NamedQuery(name = Layer.ITENS_ROOT_EXCLUDE_ITEM, query = "select layer from Layer layer"
				+ " where layer.applicationEntity.id = :applicationEntityId" + " and layer.father is null "
				+ " and layer.id <> :layerId "),

		@NamedQuery(name = Layer.ITENS_ROOT, query = "select layer from Layer layer"
				+ " where layer.applicationEntity.id = :applicationEntityId" + " and layer.father is null "),

		@NamedQuery(name = Layer.FIND_ID, query = "select layer from Layer layer where layer.id = :layerId"),

		@NamedQuery(name = Layer.FIND_APP_LAYER_ROOT, query = "SELECT layer FROM Layer layer WHERE layer.father IS NULL and layer = :layer "
				+ "ORDER BY layer.orderlayer "),

		@NamedQuery(name = Layer.FIND_APP_LAYER_PARENT_ID, query = "SELECT layer FROM Layer layer WHERE layer.father.id = :fatherId "
				+ "ORDER BY layer.orderlayer "),

		@NamedQuery(name = Layer.FIND_APP_LAYERS, query = "SELECT DISTINCT layer FROM Layer layer "
				+ "WHERE layer.applicationEntity = :applicationEntity"),

		@NamedQuery(name = Layer.FIND_APP_LAYERS_SIZE, query = "SELECT DISTINCT COUNT(layer) FROM Layer layer "
				+ "WHERE layer.applicationEntity = :applicationEntity"),

		@NamedQuery(name = Layer.FIND_ALL_ON_LAYER, query = "select layer from Layer layer where layer.applicationEntity.id = :applicationEntityId AND layer.enabled = true ORDER BY layer.orderlayer"),

		@NamedQuery(name = Layer.FIND_ALL_ON_LAYER_ROOT, query = "select layer from Layer layer where "
				+ "layer.applicationEntity.id = :applicationEntityId " + "AND layer.enabled = true "
				+ "AND layer.father is null " + "ORDER BY layer.orderlayer"),

		@NamedQuery(name = Layer.FIND_APP_LAYER_ROOT_ID, query = "SELECT DISTINCT layer.id FROM Layer layer "
				+ "WHERE layer.applicationEntity = :applicationEntity and layer.orderlayer = 0")

})
public abstract class Layer extends Comp {

	public static final String LAYER_BY_APP = "Layer.layerByApp";
	public static final String LAYER_ID = "Layer.layerId";

	public static final String FIND_APP_LAYER_ROOT_ID = "Layer.findLayerRootId";

	public static final String FIND_ALL_ON_LAYER = "Layer.findAllOnLayer";

	public static final String FIND_ALL_ON_LAYER_ROOT = "Layer.findAllOnLayerRoot";

	public static final String FIND_APP_LAYER_ROOT = "Layer.findLayerRoot";
	public static final String FIND_APP_LAYER_PARENT_ID = "Layer.findFatherId";

	public static final String FIND_APP_LAYERS = "Layer.findLayers";
	public static final String FIND_APP_LAYERS_SIZE = "Layer.findLayerSize";

	public static final String ITENS_ROOT_EXCLUDE_ITEM = "Layer.itensRootExcludeItem";
	public static final String ITENS_ROOT = "Layer.itensRoot";
	public static final String FIND_ID = "Layer.id";


	

	@OneToMany(cascade = CascadeType.DETACH, fetch = FetchType.LAZY, mappedBy = "layer")
	@OrderBy("ordering ASC")
	private List<LayerItem> layerItens = new ArrayList<LayerItem>();

	public List<LayerItem> getLayerItens() {
		return layerItens;
	}

	public void setLayerItens(List<LayerItem> layerItens) {
		this.layerItens = layerItens;
	}

	public Layer(AbstractIdentityEntity applicationEntity, Boolean enabled, String name, String title,
			String description, String icon, ClientComponentEntity clientComponentEntity, Layer layerCat,
			JsonNode strings) {
		super(applicationEntity, enabled, name, title, description, icon, clientComponentEntity, layerCat, strings);
	}

	public Layer() {

	}

	@Column(name = "image")
	private String image;

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

	@Column(name = "editable")
	private boolean editable;

	public boolean hasImage() {
		for (Field attr : this.getFields()) {
			if (attr.getTypeData().equals("image")) {
				return true;
			}
		}
		return false;
	}

	@Column(name = "scriptstyle")
	private String scriptStyle;

	@Type(JsonGeometryStyle.class)
	@Column(name = "geometry_style")
	private GeometryStyle geometryStyle;

	@Type(JsonLabelStyle.class)
	@Column(name = "label_style")
	private LabelStyle labelStyle;

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getScriptStyle() {
		return scriptStyle;
	}

	public void setScriptStyle(String scriptStyle) {
		this.scriptStyle = scriptStyle;
	}

	public GeometryStyle getGeometryStyle() {
		return geometryStyle;
	}

	public void setGeometryStyle(GeometryStyle geometryStyle) {
		this.geometryStyle = geometryStyle;
	}

	public LabelStyle getLabelStyle() {
		return labelStyle;
	}

	public void setLabelStyle(LabelStyle labelStyle) {
		this.labelStyle = labelStyle;
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

	public boolean isEditable() {
		return editable;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
	}

}