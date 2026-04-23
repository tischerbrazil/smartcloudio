package org.geoazul.model.basic.properties;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.geoazul.model.website.RBlock;

import com.fasterxml.jackson.databind.JsonNode;

public class CompJson {
		
	private String name;
	private String title;
	private String description;
	private String icon;
	private String image;
	private JsonNode field;
	
	
	private List<JsonNode> strings = new ArrayList<JsonNode>();
	
	
	
	@Deprecated
	private List<GeometryJson> fields = new ArrayList<GeometryJson>();
	
	
	private RBlock rBlock;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getDescription() {
		byte[] decodedBytes = Base64.getDecoder().decode(description);
		return new String(decodedBytes);
	}
	
	public void setDescription(String description) {
		this.description = Base64.getEncoder().encodeToString(description.getBytes());;
	}
	
	public String getIcon() {
		return icon;
	}
	
	public void setIcon(String icon) {
		this.icon = icon;
	}
	
	public String getImage() {
		return image;
	}
	
	public void setImage(String image) {
		this.image = image;
	}

	public List<GeometryJson> getFields() {
		return fields;
	}

	public void setFields(List<GeometryJson> fields) {
		this.fields = fields;
	}

	public List<JsonNode> getStrings() {
		return strings;
	}

	public void setStrings(List<JsonNode> strings) {
		this.strings = strings;
	}

	public JsonNode getField() {
		return field;
	}

	public void setField(JsonNode field) {
		this.field = field;
	}

	

	public RBlock getrBlock() {
		return rBlock;
	}

	public void setrBlock(RBlock rBlock) {
		this.rBlock = rBlock;
	}
	
	


}
