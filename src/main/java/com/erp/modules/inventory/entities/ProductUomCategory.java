
package com.erp.modules.inventory.entities;

import com.erp.modules.commonClasses.BaseEntity;
import java.util.List;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * 
 * 
 * 
  * 
 */

@Entity
@Table(name = "erp_product_uom_category")
@NamedQueries({
    @NamedQuery(name = "ProductUomCategory.findAll", query = "SELECT p FROM ProductUomCategory p"),
    @NamedQuery(name = "ProductUomCategory.findById", query = "SELECT p FROM ProductUomCategory p WHERE p.id = :id"),
    @NamedQuery(name = "ProductUomCategory.findByName", query = "SELECT p FROM ProductUomCategory p WHERE p.name = :name")})

public class ProductUomCategory extends BaseEntity {
    
    private static final long serialVersionUID = 1L;
   
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 64, message = "{LongString}")
    @Column(name = "name")
    private String name;
    @OneToMany(mappedBy = "category")
    private List<ProductUom> uoms;

    public ProductUomCategory() {
    }

    public ProductUomCategory(Integer id, String name) {
        this.name = name;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ProductUom> getUoms() {
        return uoms;
    }

    public void setUoms(List<ProductUom> uoms) {
        this.uoms = uoms;
    }


    @Override
    public String toString() {
        return "--- ProductUomCategory[ id=" + super.getId() + " ] ---";
    }
    
}
