package org.geoazul.view.website;


import static modules.LoadInitParameter.save_FILE_PATH;
import static org.primefaces.model.SortOrder.ASCENDING;
import static org.primefaces.model.SortOrder.DESCENDING;
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
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.Map.Entry;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.push.Push;
import jakarta.faces.push.PushContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.apache.tika.config.TikaConfig;
import org.apache.tika.detect.Detector;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MimeTypes;
import org.example.kickoff.model.Person;
import org.example.kickoff.view.ActiveUser;
import org.geoazul.ecommerce.view.shopping.Photo;
import org.geoazul.ecommerce.view.shopping.ShoppingCart;
import org.geoazul.model.app.ProductCategoryMapping;
import org.geoazul.model.website.UrlMenuItem;
import org.geoazul.view.utils.ConvertToASCII2;
import org.keycloak.example.oauth.UserData;
import org.omnifaces.cdi.Cookie;
import org.omnifaces.cdi.Param;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Faces;
import org.omnifaces.utils.reflect.Getter;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.TransferEvent;
import org.primefaces.model.DualListModel;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.MatchMode;
import org.primefaces.model.ResponsiveOption;
import org.primefaces.model.SortMeta;
import org.primefaces.model.filter.GlobalFilterConstraint;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.refact.ProductJpaLazyDataModel;
import com.erp.modules.inventory.entities.ErpProductUserMapping;
import com.erp.modules.inventory.entities.Manufacturer;
import com.erp.modules.inventory.entities.Product;
import com.erp.modules.inventory.entities.ProductCategoryOne;
import com.erp.modules.inventory.entities.ProductMedia;

