package org.geoazul.ecommerce.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import org.geoazul.model.app.ApplicationIdentityEntity;
import org.hibernate.annotations.Type;
import com.erp.modules.commonClasses.BaseEntity;
import com.erp.modules.inventory.entities.Block;
import com.erp.modules.inventory.entities.ProductMedia;
import java.util.List;
import org.example.kickoff.model.Person;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.NaturalId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import br.bancodobrasil.model.JsonJsonNode;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;


@Entity
@Table(name = "ERP_ITEM")
@DiscriminatorColumn(discriminatorType = DiscriminatorType.INTEGER, name = "dtype", columnDefinition = "smallint")
@NamedQueries({
@NamedQuery(name = Item.SEARCH, query = "SELECT i FROM Item i WHERE UPPER(i.name) LIKE :keyword OR UPPER(i.description) LIKE :keyword ORDER BY i.name")
})
public abstract class Item extends BaseEntity {

	public enum StatusInfo {
		NORMAL, NEW,  BESTVALUE, PROMOTION, OFFER, ONLYCONTACT, COMINGSOON
	}
	
	public static final String SEARCH = "Item.search";

	public Item() {

	}
	
	@OneToMany(mappedBy = "item", fetch = FetchType.LAZY, cascade = CascadeType.DETACH, orphanRemoval = true)
	@OrderBy("sequence ASC") //RISTEOS
	private List<Block> blocks = new ArrayList<Block>();

	@Version
	@Column(name = "version")
	private Integer version;

    @ColumnDefault("true")
	@Column(name = "enabled")
	private Boolean enabled;
	
	@Column(name = "unid_psv",length = 5)
	private String unidade;

	@Enumerated(EnumType.ORDINAL)
	@Column(name = "STATUS_INFO")
	private StatusInfo statusInfo = StatusInfo.NORMAL;
			
	// @Formula("case statusInfo when 'NORMAL' then 0 when 'BESTVALUE' then 1 when 'PROMOTION' then 2 when 'ONLYCONTACT' then 3 when 'COMINGSOON' then 4 ... end")
	// private int statusInfoInteger;

	@Column(length = 200)
	@Size(min = 1, max = 200)
	private String name;

	@Column(name = "introduction")
	@Size(min = 1, max = 10000)
	private String introduction;

	//@Size(max = 2147483647, message = "{LongString}")
	//@Type(type = "org.hibernate.type.TextType")   
	@Column(name = "DESCRIPTION", columnDefinition = "TEXT")
	private String description;

