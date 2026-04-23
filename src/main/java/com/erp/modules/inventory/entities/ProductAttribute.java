
package com.erp.modules.inventory.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.annotations.ColumnDefault;

import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotNull;



@Entity
@Table(name = "erp_product_atribute")
@NamedQueries({
    @NamedQuery(name = "ProductAttribute.findAll", query = "SELECT p FROM ProductAttribute p"),
    @NamedQuery(name = "ProductAttribute.findById", query = "SELECT p FROM ProductAttribute p WHERE p.id = :id"),
    @NamedQuery(name = "ProductAttribute.findByName", query = "SELECT p FROM ProductAttribute p WHERE p.name = :name"),
    @NamedQuery(name = "ProductAttribute.findByActive", query = "SELECT p FROM ProductAttribute p WHERE p.active = :active")})

public class ProductAttribute  {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @SequenceGenerator(name = "fruitsSequence", sequenceName = "erp_fruits_id_seq", allocationSize = 1, initialValue = 10)
    @GeneratedValue(generator = "fruitsSequence")
    private Integer id;
    
    
	@NotNull
    @Column(name = "name")
    private String name;
	
    @ColumnDefault("true")
    @Column(name = "active")
    @JsonIgnore
    private Boolean active;
    
    @ManyToMany(fetch = FetchType.EAGER, cascade = {})
    @JoinTable(name = "ERP_PRODUCT_ATTRIBUTE_MAP", joinColumns = @JoinColumn(name = "PRODUCT"), inverseJoinColumns = @JoinColumn(name = "ATTRIBUTE"))
    @JsonBackReference
    private Set<Product> compositeAttributes = new HashSet<>();
    
    public Set<Product> getCompositeAttributes() {
        return compositeAttributes;
    }

    public void setCompositeAttributes(Set<Product> compositeAttributes) {
        this.compositeAttributes = compositeAttributes;
    }
    
    
    
    
    @Transient
	private Integer totalRecords;

    public ProductAttribute() {
        
    }

    public ProductAttribute(String name, Boolean active) {
        this.name = name;
        this.active = active;
    }

    public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

   
   
    public Integer getTotalRecords() {
    	
		return this.compositeAttributes.size();
	}

	

    @Override
    public String toString() {
        return "--- ProductAttribute[ id=" + getId() + " ] ---";
    }
    
}
