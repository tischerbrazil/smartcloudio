package org.geoazul.model.basic;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import org.geoazul.model.app.AbstractIdentityEntity;
import org.geoazul.model.app.CompAttributeEntity;
import org.geoazul.model.basic.properties.Field;
import org.geoazul.model.security.ClientComponentEntity;
import org.geoazul.model.website.UrlMenuItem;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Type;
import com.erp.modules.commonClasses.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import br.bancodobrasil.model.JsonJsonNode;

	@Entity
	@Table(name = "APP_COMPONENT")
	@DiscriminatorColumn(name = "dtype")
	@NamedQueries({
	    @NamedQuery(
	    		name= Comp.COMP_ID, 
	    		query="select comp from Comp comp where comp.id = :id"),
	}) 
	@JsonFilter("layerFilter")
	public abstract class Comp  extends BaseEntity  {

	public static final String COMP_ID = "Comp.compId";
	
	
	
	
	
	

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
	@JoinColumn(name = "application_id")
	@JsonIgnore
	private AbstractIdentityEntity applicationEntity;
	
	@OneToMany(fetch = FetchType.LAZY, cascade = { CascadeType.ALL }, orphanRemoval = true, mappedBy = "component")
	@JsonIgnore
	private Collection<CompAttributeEntity> attributes = new ArrayList<CompAttributeEntity>();
	
	@OneToMany(cascade = CascadeType.DETACH, fetch = FetchType.LAZY, mappedBy = "urlMenu")
	@OrderBy("ordering ASC")
	private List<UrlMenuItem> menuItens = new ArrayList<UrlMenuItem>();  

	@Column(name = "orderlayer")
	private Integer orderlayer;
	
    @Column(name="enabled_comp")
	private Boolean enabled;
    
    //@ColumnDefault("1.00")
    @Column(name="opacity", columnDefinition="Decimal(3,2) default '1.00'")
	private BigDecimal opacity = new BigDecimal(1.00);
	

   
    
	@Column(name = "IMAGE")
	private String image;
	
	@Column(name = "dtype", insertable = false, updatable = false)
	private String dtype;
		
	@Column(name = "name")
	private String name;
	
	@Column(name = "title")
	private String title;
	
	@Column(name = "description")
	private String description;
	
	@Column(name = "icon")
	private String icon;
	
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.DETACH, mappedBy = "layer", orphanRemoval=true)
	@OrderBy("sequence ASC")
    private List<Field> fields = new ArrayList<Field>();

	@ColumnDefault("true")
	@Column(name = "selected")
	private Boolean selected;
		
	@ManyToOne(fetch = FetchType.LAZY, 
			cascade = CascadeType.DETACH)
	@JoinColumn(name = "client_id")
	@JsonIgnore
	private ClientComponentEntity clientComponentEntity;
	
	@OneToMany(fetch = FetchType.LAZY, cascade = {
			CascadeType.DETACH }, orphanRemoval = true, mappedBy = "layer")
	@OrderBy("parte ASC")
	//@OrderBy("father ASC, parte ASC")
	private List<AbstractGeometry> geometrias = new ArrayList<AbstractGeometry>();
 
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
	@JoinColumn(name = "layer_cat_id", nullable = true)
	@JsonIgnore
	private Layer layerCat;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.DETACH, mappedBy = "layerCat")
	private List<Layer> childrensCategories = new ArrayList<Layer>();

	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
	@JoinColumn(name = "father_id", nullable = true)
	@JsonIgnore
	private Layer father;

	@OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.DETACH }, orphanRemoval = true, mappedBy = "father")
	@OrderBy("orderlayer ASC")
	@JsonIgnore
	private List<Layer> childrens = new ArrayList<Layer>();
	
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.DETACH, mappedBy = "layer", orphanRemoval = true)
	@OrderBy("categoryname ASC")
	@JsonIgnore
	Set<LayerCategory> layerCategories = new HashSet<LayerCategory>(0);
	
	
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
	@JoinColumn(name = "origin_id", nullable = true)
	@JsonIgnore
	private Layer origin;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.DETACH, mappedBy = "origin")
	private List<Layer> childrensOrigins = new ArrayList<Layer>();

	public enum IconcategoryId {
		NONE, ENTITY, CATEGORY
	}
 
	@Enumerated(EnumType.ORDINAL)
	@Column(name = "iconcategory")
	private IconcategoryId iconcategory = IconcategoryId.NONE;
	
	private Integer epsg;
	
	@Type(JsonJsonNode.class)
	@Column(name = "strings")
	@ColumnDefault("'{}'") 
	private JsonNode strings;
	
	@Transient
	private Integer totalRecords;
	   
   	
	//----------
	
	public String layerToString() {
		return this.getId().toString();
	}

	
	public Comp() {
	
	}


	//---------------------- MENU CREATOR CONSTRUTOR
	public Comp( 
			AbstractIdentityEntity applicationEntity, Boolean enabled,  
			String name, String title, String description, 
			String icon,	ClientComponentEntity clientComponentEntity, 
			Layer layerCat,	JsonNode strings) {
		this.applicationEntity = applicationEntity;
		this.enabled = enabled;
		this.name = name;
		this.title = title;
		this.description = description;
		this.icon = icon;
		this.clientComponentEntity = clientComponentEntity;
		this.layerCat = layerCat;
		this.strings = strings;
	}

	public String layerString() {
		return "l" + this.getId().toString() + "-" + getDtype();
	}
	
	public String storeLayerString() {
		return "s" + this.getId().toString() + "-" + getDtype();
	}
	
	public Set<LayerCategory> getLayerCategories() {
		return layerCategories;
	}

	public void setLayerCategories(Set<LayerCategory> layerCategories) {
		this.layerCategories = layerCategories;
	}

	public void addCategoryItem(LayerCategory categoryItem) {
		layerCategories.add(categoryItem);
	}

	public void removeCategoryItem(LayerCategory categoryItem) {
		Set<LayerCategory> layerCategoriesNew = new HashSet<LayerCategory>(0);
		this.layerCategories.retainAll(this.getLayerCategories());
		for (LayerCategory attr : this.getLayerCategories()) {
			if (!attr.getId().equals(categoryItem.getId())) {
				layerCategoriesNew.add(attr);
			}
		}
		this.layerCategories = layerCategoriesNew;
	}

	public void updateCategoryItem(LayerCategory categoryItem) {
		Set<LayerCategory> layerCategoriesNew = new HashSet<LayerCategory>(0);
		this.layerCategories.retainAll(this.getLayerCategories());
		for (LayerCategory attr : this.getLayerCategories()) {
			if (attr.getId().equals(categoryItem.getId())) {
				layerCategoriesNew.add(categoryItem);
			} else {
				layerCategoriesNew.add(attr);
			}
		}
		this.layerCategories = layerCategoriesNew;
	}

	public void refreshCategoryItens() {
		Set<LayerCategory> layerCategoriesNew = new HashSet<LayerCategory>(0);
		this.layerCategories.retainAll(this.getLayerCategories());
		for (LayerCategory attr : this.getLayerCategories()) {

			layerCategoriesNew.add(attr);

		}
		this.layerCategories = layerCategoriesNew;
	}

	public void addFieldItem(Field fieldItem) {
		this.fields.add(fieldItem);
	}	
	
	public void updateFieldItem(Field fieldItem) {
		List<Field> fieldsNew = new ArrayList<Field>();
		this.fields.retainAll(this.getFields());
		 for (Field attr : this.getFields()) {
	            if (attr.getId().equals(fieldItem.getId())) {
	            	fieldsNew.add(fieldItem);
	            }else {
	            	fieldsNew.add(attr);
	            }
	        }
		this.fields = fieldsNew;
	}
	
	public void removeFieldItem(Field fieldItem) {
		List<Field> fieldsNew = new ArrayList<Field>();
		this.fields.retainAll(this.getFields());
		 for (Field attr : this.getFields()) {
	            if (!attr.getId().equals(fieldItem.getId())) {
	            	fieldsNew.add(attr);
	            }
	        }
		this.fields = fieldsNew;
	}
		    
	public AbstractIdentityEntity getApplicationEntity() {
		return applicationEntity;
	}

	public void setApplicationEntity(AbstractIdentityEntity applicationEntity) {
		this.applicationEntity = applicationEntity;
	}
	
	public Collection<CompAttributeEntity> getAttributes() {
		return attributes;
	}

	public void setAttributes(Collection<CompAttributeEntity> attributes) {
		this.attributes = attributes;
	}

	public List<UrlMenuItem> getMenuItens() {
		return menuItens;
	}

	public void setMenuItens(List<UrlMenuItem> menuItens) {
		this.menuItens = menuItens;
	}
	
	public Integer getOrderlayer() {
		return orderlayer;
	}

	public void setOrderlayer(Integer orderlayer) {
		this.orderlayer = orderlayer;
	}
    
    public Boolean getEnabled() {
        return this.enabled;
    }
    
    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
    
	public BigDecimal getOpacity() {
		return opacity;
	}

	public void setOpacity(BigDecimal opacity) {
		this.opacity = opacity;
	}


	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

   	

	
   	
	public String getDtype() {
		return dtype;
	}

	public void setDtype(String dtype) {
		this.dtype = dtype;
	}
	
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public List<Field> getFields() {
		return fields;
	}

	public void setFields(List<Field> fields) {
		this.fields = fields;
	}
				
	
	public Boolean getSelected() {
		return selected;
	}

	public void setSelected(Boolean selected) {
		this.selected = selected;
	}

	public ClientComponentEntity getClientComponentEntity() {
		return clientComponentEntity;
	}

	public void setClientComponentEntity(ClientComponentEntity clientComponentEntity) {
		this.clientComponentEntity = clientComponentEntity;
	}
	
	public List<AbstractGeometry> getGeometrias() {
		return this.geometrias;
	}

	public void setGeometrias(List<AbstractGeometry> geometrias) {
		this.geometrias = geometrias;
	}
	
	public Layer getLayerCat() {
		return layerCat;
	}

	public void setLayerCat(Layer layerCat) {
		this.layerCat = layerCat;
	}

	public List<Layer> getChildrensCategories() {
		return childrensCategories;
	}

	public void setChildrensCategories(List<Layer> childrensCategories) {
		this.childrensCategories = childrensCategories;
	}
	
	public IconcategoryId getIconcategory() {
		return iconcategory;
	}

	public void setIconcategory(IconcategoryId iconcategory) {
		this.iconcategory = iconcategory;
	}
	
	public Integer getEpsg() {
		return epsg;
	}

	public void setEpsg(Integer epsg) {
		this.epsg = epsg;
	}
	
	public JsonNode getStrings() {
		return strings;
	}

	public void setStrings(JsonNode strings) {
		this.strings = strings;
	}
	
	public Integer getTotalRecords() {
		return totalRecords;
	}

	public void setTotalRecords(Integer totalRecords) {
		this.totalRecords = totalRecords;
	}
	
	
	
	public Layer getFather() {
		return father;
	}

	public void setFather(Layer father) {
		this.father = father;
	}

	public List<Layer> getChildrens() {
		return childrens;
	}

	public void setChildrens(List<Layer> childrens) {
		this.childrens = childrens;
	}

	public List<Layer> getChildrensOrigins() {
		return childrensOrigins;
	}

	public void setChildrensOrigins(List<Layer> childrensOrigins) {
		this.childrensOrigins = childrensOrigins;
	}


	public Layer getOrigin() {
		return origin;
	}


	public void setOrigin(Layer origin) {
		this.origin = origin;
	}

}