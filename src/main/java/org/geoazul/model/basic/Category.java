package org.geoazul.model.basic;

public interface Category {
	
	public String getId();
	public void setId(String id);

	public String getCategoryname();
	public void setCategoryname(String categoryname);
	
	public Layer getLayer();
	public void setLayer(Layer layer);
				
}
