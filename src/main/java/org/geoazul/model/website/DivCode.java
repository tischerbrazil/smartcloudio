package org.geoazul.model.website;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class DivCode implements Serializable {


	private static final long serialVersionUID = 407590966655062114L;
	
	
	
	public DivCode() {
	}
	
	

	public DivCode(String div_id, String div_class, List<RComponent> rComponents) {
		super();
		this.div_id = div_id;
		this.div_class = div_class;
		this.rComponents = rComponents;
	}

	


	public DivCode(String div_id, String div_class) {
		super();
		this.div_id = div_id;
		this.div_class = div_class;
	}




	private String div_id;
	private String div_class;
	
	
	private List<RComponent> rComponents;
	
	public String getDiv_id() {
		return div_id;
	}
	
	public void setDiv_id(String div_id) {
		this.div_id = div_id;
	}
	
	public String getDiv_class() {
		return div_class;
	}
	
	public void setDiv_class(String div_class) {
		this.div_class = div_class;
	}

	public List<RComponent> getrComponents() {
		return rComponents;
	}

	public void setrComponents(List<RComponent> rComponents) {
		this.rComponents = rComponents;
	}
	
	
}
