package org.geoazul.ecommerce.view.shopping;

import java.time.LocalDateTime;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.omnifaces.optimusfaces.test.model.LocalGeneratedIdEntity;
import com.erp.modules.inventory.entities.Product;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

/**
 * @author Laercio Tischer
 */

@Entity
@Table(name = "erp_shopping_cart_item")
@NamedQueries({
  @NamedQuery(name = "ShoppingCartItem.FIND_ALL", query = "SELECT s FROM ShoppingCartItem s"),
  @NamedQuery(name = "ShoppingCartItem.UPDATE_QUANT", query = "UPDATE ShoppingCartItem SET quantity = :quantity  where id = :id"),
  @NamedQuery(name = "ShoppingCartItem.DELETE", query = "DELETE FROM ShoppingCartItem where id = :id"),
    })
public class ShoppingCartItem   extends LocalGeneratedIdEntity {

	public ShoppingCartItem() {
	}

	private static final long serialVersionUID = 1L;
	public static final String FIND_ALL = "ShoppingCartItem.FIND_ALL";
	public static final String UPDATE_QUANT = "ShoppingCartItem.UPDATE_QUANT";
	public static final String DELETE = "ShoppingCartItem.DELETE";
   
		
	@ManyToOne(fetch= FetchType.LAZY, cascade = CascadeType.DETACH)	
    @JoinColumn(name="SHOPPING_CART")
    private ShoppingCart shoppingCart;
    
    @Column(name = "DATETIME")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
	private LocalDateTime datetime;

    @ManyToOne(fetch= FetchType.EAGER, cascade = CascadeType.DETACH)	
    @JoinColumn(name="ITEM_ID")
    @NotNull
    private Product item;
    
    @NotNull
    @Min(1)
    private Integer quantity;

    public ShoppingCartItem(Product item, Integer quantity) {
    	this.datetime = LocalDateTime.now();
        this.item = item;
        this.quantity = quantity;
    }

    public Double getSubTotal() {
        return item.getSalePrice()  *  quantity;
    }
   		
    public Product getItem() {
        return item;
    }

    public void setItem(Product item) {
        this.item = item;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

	public ShoppingCart getShoppingCart() {
		return shoppingCart;
	}

	public void setShoppingCart(ShoppingCart shoppingCart) {
		this.shoppingCart = shoppingCart;
	}

    


}