@Named
@ViewScoped
public class ProductBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<FilterMeta> filterBy;

	@Inject
	EntityManager entityManager;

	private LazyDataModel<Product> model;

	private List<Product> filteredProducts;

	private String globalSearch;

	

	private String compCategory;
	
	private String groupCategory;
	
	private String subGroupCategory;

	

	private String typeFilter;
	private Map<String, String> typeFilters = new HashMap<>();

	static final String GLOBAL_FILTER_KEY = "globalFilter";

	public void ecommerceRedirect() {
		try {
			FacesContext.getCurrentInstance().getExternalContext()
					.redirect("/ecommerce/index?globalSearch=" + globalSearch);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void processSearch() {

		Map<String, SortMeta> sortByParam = new HashMap<>();
		Map<String, FilterMeta> filterByParam = new HashMap<>();

	
		SortMeta sortMeta = new SortMeta();

		if (typeFilter != null) {

			switch (typeFilter) {

			case "popularidade":
				sortMeta = SortMeta.builder().field("id").order(DESCENDING).priority(0).build();
				break;
			case "menorpreco":
				sortMeta = SortMeta.builder().field("salePrice").order(ASCENDING).priority(0).build();
				break;
			case "maiorpreco":
				sortMeta = SortMeta.builder().field("salePrice").order(DESCENDING).priority(0).build();
				break;
			case "maisvendidos":
				sortMeta = SortMeta.builder().field("sold").order(DESCENDING).priority(0).build();
				break;
			default:
				sortMeta = SortMeta.builder().field("id").order(DESCENDING).priority(0).build();
			}
			sortByParam.put(null, sortMeta);
		}
	
		if (compCategory != null) {

			GlobalFilterConstraint compCatFilterConstraint = new org.primefaces.model.filter.GlobalFilterConstraint();
			FilterMeta filter1 = FilterMeta.builder().constraint(compCatFilterConstraint)
					.field("compositeCategories.id").filterBy(null)
					.filterValue(Integer.valueOf(Integer.valueOf(compCategory))).matchMode(MatchMode.EQUALS).build();
			filterByParam.put("compositeCategories", filter1);
		}
		
		if (groupCategory != null) {

			GlobalFilterConstraint compGroupFilterConstraint = new org.primefaces.model.filter.GlobalFilterConstraint();
			FilterMeta filterGroup = FilterMeta.builder().constraint(compGroupFilterConstraint)
					.field("pro_productGroup.id")
					.filterValue(Integer.valueOf(Integer.valueOf(groupCategory))).matchMode(MatchMode.EQUALS).build();
			filterByParam.put("pro_productGroup", filterGroup);
		}
		
		if (subGroupCategory != null) {

			GlobalFilterConstraint compSubgroupFilterConstraint = new org.primefaces.model.filter.GlobalFilterConstraint();
			FilterMeta filterGroup = FilterMeta.builder().constraint(compSubgroupFilterConstraint)
					.field("pro_productSubgroup.id")
					.filterValue(Integer.valueOf(Integer.valueOf(subGroupCategory))).matchMode(MatchMode.EQUALS).build();
			filterByParam.put("pro_productSubgroup", filterGroup);
		}

		if (globalSearch != null) {
			GlobalFilterConstraint dd = new org.primefaces.model.filter.GlobalFilterConstraint();
			FilterMeta filter = FilterMeta.builder().constraint(dd).field(GLOBAL_FILTER_KEY).filterBy(null)
					.filterValue(globalSearch).matchMode(MatchMode.GLOBAL).build();
			filterByParam.put("teste", filter);
		}
		model = new ProductJpaLazyDataModel<Product>(Product.class, () -> entityManager, sortByParam, filterByParam);
	}

	private List<Manufacturer> manufactureries;

	
	private org.primefaces.model.menu.MenuModel modelMenu;
	
	
	private org.primefaces.model.menu.MenuModel modelMenuGroup;
	
	private org.primefaces.model.menu.MenuModel modelMenuPrincipal;
	
	
	@PostConstruct
	public void init() {
		
		
		

		typeFilters = new HashMap<>();
		typeFilters.put("Popularidade", "popularidade");
		typeFilters.put("Menor Preço", "menorpreco");
		typeFilters.put("Maior Preço", "maiorpreco");
		typeFilters.put("Maior Vendas", "maisvendidos");

		globalSearch = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap()
				.get("globalSearch");

		

		Optional<String> compCatId = 
				Optional.ofNullable(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap()
						.get("compCategory"));
		
		if (compCatId.isPresent()) {
			try {
				compCategory = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap()
						.get("compCategory");
			} catch (NumberFormatException ignore) {

			}

		}
		
		
		

		Map<String, FilterMeta> filterByParam = new HashMap<>();

		Map<String, SortMeta> sortByParam = new HashMap<>();
		

		if (compCategory != null) {

			
			GlobalFilterConstraint compCatFilterConstraint = new org.primefaces.model.filter.GlobalFilterConstraint();
			FilterMeta filter1 = FilterMeta.builder().constraint(compCatFilterConstraint)
					.field("compositeCategories.id").filterBy(null).filterValue(Integer.valueOf(compCategory))
					.matchMode(MatchMode.EQUALS).build();

			filterByParam.put("compositeCategories", filter1);
		}
		
		
		Optional<String> groupId = 
				Optional.ofNullable(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap()
						.get("groupCategory"));
		
		if (groupId.isPresent()) {
			try {
				groupCategory = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap()
						.get("groupCategory");
			} catch (NumberFormatException ignore) {

			}

		}
		
        if (groupCategory != null) {
			GlobalFilterConstraint groupCatFilterConstraint = new org.primefaces.model.filter.GlobalFilterConstraint();
			FilterMeta filterGroup = FilterMeta.builder().constraint(groupCatFilterConstraint)
					.field("pro_productGroup.id").filterBy(null).filterValue(Integer.valueOf(groupCategory))
					.matchMode(MatchMode.EQUALS).build();
			filterByParam.put("pro_productGroup", filterGroup);
		}
        
        Optional<String> subGroupId = 
				Optional.ofNullable(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap()
						.get("subGroupCategory"));
		
		if (subGroupId.isPresent()) {
			try {
				subGroupCategory = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap()
						.get("subGroupCategory");
			} catch (NumberFormatException ignore) {

			}

		}
        
        
        if (subGroupCategory != null) {
			GlobalFilterConstraint groupCatFilterConstraint = new org.primefaces.model.filter.GlobalFilterConstraint();
			FilterMeta filterGroup = FilterMeta.builder().constraint(groupCatFilterConstraint)
					.field("pro_productSubgroup.id").filterBy(null).filterValue(Integer.valueOf(subGroupCategory))
					.matchMode(MatchMode.EQUALS).build();
			filterByParam.put("pro_productSubgroup", filterGroup);
		}
        
        
        

		if (globalSearch != null) {
			
			GlobalFilterConstraint dd = new org.primefaces.model.filter.GlobalFilterConstraint();
			FilterMeta filter = FilterMeta.builder().constraint(dd).field(GLOBAL_FILTER_KEY).filterBy(null)
					.filterValue(globalSearch).matchMode(MatchMode.GLOBAL).build();

			filterByParam.put("teste", filter);

		}
		model = new ProductJpaLazyDataModel<Product>(Product.class, () -> entityManager, sortByParam, filterByParam);

		TypedQuery<Manufacturer> queryCodeM = entityManager.createNamedQuery(Manufacturer.FIND_ALL_ACTIVES,
				Manufacturer.class);

		manufactureries = queryCodeM.getResultList();



		importEmptyCategories();
	
		// ========================================================
		Query queryModFilter2 = entityManager.createNamedQuery(ProductCategoryOne.FIND_BY_ACTIVE);
		this.productCategoriesOne = queryModFilter2.getResultList();
		// =========================================================
		
		
		
		
		
		
			
			
		
		 
		 
		 
		 
		 
		 
		 modelMenuPrincipal = new DefaultMenuModel();
		 
			DefaultSubMenu itemGroupPri;
			DefaultMenuItem itemSubPri ;
		 
		 for (UrlMenuItem pFatherPri: this.getMenu(375)) {
			
			 
			 itemGroupPri = DefaultSubMenu.builder()
	                     .label(pFatherPri.getName())
	                     .build();
	        	 
	        	 for (UrlMenuItem pChildrenPri: pFatherPri.getChildrens()) {
	        	
	        		 itemSubPri = DefaultMenuItem.builder()
	        	                .value(pChildrenPri.getName())
	        	                .url("/?gcmid=" + pChildrenPri.getId())
	        	                .icon(pChildrenPri.getIcon()) 
	        	                .build();
	        		 
	        		 itemGroupPri.getElements().add(itemSubPri);
	            	 
	            }
	        	 modelMenuPrincipal.getElements().add(itemGroupPri);
	       }
		 

		 
		
		TypedQuery<ProductCategoryOne> xxxxxx = entityManager.createNamedQuery(ProductCategoryOne.FIND_BY_ACTIVE,
				ProductCategoryOne.class);
		
		
		 List<ProductCategoryOne> usersListRet = xxxxxx.getResultList();

		
		 List<ProductCategoryOne> teste = usersListRet.stream()
				 .filter(q -> q.getParentId() == null )
				  .collect(Collectors.toList());
		
		
		 
		 
		 
		 
		 
		 modelMenu = new DefaultMenuModel();
		 
			DefaultSubMenu firstSubmenu2;
			DefaultMenuItem item2 ;
		 
		 for (ProductCategoryOne pFather:teste) {
			
			 
	        	 firstSubmenu2 = DefaultSubMenu.builder()
	                     .label(pFather.getName())
	                     .build();
	        	 
	        	 for (ProductCategoryOne pChildren: pFather.getChildrens()) {
	        	
	        		 item2 = DefaultMenuItem.builder()
	        	                .value(pChildren.getName())
	        	                .url("/ecommerce/?groupCategory=" + pChildren.getId())
	        	                .icon(pChildren.getSymbol_svg()) 
	        	                .build();
	        		 
	        	        firstSubmenu2.getElements().add(item2);
	            	 
	            }
	        	  modelMenu.getElements().add(firstSubmenu2);
	       }
		 
		 
		
				
	}
	
	public List<UrlMenuItem> getMenu(Integer menuId) {
		try {
			
			//FIND_ALL_ON_MENU
			
			TypedQuery<UrlMenuItem> queryApp = entityManager.createNamedQuery(UrlMenuItem.FIND_ALL_ON_MENU_ROOT,
					UrlMenuItem.class);
			queryApp.setParameter("urlMenuId", menuId);

			return  queryApp.getResultList();

		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}


	
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

	public Product findById(String uuid) {

		try {
			TypedQuery<Product> queryAppLayer = entityManager.createNamedQuery(Product.FIND_BY_ID, Product.class);
			queryAppLayer.setParameter("id",  Long.valueOf(id));
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

	public void editAction(String uuid) {
		this.product = this.findById(uuid);
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
		cities = new DualListModel<>(productCategorySource, productCategoryTarget);
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
				e.printStackTrace();
			}

			ProductMedia mediaNew = new ProductMedia();
			mediaNew.setFilename("/files/" + userData.getRealmEntity() + "/media/" + fileNameOnly);

			TikaConfig tikaConfig = TikaConfig.getDefaultConfig();

			Metadata metadata = new Metadata();

			String text = null;
			try {
				text = parseUsingComponents(fileNameNew, tikaConfig, metadata);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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

	@Inject
	@Push
	PushContext wishlistChannel;

	@Inject
	@Param(pathIndex = 0)
	private String id;

	private void sendPushMessage(Object message) {
		wishlistChannel.send(message);
	}

	
	@Inject
	private ActiveUser activeUser;
	
	public ErpProductUserMapping findErpProductUserMapping(ErpProductUserMapping erpProductUserMapping) {
		try {

			TypedQuery<ErpProductUserMapping> erpProductUserMappingQuery = entityManager
					.createNamedQuery(ErpProductUserMapping.FIND, ErpProductUserMapping.class);
			erpProductUserMappingQuery.setParameter("personId", erpProductUserMapping.getPerson().getId());
			erpProductUserMappingQuery.setParameter("productId", erpProductUserMapping.getApp().getId());
			return erpProductUserMappingQuery.getSingleResult();
    	} catch (Exception ex) {
			return null;
		}
	}
	
	@Transactional
	public int deleteErpProductUserMapping(ErpProductUserMapping erpProductUserMapping) {
		try {

			
			Query erpProductUserMappingQuery = entityManager.createNamedQuery(ErpProductUserMapping.DELETE);
			erpProductUserMappingQuery.setParameter("erpProductUserMapping", erpProductUserMapping);
			erpProductUserMappingQuery.executeUpdate();
			entityManager.flush();
			return erpProductUserMappingQuery.executeUpdate();
    	} catch (Exception ex) {
			ex.printStackTrace();
		}
		return 0;
		
	}
	
	@Transactional
	public void sendMessage(String messageJ) {
		
		String[] splitter = messageJ.split(",");
		Integer valX = Integer.valueOf(splitter[1]);
		
	
		
		boolean statusInfo = false;
		
		
		if (activeUser.isPresent()) {
						
		Person userLoged = entityManager.find(Person.class, activeUser.get().getId());
		Product productChange = entityManager.find(Product.class, Integer.valueOf(splitter[1]));
		
		
		
		
		ErpProductUserMapping erpProductUserMapping = new ErpProductUserMapping();
		
		erpProductUserMapping.setApp(productChange);
		erpProductUserMapping.setPerson(userLoged);

		Optional<ErpProductUserMapping> existInWishlist = Optional.ofNullable(findErpProductUserMapping(erpProductUserMapping));
		
		if (existInWishlist.isPresent()) {
			int deleteInt = deleteErpProductUserMapping(existInWishlist.get());
			statusInfo = false;
		}else {
		entityManager.persist(erpProductUserMapping);
		entityManager.flush();
		statusInfo = true;
		}
		
		
		}
		
		
		//FIXME
		
		
	
		String retorno = "{\"index\":" + splitter[0] + ", \"value\":" + statusInfo + ", \"viewid\":\"" + splitter[2] + "\"}";
		
		
		PrimeFaces.current().ajax().addCallbackParam("retorno", retorno);
		//this.sendPushMessage(retorno);
	}

	public void importEmptyCategories() {


		// ------------


		Query queryModFilter = entityManager.createNamedQuery(ProductCategoryOne.FIND_ALL);
		List<ProductCategoryOne> productCategorySource = queryModFilter.getResultList();
		// ======================
		// ======================

		List<ProductCategoryOne> productCategoryTarget = new ArrayList<ProductCategoryOne>();

		cities = new DualListModel<>(productCategorySource, productCategoryTarget);

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

			entityManager.merge(product);
			entityManager.flush();

			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "SUCESSO", "Produto Alterado!");
			FacesContext.getCurrentInstance().addMessage(null, message);

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
		}
	}

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


	private DualListModel<ProductCategoryOne> cities;

	public DualListModel<ProductCategoryOne> getCities() {
		return cities;
	}

	public void setCities(DualListModel<ProductCategoryOne> cities) {
		this.cities = cities;
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

	public LazyDataModel<Product> getModel() {
		return model;
	}

	public void setModel(LazyDataModel<Product> model) {
		this.model = model;
	}

	public String getTypeFilter() {
		return typeFilter;
	}

	public void setTypeFilter(String typeFilter) {
		this.typeFilter = typeFilter;
	}

	public Map<String, String> getTypeFilters() {
		return typeFilters;
	}

	public void setTypeFilters(Map<String, String> typeFilters) {
		this.typeFilters = typeFilters;
	}

	public org.primefaces.model.menu.MenuModel getModelMenu() {
		return modelMenu;
	}

	public void setModelMenu(org.primefaces.model.menu.MenuModel modelMenu) {
		this.modelMenu = modelMenu;
	}

	public org.primefaces.model.menu.MenuModel getModelMenuGroup() {
		return modelMenuGroup;
	}

	public void setModelMenuGroup(org.primefaces.model.menu.MenuModel modelMenuGroup) {
		this.modelMenuGroup = modelMenuGroup;
	}

	public String getGlobalSearch() {
		return globalSearch;
	}

	public void setGlobalSearch(String globalSearch) {
		this.globalSearch = globalSearch;
	}
	
	public String getCompCategory() {
		return compCategory;
	}

	public void setCompCategory(String compCategory) {
		this.compCategory = compCategory;
	}

	public String getGroupCategory() {
		return groupCategory;
	}

	public void setGroupCategory(String groupCategory) {
		this.groupCategory = groupCategory;
	}

	public String getSubGroupCategory() {
		return subGroupCategory;
	}

	public void setSubGroupCategory(String subGroupCategory) {
		this.subGroupCategory = subGroupCategory;
	}

	public org.primefaces.model.menu.MenuModel getModelMenuPrincipal() {
		return modelMenuPrincipal;
	}

	public void setModelMenuPrincipal(org.primefaces.model.menu.MenuModel modelMenuPrincipal) {
		this.modelMenuPrincipal = modelMenuPrincipal;
	}
}