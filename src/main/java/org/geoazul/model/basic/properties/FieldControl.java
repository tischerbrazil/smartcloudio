package org.geoazul.model.basic.properties;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import jakarta.faces.model.SelectItem;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * @author Laercio Tischer
 */
@JsonPropertyOrder({ "type", "selectItems", "keys"})
public class FieldControl implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public FieldControl() {
	}

	@JsonProperty(index=1)
	private String type; 
	
	@JsonProperty(index=2)
	private List<SelectItem> selectItems = new ArrayList<SelectItem>();
	
	@JsonProperty(index=3)
	private Map<String, Object> keys;
	
	public FieldControl(String type, 			
			List<SelectItem> selectItems,Map<String, Object> keys ) {
		this.type = type;
		this.setSelectItems(selectItems);
		this.keys = keys;
	}
	
	public FieldControl(String type, 			
			List<SelectItem> selectItems ) {
		this.type = type;
		this.setSelectItems(selectItems);
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<SelectItem> getSelectItems() {
		return selectItems;
	}

	public void setSelectItems(List<SelectItem> selectItems) {
		this.selectItems = selectItems;
	}

	public Map<String, Object> getKeys() {
		return keys;
	}

	public void setKeys(Map<String, Object> keys) {
		this.keys = keys;
	}

}
