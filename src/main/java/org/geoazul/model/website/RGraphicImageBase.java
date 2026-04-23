package org.geoazul.model.website;

public class RGraphicImageBase extends RHtmlGraphicImage {

	public static final String COMPONENT_FAMILY = "org.risteos.component";
	public static final String DEFAULT_RENDERER = "/includes/widgets/ RGraphicImage.xhtml";
	    
	
	private String value;
	private String cache;
	private String name;
	private String library;
	private String stream;
	private String srcset;
	private String sizes;
	
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getCache() {
		return cache;
	}
	public void setCache(String cache) {
		this.cache = cache;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLibrary() {
		return library;
	}
	public void setLibrary(String library) {
		this.library = library;
	}
	public String getStream() {
		return stream;
	}
	public void setStream(String stream) {
		this.stream = stream;
	}
	public String getSrcset() {
		return srcset;
	}
	public void setSrcset(String srcset) {
		this.srcset = srcset;
	}
	public String getSizes() {
		return sizes;
	}
	public void setSizes(String sizes) {
		this.sizes = sizes;
	}
     
     
	

	

}