	private Integer rank;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "ERP_SERVICE_APPIDENTITY", joinColumns = {
			@JoinColumn(name = "SERVICE_ID", nullable = false) }, inverseJoinColumns = {
					@JoinColumn(name = "APP_ID", nullable = false) })
	private List<ApplicationIdentityEntity> appidentity;

	// -----------------------------------------

	private String permalink;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
	private Date date_created = new Date();

	private String type; // simple
	
	private String status; // published
	
    @ColumnDefault("true")
	private Boolean featured;
    
	private String catalog_visibility; // visible

	private String sku; // codido

	// private String price;

	// private String regular_price;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
	private Date date_on_sale_from = new Date();

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
	
	private Date date_on_sale_to = new Date();
	
    @ColumnDefault("false")
	private Boolean on_sale;
	
    
	private Integer total_sales;
	
    @ColumnDefault("false")
	private Boolean purchasable;

    @ColumnDefault("false")
	private Boolean virtual;
    
    @ColumnDefault("false")
	private Boolean downloadable;

	@Type(JsonJsonNode.class)
	@Column(name = "downloads")
	@ColumnDefault("'{}'") 
	private JsonNode downloads;

	private Integer download_limit;
	private Integer download_expiry;
	private String external_url;
	private String button_text;

	private String tax_status; // taxable
	private String tax_class;

    @ColumnDefault("true")
	private Boolean manage_stock;
    
	private Integer stock_quantity;
	private String backorders; // no
	
    @ColumnDefault("false")
	private Boolean backorders_allowed; // false
    
    @ColumnDefault("false")
	private Boolean backordered;
    
	private Integer low_stock_amount;
	
    @ColumnDefault("false")
	private Boolean sold_individually;

	@Type(JsonJsonNode.class)
	@Column(name = "dimensions")
	@ColumnDefault("'{}'") 
	private JsonNode dimensions;

    @ColumnDefault("false")
	private Boolean shipping_required;
	
    @ColumnDefault("false")
	private Boolean shipping_taxable;
	
	private String shipping_class;
	private Integer shipping_class_id;
	
    @ColumnDefault("false")
	private Boolean reviews_allowed;
	private Integer average_rating; // 0.00
	private String rating_count;

	@Type(JsonJsonNode.class)
	@Column(name = "upsell_ids")
	@ColumnDefault("'{}'") 
	private JsonNode upsell_ids;

	@Type(JsonJsonNode.class)
	@Column(name = "cross_sell_ids")
	@ColumnDefault("'{}'") 
	private JsonNode cross_sell_ids;

	private Integer parent_id;
	private String purchase_note;

	@Type(JsonJsonNode.class)
	@Column(name = "tags")
	@ColumnDefault("'{}'") 
	private JsonNode tags;

	@Type(JsonJsonNode.class)
	@Column(name = "variations")
	@ColumnDefault("'{}'") 
	private JsonNode variations;

	@Type(JsonJsonNode.class)
	@Column(name = "grouped_products")
	@ColumnDefault("'{}'") 
	private JsonNode grouped_products;

	@Type(JsonJsonNode.class)
	@Column(name = "related_ids")
	@ColumnDefault("'{}'") 
	private JsonNode related_ids;

	@Type(JsonJsonNode.class)
	@Column(name = "meta_data")
	@ColumnDefault("'{}'") 
	private JsonNode meta_data;

	private String stock_status;

	@Type(JsonJsonNode.class)
	@Column(name = "links")
	@JsonProperty("_links")
	@ColumnDefault("'{}'") 
	private JsonNode links;

	@OneToMany(mappedBy = "item", fetch = FetchType.LAZY, cascade = CascadeType.DETACH, orphanRemoval = true)
	//@OrderBy("releasedate DESC") //RISTEOS
	private List<ProductMedia> images = new ArrayList<ProductMedia>();

	
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
	@JoinColumn(name = "PARTNER_ID")
	private Person partner;
	// ----------------------------------------------

	// public Set<Item> getChildren() {
	// return children;
	// }

	// public void setChildren(Set<Item> children) {
	// this.children = children;
	// }

	// public Item getParent() {
	// return parent;
	// }

	// public void setParent(Item parent) {
	// this.parent = parent;
	// }
	
	public List<Block> getBlocks() {
		return blocks;
	}

	public void setBlocks(List<Block> blocks) {
		this.blocks = blocks;
	}

	public Integer getVersion() {
		return this.version;
	}

	public void setVersion(final Integer version) {
		this.version = version;
	}

	 public Boolean getEnabled() {
	        return enabled;
	    }

	    public void setEnabled(Boolean enabled) {
	        this.enabled = enabled;
	    }
	    
	    

	
	public String getUnidade() {
		return unidade;
	}

	public void setUnidade(String unidade) {
		this.unidade = unidade;
	}

	public StatusInfo getStatusInfo() {
		return statusInfo;
	}

	public void setStatusInfo(StatusInfo statusInfo) {
		this.statusInfo = statusInfo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getRank() {
		return rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}

	

	

	public List<ApplicationIdentityEntity> getAppidentity() {
		return appidentity;
	}

	public void setAppidentity(List<ApplicationIdentityEntity> appidentity) {
		this.appidentity = appidentity;
	}

	public String getPermalink() {
		return permalink;
	}

	public void setPermalink(String permalink) {
		this.permalink = permalink;
	}

	public Date getDate_created() {
		return date_created;
	}

	public void setDate_created(Date date_created) {
		this.date_created = date_created;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Boolean getFeatured() {
		return featured;
	}

	public void setFeatured(Boolean featured) {
		this.featured = featured;
	}

	public String getCatalog_visibility() {
		return catalog_visibility;
	}

	public void setCatalog_visibility(String catalog_visibility) {
		this.catalog_visibility = catalog_visibility;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public Date getDate_on_sale_from() {
		return date_on_sale_from;
	}

	public void setDate_on_sale_from(Date date_on_sale_from) {
		this.date_on_sale_from = date_on_sale_from;
	}

	public Date getDate_on_sale_to() {
		return date_on_sale_to;
	}

	public void setDate_on_sale_to(Date date_on_sale_to) {
		this.date_on_sale_to = date_on_sale_to;
	}

	public Boolean getOn_sale() {
		return on_sale;
	}

	public void setOn_sale(Boolean on_sale) {
		this.on_sale = on_sale;
	}

	public Boolean getPurchasable() {
		return purchasable;
	}

	public void setPurchasable(Boolean purchasable) {
		this.purchasable = purchasable;
	}

	public Integer getTotal_sales() {
		return total_sales;
	}

	public void setTotal_sales(Integer total_sales) {
		this.total_sales = total_sales;
	}

	public Boolean getVirtual() {
		return virtual;
	}

	public void setVirtual(Boolean virtual) {
		this.virtual = virtual;
	}

	public Boolean getDownloadable() {
		return downloadable;
	}

	public void setDownloadable(Boolean downloadable) {
		this.downloadable = downloadable;
	}

	public JsonNode getDownloads() {
		return downloads;
	}

	public void setDownloads(JsonNode downloads) {
		this.downloads = downloads;
	}

	public JsonNode getDimensions() {
		return dimensions;
	}

	public void setDimensions(JsonNode dimensions) {
		this.dimensions = dimensions;
	}

	public Integer getDownload_limit() {
		return download_limit;
	}

	public void setDownload_limit(Integer download_limit) {
		this.download_limit = download_limit;
	}

	public Integer getDownload_expiry() {
		return download_expiry;
	}

	public void setDownload_expiry(Integer download_expiry) {
		this.download_expiry = download_expiry;
	}

	public String getExternal_url() {
		return external_url;
	}

	public void setExternal_url(String external_url) {
		this.external_url = external_url;
	}

	public String getButton_text() {
		return button_text;
	}

	public void setButton_text(String button_text) {
		this.button_text = button_text;
	}

	public String getTax_status() {
		return tax_status;
	}

	public void setTax_status(String tax_status) {
		this.tax_status = tax_status;
	}

	public String getTax_class() {
		return tax_class;
	}

	public void setTax_class(String tax_class) {
		this.tax_class = tax_class;
	}

	public Boolean getManage_stock() {
		return manage_stock;
	}

	public void setManage_stock(Boolean manage_stock) {
		this.manage_stock = manage_stock;
	}

	public Integer getStock_quantity() {
		return stock_quantity;
	}

	public void setStock_quantity(Integer stock_quantity) {
		this.stock_quantity = stock_quantity;
	}

	public String getBackorders() {
		return backorders;
	}

	public void setBackorders(String backorders) {
		this.backorders = backorders;
	}

	public Boolean getBackorders_allowed() {
		return backorders_allowed;
	}

	public void setBackorders_allowed(Boolean backorders_allowed) {
		this.backorders_allowed = backorders_allowed;
	}

	public Boolean getBackordered() {
		return backordered;
	}

	public void setBackordered(Boolean backordered) {
		this.backordered = backordered;
	}

	public Integer getLow_stock_amount() {
		return low_stock_amount;
	}

	public void setLow_stock_amount(Integer low_stock_amount) {
		this.low_stock_amount = low_stock_amount;
	}

	public Boolean getSold_individually() {
		return sold_individually;
	}

	public void setSold_individually(Boolean sold_individually) {
		this.sold_individually = sold_individually;
	}

	public Boolean getShipping_required() {
		return shipping_required;
	}

	public void setShipping_required(Boolean shipping_required) {
		this.shipping_required = shipping_required;
	}

	public Boolean getShipping_taxable() {
		return shipping_taxable;
	}

	public void setShipping_taxable(Boolean shipping_taxable) {
		this.shipping_taxable = shipping_taxable;
	}

	public String getShipping_class() {
		return shipping_class;
	}

	public void setShipping_class(String shipping_class) {
		this.shipping_class = shipping_class;
	}

	public Integer getShipping_class_id() {
		return shipping_class_id;
	}

	public void setShipping_class_id(Integer shipping_class_id) {
		this.shipping_class_id = shipping_class_id;
	}

	public Boolean getReviews_allowed() {
		return reviews_allowed;
	}

	public void setReviews_allowed(Boolean reviews_allowed) {
		this.reviews_allowed = reviews_allowed;
	}

	public Integer getAverage_rating() {
		return average_rating;
	}

	public void setAverage_rating(Integer average_rating) {
		this.average_rating = average_rating;
	}

	public String getRating_count() {
		return rating_count;
	}

	public void setRating_count(String rating_count) {
		this.rating_count = rating_count;
	}

	public Integer getParent_id() {
		return parent_id;
	}

	public void setParent_id(Integer parent_id) {
		this.parent_id = parent_id;
	}

	public String getPurchase_note() {
		return purchase_note;
	}

	public void setPurchase_note(String purchase_note) {
		this.purchase_note = purchase_note;
	}

	public JsonNode getUpsell_ids() {
		return upsell_ids;
	}

	public void setUpsell_ids(JsonNode upsell_ids) {
		this.upsell_ids = upsell_ids;
	}

	public JsonNode getCross_sell_ids() {
		return cross_sell_ids;
	}

	public void setCross_sell_ids(JsonNode cross_sell_ids) {
		this.cross_sell_ids = cross_sell_ids;
	}

	public JsonNode getTags() {
		return tags;
	}

	public void setTags(JsonNode tags) {
		this.tags = tags;
	}

	
	

	public JsonNode getRelated_ids() {
		return related_ids;
	}

	public void setRelated_ids(JsonNode related_ids) {
		this.related_ids = related_ids;
	}

	public JsonNode getMeta_data() {
		return meta_data;
	}

	public void setMeta_data(JsonNode meta_data) {
		this.meta_data = meta_data;
	}

	public String getStock_status() {
		return stock_status;
	}

	public void setStock_status(String stock_status) {
		this.stock_status = stock_status;
	}

	public JsonNode getLinks() {
		return links;
	}

	public void setlinks(JsonNode links) {
		this.links = links;
	}

	// public Collection<ProductAttributeEntity> getAttributes() {
	// return attributes;
	// }

	// public void setAttributes(Collection<ProductAttributeEntity> attributes) {
	// this.attributes = attributes;
	// }

	public JsonNode getVariations() {
		return variations;
	}

	public void setVariations(JsonNode variations) {
		this.variations = variations;
	}

	public JsonNode getGrouped_products() {
		return grouped_products;
	}

	public void setGrouped_products(JsonNode grouped_products) {
		this.grouped_products = grouped_products;
	}

	public List<ProductMedia> getImages() {
		return images;
	}

	public void setImages(List<ProductMedia> images) {
		this.images = images;
	}

	// -------------

	

	public Person getPartner() {
		return partner;
	}

	public void setPartner(Person partner) {
		this.partner = partner;
	}





}