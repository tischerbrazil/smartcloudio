package org.geoazul.model.app;

import java.util.ArrayList;
import java.util.List;

import org.geoazul.model.website.media.Media;

public class DataType {

	private String datatype;
	private List<ControlType> mediasNew = new ArrayList<ControlType>();
	

	public DataType(String datatype) {
		this.datatype = datatype;
	}

	public String getDatatype() {
		return datatype;
	}

	public void setDatatype(String datatype) {
		this.datatype = datatype;
	}

	public List<ControlType> getMediasNew() {
		return mediasNew;
	}

	public void setMediasNew(List<ControlType> mediasNew) {
		this.mediasNew = mediasNew;
	}
	
	
	
	
	
}
