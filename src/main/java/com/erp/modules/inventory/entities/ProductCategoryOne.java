package com.erp.modules.inventory.entities;

import com.erp.modules.commonClasses.BaseEntity;
import java.util.List;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.geoazul.model.app.ProductCategoryMapping;
import org.hibernate.annotations.ColumnDefault;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import jakarta.persistence.CascadeType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Transient;

@Entity
@Table(name = "erp_product_category_one")
@NamedQueries({
    @NamedQuery(name = "ProductCategoryOne.findAll", query = "SELECT p FROM ProductCategoryOne p WHERE p.parentId is not null order by p.sequence"),
    @NamedQuery(name = "ProductCategoryOne.findById", query = "SELECT p FROM ProductCategoryOne p WHERE p.id = :id order by p.sequence"),
    @NamedQuery(name = "ProductCategoryOne.findByName", query = "SELECT p FROM ProductCategoryOne p WHERE p.name = :name order by p.sequence"),
    @NamedQuery(name = "ProductCategoryOne.findByActive", query = "SELECT p FROM ProductCategoryOne p WHERE p.active = true order by p.sequence")})
public class ProductCategoryOne extends BaseEntity {
    
    private static final long serialVersionUID = 1L;
    
    public static final String FIND_BY_ACTIVE = "ProductCategoryOne.findByActive";
    public static final String FIND_ALL = "ProductCategoryOne.findAll";
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40, message = "{LongString}")
    @Column(name = "NAME")
    private String name;
    
    @Column(name = "LINK_HREF", length = 255)
	private String link_href;
	
	@Column(name = "SYMBOL_SVG", length = 255)
	private String symbol_svg;
    
    @ColumnDefault("true")
    @Column(name = "ACTIVE")
    private Boolean active;
    
    @Column(name = "SEQUENCE")
	private Integer sequence;
        
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.DETACH, mappedBy = "productCategoryOne")
	@JsonIgnore
	private List<ProductCategoryMapping> compositeCategories = new ArrayList<ProductCategoryMapping>();

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
   	@JoinColumn(name = "parent_id", referencedColumnName = "id", updatable = true, nullable = true)
   	private ProductCategoryOne parentId;
       
   	@OneToMany(mappedBy = "parentId", fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
   	@OrderBy("id ASC")
   	private List<ProductCategoryOne> childrens = new ArrayList<ProductCategoryOne>();
       
    public ProductCategoryOne() {
    }

    public ProductCategoryOne(String name, Boolean active) {
        this.name = name;
        this.active = active;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
     
    public List<ProductCategoryMapping> getCompositeCategories() {
		return compositeCategories;
	}

	public void setCompositeCategories(List<ProductCategoryMapping> compositeCategories) {
		this.compositeCategories = compositeCategories;
	}

	public ProductCategoryOne getParentId() {
		return parentId;
	}

	public void setParentId(ProductCategoryOne parentId) {
		this.parentId = parentId;
	}
	
	public Integer getsequence() {
		return sequence;
	}

	public void setsequence(Integer sequence ) {
		this.sequence =sequence;
	}

	public List<ProductCategoryOne> getChildrens() {
		return childrens;
	}

	public void setChildrens(List<ProductCategoryOne> childrens) {
		this.childrens = childrens;
	}

	@Transient
	private Integer totalRecords;

	
	

}
