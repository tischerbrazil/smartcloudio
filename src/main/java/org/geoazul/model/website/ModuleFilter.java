package org.geoazul.model.website;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.geoazul.model.basic.properties.CompJson;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ModuleFilter implements java.io.Serializable  { 

	private static final long serialVersionUID = 1L;
	
	ObjectMapper objectMapper = new ObjectMapper();
		
	private String id;
	
	private List<CompJson> strings = new ArrayList<CompJson>();
		
	private Long moduloId;
	private String folder;
	private String path;
	
	private String htmltag;
	private JsonNode fields;
	
	
	private Long urlMenuItem;
	private JsonNode components;
	private Integer ordering;
	
	private Long type;
	private Integer ordering2;
	
	private String complete;
	
	private String widget_id;
	
	private RBlock rBlock;
	
	
	
	
	 public ModuleFilter(String id,  
			 Long moduloId, String folder,  String path, 
			 String htmltag,  JsonNode fields, 	
			 Long urlMenuItem, JsonNode components, Integer ordering, Long type,
			 Integer ordering2, String complete, String  widget_id , RBlock rBlock) {
		 
			this.id = id; //  Ordering ++
			
			this.moduloId = moduloId;  // 0
			this.folder = folder;  // 1
			this.path = path;  // 2
			
		 	this.htmltag = htmltag;  // 3
		 	this.fields = fields;  // 4

	    	
	    	this.urlMenuItem = urlMenuItem;  // 6
	    	this.components = components; // 7
	    	this.ordering = ordering;  // 8
	    	
	    	this.type = type; // 9
	    	this.ordering2 = ordering2;  // 10
	    	
	    	this.complete = complete;
	    	
	    	this.widget_id = widget_id;
	    	
	    	this.rBlock = rBlock;
	     }
	 	 
	 	public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}
		
		
		public String getFolder() {
			return folder;
		}

		public void setFolder(String folder) {
			this.folder = folder;
		}
	
		
		public String getPath() {
			return path;
		}

		public void setPath(String path) {
			this.path = path;
		}
		
		public String getHtmltag() {
			return htmltag;
		}

		public void setHtmltag(String htmltag) {
			this.htmltag = htmltag;
		}
		
		public JsonNode getFields() {
			return fields;
		}

		public void setFields(JsonNode fields) {
			this.fields = fields;
		}
		
		public Long getUrlMenuItem() {
			return urlMenuItem;
		}

		public void setUrlMenuItem(Long urlMenuItem) {
			this.urlMenuItem = urlMenuItem;
		}

		public JsonNode getComponents() {
			return components; 
		}

		public void setComponents(JsonNode components) {
			this.components = components;
		}
		
		public Integer getOrdering() {
			return ordering;
		}

		public void setOrdering(Integer ordering) {
			this.ordering = ordering;
		}
		
		public Long getType() {
			return type;
		}

		public void setType(Long type) {
			this.type = type;
		}
		
		public Long getModuloId() {
			return moduloId;
		}

		public void setModuloId(Long moduloId) {
			this.moduloId = moduloId;
		}
		
		public Integer getOrdering2() {
			return ordering2;
		}

		public void setOrdering2(Integer ordering2) {
			this.ordering2 = ordering2;
		}
		 

			public String getComplete() {
				return complete;
			}

			public void setComplete(String complete) {
				this.complete = complete;
			}
			
			public String getWidget_id() {
				return widget_id;
			}

			public void setWidget_id(String widget_id) {
				this.widget_id = widget_id;
			}

					
		
	
			@Override
			public String toString() {
				  StringBuilder builder = new StringBuilder();
				  String reg = this.getComponents().at("/type").asText();           
		          builder.append("/includes/");
		          builder.append(reg);
		          builder.append(".xhtml");
		          return builder.toString();

			}

			@Override
			public int hashCode() {
				final int prime = 31;
				int result = 1;
				result = prime * result + ((id == null) ? 0 : id.hashCode());
				return result;
			}





			@Override
			public boolean equals(Object obj) {
				if (this == obj)
					return true;
				if (obj == null)
					return false;
				if (getClass() != obj.getClass())
					return false;
				ModuleFilter other = (ModuleFilter) obj;
				if (id == null) {
					if (other.id != null)
						return false;
				} else if (!id.equals(other.id))
					return false;
				return true;
			}
			
			

			public CompJson getCompJson() {
				try {
					String jsonArray = this.fields.toString();
					return objectMapper.readValue(jsonArray, 
									new TypeReference<CompJson>(){});
				} catch (JsonMappingException e) {
					e.printStackTrace();
				} catch (JsonProcessingException e) {
					e.printStackTrace();
				}
				
				return null;
			}

			public RBlock getrBlock() {
				return rBlock;
			}

			public void setrBlock(RBlock rBlock) {
				this.rBlock = rBlock;
			}

}