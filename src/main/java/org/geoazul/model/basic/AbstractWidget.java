package org.geoazul.model.basic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Map.Entry;
import java.util.Objects;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import org.geoazul.model.website.ModuleComponentMap;
import org.geoazul.model.website.media.Media;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Type;
import org.omnifaces.optimusfaces.test.model.LocalGeneratedIdEntity;

import com.erp.modules.inventory.entities.Block;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

import br.bancodobrasil.model.JsonJsonNode;
import io.hypersistence.utils.hibernate.id.Tsid;



@Entity
@Table(name = "APP_WIDGET") 

@NamedQueries({
	@NamedQuery(name = AbstractWidget.WIDGET_ALL, query = 
			"select widget from AbstractWidget widget where widget.enabled = true"),
	@NamedQuery(name = AbstractWidget.WIDGET_NAMES, query = 
			"select widget.nome from AbstractWidget widget where widget.enabled = true"),
	@NamedQuery(name = AbstractWidget.WIDGET_FIND, query = 
			"select widget from AbstractWidget widget where widget.id = :id"),
		 })
public class AbstractWidget  extends LocalGeneratedIdEntity {

	public static final String WIDGET_ALL = "Widget.all";
	public static final String WIDGET_NAMES = "Widget.Names";
	public static final String WIDGET_FIND = "Widget.find";
	
	public enum CompDef {
		point, polygon, linestring, multipolygon,polygonpoint,
		tile, rastergeoserver,
		custom, control, menu, post, register
	}
	//point, 0
	//polygon, 1
	//linestring, 2
	//multipolygon,3
	//polygonpoint,4
	//tile, 5
	//rastergeoserver,6
	//custom, 7
	//control, 8
	//menu, 9
	//post, 10
	//register11
	
	public AbstractWidget(boolean enabled, 
			String nome, String iconflag, JsonNode strings) {
		super();
		this.enabled = enabled;
		this.nome = nome;
		this.iconflag = iconflag;
		this.strings = strings;
	}

	private static final long serialVersionUID = 1L;

	@Column(name = "arquivo")
	private String arquivo;
	
	@Column(name = "enabled", columnDefinition = "boolean default true")
	@JsonIgnore
	protected boolean enabled = true;

	@Column(name = "nome")
	@JsonProperty("title")
	public String nome;

	@Column(name = "iconflag")
	private String iconflag;
	
	@Column(name = "compdef")
	private CompDef compDef;
	
	public AbstractWidget() {
		this.enabled = true;
	}

	@Type(JsonJsonNode.class)
	@Column(name = "strings")
	@ColumnDefault("'{}'") 
	private JsonNode strings;
		
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL,  mappedBy = "abstractWidget")
	@OrderBy("ordering ASC")
	private List<ModuleComponentMap> components = new ArrayList<ModuleComponentMap>();
	
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL,  mappedBy = "abstractWidget")
	@OrderBy("sequence ASC")
	private List<Block> blocs = new ArrayList<Block>();
			
	public String getArquivo() {
		return arquivo;
	}

	public void setArquivo(String arquivo) {
		this.arquivo = arquivo;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getNome() {
		return this.nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getIconflag() {
		return iconflag;
	}

	public void setIconflag(String iconflag) {
		this.iconflag = iconflag;
	}

	static final String TEXT_NODE = "TextNode";
	static final String DOUBLE_NODE = "DoubleNode";
	static final String BOOL_NODE = "BooleanNode";
	static final String DEC_NODE = "DecimalNode";
	static final String BIGINT_NODE = "BigIntegerNode";
	static final String FLOAT_NODE = "FloatNode";
	static final String INT_NODE = "IntNode";
	static final String LONG_NODE = "LongNode";
	static final String NULL_NODE = "NullNode";
	static final String SHORT_NODE = "ShortNode";
	static final String NUMERIC_NODE = "NumericNode";

	@JsonIgnore
	public Properties getPropsStrings() {
		if (this.getStrings().size() > 0) {
			Iterator<Entry<String, JsonNode>> nodes = this.getStrings()
					.fields();
			
		//	StreamSupport.stream(this.getStrings().spliterator(), false /* or whatever */);
			
			Properties prop = new Properties();
			while (nodes.hasNext()) {
				Entry<String, JsonNode> node = nodes.next();
				switch (node.getValue().getClass().getSimpleName()) {
					case TEXT_NODE :
						prop.put(node.getKey().toString(), node.getValue()
								.asText());
						break;
					case DOUBLE_NODE :
						prop.put(node.getKey().toString(), node.getValue()
								.asDouble());
						break;
					case BOOL_NODE :
						prop.put(node.getKey().toString(), node.getValue()
								.asBoolean());
						break;
					case BIGINT_NODE :
						prop.put(node.getKey().toString(), node.getValue()
								.asLong());
						break;
					case FLOAT_NODE :
						prop.put(node.getKey().toString(), node.getValue()
								.asInt());
						break;
					case INT_NODE :
						prop.put(node.getKey().toString(), node.getValue()
								.asInt());
						break;
					case LONG_NODE :
						prop.put(node.getKey().toString(), node.getValue()
								.asLong());
						break;
					case SHORT_NODE :
						prop.put(node.getKey().toString(), node.getValue()
								.asInt());
						break;
					case NUMERIC_NODE :
						prop.put(node.getKey().toString(), node.getValue()
								.asDouble());
						break;
					case NULL_NODE :
						prop.put(node.getKey().toString(), "null");
						break;
				}
			}
			return prop;
		} else {
			return null;
		}
	}

	public JsonNode getStrings() {
		return strings;
	}

	public void setStrings(JsonNode strings) {
		this.strings = strings;
	}

	

	public CompDef getCompDef() {
		return compDef;
	}

	public void setCompDef(CompDef compDef) {
		this.compDef = compDef;
	}

	public List<ModuleComponentMap> getComponents() {
		return components;
	}

	public void setComponents(List<ModuleComponentMap> components) {
		this.components = components;
	}
	

	@Override
	public String toString() {
		return nome;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AbstractWidget other = (AbstractWidget) obj;
		return Objects.equals(id, other.id);
	}
	
	
	
}