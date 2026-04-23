package com.erp.modules.inventory.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import br.bancodobrasil.model.JsonJsonNode;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.geoazul.ecommerce.model.Item;
import org.geoazul.ecommerce.view.shopping.ShoppingCartItem;
import org.geoazul.model.app.ProductCategoryMapping;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Type;

@Entity
@NamedQueries({
		@NamedQuery(name = "Product.findAll", query = "SELECT p FROM Product p where p.enabled = true AND p.active = true"),
		
		@NamedQuery(name = "Product.findById", query = "SELECT p FROM Product p WHERE p.id = :id"),
		@NamedQuery(name = "Product.findByDefaultCode", query = "SELECT p FROM Product p WHERE p.defaultCode = :defaultCode AND p.active = true"),
		@NamedQuery(name = "Product.findByName", query = "SELECT p FROM Product p WHERE p.name = :name AND p.active = true"),
		@NamedQuery(name = "Product.findBySalePrice", query = "SELECT p FROM Product p WHERE p.salePrice = :salePrice AND p.active = true"),
		@NamedQuery(name = "Product.findByPurchasePrice", query = "SELECT p FROM Product p WHERE p.purchasePrice = :purchasePrice AND p.active = true"),
		@NamedQuery(name = "Product.findByWeight", query = "SELECT p FROM Product p WHERE p.weight = :weight AND p.active = true"),
		@NamedQuery(name = "Product.findByVolume", query = "SELECT p FROM Product p WHERE p.volume = :volume AND p.active = true"),
		@NamedQuery(name = "Product.findBySaleOk", query = "SELECT p FROM Product p WHERE p.saleOk = true AND p.active = true"),
		@NamedQuery(name = "Product.findByPurchaseOk", query = "SELECT p FROM Product p WHERE p.purchaseOk = true AND p.active = true"),
		@NamedQuery(name = "Product.findByActive", query = "SELECT p FROM Product p WHERE p.active = :active") })
@DiscriminatorValue("2")
public class Product extends Item {

	public static final String FIND_ALL = "Product.findAll";
	public static final String FIND_BY_ID = "Product.findById";

	private static final long serialVersionUID = 1L;

