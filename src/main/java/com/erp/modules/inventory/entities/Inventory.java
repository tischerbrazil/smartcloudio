
package com.erp.modules.inventory.entities;

import org.hibernate.annotations.ColumnDefault;

import com.erp.modules.commonClasses.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Basic;
import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

/**
 * 
 * 
 * 
 * 
 */

@Entity
@Table(name = "erp_inventory")
@NamedQueries({
    @NamedQuery(name = "Inventory.findAll", query = "SELECT i FROM Inventory i"),
    @NamedQuery(name = "Inventory.findById", query = "SELECT i FROM Inventory i WHERE i.id = :id"),
    @NamedQuery(name = "Inventory.findByMaxQty", query = "SELECT i FROM Inventory i WHERE i.maxQty = :maxQty"),
    @NamedQuery(name = "Inventory.findByMinQty", query = "SELECT i FROM Inventory i WHERE i.minQty = :minQty"),
    @NamedQuery(name = "Inventory.findByActive", query = "SELECT i FROM Inventory i WHERE i.active = :active")})
//@Cacheable
//@org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.READ_WRITE)
public class Inventory extends BaseEntity{
    
    private static final long serialVersionUID = 1L;
    
    @Column(name = "max_qty")
    private Double maxQty;
    @Column(name = "min_qty")
    private Double minQty;
    @NotNull
    @Column(name = "quantity")
    private Double quantityOnHand = 0d;
    @NotNull
    @Column(name = "incoming")
    private Double incomingQuantity = 0d;
    @NotNull
    @Column(name = "reserved")
    private Double reservedQuantity = 0d;
 //   @Basic(optional = false)
    @NotNull
    @Column(name = "unit_cost")
    private Double unitCost;
    @Basic(optional = false)
    @NotNull
    @Column(name = "total_cost")
    private Double totalCost;
    

    @ColumnDefault("true")
    @Column(name = "active", nullable = false)
    private Boolean active;
    
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    @OneToOne(optional = false)
    @JsonIgnore
    private Product product;

    public Inventory() {
    }

    public Inventory(Double quantityOnHand, Boolean active) {
        this.quantityOnHand = quantityOnHand;
        this.active = active;
    }


    public Double getMaxQty() {
        return maxQty;
    }

    public void setMaxQty(Double maxQty) {
        this.maxQty = maxQty;
    }

    public Double getMinQty() {
        return minQty;
    }

    public void setMinQty(Double minQty) {
        this.minQty = minQty;
    }

    public Double getQuantityOnHand() {
        return quantityOnHand;
    }

    public void setQuantityOnHand(Double quantityOnHand) {
        this.quantityOnHand = quantityOnHand;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Double getQuantityAvailable() {
    	try {
    	        return (quantityOnHand + incomingQuantity - reservedQuantity );
    	}catch (Exception wc){
    	  		wc.printStackTrace();
    	}
    	return 0d;
    }

    public Double getIncomingQuantity() {
        return incomingQuantity;
    }

    public void setIncomingQuantity(Double incomingQuantity) {
        this.incomingQuantity = incomingQuantity;
    }

    public Double getReservedQuantity() {
        return reservedQuantity;
    }

    public void setReservedQuantity(Double reservedQuantity) {
        this.reservedQuantity = reservedQuantity;
    }

    public Double getUnitCost() {
        return unitCost;
    }

    public void setUnitCost(Double unitCost) {
        this.unitCost = unitCost;
    }

    public Double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(Double totalCost) {
        this.totalCost = totalCost;
    }

    @Override
    public String toString() {
        return "--- Inventory[ id=" + super.getId() + " ] ---";
    }
    
}
