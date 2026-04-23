package org.geoazul.model.website.media;

import org.geoazul.model.basic.AbstractGeometry;
import org.omnifaces.optimusfaces.test.model.LocalGeneratedIdEntity;
import org.primefaces.model.SelectableDataModel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
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

/**
 *  Class of media type.
 *
 * @author Laercio Tischer
 * @version 2.0
 * @since 2.0
 */

@Entity
@Table(name = "app_media")
@NamedQueries({
	@NamedQuery(name = Media.ALL, query = 	"select med from Media med"),
	@NamedQuery(name = Media.ALL_ID, query = 	"select med from Media med where med.abstractGeometry.id = :id"),
	@NamedQuery(name = Media.FIND_ID, query = 	"select med from Media med where med.id = :id")
		 })
public class Media  extends LocalGeneratedIdEntity implements SelectableDataModel {
	
	public static final String ALL = "Media.all";
	public static final String ALL_ID = "Media.allId";
	public static final String FIND_ID = "Media.findId";
	
    public Media() {
		super();
		this.releaseDate =  LocalDateTime.now();
	}
    
	public Media(String mimeType, String title, String filename) {
		super();
		this.mimeType = mimeType;
		this.title = title;
		this.filename = filename;
		this.releaseDate = LocalDateTime.now();
	}

	/**
     * Identifier of the media.
     *
     * @since 2.0
     */
	

	
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
	@JoinColumn(name = "geometry_id")
	@JsonIgnore
	private AbstractGeometry abstractGeometry;
	
	
    /**
     * mimeType of the media.
     *
     * @since 2.0
     */
		
	@Column(name = "mime_type", insertable = true, updatable = false)
	private String mimeType;

    /**
     * title of the media.
     *
     * @since 2.0
     */
	
    @Column(name = "title")
    protected String title;
    
    /**
     * filename of the filename.
     *
     * @since 2.0
     */
	
    @Column(name = "filename")
    protected String filename;

     /**
     * Date of release for the media.
     *
     * @since 2.0
     */
    
    @Column(name = "releasedate")
	@JsonIgnore
    protected LocalDateTime releaseDate;
    
    /**
     * Date of release for the media.
     *
     * @since 3.4
     */
    
    @Column(name = "alt")
    @JsonProperty("alt")
    private String alt;
    
    
    /**
     * Return the type.
     *
     * @return The type from media.
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
    
	public AbstractGeometry getAbstractGeometry() {
		return abstractGeometry;
	}

	public void setAbstractGeometry(AbstractGeometry abstractGeometry) {
		this.abstractGeometry = abstractGeometry;
	}
	
	

	public String getAlt() {
		return alt;
	}

	public void setAlt(String alt) {
		this.alt = alt;
	}

	
	@Override
	public Object getRowData(String arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public String getRowKey(Object arg0) {
		return arg0.toString();
	}
    	
	
    
}
