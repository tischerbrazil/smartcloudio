package org.geoazul.model.basic;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Map.Entry;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import org.geoazul.model.website.media.Media;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Type;
import org.omnifaces.optimusfaces.test.model.LocalGeneratedIdEntity;
import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import br.bancodobrasil.model.JsonJsonNode;

@Entity
@Table(name = "APP_GEOMETRY")
@Inheritance(strategy = InheritanceType.JOINED
)
@JsonFilter("abstractGeometryFilter")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@NamedQueries({ 
	
	@NamedQuery(name = AbstractGeometry.SURFACE_ID, query = "SELECT  a "
			+ "FROM AbstractGeometry a "
			+ "WHERE a.id = :id"), 
	
	@NamedQuery(name = "getFhaterId", query = "SELECT  a "
			+ "FROM AbstractGeometry a "
			+ "WHERE a.father.id = :fatherId"),
	 
	@NamedQuery(name = "getOriginId", query = "SELECT  a "
			+ "FROM AbstractGeometry a "
			+ "WHERE a.origin.id = :originId"),

	@NamedQuery(name = AbstractGeometry.SURFACE_ITEMS, query = "SELECT  polygon "
			+ "FROM Polygon polygon "
			+ "WHERE polygon.father is not null AND "
			+ "polygon.layer = :layer"),

	@NamedQuery(name = AbstractGeometry.SURFACE_ITEMS_FILTER, query = "SELECT  polygon FROM Polygon polygon "
			+ "WHERE polygon.father = :father AND " 
			+ "polygon.layer = :layer"),

	@NamedQuery(name = AbstractGeometry.SURFACE_ITEMS_FILTER_SEC, query = 
			"SELECT  polygon FROM Polygon polygon "
			+ "JOIN AbstractGeometry father  " 
			+ "ON father = polygon.father " 
			+ "WHERE polygon.layer = :layer AND " 
			+ "polygon.father is not null AND "
			+ "father.father = :fatherSecond"),
	
	    @NamedQuery(name = "getByLayer", query = "select a from AbstractGeometry "
	    		+ "a where a.layer.id = :id and a.enabled = true order by a.id desc"),
	}) 

	public class AbstractGeometry  extends LocalGeneratedIdEntity
	
	{

		public static final String MESSAGES_GET_BY_LAYER = "getByLayer";
		
		public static final String SURFACE_ID = "getSurfacesID";
		
		public static final String ORIGIN_ID = "getOriginId";
		
		public static final String FATHER_ID = "getFhaterId";
		
		public static final String SURFACE_ITEMS = "getSurfacesItems";
		public static final String SURFACE_ITEMS_FILTER = "getSurfacesItemsFilter";
		public static final String SURFACE_ITEMS_FILTER_SEC = "getSurfacesItemsFilterSec";

		
	/**
	 * 
	 */

	private static final long serialVersionUID = 1L;




	@Column(name = "enabled", columnDefinition = "boolean default true")
	protected Boolean enabled = true;
	
	


	@Column(name = "nome")
	@JsonProperty("title")
	public String nome;

	@Column(name = "iconflag")
	private String iconflag;

	@Column(name = "situacao")
	@JsonIgnore
	protected Short situacao;

	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
	@JoinTable(name = "APP_CATEGORY_GEOMETRY", 
	joinColumns = @JoinColumn(name = "geometry_id"), 
	inverseJoinColumns = @JoinColumn(name = "category_id"))
	@JsonIgnore
	private List<LayerCategory> categories = new ArrayList<>();

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
	@JoinColumn(name = "imovel_id", updatable = true, nullable = true)
	private AbstractGeometry father;
	

	@OneToMany(mappedBy = "father", fetch = FetchType.LAZY, cascade = CascadeType.DETACH, orphanRemoval = true)
	@OrderBy("parte ASC")
	@JsonIgnore
	private List<AbstractGeometry> childrens = new ArrayList<AbstractGeometry>();
	
	
	
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
	@JoinColumn(name = "origin_id", updatable = true, nullable = true)
	private AbstractGeometry origin;
	

	@OneToMany(mappedBy = "origin", fetch = FetchType.LAZY, cascade = CascadeType.DETACH, orphanRemoval = true)
	@OrderBy("parte ASC")
	@JsonIgnore
	private List<AbstractGeometry> originChildrens = new ArrayList<AbstractGeometry>();


	@Column(name = "parte")
	@JsonIgnore
	private Integer parte;


	
	//@JsonIgnore
	@OneToMany(mappedBy = "abstractGeometry", 
	fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@OrderBy("id")
	private List<Media> medias = new ArrayList<>();
	
	
	
	
	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public void removeMedia(Media media) {
		List<Media> mediasNew = new ArrayList<Media>();
		this.medias.retainAll(this.getMedias());
		for (Media attr : this.getMedias()) {
			if (!attr.getId().equals(media.getId())) {
				mediasNew.add(attr);
			}
		}
		this.medias = mediasNew;
	}

	public AbstractGeometry() {
		this.situacao = (short) 1;
		this.enabled = true;
	}


	@Type(JsonJsonNode.class)
	@Column(name = "field")
	@ColumnDefault("'{}'") 
	private JsonNode strings;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
	@JoinColumn(name = "layer_id")
	private Comp layer;

	

	
	
	
	
	

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

	public Comp getLayer() {
		return layer;
	}

	public void setLayer(Comp layer) {
		this.layer = layer;
	}

	public Short getSituacao() {
		return this.situacao;
	}

	public void setSituacao(Short situacao) {
		this.situacao = situacao;
	}

	public List<LayerCategory> getCategories() {
		return categories;
	}

	public void setCategories(List<LayerCategory> categories) {
		this.categories = categories;
	}

	public AbstractGeometry getFather() {
		return father;
	}

	public void setFather(AbstractGeometry father) {
		this.father = father;
	}

	public List<AbstractGeometry> getChildrens() {
		return childrens;
	}

	public void setChildrens(List<AbstractGeometry> childrens) {
		this.childrens = childrens;
	}
	
	
	
	
	
	

	public AbstractGeometry getOrigin() {
		return origin;
	}

	public void setOrigin(AbstractGeometry origin) {
		this.origin = origin;
	}

	public List<AbstractGeometry> getOriginChildrens () {
		return originChildrens;
	}

	public void setOriginChildrens(List<AbstractGeometry> originChildrens) {
		this.originChildrens = originChildrens;
	}

	public Integer getParte() {
		return this.parte;
	}

	public void setParte(Integer parte) {
		this.parte = parte;
	}

	/**
	 * Return all authors who written the book.
	 *
	 * @return All authors who written the book.
	 *
	 * @version 1.0
	 * @since 1.0
	 */
	public List<Media> getMedias() {
		return medias;
	}

	/**
	 * Set all authors who write the book.
	 *
	 * @param authors
	 *            New st of Authors.
	 *
	 * @version 1.0
	 * @since 1.0
	 */
	public void setMedias(List<Media> medias) {
		this.medias = medias;
	}

	




}
