package org.geoazul.model.basic.properties;

import com.fasterxml.jackson.databind.JsonNode;

public class GeometryJson {

		private String name;
		private String icon;
		private Integer sequence;
		private JsonNode strings;
		
		public String getName() {
			return name;
		}
		
		public void setName(String name) {
			this.name = name;
		}
		
		public String getIcon() {
			return icon;
		}
		
		public void setIcon(String icon) {
			this.icon = icon;
		}
		
		public Integer getSequence() {
			return sequence;
		}
		
		public void setSequence(Integer sequence) {
			this.sequence = sequence;
		}
		
		public JsonNode getStrings() {
			return strings;
		}
		
		public void setStrings(JsonNode strings) {
			this.strings = strings;
		}

			
}
