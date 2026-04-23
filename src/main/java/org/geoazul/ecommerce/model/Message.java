package org.geoazul.ecommerce.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import jakarta.persistence.*;
import org.geoazul.ecommerce.view.shopping.ShoppingCart;
import org.example.kickoff.model.Person;
import com.erp.modules.commonClasses.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

/**
 * @author Laercio Tischer
 */

@Entity
@Table(name = "ERP_MESSAGE")
@NamedQueries({

		@NamedQuery(name = "Message.FIND_BY_USER_ID", query = "SELECT m FROM Message m WHERE m.user = :person") })

public class Message extends BaseEntity {

	private static final long serialVersionUID = 1L;

	public static final String FIND_BY_USER_ID = "Message.FIND_BY_USER_ID";

	public Message() {
		this.datetime = LocalDateTime.now();
	}

	@Column(name = "DATETIME")
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
	private LocalDateTime datetime;

	@Column(name = "NAME")
	private String name;

	@Column(name = "EMAIL")
	private String email;

	@Column(name = "FONE")
	private String fone;

	@Column(name = "MESSAGE")
	private String message;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
	@JoinColumn(name = "SHOPPING_CART")
	private ShoppingCart shoppingCart;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
	@JoinColumn(name = "USER_ID")
	private Person user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parent_id", updatable = true, nullable = true)
	private Message parentId;

	@OneToMany(mappedBy = "parentId", fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
	@OrderBy("id DESC")
	private List<Message> childrens = new ArrayList<Message>();

	@Column(name = "cookie_id")
	private UUID cookie;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFone() {
		return fone;
	}

	public void setFone(String fone) {
		this.fone = fone;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public ShoppingCart getShoppingCart() {
		return shoppingCart;
	}

	public void setShoppingCart(ShoppingCart shoppingCart) {
		this.shoppingCart = shoppingCart;
	}

	public Person getUser() {
		return user;
	}

	public void setUser(Person user) {
		this.user = user;
	}

	public UUID getCookie() {
		return cookie;
	}

	public void setCookie(UUID cookie) {
		this.cookie = cookie;
	}

	public LocalDateTime getDatetime() {
		return datetime;
	}

	public void setDatetime(LocalDateTime datetime) {
		this.datetime = datetime;
	}

	public Message getParentId() {
		return parentId;
	}

	public void setParentId(Message parentId) {
		this.parentId = parentId;
	}

	public List<Message> getChildrens() {
		return childrens;
	}

	public void setChildrens(List<Message> childrens) {
		this.childrens = childrens;
	}

}
