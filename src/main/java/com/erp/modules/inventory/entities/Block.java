package com.erp.modules.inventory.entities;

import org.geoazul.ecommerce.model.Item;
import org.geoazul.model.basic.AbstractWidget;
import org.geoazul.model.website.RBlock;
import org.geoazul.model.website.RButton;
import org.geoazul.model.website.RDivBreak;
import org.geoazul.model.website.RFlexDiv;
import org.geoazul.model.website.RGraphicImage;
import org.geoazul.model.website.RHeading;
import org.geoazul.model.website.RParagraph;
import org.geoazul.model.website.RVideo;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.Type;
import org.omnifaces.optimusfaces.test.model.GeneratedIdEntity;
import org.primefaces.model.SelectableDataModel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import br.bancodobrasil.model.JsonJsonNode;
import java.util.UUID;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

/**
 * Class of productProductMedia type.
 *
 * @author Laercio Tischer
 * @version 2.0
 * @since 2.0
 */

@Entity
@Table(name = "erp_product_block")
@NamedQueries({ @NamedQuery(name = Block.ALL, query = "select med from ProductMedia med where med.item.id = :id"), })
public class Block extends GeneratedIdEntity implements SelectableDataModel {

	public static final String ALL = "Block.all";

	public Block() {
		super();
	}

	public Block(String mimeType, String title, String alt, String filename) {
		super();
	}

	@Column(name = "SEQUENCE")
	private Integer sequence;

	@Type(JsonJsonNode.class)
	@Column(name = "BLOCK_CODE")
	@ColumnDefault("'{}'") 
	private JsonNode blockCode;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
	@JoinColumn(name = "widget_id")
	@JsonIgnore
	private AbstractWidget abstractWidget;

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
	
	
	
	
	
	
	

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
	@JoinColumn(name = "ITEM_ID")
	@JsonIgnore
	private Item item;



//	@Transient
//	private List<RComponent> rComp_col_0 = new ArrayList<RComponent>();

//	@Transient
//	private List<RComponent> rComp_col_1 = new ArrayList<RComponent>();

//	@Transient
//	private List<RComponent> rComp_col_2 = new ArrayList<RComponent>();

//	public List<RComponent> getrComp_col_0() {
//		return rComp_col_0;
//	}

//	public void setrComp_col_0(List<RComponent> rComp_col_0) {
//		this.rComp_col_0 = rComp_col_0;
//	}

//	public List<RComponent> getrComp_col_1() {
//		return rComp_col_1;
//	}

//	public void setrComp_col_1(List<RComponent> rComp_col_1) {
//		this.rComp_col_1 = rComp_col_1;
//	}

//	public List<RComponent> getrComp_col_2() {
//		return rComp_col_2;
//	}

//	public void setrComp_col_2(List<RComponent> rComp_col_2) {
//		this.rComp_col_2 = rComp_col_2;
//	}

	@Override
	public String getRowKey(Object arg0) {
		return arg0.toString();
	}

	@Override
	public Object getRowData(String arg0) {
		return arg0;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	public JsonNode getBlockCode() {
		return blockCode;
	}

	public void setBlockCode(JsonNode blockCode) {
		this.blockCode = blockCode;
	}

	public AbstractWidget getAbstractWidget() {
		return abstractWidget;
	}

	public void setAbstractWidget(AbstractWidget abstractWidget) {
		this.abstractWidget = abstractWidget;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

}