package org.geoazul.view.website;

import static modules.LoadInitParameter.save_FILE_PATH;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.Map.Entry;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import jsonb.JacksonUtil;
import org.apache.tika.config.TikaConfig;
import org.apache.tika.detect.Detector;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MimeTypes;
import org.example.kickoff.model.Person;
import org.geoazul.ecommerce.model.Item;
import org.geoazul.ecommerce.view.shopping.Photo;
import org.geoazul.ecommerce.view.shopping.ShoppingCart;
import org.geoazul.model.app.ProductCategoryMapping;
import org.geoazul.model.basic.AbstractWidget;
import org.geoazul.model.security.ClientOAuthEntity;
import org.geoazul.model.website.Modulo;
import org.geoazul.model.website.RBlock;
import org.geoazul.model.website.RButton;
import org.geoazul.model.website.RComponent;
import org.geoazul.model.website.RDivBreak;
import org.geoazul.model.website.RFlexDiv;
import org.geoazul.model.website.RGraphicImage;
import org.geoazul.model.website.RHeading;
import org.geoazul.model.website.RParagraph;
import org.geoazul.model.website.RVideo;
import org.geoazul.view.utils.ConvertToASCII2;
import org.keycloak.example.oauth.UserData;
import org.omnifaces.cdi.Cookie;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Faces;
import org.omnifaces.utils.reflect.Getter;
import org.owasp.html.AttributePolicy;
import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.PolicyFactory;
import org.owasp.html.Sanitizers;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.TransferEvent;
import org.primefaces.model.DualListModel;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.MatchMode;
import org.primefaces.model.ResponsiveOption;
import org.primefaces.model.SortMeta;
import org.primefaces.model.file.UploadedFile;
import org.primefaces.model.filter.GlobalFilterConstraint;
import org.primefaces.refact.ProductAllJpaLazyDataModel;
import org.primefaces.util.LangUtils;
import com.erp.modules.inventory.entities.Block;
import com.erp.modules.inventory.entities.Manufacturer;
import com.erp.modules.inventory.entities.Product;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.erp.modules.inventory.entities.ProductCategoryOne;
import com.erp.modules.inventory.entities.ProductMedia;

