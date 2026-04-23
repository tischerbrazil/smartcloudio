package org.geoazul.model.app;

import jakarta.persistence.NamedNativeQuery;
import jakarta.persistence.NamedNativeQueries;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import org.geoazul.model.basic.Comp;
import org.geoazul.model.security.ClientEntity;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Type;
import org.omnifaces.optimusfaces.test.model.LocalGeneratedIdEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import br.bancodobrasil.model.JsonJsonNode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Properties;
import java.util.UUID;
import java.util.Map.Entry;

/**
 *
 * @author Laercio Tischer
 */

@Entity
@Table(name = "APP_IDENTITY")
@DiscriminatorColumn(name = "dtype")
@NamedQueries({
		@NamedQuery(name = AbstractIdentityEntity.DEFAULT_APP, query = "SELECT a FROM " + "AbstractIdentityEntity a "
				+ "WHERE a.locale = :locale and a.clientEntity.id = :dtype order by a.defaultApp"),
		@NamedQuery(name = AbstractIdentityEntity.DEFAULT_APP_UNION, query = 
		
		"SELECT a FROM AbstractIdentityEntity a  "
		+ " WHERE a.locale= :locale AND a.clientEntity.clientId = :dtype AND a.enabled = true AND a.defaultApp = true"
		
		),
		@NamedQuery(name = AbstractIdentityEntity.CHANGE_LIKE, query = "update AbstractIdentityEntity set likes= :likes where id = :appid") })

@NamedNativeQueries({ @NamedNativeQuery(name = AbstractIdentityEntity.DEFAULT_APP_NAT, query = "SELECT * FROM "
		+ "app_identity  "
		+ "WHERE locale = :locale and client_id = :dtype order where defaultApp = true", resultClass = AbstractIdentityEntity.class),

})
public abstract class AbstractIdentityEntity extends LocalGeneratedIdEntity {

	public static final String DEFAULT_APP = "AbstractIdentityEntity.defaultAPP";
	public static final String DEFAULT_APP_UNION = "AbstractIdentityEntity.defaultAPP_UNION";
	public static final String DEFAULT_APP_NAT = "AbstractIdentityEntity.defaultAPP_NAT";
	public static final String CHANGE_LIKE = "AbstractIdentityEntity.add_Like";

	public AbstractIdentityEntity() {
		super();
	}

	// IDENTITY ENTITY
	public AbstractIdentityEntity(ClientEntity clientEntity, String name, String title, String description,
			boolean enabled, String image, String template, String locale, Boolean defaultApp, Integer epsg,
			JsonNode strings) {
		this.clientEntity = clientEntity;
		this.name = name;
		this.title = title;
		this.description = description;
		this.createdDate = LocalDateTime.now();
		this.enabled = enabled;
		this.image = image;
		this.template = template;
		this.locale = locale;
		this.defaultApp = defaultApp;
		this.epsg = epsg;
		this.strings = strings;
	}


	@OneToMany(fetch = FetchType.LAZY, orphanRemoval = true, cascade = { CascadeType.ALL }, mappedBy = "applicationEntity")
	@JsonIgnore
	private List<Comp> components = new ArrayList<Comp>();

	@Column(name = "dtype", insertable = false, updatable = false)
	private String dtype;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
	@JoinColumn(name = "client_id")
	@JsonIgnore

	private ClientEntity clientEntity;

	@Column(name = "NAME")
	private String name;

	@Column(name = "TITLE")
	private String title;

	@Column(name = "DESCRIPTION")
	private String description;

	@Column(name = "CREATEDDATE")
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
	private LocalDateTime createdDate;

	@ColumnDefault("true")
	@Column(name = "ENABLED")
	private Boolean enabled;

	@Column(name = "IMAGE")
	private String image;

	@Column(name = "TEMPLATE")
	private String template;

	@Column(name = "LOCALE")
	private String locale;

	@ColumnDefault("true")
	@Column(name = "DEFAULT_APP")
	private Boolean defaultApp;

	@OneToMany(fetch = FetchType.LAZY, cascade = { CascadeType.ALL }, orphanRemoval = true, mappedBy = "application")
	@JsonIgnore
	private Collection<ApplicationAttributeEntity> attributes = new ArrayList<ApplicationAttributeEntity>();

	@JsonIgnore
	@Column(name = "EPSG")
	protected Integer epsg;

	@Type(JsonJsonNode.class)
	@Column(name = "strings")
	@ColumnDefault("'{}'") 
	private JsonNode strings;

	@OneToMany(fetch = FetchType.LAZY, orphanRemoval = true,  mappedBy = "app")
	private List<AppUserMappingEntity> shareds = new ArrayList<AppUserMappingEntity>();

	@Column(name = "likes", columnDefinition = "integer default 0")
	private Integer likes;

