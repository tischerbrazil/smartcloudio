package org.geoazul.model.basic.properties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import org.geoazul.model.basic.Comp;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Type;

import com.erp.modules.commonClasses.BaseEntity;

import br.bancodobrasil.model.JsonFieldControl;

@Entity
@Table(name = "APP_FIELD")
public class Field  extends BaseEntity  {

	public Field(
			Integer sequence, 
			Comp layer, 
			String name, 
			String label, 
			String placeHolder, 
			String icon,
			Boolean required, 
			String typeData, 
			FieldControl fieldControl) {
		super();
		this.sequence = sequence;
		this.layer = layer;
		this.name = name;
		this.label = label;
		this.placeHolder = placeHolder;
		this.icon = icon;
		this.required = required;
		this.typeData = typeData;
		this.fieldControl = fieldControl;
	}



	@Column(name = "sequence", nullable = true)
	private Integer sequence;

	@ManyToOne
	@JoinColumn(name = "layer_id", nullable = false)
	private Comp layer;

	@Column(name = "name", updatable = false, nullable = false, length = 36)
	private String name;

	@Column(name = "label", nullable = false, length = 36)
	private String label;

	@Column(name = "placeholder", nullable = true, length = 100)
	private String placeHolder;

	@Column(name = "icon", nullable = true, length = 20)
	private String icon;

    @ColumnDefault("false")
	@Column(name = "required")
	private Boolean required;

	@Column(name = "typedata", updatable = false, nullable = false, length = 20)
	private String typeData;

	
	@Type(JsonFieldControl.class)
	@Column(name = "fieldtype")
	private FieldControl fieldControl;

	public Field() {
		this.required = false;
	}

	public Field(Comp layer) {
		this.icon = "";
		this.layer = layer;
		this.required = false;
	}

	public Comp getLayer() {
		return layer;
	}

	public void setLayer(Comp layer) {
		this.layer = layer;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getPlaceHolder() {
		return placeHolder;
	}

	public void setPlaceHolder(String placeHolder) {
		this.placeHolder = placeHolder;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public Boolean getRequired() {
		return this.required;
	}

	public void setRequired(Boolean required) {
		this.required = required;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	public String getTypeData() {
		return typeData;
	}

	public void setTypeData(String typeData) {
		this.typeData = typeData;
	}

	public FieldControl getFieldControl() {
		return fieldControl;
	}

	public void setFieldControl(FieldControl fieldControl) {
		this.fieldControl = fieldControl;
	}

}