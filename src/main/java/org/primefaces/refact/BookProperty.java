/*
 * Copyright 2011-2015 PrimeFaces Extensions
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * $Id$
 */

package org.primefaces.refact;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import jakarta.faces.model.SelectItem;
import jakarta.persistence.Column;

import org.hibernate.annotations.Type;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * BookProperty
 *
 * @author  Oleg Varaksin / last modified by $Author$
 * @version $Revision$
 */
public class BookProperty implements Serializable {

	private static final long serialVersionUID = 1L;

	private String name;
	private String label;
	private String placeholder;
	private String icon;
	private Object value;
	private boolean required = false;
	private List<SelectItem> selectItems;
	private JsonNode strings; 
	private Map<String, Object> keys;
	
	public BookProperty(String name, boolean required, JsonNode strings) {
		this.name = name;
		this.required = required;
		this.strings = strings;
	}

	public BookProperty(String name, Object value, boolean required,
			JsonNode strings, Map<String, Object> keys) {
		this.name = name;
		this.value = value;
		this.required = required;
		this.strings = strings;
		this.keys = keys;
	}
	
	public BookProperty(String name, String label, Object value, 
			boolean required, JsonNode strings, Map<String, Object> keys) {
		this.name = name;
		this.label = label;
		this.value = value;
		this.required = required;
		this.strings = strings;
		this.keys = keys;
	}
	
	public BookProperty(String name, String label, String placeholder, 
			Object value, boolean required, JsonNode strings, Map<String, Object> keys) {
		this.name = name;
		this.label = label;
		this.placeholder = placeholder;
		this.value = value;
		this.required = required;
		this.strings = strings;
		this.keys = keys;
	}
	
	public BookProperty(String name, String label, String placeholder, 
			String icon, Object value, boolean required, JsonNode strings,
			Map<String, Object> keys) {
		this.name = name;
		this.label = label;
		this.placeholder = placeholder;
		this.setIcon(icon);
		this.value = value;
		this.required = required;
		this.strings = strings;
		this.keys = keys;
	}
	
	public BookProperty(String name, String label, Object value, boolean required, 
			List<SelectItem> selectItems, JsonNode strings, Map<String, Object> keys) {
		this.name = name;
		this.label = label;
		this.value = value;
		this.required = required;
		this.selectItems = selectItems;
		this.strings = strings;
		this.keys = keys;
	}
	
	
	public BookProperty(String name, boolean required) {
		this.name = name;
		this.required = required;
	}

	public BookProperty(String name, Object value, boolean required) {
		this.name = name;
		this.value = value;
		this.required = required;
	}
	
	public BookProperty(String name, String label, Object value, boolean required) {
		this.name = name;
		this.label = label;
		this.value = value;
		this.required = required;
	}
	
	public BookProperty(String name, String label, String placeholder, Object value, boolean required) {
		this.name = name;
		this.label = label;
		this.placeholder = placeholder;
		this.value = value;
		this.required = required;
	}
	
	public BookProperty(String name, String label, String placeholder, 
			String icon, Object value, boolean required) {
		this.name = name;
		this.label = label;
		this.placeholder = placeholder;
		this.setIcon(icon);
		this.value = value;
		this.required = required;
	}
	
	public BookProperty(String name, String label, Object value, boolean required, 
			List<SelectItem> selectItems) {
		this.name = name;
		this.label = label;
		this.value = value;
		this.required = required;
		this.selectItems = selectItems;
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
	
	public String getPlaceholder() {
		return placeholder;
	}

	public void setPlaceholder(String placeholder) {
		this.placeholder = placeholder;
	}
	
	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public Object getValue() {
		return value;
	}

	public Object getFormattedValue() {
		if (value instanceof LocalDate) {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("d MMM yyyy");

			return simpleDateFormat.format(value);
		}

		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}
	
	public List<SelectItem> getSelectItems() {
		return selectItems;
	}

	public void setSelectItems(List<SelectItem> selectItems) {
		this.selectItems = selectItems;
	}

	public JsonNode getStrings() {
		return strings;
	}

	public void setStrings(JsonNode strings) {
		this.strings = strings;
	}

	public Map<String, Object> getKeys() {
		return keys;
	}

	public void setKeys(Map<String, Object> keys) {
		this.keys = keys;
	}

}