@Named
@ViewScoped
public class ProductAllBean implements Serializable {

	
	public void openItemUuid() {
        String param1 = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("uuid");
        String param2 = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("gcmid");
        
        
        if (param1 != null) {
       
        	String itemUuid = param1;
        	
        						
        	try {
        		
        		Query queryModFilter = entityManager.createNamedQuery(Product.FIND_BY_ID);
				queryModFilter.setParameter("id",  Long.valueOf(param1));
				
    			product =	(Product) queryModFilter.getSingleResult();
    			
    			rBlocks.clear();
    			blocks = product.getBlocks();
    			
    			
    			for (Block blockLoad :   product.getBlocks()){
    				RBlock newBlock = blockLoad.getBlock();
    				newBlock.setId(blockLoad.getId());
    				newBlock.setAbstractWidget(blockLoad.getAbstractWidget().toString());
    				rBlocks.add(newBlock);
    			}
    			
    				
    			} catch (Exception e) {
    				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("ERRO", "ITEM INEXISTENTE!"));

    				e.printStackTrace();
    			}
        }
    }
	
	
	
	
	public List<AbstractWidget> getAllWidgets() {
		try {
			TypedQuery<AbstractWidget> query = entityManager.createNamedQuery(AbstractWidget.WIDGET_ALL,
					AbstractWidget.class);
			return query.getResultStream().toList();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	
	public void loadWidgets() {
		
			this.widgets = getAllWidgets() ;
		
	}

	private List<AbstractWidget> widgets;

	public List<AbstractWidget> getWidgets() {
		return widgets;
	}

	public void setWidgets(List<AbstractWidget> widgets) {
		this.widgets = widgets;
	}

	public Block findBlockById(Long id) {

		try {
			return entityManager.find(Block.class, id);
		} catch (Exception e) {
			return null;
		}

	}

	public RBlock getRBlock(JsonNode jsonNode) {

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
				newRBlock = map33.readValue(jsonNode.toString(), RBlock.class);
			} catch (JsonProcessingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			return newRBlock;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private RComponent selectedRComponent;

	public RComponent getSelectedRComponent() {
		return selectedRComponent;
	}

	public void setSelectedRComponent(RComponent selectedRComponent) {
		this.selectedRComponent = selectedRComponent;
	}

	private String position = "left";

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	private AbstractWidget widget;

	public AbstractWidget getWidget() {
		return widget;
	}

	public void setWidget(AbstractWidget widget) {
		this.widget = widget;
	}

	@Transactional
	public void createNewBlock() {
		FacesMessage msg;
		if (widget != null && product != null) {

			
			
			AbstractWidget widgety = entityManager.find(AbstractWidget.class, this.widget.getId());

			Block blocNew = new Block();
			blocNew.setAbstractWidget(widgety);
			blocNew.setSequence(this.rBlocks.size());
			blocNew.setBlockCode(widgety.getStrings());
			blocNew.setItem(product);

			entityManager.persist(blocNew);
			entityManager.flush();
			
			
			this.rBlocks.add(blocNew.getBlock());

			msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "SUCESSO", "Bloco Adicionado!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}

	}
	
	private List<Block> blocks = new ArrayList<Block>();
	private List<RBlock> rBlocks = new ArrayList<RBlock>();
	

	@Transactional
	public void saveBlockAction() {

		try {

			Block editBlock = findBlockById(Long.valueOf(selectedRComponent.getBlockId()));

			RBlock rBlockEdit = editBlock.getBlock();
			
			rBlockEdit.setAbstractWidget(editBlock.getAbstractWidget().toString());
			rBlockEdit.setId(editBlock.getId());
			
			
			if (selectedRComponent.getParentId() != null) {

				
				
				rBlockEdit.getDivs().get(selectedRComponent.getColumn()).getrComponents()
				.get(selectedRComponent.getOrder()).getChildren()
				.set(0, selectedRComponent);
				
				rBlockEdit.getDivs().get(selectedRComponent.getColumn()).getrComponents()
						.get(selectedRComponent.getOrder()).getChildren()
						.set(selectedRComponent.getParentId(), selectedRComponent);
			} else {
				
				rBlockEdit.getDivs().get(selectedRComponent.getColumn()).getrComponents()
						.set(selectedRComponent.getOrder(), selectedRComponent);
			}
			ObjectMapper objectMapper = new ObjectMapper();

			JsonNode retornoBlockJsonNode = JacksonUtil.toJsonNode("{}");

			retornoBlockJsonNode = JacksonUtil.toJsonNode(objectMapper.writeValueAsString(rBlockEdit));
			editBlock.setBlockCode(retornoBlockJsonNode);

			
		

			entityManager.merge(editBlock);
			entityManager.flush();
			
			this.rBlocks.set(editBlock.getSequence(), rBlockEdit);
		} catch (Exception e) {
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
		}

	}
	
	private String fileTypeDirection;
	
	
	
	public void executeFileType() {
	
		fileTypeDirection = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("fileTypeDirection");
	
	//saveBlockAction
	}
	

	public void executeSelect() {

		String blockId = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("blockid");
		String columnId = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap()
				.get("columnid");
		String orderId = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("orderid");
		String parentId = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap()
				.get("parentid");


		Integer orderInt = Integer.valueOf(orderId);
		Integer parentIdInt = null;
		if (parentId != null) {
			parentIdInt = Integer.parseInt(parentId);
		}

		Block editBlock = findBlockById(Long.valueOf(blockId));


		if (editBlock != null || columnId != null || orderId != null) {

			RBlock blockCodeXML = getRBlock(editBlock.getBlockCode());

			if (columnId.equals("0")) {
				selectedRComponent = blockCodeXML.getDivs().get(0).getrComponents().stream()
						.filter(rcomp -> rcomp.getOrder() == orderInt).findAny().orElse(null);
				if (parentIdInt != null) {
					selectedRComponent = selectedRComponent.getChildren().get(parentIdInt);
				}
				selectedRComponent.setBlockId(Long.valueOf(blockId));
				position = "left";
			} else if (columnId.equals("1")) {
				selectedRComponent = blockCodeXML.getDivs().get(1).getrComponents().stream()
						.filter(rcomp -> rcomp.getOrder() == orderInt).findAny().orElse(null);
				if (parentIdInt != null) {
					selectedRComponent = selectedRComponent.getChildren().get(parentIdInt);
				}
				selectedRComponent.setBlockId(Long.valueOf(blockId));
				position = "right";
			} else if (columnId.equals("2")) {
				selectedRComponent = blockCodeXML.getDivs().get(2).getrComponents().stream()
						.filter(rcomp -> rcomp.getOrder() == orderInt).findAny().orElse(null);
				if (parentIdInt != null) {
					selectedRComponent = selectedRComponent.getChildren().get(parentIdInt);
				}
				selectedRComponent.setBlockId(Long.valueOf(blockId));
				position = "right";
			} else {
				return;
			}

		}

	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<FilterMeta> filterBy;

	@Inject
	EntityManager entityManager;

	private ProductAllJpaLazyDataModel<Product> model;
	
	
	private List<Product> filteredProducts;

	private String globalSearch;

	public String getGlobalSearch() {
		return globalSearch;
	}

	public void setGlobalSearch(String globalSearch) {
		this.globalSearch = globalSearch;
	}

	static final String GLOBAL_FILTER_KEY = "globalFilter";

	public void ecommerceRedirect() {
		try {
			FacesContext.getCurrentInstance().getExternalContext()
					.redirect("/ecommerce/index?globalSearch=" + globalSearch);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private List<ProductMedia> selectedMedias;

	public List<ProductMedia> getSelectedMedias() {
		return selectedMedias;
	}

	public void setSelectedMedias(List<ProductMedia> selectedMedias) {
		this.selectedMedias = selectedMedias;
	}

	@Transactional
	public void deleteMedias() {


	}

	private List<ProductMedia> allMedias;

	
	private Long id;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void retrieve() {

		if (FacesContext.getCurrentInstance().isPostback()) {
			return;
		}
		

		if (this.id == null) {

			return;

		} else {
			Product dd = findById(id);
			if (dd != null) {
				allMedias = dd.getImages();
			}

		}

	}

	public void processSearch() {

		Map<String, SortMeta> sortByParam = new HashMap<>();

		if (globalSearch == null) {

		} else {

			GlobalFilterConstraint dd = new org.primefaces.model.filter.GlobalFilterConstraint();
			FilterMeta filter = FilterMeta.builder().constraint(dd).field(GLOBAL_FILTER_KEY).filterBy(null)
					.filterValue(globalSearch).matchMode(MatchMode.GLOBAL).build();

			Map<String, FilterMeta> filterByParam = new HashMap<>();
			filterByParam.put("teste", filter);

			
			
			model = new ProductAllJpaLazyDataModel<Product>(Product.class, () -> entityManager, filterByParam);
			
		}

	}

	private List<Manufacturer> manufactureries;

	@PostConstruct
	public void init() {

		globalSearch = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap()
				.get("globalSearch");

		if (globalSearch != null) {
			GlobalFilterConstraint dd = new org.primefaces.model.filter.GlobalFilterConstraint();
			FilterMeta filter = FilterMeta.builder().constraint(dd).field(GLOBAL_FILTER_KEY).filterBy(null)
					.filterValue(globalSearch).matchMode(MatchMode.GLOBAL).build();

			Map<String, FilterMeta> filterByParam = new HashMap<>();
			filterByParam.put("teste", filter);

			Map<String, SortMeta> sortByParam = new HashMap<>();
			
		
			
			model = new ProductAllJpaLazyDataModel<Product>(Product.class, () -> entityManager, filterByParam);
			
			
			
			//model = new ProductAllJpaLazyDataModel<Product>(Product.class, 
			//		() -> entityManager, filterByParam);
		} else {
			model = new ProductAllJpaLazyDataModel<Product>(Product.class, () -> entityManager);
				
			
			//model = new ProductAllJpaLazyDataModel<>(Product.class, () -> entityManager);
		}

		TypedQuery<Manufacturer> queryCodeM = entityManager.createNamedQuery(Manufacturer.FIND_ALL_ACTIVES,
				Manufacturer.class);

		manufactureries = queryCodeM.getResultList();

	

		

		importEmptyCategories(); //FIXME

		// ========================================================
		Query queryModFilter2 = entityManager.createNamedQuery(ProductCategoryOne.FIND_BY_ACTIVE);
		this.productCategoriesOne = queryModFilter2.getResultList();
		// =========================================================

		loadWidgets();

	}

	public List<Product> getFilteredProducts() {
		return filteredProducts;
	}

	public void setFilteredProducts(List<Product> filteredProducts) {
		this.filteredProducts = filteredProducts;
	}

	private List<ResponsiveOption> responsiveOptions;

	private List<Product> products;

	public List<Product> getProductByGroupId(Integer groupId) {
		try {
			if (!groupId.equals(null)) {
				Query queryModFilter = entityManager.createNamedQuery(Product.FIND_ALL);
			
				return queryModFilter.getResultList();
			} else {
				return null;
			}

		} catch (Exception ex) {

		}
		return null;
	}

	private int getInteger(String string) {
		try {
			return Integer.parseInt(string);
		} catch (NumberFormatException ex) {
			return 0;
		}
	}

	private List<Product> filteredProducts1;

	public boolean globalFilterFunction(Object value, Object filter, Locale locale) {
		String filterText = (filter == null) ? null : filter.toString().trim().toLowerCase();
		if (LangUtils.isBlank(filterText)) {
			return true;
		}
		int filterInt = getInteger(filterText);


		Product product = (Product) value;
		return product.getName().toLowerCase().contains(filterText);

	}

	private Date checkin;
	private Date checkout;

	public Date getCheckin() {
		return checkin;
	}

	public void setCheckin(Date checkin) {
		this.checkin = checkin;
	}

	public Date getCheckout() {
		return checkout;
	}

	public void setCheckout(Date checkout) {
		this.checkout = checkout;
	}

	public List<Product> getFilteredProducts1() {
		return filteredProducts1;
	}

	public void setFilteredProducts1(List<Product> filteredProducts1) {
		this.filteredProducts1 = filteredProducts1;
	}

	public List<FilterMeta> getFilterBy() {
		return filterBy;
	}

	public void setFilterBy(List<FilterMeta> filterBy) {
		this.filterBy = filterBy;
	}

	private List<Photo> photosBanner;

	private List<ResponsiveOption> responsiveOptions1;

	private List<ResponsiveOption> responsiveOptions2;

	private List<ResponsiveOption> responsiveOptions3;

	private int activeIndex = 0;

	public void changeActiveIndex() {
		Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		this.activeIndex = Integer.valueOf(params.get("index"));
	}

	public List<ResponsiveOption> getResponsiveOptions() {
		return responsiveOptions;
	}

	public void setResponsiveOptions(List<ResponsiveOption> responsiveOptions) {
		this.responsiveOptions = responsiveOptions;
	}

	public List<ResponsiveOption> getResponsiveOptions1() {
		return responsiveOptions1;
	}

	public List<ResponsiveOption> getResponsiveOptions2() {
		return responsiveOptions2;
	}

	public List<ResponsiveOption> getResponsiveOptions3() {
		return responsiveOptions3;
	}

	public int getActiveIndex() {
		return activeIndex;
	}

	public void setActiveIndex(int activeIndex) {
		this.activeIndex = activeIndex;
	}

	public List<Photo> getPhotosBanner() {
		return photosBanner;
	}

	public void setPhotosBanner(List<Photo> photosBanner) {
		this.photosBanner = photosBanner;
	}

	public List<Product> getProducts() {
		return products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}

	// --------------------------

	private static final Map<String, Entry<Getter<Product>, Object>> AVAILABLE_CRITERIA = new LinkedHashMap<>();

	public InputStream getSvgLogo() {
		// Note: this is a dummy example. In reality, you should be able to take e.g. a
		// Long argument as ID and then
		// return the desired byte[] content from some service class by given ID.
		return Faces.getResourceAsStream("/resources/layout/img/a.svg");
	}

	public InputStream getSvgLogo2() {
		// Note: this is a dummy example. In reality, you should be able to take e.g. a
		// Long argument as ID and then
		// return the desired byte[] content from some service class by given ID.
		return Faces.getResourceAsStream("/resources/layout/img/b.svg");
	}

	@Inject
	private HttpServletRequest request;

	@Inject
	private UserData userData;

	

	private List<ProductCategoryOne> productCategoriesOne;

	public Product findById(Long id) {

		try {
			TypedQuery<Product> queryAppLayer = entityManager.createNamedQuery(Product.FIND_BY_ID, Product.class);
			queryAppLayer.setParameter("id",  id);
			Product product = queryAppLayer.getSingleResult();
			return product;
		} catch (Exception e) {
			return null;
		}

	}

	private Product product;

	public void newAction() {
		this.product = new Product();
	}

	public void editAction(Long id) {
		this.product = this.findById(id);
		// this.product = productService.findById(uuid);
		Query queryModFilter = entityManager.createNamedQuery(ProductCategoryOne.FIND_ALL);

		List<ProductCategoryOne> productCategorySource = queryModFilter.getResultList();
		List<ProductCategoryOne> productCategoryTarget = new ArrayList<ProductCategoryOne>();
		List<ProductCategoryMapping> f = product.getCompositeCategories();

		for (ProductCategoryMapping ddd : f) {
			ProductCategoryOne categ = ddd.getProductCategoryOne();
			productCategoryTarget.add(categ);
		}

		for (ProductCategoryOne cccc : productCategoryTarget) {
			if (productCategorySource.contains(cccc)) {
				productCategorySource.remove(cccc);
			} else {
			}
		}
		categs = new DualListModel<>(productCategorySource, productCategoryTarget);
		
		rBlocks.clear();
		blocks = product.getBlocks();
		
		
		for (Block blockLoad :   product.getBlocks()){
			RBlock newBlock = blockLoad.getBlock();
			newBlock.setId(blockLoad.getId());
			newBlock.setAbstractWidget(blockLoad.getAbstractWidget().toString());
			rBlocks.add(newBlock);
		}
		
		
	}

	protected Integer getParamId(String param) {
		FacesContext context = FacesContext.getCurrentInstance();
		Map<String, String> map = context.getExternalContext().getRequestParameterMap();
		return Integer.valueOf(map.get(param));
	}

	@Transactional
	public void removeImage() {
		try {

			ProductMedia item = entityManager.find(ProductMedia.class, getParamId("itemId").longValue());

			this.product.getImages().remove(item);

			entityManager.remove(item);
			entityManager.flush();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("SUCESSO", "ITEM REMOVIDO!"));
		} catch (Exception e) {
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("ERRO", "ERRO INESPERADO 8811!"));
		}
	}

	@Transactional
	public void importIMAGE(FileUploadEvent event) throws Exception {


		try {

			FacesMessage msg = new FacesMessage("Succesful", event.getFile().getFileName() + " is uploaded.");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			String fileNameOnly = "";
			String fileNameNew = null;

			UploadedFile uploadedFile = event.getFile();
			byte[] image = uploadedFile.getContent();
			// BufferedImage bi = ImageIO.read(new ByteArrayInputStream(image));
			// int width = bi.getWidth();
			// int height = bi.getHeight();


			try {

				Random random = new Random();
				int randomInt = random.nextInt(100);

				LocalDateTime data = LocalDateTime.now();
				// SimpleDateFormat formatador = new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss");
				String fileName = ConvertToASCII2.convert(event.getFile().getFileName()).toLowerCase();
				fileNameOnly = data.toString() + "-" + String.valueOf(randomInt) + "_" + fileName;
				String appDirectory = save_FILE_PATH + "/files/" + userData.getRealmEntity() + "/media";
				File appDirImg = new File(appDirectory);
				if (!appDirImg.exists()) {
					appDirImg.mkdir();
				}
				fileNameNew = appDirectory + "/" + fileNameOnly;

				OutputStream out = new FileOutputStream(new File(fileNameNew));
				int read = 0;
				byte[] bytes = new byte[1024];
				InputStream in = event.getFile().getInputStream();
				while ((read = in.read(bytes)) != -1) {
					out.write(bytes, 0, read);
				}
				in.close();
				out.flush();
				out.close();
			} catch (IOException e) {
			}

			ProductMedia mediaNew = new ProductMedia();

			mediaNew.setFilename("/files/" + userData.getRealmEntity() + "/media/" + fileNameOnly);

			TikaConfig tikaConfig = TikaConfig.getDefaultConfig();

			Metadata metadata = new Metadata();

			String text = null;
			try {
				text = parseUsingComponents(fileNameNew, tikaConfig, metadata);
			} catch (Exception e) {
				// e.printStackTrace();
			}

			mediaNew.setMimeType(text);

			mediaNew.setReleaseDate(LocalDateTime.now());
			mediaNew.setAlt("");
			mediaNew.setDate_created(new Date());
			mediaNew.setDate_created_gmt(new Date());
			mediaNew.setDate_modified(new Date());
			mediaNew.setDate_modified_gmt(new Date());
			mediaNew.setReleaseDate(null);
			
			mediaNew.setTitle("");
			mediaNew.setItem(this.product);


			entityManager.persist(mediaNew);



			entityManager.flush();

			this.getProduct().getImages().add(mediaNew);
		} catch (Exception e) {
			// FacesContext.getCurrentInstance().addMessage(null, new
			// FacesMessage(e.getMessage()));
			e.printStackTrace();
			// return "complete?faces-redirect=true&id=" + this.person.getId();
		}

	}

	public String parseUsingComponents(String filename, TikaConfig tikaConfig, Metadata metadata) throws Exception {
		MimeTypes mimeRegistry = tikaConfig.getMimeRepository();


		Path path = Paths.get(filename);
		TikaInputStream tikaStream = TikaInputStream.get(path);
		Detector detector = tikaConfig.getDetector();
	
		return detector.detect(tikaStream, metadata).toString();

	}

	public void importEmptyCategories() {

		
		Query queryModFilter = entityManager.createNamedQuery(ProductCategoryOne.FIND_ALL);
		
		List<ProductCategoryOne> productCategorySource = queryModFilter.getResultList();
		
		List<ProductCategoryOne> productCategoryTarget = new ArrayList<ProductCategoryOne>();

		categs = new DualListModel<>(productCategorySource, productCategoryTarget);

	}

	//

	public Optional<String> readCookie(String key) {
		try {
			return Arrays.stream(request.getCookies()).filter(c -> key.equals(c.getName()))
					.map(jakarta.servlet.http.Cookie::getValue).findAny();
		} catch (Exception ignore) {
		}
		return Optional.empty();
	}

	private String my_cookie_uuid;

	public String getMy_cookie_uuid() {
		return my_cookie_uuid;
	}

	public void setMy_cookie_uuid(String my_cookie_uuid) {
		this.my_cookie_uuid = my_cookie_uuid;
	}

	public String getCart_uuid() {
		return cart_uuid;
	}

	public void setCart_uuid(String cart_uuid) {
		this.cart_uuid = cart_uuid;
	}

	private static final String CART_COOKIE_NAME = "cart_uuid";
	private static final int CART_COOKIE_AGE = -1; // Expires after 30 days
	private static final int CART_COOKIE_AGE_EXCLUDE = 0; // Exclude

	private static final String POSTAL_CODE_COOKIE_NAME = "postal_code";
	private static final int POSTAL_CODE_COOKIE_AGE = -1; // Newer Expires

	@Inject
	@Cookie
	private String cart_uuid;

	private void addCookie(String cookie_name, String value, int cookie_age) {

		Faces.addResponseCookie("cart_uuid", value, cookie_age);
		Faces.refresh();

	}

	private static boolean isFacesEvent(HttpServletRequest request) {
		return request.getParameter("jakarta.faces.behavior.event") != null
				|| request.getParameter("omnifaces.event") != null;
	}

	public ShoppingCart findShoppingCart(String uuid, org.geoazul.ecommerce.view.shopping.ShoppingCart.Status status) {
		try {

			TypedQuery<ShoppingCart> shoppingCartDraft = entityManager
					.createNamedQuery(ShoppingCart.FIND_BY_UUID_AND_STATUS, ShoppingCart.class);
			shoppingCartDraft.setParameter("uuid", UUID.fromString(uuid));
			shoppingCartDraft.setParameter("status", status);

			return shoppingCartDraft.getSingleResult();

		} catch (Exception ex) {
			return null;
		}
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	@Transactional
	public void update() {
		try {
			PolicyFactory policy = Sanitizers.FORMATTING.and(Sanitizers.BLOCKS).and(HTML).and(Sanitizers.LINKS)
					.and(Sanitizers.TABLES).and(IMAGES);
			product.setDescription(policy.sanitize(product.getDescription()));
			entityManager.merge(product);
			entityManager.flush();

			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "SUCESSO", "Produto Alterado!");
			FacesContext.getCurrentInstance().addMessage(null, message);

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
		}
	}
	
	@Transactional
	public void delete() {
		try {
			Product productDelete = entityManager.find(Product.class, product.getId());
			entityManager.remove(productDelete);
			entityManager.flush();

			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "SUCESSO", "Produto Excluido!");
			FacesContext.getCurrentInstance().addMessage(null, message);

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
		}
	}

	private static final AttributePolicy INTEGER = new AttributePolicy() {
		public String apply(String elementName, String attributeName, String value) {
			int n = value.length();
			if (n == 0) {
				return null;
			}
			for (int i = 0; i < n; ++i) {
				char ch = value.charAt(i);
				if (ch == '.') {
					if (i == 0) {
						return null;
					}
					return value.substring(0, i); // truncate to integer.
				} else if (!('0' <= ch && ch <= '9')) {
					return null;
				}
			}
			return value;
		}
	};

	public static final PolicyFactory MEDIAS = new HtmlPolicyBuilder().allowElements("video", "audio", "source")
			.allowAttributes("controls", "width", "height").onElements("video").allowAttributes("controls")
			.onElements("video").allowAttributes("src").onElements("audio").allowAttributes("src", "type")
			.onElements("source").allowTextIn("video", "audio")

			.toFactory();

	public static final PolicyFactory IMAGES = new HtmlPolicyBuilder().allowAttributes("class", "style").globally()
			.allowUrlProtocols("http", "https").allowElements("img").allowAttributes("float", "style", "alt", "src")
			.onElements("img").allowAttributes("border", "height", "width").matching(INTEGER).onElements("img")
			.toFactory();

	public static final PolicyFactory HTML = new HtmlPolicyBuilder()

			.allowElements("table", "tr", "td", "thead", "tbody", "th", "hr", "font", "button", "input", "select",
					"option", "video", "audio")
			.allowAttributes("class").globally().allowAttributes("color").globally().allowAttributes("bgcolor")
			.globally().allowAttributes("align").globally().allowAttributes("target").globally()
			.allowAttributes("value").globally().allowAttributes("name").globally().allowAttributes("controls")
			.globally().allowAttributes("src").globally().allowAttributes("autoplay").globally()
			.allowAttributes("muted").globally().allowAttributes("loop").globally().allowAttributes("poster").globally()
			.allowElements("a").requireRelNofollowOnLinks().allowAttributes("href").onElements("a")
			.allowUrlProtocols("http", "https", "mailto", "chat").toFactory();

	@Transactional
	public void onTransfer(TransferEvent event) {

		if (product != null) {
			FacesMessage msg = null;

			Product productEntity = entityManager.find(Product.class, product.getId());

			List<?> ppp = event.getItems();
			ProductCategoryOne productCategoryNew;
			try {
				for (Object item : event.getItems()) {


					int startIndex = item.toString().indexOf('[');


					int endIndex = item.toString().indexOf(']');


					item = item.toString().substring(startIndex + 1, endIndex).trim();


					productCategoryNew = entityManager.find(ProductCategoryOne.class,
							Integer.parseInt(item.toString()));


					if (event.isRemove()) {
						Query query = entityManager.createNamedQuery(ProductCategoryMapping.DELETE);
						query.setParameter("productEntity", productEntity);
						query.setParameter("categoryEntity", productCategoryNew);
						query.executeUpdate();
						entityManager.flush();

						msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "SUCESSO", "Categoria Removida!");

					} else {
						ProductCategoryMapping productCategoryMapping = new ProductCategoryMapping();
						productCategoryMapping.setProduct(productEntity);
						productCategoryMapping.setProductCategoryOne(productCategoryNew);
						entityManager.persist(productCategoryMapping);
						// productEntity.addCategory(productCategoryMapping);
						entityManager.flush();
						msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "SUCESSO", "Categoria Anexada!");

					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}

		}

	}



	private DualListModel<ProductCategoryOne> categs = new DualListModel<>();

	public DualListModel<ProductCategoryOne> getCategs() {
		return categs;
	}

	public void setCategs(DualListModel<ProductCategoryOne> categs) {
		this.categs = categs;
	}

	public List<ProductCategoryOne> getProductCategoriesOne() {
		return productCategoriesOne;
	}

	public void setProductCategoriesOne(List<ProductCategoryOne> productCategoriesOne) {
		this.productCategoriesOne = productCategoriesOne;
	}

	public List<Manufacturer> getManufactureries() {
		return manufactureries;
	}

	public void setManufactureries(List<Manufacturer> manufactureries) {
		this.manufactureries = manufactureries;
	}

	public ProductAllJpaLazyDataModel<Product> getModel() {
		return model;
	}

	public void setModel(ProductAllJpaLazyDataModel<Product> model) {
		this.model = model;
	}

	public List<ProductMedia> getAllMedias() {
		return allMedias;
	}

	public void setAllMedias(List<ProductMedia> allMedias) {
		this.allMedias = allMedias;
	}

	public List<Block> getBlocks() {
		return blocks;
	}

	public void setBlocks(List<Block> blocks) {
		this.blocks = blocks;
	}

	public List<RBlock> getrBlocks() {
		return rBlocks;
	}

	public void setrBlocks(List<RBlock> rBlocks) {
		this.rBlocks = rBlocks;
	}




	public String getFileTypeDirection() {
		return fileTypeDirection;
	}




	public void setFileTypeDirection(String fileTypeDirection) {
		this.fileTypeDirection = fileTypeDirection;
	}

}