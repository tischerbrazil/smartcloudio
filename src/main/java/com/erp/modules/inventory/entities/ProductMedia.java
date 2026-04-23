package com.erp.modules.inventory.entities;


import org.geoazul.ecommerce.model.Item;
import org.omnifaces.optimusfaces.test.model.LocalGeneratedIdEntity;
import org.primefaces.model.SelectableDataModel;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.util.Date;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

/**
 *  Class of productProductMedia type.
 *
 * @author Laercio Tischer
 * @version 2.0
 * @since 2.0
 */

@Entity
@Table(name = "erp_product_media")
@NamedQueries({
	@NamedQuery(name = ProductMedia.ALL, query = 	"select med from ProductMedia med where med.item.id = :id"),
	
		 })
public class ProductMedia  extends LocalGeneratedIdEntity 
implements SelectableDataModel
{
	
	private static final long serialVersionUID = 1L;
	public static final String ALL = "ProductMedia.all";

	
    public ProductMedia() {
		super();
		this.releaseDate =  LocalDateTime.now();
	}
    
	public ProductMedia(String mimeType, String title, String alt, String filename) {
		super();
		this.mimeType = mimeType;
		this.title = title;
		this.setAlt(alt);
		this.filename = filename;
		this.releaseDate = LocalDateTime.now();
		
	   
	}

	/**
     * Identifier of the productProductMedia.
     *
     * @since 2.0
     */
	

	
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
	@JoinColumn(name = "item_id")
	@JsonIgnore
	private Item item;
	
    /**
     * mimeType of the productProductMedia.
     *
     * @since 2.0
     */
		
	@Column(name = "mime_type", insertable = true, updatable = false)
	@JsonIgnore
	private String mimeType;

    /**
     * title of the productProductMedia.
     *
     * @since 2.0
     */
	
    @Column(name = "title")
    @JsonProperty("name")
    protected String title;
    
    /**
     * filename of the filename.
     *
     * @since 2.0
     */
	
    @Column(name = "filename")
    protected String filename;

     /**
     * Date of release for the productProductMedia.
     *
     * @since 2.0
     */
    
    @Column(name = "releasedate")
	@JsonIgnore
    protected LocalDateTime releaseDate;
    
    
    @Column(name = "alt")
    @JsonProperty("alt")
    private String alt;
    
 
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Date date_created = new Date();
       
   @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Date date_modified = new Date();
   
   @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Date date_created_gmt = new Date();
    
   @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
   private Date date_modified_gmt = new Date();
    

    /**
     * Return the type.
     *
     * @return The type from productProductMedia.
     *
     * @version 2.0
     * @since 2.0
     */
    public String getMimeType() {
		return mimeType;
	}
    
    /**
     * Set the type.
     *
     * @param mimeType New mimeType.
     *
     * @version 2.0
     * @since 2.0
     */
	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}
    
    
    /**
     * Return the title.
     *
     * @return The title of the movie.
     *
     * @version 2.0
     * @since 2.0
     */
    public String getTitle() {
        return this.title;
    }
    
    /**
     * Set title.
     *
     * @param title New title.
     *
     * @version 2.0
     * @since 2.0
     */
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    
    /**
     * Set filename.
     *
     * @param filename New filename.
     *
     * @version 2.0
     * @since 2.0
     */
    
    public void setFilename(String filename) {
        this.filename = filename;
    }
    
    
    /**
     * Return the filename.
     *
     * @return The filename of the movie.
     *
     * @version 2.0
     * @since 2.0
     */
    public String getFilename() {
        return this.filename;
    }

    /**
     * Return the release date.
     *
     * @return The release date.
     *
     * @version 2.0
     * @since 2.0
     */
    public LocalDateTime getReleaseDate() {
        return releaseDate;
    }

    /**
     * Set releaseDate.
     *
     * @param releaseDate New date of release.
     *
     * @version 2.0
     * @since 2.0
     */
    public void setReleaseDate(LocalDateTime releaseDate) {
        this.releaseDate = releaseDate;
    }
    
	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	
	
	public String getAlt() {
		return alt;
	}

	public void setAlt(String alt) {
		this.alt = alt;
	}
	
	public Date getDate_created() {
		return date_created;
	}

	public void setDate_created(Date date_created) {
		this.date_created = date_created;
	}

	public Date getDate_created_gmt() {
		return date_created_gmt;
	}

	public void setDate_created_gmt(Date date_created_gmt) {
		this.date_created_gmt = date_created_gmt;
	}

	public Date getDate_modified() {
		return date_modified;
	}

	public void setDate_modified(Date date_modified) {
		this.date_modified = date_modified;
	}

	public Date getDate_modified_gmt() {
		return date_modified_gmt;
	}

	public void setDate_modified_gmt(Date date_modified_gmt) {
		this.date_modified_gmt = date_modified_gmt;
	}
	
	@Override
	public String getRowKey(Object arg0) {
		return arg0.toString();
	}


	@Override
	public Object getRowData(String arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}


	
    
}