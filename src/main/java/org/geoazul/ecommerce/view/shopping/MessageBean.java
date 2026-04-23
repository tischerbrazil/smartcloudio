package org.geoazul.ecommerce.view.shopping;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.geoazul.ecommerce.model.Message;
import org.example.kickoff.model.Person;
import org.example.kickoff.view.ActiveUser;
import org.omnifaces.cdi.ViewScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Named
@ViewScoped
public class MessageBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private EntityManager entityManager;
	
	
	@Inject
	private ActiveUser activeUser;
	
	@PostConstruct
	public void init() {
		this.message = new Message();
		
		try {
			
			
				if (activeUser.isPresent()) {
					activeUserMessages = findUserMessages(this.activeUser.get());
			    }
							
			
			
		}catch(Exception ex) {
			activeUserMessages = new ArrayList<Message>();
		}
		
	}
	
	
	private List<Message> activeUserMessages;
	
	public List<Message> findUserMessages(Person user) {
		
		try {
			
			TypedQuery<Message> messages = 
					entityManager.createNamedQuery(Message.FIND_BY_USER_ID,
							Message.class);
			messages.setParameter("person", user);
			return messages.getResultList();

		} catch (Exception ex) {
			return null;
		}
	}
	
	
	
	

	@Inject
	private HttpServletRequest request;
	
	
	
	public Optional<String> readCookie(String key) {
		try {
			return Arrays.stream(request.getCookies()).filter(c -> key.equals(c.getName())).map(Cookie::getValue)
					.findAny();
		} catch (Exception ignore) {
		}
		return Optional.empty();
	}
	
	public ShoppingCart findShoppingCart(String uuid) {
		try {
			
			TypedQuery<ShoppingCart> shoppingCartDraft = 
					entityManager.createNamedQuery(ShoppingCart.FIND_BY_UUID_AND_STATUS,
							ShoppingCart.class);
			shoppingCartDraft.setParameter("uuid", UUID.fromString(uuid));
			shoppingCartDraft.setParameter("status", org.geoazul.ecommerce.view.shopping.ShoppingCart.Status.DRAFT); 
			return shoppingCartDraft.getSingleResult();

		} catch (Exception ex) {
			return null;
		}
	}
	
	@Transactional
	public void sendForm() {
		
		Optional<String> cookie_uuid = readCookie("cart_uuid");

		
		if (cookie_uuid.isPresent()) {
			message.setCookie(UUID.fromString(cookie_uuid.get()));
			message.setShoppingCart(findShoppingCart(cookie_uuid.get()));
		}

		FacesContext context = FacesContext.getCurrentInstance();
		
		
		message.setUser(activeUser.get());
		entityManager.persist(message);
		entityManager.flush();
		this.message = new Message();
	
		
		context.addMessage(null, 
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Mensagem Enviada",
				"Aguarde nosso contato!"));
	

	}

	public Message getMessage() {
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}

	
	
	public List<Message> getActiveUserMessages() {
		return activeUserMessages;
	}

	public void setActiveUserMessages(List<Message> activeUserMessages) {
		this.activeUserMessages = activeUserMessages;
	}


	private Message message;
	
	
	
}