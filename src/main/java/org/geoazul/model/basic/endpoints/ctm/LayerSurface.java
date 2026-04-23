package org.geoazul.model.basic.endpoints.ctm;

import java.util.ArrayList;
import java.util.List;
import org.geoazul.model.basic.Polygon;

public class LayerSurface  {
	
    private List<Polygon> surfaces = new ArrayList<Polygon>();
    private Integer totalRecords;
   
   	public Integer getTotalRecords() {
		return totalRecords;
	}

	public void setTotalRecords(Integer totalRecords) {
		this.totalRecords = totalRecords;
	}

	public List<Polygon> getSurfaces() {
		return surfaces;
	}

	public void setSurfaces(List<Polygon> surfaces) {
		this.surfaces = surfaces;
	}
  
}
