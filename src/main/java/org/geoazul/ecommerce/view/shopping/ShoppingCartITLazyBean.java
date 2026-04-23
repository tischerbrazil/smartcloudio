package org.geoazul.ecommerce.view.shopping;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.utils.reflect.Getter;

@Named
@ViewScoped
public class ShoppingCartITLazyBean implements Serializable {

	private static final long serialVersionUID = 1L;
		
	private static final Map<String, Entry<Getter<ShoppingCart>, Object>> AVAILABLE_CRITERIA = new LinkedHashMap<>();

	static {
	//	AVAILABLE_CRITERIA.put("Enabled", 
	// FIXME			new SimpleEntry<>(ShoppingCart::enabled, true));
	}
	
	
	
	public ShoppingCart findById(String uuid) {
		try {
			
			Query queryModFilter = entityManager.createNamedQuery(ShoppingCart.FIND_BY_UUID_AND_STATUS);
			queryModFilter.setParameter("uuid", UUID.fromString(uuid));
			
			
			ShoppingCart hhh = (ShoppingCart) queryModFilter.getSingleResult();
			
			for (ShoppingCartItem teste: hhh.getShoppingCartItens()){
				
				
				teste.getId();
				
				teste.getQuantity();
				teste.getShoppingCart().getId();
				teste.getSubTotal();
				
				
			}
			
			return hhh;
		} catch (Exception ex) {
			return null;
		}
	}
	
	private ShoppingCart shoppingCart;
	
	
	
	public void editAction(String uuid) {
		
		shoppingCart = findById(uuid);
	}
	
	
	
	
	
	@PostConstruct
	public void init() {
		
		

    Map<String, Object> predefinedCriteria =  new HashMap<>();
		 //predefinedCriteria.put("status", "PAIDOUT");
	}

	public ShoppingCart getShoppingCart() {
		return shoppingCart;
	}

	public void setShoppingCart(ShoppingCart shoppingCart) {
		this.shoppingCart = shoppingCart;
	}
	
	@Inject
	private EntityManager entityManager;
	
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