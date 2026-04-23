package org.geoazul.model.basic;

import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;

public interface Shape {
	
	public String getId();
	public void setId(String id);

	public String getNome();

	public void setNome(String nome);

	//public Short getSituacao();

	//public void setSituacao(Short situacao);

	public JsonNode getStrings();

	public void setStrings(JsonNode strings);
	
	public Layer getLayer();

	public void setLayer(Layer layer);
			
	
	public List<LayerCategory> getCategories();
	
	public void setCategories(List<LayerCategory> categories);
	
	
	

}