	@Size(max = 25)
	@Column(name = "default_code")
	private String defaultCode;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.DETACH, mappedBy = "product")
	private List<ErpProductUserMapping> wishlist = new ArrayList<ErpProductUserMapping>();

	// @Column(name = "priceamountvalue", precision = 19, scale = 2)
	// private BigDecimal priceAmountValue;

	// @Columns(columns = {
	// @Column(name = "price_amount"),
	// @Column(name = "price_currency")
	// })
	// private MonetaryAmount priceAmount;

	@Column(name = "sale_price")
	private Double salePrice;

	@Column(name = "purchase_price")
	private Double purchasePrice;

	@Column(name = "weight")
	private Integer weight;

	@Column(name = "volume")
	private Double volume = 0d;

	@Column(name = "lenght")
	private Double length = 0d;
	
	@Column(name = "width")
	private Double width = 0d;
	
	@Column(name = "height")
	private Double height = 0d;

    @ColumnDefault("true")
	@Column(name = "sale_ok")
	private Boolean saleOk;

    @ColumnDefault("true")
	@Column(name = "purchase_ok")
	private Boolean purchaseOk;

    @ColumnDefault("true")
	@Column(name = "active")
	private Boolean active;

	@JoinColumn(name = "uom_id")
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private ProductUom uom;

	@OneToOne(cascade = CascadeType.DETACH, mappedBy = "product", fetch = FetchType.LAZY)
	private Inventory inventory;


	@JsonProperty("categories")
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "product")
	@JsonIgnore
	private List<ProductCategoryMapping> compositeCategories = new ArrayList<ProductCategoryMapping>();

	public void addCategory(ProductCategoryMapping productCategoryMapping) {
		this.compositeCategories.add(productCategoryMapping);
	}

	@ManyToMany(fetch = FetchType.LAZY, cascade = {}) // FIXME
	@JoinTable(name = "ERP_PRODUCT_ATTRIBUTE_MAP", joinColumns = @JoinColumn(name = "PRODUCT"), inverseJoinColumns = @JoinColumn(name = "ATTRIBUTE"))
	@JsonManagedReference
	@JsonProperty("attributes")
	private Set<ProductAttribute> compositeAttributes = new HashSet<>();


	@JsonProperty("default_attributes")
	@Type(JsonJsonNode.class)
	@Column(name = "default_attributes")
	@ColumnDefault("'{}'") 
	private JsonNode defaultAttributes;

	@OneToMany(mappedBy = "item")
	private List<ShoppingCartItem> shoppingCartItem = new ArrayList<ShoppingCartItem>();

	
	@JoinColumn(name = "manufacturer_id")
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private Manufacturer manufacturer;
	
	@Column(name = "sold")
	private Integer sold;

	
	public String getDefaultCode() {
		return defaultCode;
	}

	public void setDefaultCode(String defaultCode) {
		this.defaultCode = defaultCode;
	}

	

	public Double getSalePrice() {
		return salePrice;
	}

	public void setSalePrice(Double salePrice) {
		this.salePrice = salePrice;
	}

	public Double getPurchasePrice() {
		return purchasePrice;
	}

	public void setPurchasePrice(Double purchasePrice) {
		this.purchasePrice = purchasePrice;
	}

	public Integer getWeight() {
		return weight;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
	}

	public Double getVolume() {
		return volume;
	}

	public void setVolume(Double volume) {
		this.volume = volume;
	}

	public Double getLength() {
		return length;
	}

	public void setLength(Double length) {
		this.length = length;
	}

	public Boolean getSaleOk() {
		return saleOk;
	}

	public void setSaleOk(Boolean saleOk) {
		this.saleOk = saleOk;
	}

	public Boolean getPurchaseOk() {
		return purchaseOk;
	}

	public void setPurchaseOk(Boolean purchaseOk) {
		this.purchaseOk = purchaseOk;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public ProductUom getUom() {
		return uom;
	}

	public void setUom(ProductUom uom) {
		this.uom = uom;
	}

	public Inventory getInventory() {
		if (inventory == null) {
			inventory = new Inventory();
		}
		return inventory;
	}

	public void setInventory(Inventory inventory) {
		this.inventory = inventory;
	}

	public Set<ProductAttribute> getCompositeAttributes() {
		return compositeAttributes;
	}

	public void setCompositeAttributes(Set<ProductAttribute> compositeAttributes) {
		this.compositeAttributes = compositeAttributes;
	}

	public JsonNode getDefaultAttributes() {
		return defaultAttributes;
	}

	public void setDefaultAttributes(JsonNode defaultAttributes) {
		this.defaultAttributes = defaultAttributes;
	}

	public String getSale_price() {
		return this.salePrice.toString();
	}

	@Override
	public String toString() {
		return "--- Product[ id=" + super.getId() + " ] ---";
	}

	public List<ShoppingCartItem> getShoppingCartItem() {
		return shoppingCartItem;
	}

	public void setShoppingCartItem(List<ShoppingCartItem> shoppingCartItem) {
		this.shoppingCartItem = shoppingCartItem;
	}

	public List<ProductCategoryMapping> getCompositeCategories() {
		return compositeCategories;
	}

	public void setCompositeCategories(List<ProductCategoryMapping> compositeCategories) {
		this.compositeCategories = compositeCategories;
	}
	

	public Double getWidth() {
		return width;
	}

	public void setWidth(Double width) {
		this.width = width;
	}

	public Double getHeight() {
		return height;
	}

	public void setHeight(Double height) {
		this.height = height;
	}
	
	public Manufacturer getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(Manufacturer manufacturer) {
		this.manufacturer = manufacturer;
	}

	public Integer getSold() {
		return sold;
	}

	public void setSold(Integer sold) {
		this.sold = sold;
	}

	public List<ErpProductUserMapping> getWishlist() {
		return wishlist;
	}

	public void setWishlist(List<ErpProductUserMapping> wishlist) {
		this.wishlist = wishlist;
	}




	

	// public MonetaryAmount getPriceAmount() {
	// return priceAmount;
	// }

	// public void setPriceAmount(MonetaryAmount priceAmount) {
	// this.priceAmount = priceAmount;
	// }

	// public BigDecimal getPriceAmountValue() {
	// return priceAmountValue;
	// }

	// public void setPriceAmountValue(BigDecimal priceAmountValue) {
	// this.priceAmountValue = priceAmountValue;
	// }
	
	
	

}