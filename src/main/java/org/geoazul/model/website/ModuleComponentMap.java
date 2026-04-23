package org.geoazul.model.website;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import org.geoazul.model.basic.AbstractWidget;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Type;
import com.erp.modules.commonClasses.BaseEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import br.bancodobrasil.model.JsonJsonNode;

@Table(name="APP_CONTAINER_COMPONENT")
@Entity
@NamedQueries({
	@NamedQuery(name=ModuleComponentMap.DELETE, query="delete from ModuleComponentMap m where m = :moduleComponentMap"),
	@NamedQuery(name=ModuleComponentMap.FIND_BY_ID, query="select m from ModuleComponentMap m where m.id = :id"),
    @NamedQuery(name=ModuleComponentMap.DELETE_BY_MOD_AND_MID, query="delete from ModuleComponentMap m where m.module = :module and m.comp = :CompItem"),
    @NamedQuery(name=ModuleComponentMap.UPDATE_RBLOCK, query="update ModuleComponentMap map33 "
    		+ "set  blockCode = :newBlockCode where map33 = :moduleComponentMap"),
    @NamedQuery(name=ModuleComponentMap.UPDATE_ORDERING, query="update ModuleComponentMap map33 "
    		+ "set enabled = :enabled , ordering = :ordering ,  strings = :newStrings, comp = :newComp,  abstractWidget = :abstractWidget where map33 = :moduleComponentMap"),

}) 
public class ModuleComponentMap extends BaseEntity  {
	
	 public static final String DELETE = "ModuleComponentMap.DELETE";
	 public static final String DELETE_BY_MOD_AND_MID = "ModuleComponentMap.DELETE_BY";
	 public static final String FIND_BY_ID = "ModuleComponentMap.FIND_BY_ID";
	 public static final String UPDATE_ORDERING = "ModuleComponentMap.UPDATE_ORDERING";
	 public static final String UPDATE_RBLOCK = "ModuleComponentMap.UPDATE_RBLOCK";
   
    @ManyToOne(fetch= FetchType.LAZY, cascade = CascadeType.DETACH)
    @JoinColumn(name="MODULE_ID")
    private Modulo module;
    
	@ColumnDefault("true")
    @Column(name = "ENABLED")
	private Boolean enabled;
         
    @Column(name = "ORDERING", nullable = true)
    private Integer ordering;
    
    @ManyToOne(fetch = FetchType.LAZY, 
			cascade = CascadeType.DETACH)
	@JoinColumn(name = "widget_id")
	//@JsonIgnore
	private AbstractWidget abstractWidget;
    
    @Type(JsonJsonNode.class)
	@Column(name = "strings")
	@ColumnDefault("'{}'") 
	private JsonNode strings;

	@Type(JsonJsonNode.class)
	@Column(name = "comp")
	@ColumnDefault("'{}'") 
	private JsonNode comp;
	
	@Type(JsonJsonNode.class)
	@Column(name = "BLOCK_CODE")
	private JsonNode blockCode;

	public RBlock getBlock() {

		try {
			ObjectMapper map33 = new ObjectMapper();

			map33.registerSubtypes(new NamedType(RVideo.class, "RVideo"));
			map33.registerSubtypes(new NamedType(RButton.class, "RButton"));
			map33.registerSubtypes(new NamedType(RHeading.class, "RHeading"));
			map33.registerSubtypes(new NamedType(RFlexDiv.class, "RFlexDiv"));
			map33.registerSubtypes(new NamedType(RDivBreak.class, "RDivBreak"));
			map33.registerSubtypes(new NamedType(RParagraph.class, "RParagraph"));
			map33.registerSubtypes(new NamedType(RGraphicImage.class, "RGraphicImage"));

			RBlock newRBlock = null;
			;
			try {
				newRBlock = map33.readValue(blockCode.toString(), RBlock.class);
			} catch (JsonProcessingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			newRBlock.setId(this.getId());
			newRBlock.setAbstractWidget(this.abstractWidget.toString());
			return newRBlock;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
    public ModuleComponentMap() {
    	super();
    	this.enabled = true;
	}

    public ModuleComponentMap(Modulo module, JsonNode comp, 
    		Integer ordering, AbstractWidget abstractWidget,
    		JsonNode strings, JsonNode blockCode) {
		super();
		this.enabled = true;
		this.module = module;
		this.comp = comp;
		this.ordering = ordering;
		this.abstractWidget = abstractWidget;
		this.strings = strings;
		this.blockCode = blockCode;
	}
    
	public Modulo getModule() {
        return module;
    }

    public void setModule(Modulo module) {
        this.module = module;
    }

    public boolean isEnabled() {
		return this.enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
    
	public Integer getOrdering() {
		return ordering;
	}

	public void setOrdering(Integer ordering) {
		this.ordering = ordering;
	}
	
	public AbstractWidget getAbstractWidget() {
		return abstractWidget;
	}

	public void setAbstractWidget(AbstractWidget abstractWidget) {
		this.abstractWidget = abstractWidget;
	}
 
	public JsonNode getStrings() {
		return this.strings;
	}

	public void setStrings(JsonNode strings) {
		this.strings = strings;
	}

    public JsonNode getComp() {
		return comp;
	}

	public void setComp(JsonNode comp) {
		this.comp = comp;
	}

	public JsonNode getBlockCode() {
		return blockCode;
	}

	public void setBlockCode(JsonNode blockCode) {
		this.blockCode = blockCode;
	}

    
}