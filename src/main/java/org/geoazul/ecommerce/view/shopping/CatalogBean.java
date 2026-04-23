package org.geoazul.ecommerce.view.shopping;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ActionEvent;
import org.omnifaces.cdi.Param;
import org.omnifaces.cdi.ViewScoped;
import org.primefaces.model.ResponsiveOption;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.servlet.http.HttpServletRequest;
import org.geoazul.ecommerce.model.Item;
import com.erp.modules.inventory.entities.Product;
import com.erp.modules.inventory.entities.ProductMedia;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Named
@ViewScoped
public class CatalogBean implements Serializable {

	@Inject
	private EntityManager entityManager;

	@Inject
	@Param(pathIndex = 0)
	private Long gcmid;

	@Inject
	@Param(pathIndex = 1)
	private Long id;

	@PostConstruct
	public void init() {

		if (id != null) {

			Query queryModFilter = entityManager.createNamedQuery(Product.FIND_BY_ID);
			queryModFilter.setParameter("id", id);

			item = (Item) queryModFilter.getSingleResult();

			List<ProductMedia> kk = this.item.getImages();

			productMedia = kk;

			photos = new ArrayList<>();
			for (ProductMedia pm : kk) {

				photos.add(new Photo(pm.getFilename(), pm.getFilename(), pm.getAlt(), pm.getTitle()));

			}

		}

		responsiveOptions1 = new ArrayList<>();
		responsiveOptions1.add(new ResponsiveOption("1024px", 5));
		responsiveOptions1.add(new ResponsiveOption("768px", 3));
		responsiveOptions1.add(new ResponsiveOption("560px", 1));

		responsiveOptions2 = new ArrayList<>();
		responsiveOptions2.add(new ResponsiveOption("1024px", 5));
		responsiveOptions2.add(new ResponsiveOption("960px", 4));
		responsiveOptions2.add(new ResponsiveOption("768px", 3));
		responsiveOptions2.add(new ResponsiveOption("560px", 1));

		responsiveOptions3 = new ArrayList<>();
		responsiveOptions3.add(new ResponsiveOption("1500px", 5));
		responsiveOptions3.add(new ResponsiveOption("1024px", 3));
		responsiveOptions3.add(new ResponsiveOption("768px", 2));
		responsiveOptions3.add(new ResponsiveOption("560px", 1));

	}

	public void redirecionar(ActionEvent actionEvent) {
		try {

			String dGOD = (String) actionEvent.getComponent().getAttributes().get("objectid");

			FacesContext context = FacesContext.getCurrentInstance();
			String url = context.getExternalContext().getRequestContextPath();

			context.getExternalContext().redirect(url + "/index?id=" + dGOD);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Inject
	private HttpServletRequest request;

	private List<ProductMedia> productMedia;

	private List<Photo> photos;
	private List<Photo> photosBanner;

	private List<ResponsiveOption> responsiveOptions1;

	private List<ResponsiveOption> responsiveOptions2;

	private List<ResponsiveOption> responsiveOptions3;

	private int activeIndex = 0;

	private static boolean isFacesEvent(HttpServletRequest request) {
		return request.getParameter("jakarta.faces.behavior.event") != null
				|| request.getParameter("omnifaces.event") != null;
	}

	public Optional<String> readCookie(String key) {
		try {
			return Arrays.stream(request.getCookies()).filter(c -> key.equals(c.getName()))
					.map(jakarta.servlet.http.Cookie::getValue).findAny();
		} catch (Exception ignore) {
		}
		return Optional.empty();
	}

	public void changeActiveIndex() {
		Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		this.activeIndex = Integer.valueOf(params.get("index"));
	}

	public List<Photo> getPhotos() {
		return photos;
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

	// ===============================================================
	// ===============================================================

	private String keyword = "";
	private List<Product> items;
	private Item item;
	private Integer itemId;

	private String uuid;

	public String getUuid() {
		return this.uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	// String valorCookieCarrinho = ID; //O que você quiser definir, basta ser único
	// Cookie cookieCarrinho = new Cookie("cookieCarrinho_br.com.seusite",
	// valorCookieCarrinho);
	// cookieCarrinho.setMaxAge(60*60*24*7); //Defina a validade - 1 semana?
	// response.addCookie(cookieCarrinho);

	// Cookie[] cookies = request.getCookies();

//if (cookies != null) {
	// for (Cookie cookie : cookies) {
	// if (cookie.getName().equals("cookieCarrinho_br.com.seusite")) {
	// //pegar o valor do ID com cookie.getValue() e buscar no bd
	// }
	// }
//}

	public String doSearch() {
		try {
			// TypedQuery<Item> typedQuery = em.createNamedQuery(Item.SEARCH, Item.class);
			// typedQuery.setParameter("keyword", "%" + keyword.toUpperCase() + "%");
			// items = typedQuery.getResultList();
			CriteriaBuilder builder = entityManager.getCriteriaBuilder();
			CriteriaQuery<Product> criteria = builder.createQuery(Product.class);
			Root<Product> root = criteria.from(Product.class);
			TypedQuery<Product> query = entityManager.createQuery(criteria.select(root)
					.where(getSearchPredicates(root, entityManager)).orderBy(builder.asc(root.get("id"))));
			this.items = query.getResultList();
			return null;
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("ERRO", "ERRO INESPERADO!"));
			return null;
		}
	}

	private Predicate[] getSearchPredicates(Root<Product> root, EntityManager session) {
		CriteriaBuilder builder = session.getCriteriaBuilder();
		List<Predicate> predicatesList = new ArrayList<Predicate>();
		predicatesList.add(builder.isTrue(root.get("enabled")));
		return predicatesList.toArray(new Predicate[predicatesList.size()]);
	}

	public void getInjectId(String itemId) {
		try {
			if (!itemId.equals(null)) {
				this.setItemId(Integer.valueOf(itemId));
				this.retrieve();
			}
		} catch (Exception ign) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("ERRO", "ITEM INEXISTENTE!"));
		}
	}

	public void retrieve() {
		if (this.itemId != null) {
			try {
				item = entityManager.find(Item.class, itemId);

				init();

			} catch (Exception e) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("ERRO", "ITEM INEXISTENTE!"));
			}
		} else {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("ERRO", "ID NÃO INFORMADO!"));
		}
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public List<Product> getItems() {
		return items;
	}

	public void setItems(List<Product> items) {
		this.items = items;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public List<Photo> getPhotosBanner() {
		return photosBanner;
	}

	public void setPhotosBanner(List<Photo> photosBanner) {
		this.photosBanner = photosBanner;
	}

	public List<ProductMedia> getProductMedia() {
		return productMedia;
	}

	public void setProductMedia(List<ProductMedia> productMedia) {
		this.productMedia = productMedia;
	}

	public Long getGcmid() {
		return gcmid;
	}

	public void setGcmid(Long gcmid) {
		this.gcmid = gcmid;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}