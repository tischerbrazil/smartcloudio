package org.geoazul.view.website;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.geoazul.model.website.RBlock;
import org.geoazul.model.website.RButton;
import org.geoazul.model.website.RComponent;
import org.geoazul.model.website.RDivBreak;
import org.geoazul.model.website.RFlexDiv;
import org.geoazul.model.website.RGraphicImage;
import org.geoazul.model.website.RHeading;
import org.geoazul.model.website.RParagraph;
import org.geoazul.model.website.RVideo;
import org.geoazul.model.website.RHeading.TypeHeading;
import org.geoazul.model.website.ModuleComponentMap;
import org.apache.commons.compress.compressors.lz77support.LZ77Compressor.Block;
import org.geoazul.model.website.DivCode;
import org.omnifaces.cdi.ViewScoped;
import org.primefaces.component.button.Button;
import org.primefaces.component.video.Video;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;

/**
 * I am need a ViewScoped here to manage the media for exclude
 */

@Named
@ViewScoped
public class BlockBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@PreDestroy
	public void preDestroy() {
	}

	@Inject
	private EntityManager entityManager;

	private  RBlock block;
	
	
	List<DivCode> divs;
	

	public RBlock getBlock(Long id) {
		try {
			TypedQuery<ModuleComponentMap> queryModule = entityManager.createNamedQuery(ModuleComponentMap.FIND_BY_ID,
					ModuleComponentMap.class);
			queryModule.setParameter("id", id);
			
			
			ObjectMapper map33 = new ObjectMapper();

			 map33.registerSubtypes(new NamedType(RVideo.class, "RVideo"));
			 map33.registerSubtypes(new NamedType(RButton.class, "RButton"));
			 map33.registerSubtypes(new NamedType(RHeading.class, "RHeading"));
			 map33.registerSubtypes(new NamedType(RFlexDiv.class, "RFlexDiv"));
			 map33.registerSubtypes(new NamedType(RDivBreak.class, "RDivBreak"));
			 map33.registerSubtypes(new NamedType(RParagraph.class, "RParagraph"));
			 map33.registerSubtypes(new NamedType(RGraphicImage.class, "RGraphicImage"));
			 
			 JsonNode json = queryModule.getSingleResult().getBlockCode();
			RBlock newRBlock = null;;
			try {
				newRBlock = map33.readValue(json.toString(), RBlock.class);
			} catch (JsonProcessingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}	
			
			
			return  newRBlock;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public RBlock findModuleCompById(int blockId) {
		try {
			
			return entityManager.find(RBlock.class, blockId);
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public RBlock getBlock(int blockId) {
		return 	findModuleCompById(blockId);
	}
	
	
	@PostConstruct
	public void init() {
	
		FacesContext context = FacesContext.getCurrentInstance();
		if (context.isPostback()) {
			return;
		}
		
		block = new RBlock();
		
		block.setSection_id("section_id");
		block.setSection_class("pt-15 pb-10 py-5");
				
		block.setContainer_id("container_id");
		block.setContainer_class("container px-lg-7 px-xxl-3");
		
		block.setRow_div_id("row_id");
		block.setRow_div_class("row mt-2 align-items-center justify-content-between text-center text-lg-start mb-6 mb-lg-0");
		
		divs = new ArrayList<>();
		
		//-----------------------------START COLUMNS 1 ------------------------------------------------------
		
		        
		        DivCode divCode1 = new DivCode();
		        List<RComponent> rComponents = new ArrayList<RComponent>();
		        
		      
		        
				divCode1.setDiv_id("div_code_id_1");
				divCode1.setDiv_class("col-lg-6 order-0 order-lg-1 card-banner");
				
			
				//  RVideo rVideo = new RVideo();
				//rVideo.setStyleClass("feature-image img-fluid mb-9 mb-lg-0");
				//rVideo.setPoster("themes/bs5-ecommerce/img/trat.jpg");
				//rVideo.setValue("/themes/bs5-ecommerce/img/tratoron.mp4");
				//rVideo.setWidth("906");
				//rVideo.setHeight("540");
				//rVideo.setCache(false);
				//rVideo.setId("1");
				//rVideo.setOrder(1);
				//rVideo.setPlayer(null);
				//rVideo.setParent(null);
				//rVideo.setPreload(null);
				//rVideo.setStyle(null);
				
		
				
				//	rComponents.add(rVideo);
										
					
				RGraphicImage rGraphicImage = new RGraphicImage();
				rGraphicImage.setStyleClass("feature-image img-fluid mb-9 mb-lg-0");
				
				rGraphicImage.setValue("themes/bs5-ecommerce/img/trat.jpg");
				rGraphicImage.setWidth("906");
				rGraphicImage.setHeight("540");
				
				rGraphicImage.setId("1");
				rGraphicImage.setOrder(0);
				
				rGraphicImage.setParent(null);
		
				rGraphicImage.setStyle(null);
				
				rComponents.add(rGraphicImage);
				
				
					
				divCode1.setrComponents(rComponents);
				divs.add(divCode1);  // vou adiciona o div coluna
				
		//---------------------------END COLUMN 2 --------------------------------------------------------
		
				
				
		//---------------------------START COLUMN 1 --------------------------------------------------------

				DivCode divCode2 = new DivCode();
				List<RComponent> rComponents2 = new ArrayList<RComponent>();
				
		
		
		divCode2.setDiv_id("div_code_id_2");
		divCode2.setDiv_class("col-lg-6");
		
		
		
		
		
		
		
		//------------------------- START H3---------------------------------------------------------	
		RHeading rHeading1 = new RHeading();	
		rHeading1.setOrder(1);
		rHeading1.setId("3");
		rHeading1.setTypeHeading(TypeHeading.h3);
		rHeading1.setStyleClass("text-primary mb-2 ls-2");
		rHeading1.setValue("<strong>Tratoron New-Holland</strong>");
		rComponents2.add(rHeading1);
		//------------------------- END H3 ----------------------------------------------------------
		
		
	
		//------------------------- START p---------------------------------------------------------	
		RHeading rHeading2 = new RHeading();	
		rHeading2.setTypeHeading(TypeHeading.h4);
		rHeading2.setStyleClass("fw-bolder mb-3");
		rHeading2.setId("4");
		rHeading2.setValue("Nova Loja em Rio Branco/AC");
		rComponents2.add(rHeading2);
		//------------------------- END p ----------------------------------------------------------
		
		
		//------------------------- START p---------------------------------------------------------	
				RHeading rHeading3 = new RHeading();	
				rHeading3.setTypeHeading(TypeHeading.p);
				rHeading3.setId("5");
				rHeading3.setStyleClass("mb-4 px-md-7 px-lg-0");
				rHeading3.setValue("Test Head Description.");
				rComponents2.add(rHeading3);
				//------------------------- END p ----------------------------------------------------------
				
				//------------------------- START p---------------------------------------------------------	
				RHeading rHeading4 = new RHeading();	
				rHeading4.setTypeHeading(TypeHeading.p);
				rHeading4.setId("6");
				rHeading4.setStyleClass("");
				rHeading4.setValue("<strong>Siga-nos em nossas redes sociais</strong>");
				rComponents2.add(rHeading4);
				//------------------------- END p ----------------------------------------------------------

		//------------------------- START BUTTON 1 ----------------------------------------------------------	
		RButton rButton1 = new RButton();
		rHeading1.setId("7");
		rButton1.setValue("Facebook");
		rButton1.setTitle("Facebook");
		rButton1.setHref("https://www.facebook.com/tratoronnewholland");
		rButton1.setTarget("_blank");
		rButton1.setStyleClass("rounded-button ui-button-primary p-2 m-2");
		rButton1.setIcon("fa fa-facebook");
		rComponents2.add(rButton1);
		//--------------------------- END BUTTON 1 -------------------------------------------------------------
		
		//------------------------- START BUTTON 1 ----------------------------------------------------------	
				RButton rButton2 = new RButton();
				rHeading1.setId("8");
				rButton2.setValue("Instagram");
				rButton2.setTitle("Instagram");
				rButton2.setHref("https://www.instagram.com/tratoronnewholland");
				rButton2.setTarget("_blank");
				rButton2.setStyleClass("rounded-button ui-button-primary p-2 m-2");
				rButton2.setIcon("fa fa-instagram");
				rComponents2.add(rButton2);
				//--------------------------- END BUTTON 1 -------------------------------------------------------------
			

		divCode2.setrComponents(rComponents2);
		divs.add(divCode2);  // vou adiciona o div coluna
		
		//========================== END COLUMN 2 ==========================================================");
		
		
		
		
		
		block.setDivs(divs);
		
		
		ObjectMapper objectMapper = new ObjectMapper();
		
		String blockAsString = "{}";
		try {
			blockAsString = objectMapper.writeValueAsString(block);
			//blockAsString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(block);
			
			
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		 ObjectMapper map33 = new ObjectMapper();

		 map33.registerSubtypes(new NamedType(RVideo.class, "RVideo"));
		 map33.registerSubtypes(new NamedType(RButton.class, "RButton"));
		 map33.registerSubtypes(new NamedType(RHeading.class, "RHeading"));
		 map33.registerSubtypes(new NamedType(RFlexDiv.class, "RFlexDiv"));
		 map33.registerSubtypes(new NamedType(RDivBreak.class, "RDivBreak"));
		 map33.registerSubtypes(new NamedType(RParagraph.class, "RParagraph"));
		 map33.registerSubtypes(new NamedType(RGraphicImage.class, "RGraphicImage"));
		  
		 
		  
		
		String json = blockAsString;
		RBlock newRBlock = null;;
		try {
			newRBlock = map33.readValue(json, RBlock.class);
		} catch (JsonProcessingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}	
		
		
		
		
		
		
		String blockAsString2 = "{}";
		ObjectMapper objectMapper2 = new ObjectMapper();
		try {
			blockAsString2 = objectMapper2.writeValueAsString(newRBlock);
			
			
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		block = newRBlock;
		
		
		
	}

	
	
	
	

	
	

	public RBlock getBlock() {
		return block;
	}

	public void setBlock(RBlock block) {
		this.block = block;
	}



}