package org.geoazul.model.website;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;
import org.geoazul.model.basic.Layer;
import com.erp.modules.commonClasses.BaseEntity;

/**
 *  
 */

@Entity
@Table(name = "app_layer_item", uniqueConstraints = @UniqueConstraint(columnNames = { "id", "parent_id" }))
@NamedQueries({
		
		@NamedQuery(name = LayerItem.ITENS_ROOT_EXCLUDE_ITEM, query = "select layerItem from LayerItem layerItem"
				+ " where layerItem.layer.id = :urlmenuid" + " and layerItem.parentId is null "
				+ " and layerItem.id <> :urlmenuitemid "),
		
		@NamedQuery(name = LayerItem.ITENS_ROOT, query = "select layerItem from LayerItem layerItem"
				+ " where layerItem.layer.id = :urlmenuid" + " and layerItem.parentId is null "),
		
		@NamedQuery(name = LayerItem.FIND_ID, query = "select layerItem from LayerItem layerItem where layerItem.id = :id"),

		@NamedQuery(name = LayerItem.FIND_APP_LAYER_ROOT, query = "SELECT m FROM LayerItem m WHERE m.parentId IS NULL and m.layer = :layer "
				+ "ORDER BY m.ordering "),
		
		@NamedQuery(name = LayerItem.FIND_APP_LAYER_PARENT_ID, query = "SELECT m FROM LayerItem m WHERE m.parentId.id = :parentId "
				+ "ORDER BY m.ordering "),

		@NamedQuery(name = LayerItem.FIND_APP_LAYERS, query = "SELECT DISTINCT layerItem.layer FROM LayerItem layerItem "
				+ "LEFT JOIN Layer layer  " + "ON layer = layerItem.layer "
				+ "WHERE layer.applicationEntity = :abstractIdentityEntity"),

		@NamedQuery(name = LayerItem.FIND_APP_LAYERS_SIZE, query = "SELECT DISTINCT COUNT(layerItem.layer) FROM LayerItem layerItem "
				+ "LEFT JOIN Layer layer  " + "ON layer = layerItem.layer "
				+ "WHERE layer.applicationEntity = :abstractIdentityEntity"),
		
		
		 @NamedQuery(
		    		name= LayerItem.FIND_ALL_ON_LAYER,
		    		query="select layerItem from LayerItem layerItem where layerItem.layer.id = :layerId AND layerItem.enabled = true ORDER BY layerItem.ordering"),
		
		 
		 @NamedQuery(
		    		name= LayerItem.FIND_ALL_ON_LAYER_ROOT,
		    		query="select layerItem from LayerItem layerItem where "
		    				+ "layerItem.layer.id = :layerId "
		    				+ "AND layerItem.enabled = true "
		    				+ "AND layerItem.parentId is null "
		    				+ "ORDER BY layerItem.ordering"),
	
		 
		 
		@NamedQuery(name = LayerItem.FIND_APP_LAYER_ROOT_ID, query = "SELECT DISTINCT layerItem.id FROM LayerItem layerItem "
				+ "LEFT JOIN Layer layer  " + "ON layer = layerItem.layer "
				+ "WHERE layer.applicationEntity = :abstractIdentityEntity and layerItem.ordering = 0"),
		
	})

public class LayerItem  extends BaseEntity  {

	private static final long serialVersionUID = 1L;

	public static final String FIND_APP_LAYER_ROOT_ID= "LayerItem.findMenuRootId";
	
	public static final String FIND_ALL_ON_LAYER = "LayerItem.findAllOnMenu";
	
	public static final String FIND_ALL_ON_LAYER_ROOT = "LayerItem.findAllOnMenuRoot";
	
	public static final String FIND_APP_LAYER_ROOT = "LayerItem.findMenuRoot";
	public static final String FIND_APP_LAYER_PARENT_ID = "LayerItem.findParentId";

	public static final String FIND_APP_LAYERS = "LayerItem.findMenus";
	public static final String FIND_APP_LAYERS_SIZE = "LayerItem.findMenusSize";

	public static final String ITENS_ROOT_EXCLUDE_ITEM = "LayerItem.itensRootExcludeItem";
	public static final String ITENS_ROOT = "LayerItem.itensRoot";
	public static final String FIND_ID = "LayerItem.id";
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "LAYER_ID")
	private Layer layer;

	@Basic(optional = false) //FIXME
	@Column(name = "name", nullable = false, length = 255)
	@NotNull
	private String name;

	@Column(name = "path", length = 1024)
	private String path;

	@Column(name = "link", length = 1024)
	private String link;

	@Column(name = "icon", length = 50)
	private String icon;
	
	@Column(name = "LINK_HREF", length = 50)
	private String link_href;
	
	@Column(name = "SYMBOL_SVG", length = 255)
	private String symbol_svg;

	@Column(name = "type", nullable = false, length = 16)
	@NotNull
	private String type;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "parent_id", referencedColumnName = "id", updatable = true, nullable = true)
	private LayerItem parentId;

	@Column(name = "language_id", length = 5)
	private String languageId;

	@Column(name = "ordering")
	private int ordering;

	@OneToMany(mappedBy = "parentId", fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
	@OrderBy("ordering ASC")
	private List<LayerItem> childrens = new ArrayList<LayerItem>();

	@Column(name = "enabled")
	private boolean enabled;
		
    //Generators.timeBasedGenerator().generate()
	
	
	public LayerItem() {

	}

	
	

	public LayerItem(Layer layer, String name, String path,
			String link, String icon, String type,	LayerItem parentId, 
			String languageId, int ordering,boolean enabled) {
		this.layer = layer;
		this.name = name;
		this.path = path;
		this.link = link;
		this.icon = icon;
		this.type = type;
		this.parentId = parentId;
		this.languageId = languageId;
		this.ordering = ordering;
		this.enabled = enabled;
	}
	
	

	public Layer getLayer() {
		return layer;
	}

	public void setLayer(Layer layer) {
		this.layer = layer;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		return this.path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getLink() {
		return this.link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public LayerItem getParentId() {
		return this.parentId;
	}

	public void setParentId(LayerItem parentId) {
		this.parentId = parentId;
	}

	public String getLanguageId() {
		return this.languageId;
	}

	public void setLanguageId(String languageId) {
		this.languageId = languageId;
	}

	public List<LayerItem> getChildrens() {
		return childrens;
	}

	public void setChildrens(List<LayerItem> childrens) {
		this.childrens = childrens;
	}

	public int getOrdering() {
		return ordering;
	}

	public void setOrdering(int ordering) {
		this.ordering = ordering;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}




	public String getLink_href() {
		return link_href;
	}




	public void setLink_href(String link_href) {
		this.link_href = link_href;
	}




	public String getSymbol_svg() {
		return symbol_svg;
	}




	public void setSymbol_svg(String symbol_svg) {
		this.symbol_svg = symbol_svg;
	}



}
