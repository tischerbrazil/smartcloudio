package org.geoazul.ecommerce.view.shopping;

import java.io.Serializable;

public class Photo implements Serializable {

    /**
	 * 
	 */
	
	private static final long serialVersionUID = 1L;
	
	private String itemImageSrc;
    private String thumbnailImageSrc;
    private String alt;
    private String title;

    public Photo() {
    }

    public Photo(String itemImageSrc, String thumbnailImageSrc, String alt, String title) {
        this.itemImageSrc = itemImageSrc;
        this.thumbnailImageSrc = thumbnailImageSrc;
        this.alt = alt;
        this.title = title;
    }

    public String getItemImageSrc() {
        return itemImageSrc;
    }

    public void setItemImageSrc(String itemImageSrc) {
        this.itemImageSrc = itemImageSrc;
    }

    public String getThumbnailImageSrc() {
        return thumbnailImageSrc;
    }

    public void setThumbnailImageSrc(String thumbnailImageSrc) {
        this.thumbnailImageSrc = thumbnailImageSrc;
    }

    public String getAlt() {
        return alt;
    }

    public void setAlt(String alt) {
        this.alt = alt;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}