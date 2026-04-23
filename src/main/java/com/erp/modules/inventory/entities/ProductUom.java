package com.erp.modules.inventory.entities;

import com.erp.modules.commonClasses.BaseEntity;
import java.util.List;

import org.hibernate.annotations.ColumnDefault;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "erp_product_uom")
@NamedQueries({
    @NamedQuery(name = "ProductUom.findAll", query = "SELECT p FROM ProductUom p"),
    @NamedQuery(name = "ProductUom.findById", query = "SELECT p FROM ProductUom p WHERE p.id = :id"),
    @NamedQuery(name = "ProductUom.findByName", query = "SELECT p FROM ProductUom p WHERE p.name = :name"),
    @NamedQuery(name = "ProductUom.findByActive", query = "SELECT p FROM ProductUom p WHERE p.active = :active")})

public class ProductUom extends BaseEntity {
    
    private static final long serialVersionUID = 1L;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 64, message = "{LongString}")
    @Column(name = "name")
    private String name;    
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "decimals")
    private Integer decimals;    
    
    @ColumnDefault("true")
    @Column(name = "active")
    private Boolean active;
    
    @OneToMany(mappedBy = "uom")
    private List<Product> products;
    
    
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private ProductUomCategory category;
    

    public ProductUom() {
    }


    public ProductUom(String name, Boolean active) {
        this.name = name;
        this.active = active;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Integer getDecimals() {
        return decimals;
    }

    public void setDecimals(Integer decimals) {
        this.decimals = decimals;
    }
 
    public ProductUomCategory getCategory() {
        return category;
    }

    public void setCategory(ProductUomCategory category) {
        this.category = category;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    @Override
    public String toString() {
        return "--- ProductUom[ id=" + super.getId() + " ] ---";
    }
    
}
