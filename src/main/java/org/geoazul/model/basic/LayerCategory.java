package org.geoazul.model.basic;

import java.util.ArrayList;
import java.util.List;
import org.omnifaces.optimusfaces.test.model.LocalGeneratedIdEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "APP_LAYER_CATEGORY")
public abstract class LayerCategory extends LocalGeneratedIdEntity {
	
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "layer_id")
	private Layer layer;
		
	@Column(name = "categoryname")
	private String categoryname;
	
	@Column(name = "descri")
	private String descri;
	
	@Column(name = "iconflag")
	private String iconflag;
	
	@ManyToMany(cascade = CascadeType.DETACH)
	@JoinTable(name = "app_category_geometry", 
	joinColumns = @JoinColumn(name = "category_id"), 
	inverseJoinColumns = @JoinColumn(name = "geometry_id"))
	private List<AbstractGeometry> geometrias = new ArrayList<AbstractGeometry>();
	
	public List<AbstractGeometry> getGeometrias() {
		return geometrias;
	}

	public void setGeometrias(List<AbstractGeometry> geometrias) {
		this.geometrias = geometrias;
	}
		
	public LayerCategory() {
		super();
	}
	
	public LayerCategory(String categoryname, String descri) {
		this.categoryname = categoryname;
		this.descri = descri;
	}

	public Layer getLayer() {
		return layer;
	}

	
	public void setLayer(Layer layer) {
		this.layer = layer;
	}
	
	
	public String getCategoryname() {
		return categoryname;
	}

	
	public void setCategoryname(String categoryname) {
		this.categoryname = categoryname;
	}

	public String getDescri() {
		return descri;
	}

	public void setDescri(String descri) {
		this.descri = descri;
	}

	public String getIconflag() {
		return iconflag;
	}

	public void setIconflag(String iconflag) {
		this.iconflag = iconflag;
	}


}
