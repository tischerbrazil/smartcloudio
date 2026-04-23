

package org.geoazul.view;

import java.io.Serializable;
import jakarta.xml.bind.annotation.*;

@XmlRootElement(name = "NamedLayer", namespace="namespace")
@XmlAccessorType(XmlAccessType.FIELD)
public class LayerStyleLSD implements Serializable {

	private static final long serialVersionUID = -1422690015831329825L;
	
	 public LayerStyleLSD() {}
	    public LayerStyleLSD(String name, int id) { this.name = name; this.id = id; }


	@XmlElement(name = "name")
	private String name;

	@XmlElement(name = "id")
	private int id;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	

}