	@Column(name = "shared", columnDefinition = "integer default 0")
	private Integer shared;
	
	
	
	
	
	
	

	public String getAttib(String name) {
		for (ApplicationAttributeEntity attr : this.getAttributes()) {
			if (attr.getName().equals(name)) {
				return attr.getValue();
			}
		}
		return null;
	}

	public void add(Comp component) {
		this.getComponents().add(component);
		component.setApplicationEntity(this);
	}

	public Comp getLayerId(Long layerID) {
		for (Comp attr : this.getComponents()) {
			if (attr.getId().equals(layerID)) {
				return attr;
			}
		}
		return null;
	}

	@JsonIgnore

	public List<Comp> getLayersNotTile() {
		List<Comp> layersNotTile = new ArrayList<Comp>();
		for (Comp layersLoad : this.getComponents()) {
			if (!layersLoad.getDtype().equals("Tile")) {
				layersNotTile.add(layersLoad);
			}
		}
		return layersNotTile;
	}

	@JsonIgnore

	public List<Comp> getLayersTile() {
		List<Comp> layersNotTile = new ArrayList<Comp>();
		for (Comp layersLoad : this.getComponents()) {
			if (layersLoad.getDtype().equals("rastergeoserver") || layersLoad.getDtype().equals("Tile")) {
				layersNotTile.add(layersLoad);
			}
		}
		return layersNotTile;
	}

	@JsonIgnore

	public List<Comp> getLayersEnabledNottile() {
		List<Comp> layersNotTile2 = new ArrayList<Comp>();
		for (Comp layersLoad3 : this.getComponents()) {
			if (!layersLoad3.getDtype().equals("Tile") && layersLoad3.getSelected()) {
				layersNotTile2.add(layersLoad3);
			}
		}
		return layersNotTile2;
	}

	public List<Comp> getComponents() {
		return components;
	}

	public void setComponents(List<Comp> components) {
		this.components = components;
	}

	public String getDtype() {
		return dtype;
	}

	public void setDtype(String dtype) {
		this.dtype = dtype;
	}

	public ClientEntity getClientEntity() {
		return clientEntity;
	}

	public void setClientEntity(ClientEntity clientEntity) {
		this.clientEntity = clientEntity;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LocalDateTime getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}
	
	public Boolean getDefaultApp() {
		return defaultApp;
	}

	public void setDefaultApp(Boolean defaultApp) {
		this.defaultApp = defaultApp;
	}

	public Collection<ApplicationAttributeEntity> getAttributes() {
		return attributes;
	}

	public void setAttributes(Collection<ApplicationAttributeEntity> attributes) {
		this.attributes = attributes;
	}

	public Integer getEpsg() {
		return this.epsg;
	}

	public void setEpsg(Integer epsg) {
		this.epsg = epsg;
	}

	public JsonNode getStrings() {
		return strings;
	}

	public void setStrings(JsonNode strings) {
		this.strings = strings;
	}

	public List<AppUserMappingEntity> getShareds() {
		return shareds;
	}

	public void setShareds(List<AppUserMappingEntity> shareds) {
		this.shareds = shareds;
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

			Iterator<Entry<String, JsonNode>> nodes = this.getStrings().fields();
			Properties prop = new Properties();
			while (nodes.hasNext()) {
				Entry<String, JsonNode> node = nodes.next();
				switch (node.getValue().getClass().getSimpleName()) {
				case TEXT_NODE:
					prop.put(node.getKey().toString(), node.getValue().asText());
					break;
				case DOUBLE_NODE:
					prop.put(node.getKey().toString(), node.getValue().asDouble());
					break;
				case BOOL_NODE:
					prop.put(node.getKey().toString(), node.getValue().asBoolean());
					break;
				case BIGINT_NODE:
					prop.put(node.getKey().toString(), node.getValue().asLong());
					break;
				case FLOAT_NODE:
					prop.put(node.getKey().toString(), node.getValue().asInt());
					break;
				case INT_NODE:
					prop.put(node.getKey().toString(), node.getValue().asInt());
					break;
				case LONG_NODE:
					prop.put(node.getKey().toString(), node.getValue().asLong());
					break;
				case SHORT_NODE:
					prop.put(node.getKey().toString(), node.getValue().asInt());
					break;
				case NUMERIC_NODE:
					prop.put(node.getKey().toString(), node.getValue().asDouble());
					break;
				case NULL_NODE:
					prop.put(node.getKey().toString(), "null");
					break;
				}
			}
			return prop;
		} else {
			return null;
		}
	}

	public Integer getLikes() {
		return likes;
	}

	public void setLikes(Integer likes) {
		this.likes = likes;
	}

	public Integer getShared() {
		return shared;
	}

	public void setShared(Integer shared) {
		this.shared = shared;
	}

}