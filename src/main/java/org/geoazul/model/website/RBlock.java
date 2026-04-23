package org.geoazul.model.website;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;


public class RBlock implements Serializable {

	private static final long serialVersionUID = 5390325751512384879L;
	
	public RBlock(String section_id, String section_class, boolean section_hide, String container_id,
			String container_class, boolean container_hide, String row_div_id, String row_div_class, boolean row_hide,
			List<DivCode> divs) {
		super();
		this.section_id = section_id;
		this.section_class = section_class;
		this.section_hide = section_hide;
		this.container_id = container_id;
		this.container_class = container_class;
		this.container_hide = container_hide;
		this.row_div_id = row_div_id;
		this.row_div_class = row_div_class;
		this.row_hide = row_hide;
		this.divs = divs;
	}
	
	public RBlock() {
		super();
	}

	public RBlock(String section_id, String section_class, boolean section_hide, String container_id,
			String container_class, boolean container_hide, String row_div_id, String row_div_class, boolean row_hide) {
		super();
		this.section_id = section_id;
		this.section_class = section_class;
		this.section_hide = section_hide;
		this.container_id = container_id;
		this.container_class = container_class;
		this.container_hide = container_hide;
		this.row_div_id = row_div_id;
		this.row_div_class = row_div_class;
		this.row_hide = row_hide;
	}


	private Long id;
	private String abstractWidget;
	private String section_id;
	private String section_class;
	private boolean section_hide = false;
	
	private String container_id;
	private String container_class;
	private boolean container_hide = false;
	
	private String row_div_id;
	private String row_div_class;
	private boolean row_hide = false;
	
	
	private List<DivCode> divs = new ArrayList<DivCode>();

	
	public String getSection_id() {
		return section_id;
	}


	public void setSection_id(String section_id) {
		this.section_id = section_id;
	}


	public String getSection_class() {
		return section_class;
	}


	public void setSection_class(String section_class) {
		this.section_class = section_class;
	}


	public boolean isSection_hide() {
		return section_hide;
	}


	public void setSection_hide(boolean section_hide) {
		this.section_hide = section_hide;
	}


	public String getContainer_id() {
		return container_id;
	}


	public void setContainer_id(String container_id) {
		this.container_id = container_id;
	}


	public String getContainer_class() {
		return container_class;
	}


	public void setContainer_class(String container_class) {
		this.container_class = container_class;
	}


	public boolean isContainer_hide() {
		return container_hide;
	}


	public void setContainer_hide(boolean container_hide) {
		this.container_hide = container_hide;
	}


	public String getRow_div_id() {
		return row_div_id;
	}


	public void setRow_div_id(String row_div_id) {
		this.row_div_id = row_div_id;
	}


	public String getRow_div_class() {
		return row_div_class;
	}


	public void setRow_div_class(String row_div_class) {
		this.row_div_class = row_div_class;
	}


	public boolean isRow_hide() {
		return row_hide;
	}


	public void setRow_hide(boolean row_hide) {
		this.row_hide = row_hide;
	}


	public List<DivCode> getDivs() {
		return divs;
	}


	public void setDivs(List<DivCode> divs) {
		this.divs = divs;
	}

	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getAbstractWidget() {
		return abstractWidget;
	}


	public void setAbstractWidget(String abstractWidget) {
		this.abstractWidget = abstractWidget;
	}


	
	 
    
}