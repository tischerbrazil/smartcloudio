package org.geoazul.view.website;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.geoazul.ecommerce.view.shopping.ShoppingCart;
import org.geoazul.ecommerce.view.shopping.ShoppingCart.Status;
import org.keycloak.example.oauth.UserData;
import org.omnifaces.cdi.ViewScoped;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.MatchMode;
import org.primefaces.model.filter.GlobalFilterConstraint;
import org.primefaces.refact.ShoppingCartJpaLazyDataModel;

@Named
@ViewScoped
public class OrderBean implements Serializable {

	/**
	 * 
	 */
	
	private static final long serialVersionUID = 1L;

	private List<FilterMeta> filterBy;

	@Inject
	EntityManager entityManager;

	private LazyDataModel<ShoppingCart> model;


	private List<ShoppingCart> filteredShoppingCarts;

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

	public void processSearch() {
		GlobalFilterConstraint dd = new org.primefaces.model.filter.GlobalFilterConstraint();
		FilterMeta filter = FilterMeta.builder().constraint(dd).field("user.name").filterBy(null)
				.filterValue(globalSearch).matchMode(MatchMode.GLOBAL).build();
		Map<String, FilterMeta> filterByParam = new HashMap<>();
		
		filterByParam.put("teste", filter);
		
		//setModel(new ShoppingCartJpaLazyDataModel<ShoppingCart>(ShoppingCart.class, () -> entityManager, filterByParam));
	
		model = new ShoppingCartJpaLazyDataModel<ShoppingCart>(ShoppingCart.class, () -> entityManager, filterByParam);
		
	}


	@PostConstruct
	public void init() {
		globalSearch = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap()
				.get("globalSearch");

		if (globalSearch != null) {
			GlobalFilterConstraint dd = new org.primefaces.model.filter.GlobalFilterConstraint();
			FilterMeta filter = FilterMeta.builder().constraint(dd).field("user.name").filterValue(globalSearch).matchMode(MatchMode.CONTAINS).build();
			
			Map<String, FilterMeta> filterByParam = new HashMap<>();
			
			filterByParam.put("teste", filter);
			
			//model  = new ShoppingCartJpaLazyDataModel<ShoppingCart>(ShoppingCart.class, () -> entityManager, filterByParam);
			
			model = new ShoppingCartJpaLazyDataModel<ShoppingCart>(ShoppingCart.class, () -> entityManager, filterByParam);
			
		
		
		} else {
		//	model = new ShoppingCartJpaLazyDataModel<>(ShoppingCart.class, () -> entityManager);
		
			model = new ShoppingCartJpaLazyDataModel<ShoppingCart>(ShoppingCart.class, () -> entityManager);
			
		}




	}

	public List<ShoppingCart> getFilteredShoppingCarts() {
		return filteredShoppingCarts;
	}

	public void setFilteredShoppingCarts(List<ShoppingCart> filteredShoppingCarts) {
		this.filteredShoppingCarts = filteredShoppingCarts;
	}

	

	public List<FilterMeta> getFilterBy() {
		return filterBy;
	}

	public void setFilterBy(List<FilterMeta> filterBy) {
		this.filterBy = filterBy;
	}

	



	// --------------------------

	

	public ShoppingCart findById(String uuid) {

		try {
			
			TypedQuery<ShoppingCart> shoppingCartDraft = entityManager
					.createNamedQuery(ShoppingCart.FIND_BY_UUID_AND_STATUS, ShoppingCart.class);
			shoppingCartDraft.setParameter("uuid", UUID.fromString(uuid));
			shoppingCartDraft.setParameter("status", Status.PAIDOUT);
			return shoppingCartDraft.getSingleResult();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	private ShoppingCart shoppingCart;

	public void newAction() {
		this.shoppingCart = new ShoppingCart();
	}

	public void editAction(String uuid) {
		this.shoppingCart = this.findById(uuid);
	}

	protected Integer getParamId(String param) {
		FacesContext context = FacesContext.getCurrentInstance();
		Map<String, String> map = context.getExternalContext().getRequestParameterMap();
		return Integer.valueOf(map.get(param));
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

	


	
	public ShoppingCart getShoppingCart() {
		return shoppingCart;
	}

	public void setShoppingCart(ShoppingCart shoppingCart) {
		this.shoppingCart = shoppingCart;
	}

	@Transactional
	public void update() {
		try {

			entityManager.merge(shoppingCart);
			entityManager.flush();

			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "SUCESSO", "Produto Alterado!");
			FacesContext.getCurrentInstance().addMessage(null, message);

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
		}
	}

	
	

	public LazyDataModel<ShoppingCart> getModel() {
		return model;
	}

	public void setModel(LazyDataModel<ShoppingCart> model) {
		this.model = model;
	}
	
	
	public String getStatus(String status) {
		switch (status) {
			case "DRAFT":
				return "DRAFT";
			case "REQUEST":
				return "ABERTA";
			case "PAIDOUT":
				return "PAGO";
			default:
				return "DRAFT";
			}
		}

	public String getShippingStatus(String status) {
		switch (status) {
			case "WAITING":
				return "NENHUM";
			case "PROCESSING":
				return "PROCESSO";
			case "POSTED":
				return "POSTADO";
				
			case "SEND":
				return "ENVIADO";
			case "RECEIVED":
				return "ENTREGUE";
				
			default:
				return "NENHUM";
			}
		}


		public String getStatusColor(String status) {
			switch (status) {
			case "DRAFT":
				return "#FF0000";
			case "REQUEST":
				return "#0000FF";
			case "PAIDOUT":
				return "#00ff00";
			default:
				return "#6d8891";
			}
		}

}