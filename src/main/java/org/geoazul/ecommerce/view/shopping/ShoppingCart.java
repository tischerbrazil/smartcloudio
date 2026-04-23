package org.geoazul.ecommerce.view.shopping;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import org.geoazul.erp.Cities;
import org.geoazul.erp.Countries;
import org.geoazul.erp.States;
import org.example.kickoff.model.Person;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.NaturalId;
import com.erp.modules.commonClasses.BaseEntity;
import com.erp.modules.shippingmethods.entities.ShippingMethod;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import br.bancodobrasil.model.BancoBrasilPixPayment;
import br.safrapay.api.model.SafraPayTransaction;

/**
 * @author Laercio Tischer
 */

@Entity
@Table(name = "erp_shopping_cart")
@NamedQueries({ @NamedQuery(name = "ShoppingCart.FIND_ALL", query = "SELECT s FROM ShoppingCart s"),
		@NamedQuery(name = "ShoppingCart.FIND_BY_UUID_AND_STATUS", query = "SELECT s FROM ShoppingCart s WHERE s.uuid = :uuid AND s.status = :status ORDER BY s.id DESC"),
		@NamedQuery(name = "ShoppingCart.FIND_BY_PAYMENTID_AND_STATUS", query = "SELECT s FROM ShoppingCart s WHERE s.paymentId = :paymentId AND s.status = :status"),
		@NamedQuery(name = "ShoppingCart.FIND_BY_USER_ID", query = "SELECT s FROM ShoppingCart s WHERE s.user.id = :userid"),
		@NamedQuery(name = "setPaymentTrue", query = "UPDATE ShoppingCart SET paymentMethod = true  where id = :id"),
		@NamedQuery(name = "setPaymentFalse", query = "UPDATE ShoppingCart SET paymentMethod = false  where id = :id"),
		
})
public class ShoppingCart extends BaseEntity {

	public ShoppingCart() {
	}

	private static final long serialVersionUID = 1L;

	public static final String FIND_ALL = "ShoppingCart.FIND_ALL";
	public static final String FIND_BY_UUID_AND_STATUS = "ShoppingCart.FIND_BY_UUID_AND_STATUS";
	public static final String FIND_BY_PAYMENTID_AND_STATUS = "ShoppingCart.FIND_BY_PAYMENTID_AND_STATUS";
	public static final String FIND_BY_USER_ID = "ShoppingCart.FIND_BY_USER_ID";
	public static final String DELETE = "ShoppingCart.DELETE";

	public static final String PAYMENT_TRUE = "setPaymentTrue";
	public static final String PAYMENT_FALSE = "setPaymentFalse";

	@Column(name = "UUID")
	@NaturalId
	private UUID uuid;
	

	@OneToMany(mappedBy = "shoppingCart", fetch = FetchType.LAZY)
	@JsonIgnore
	private List<BancoBrasilPixPayment> pixPayment;

	
	public UUID getUuid() {
		return uuid;
	}

	public void setUuid(UUID uuid) {

		this.uuid = uuid;
	}
	
	public List<BancoBrasilPixPayment> getPixPayment() {
		return pixPayment;
	}

	public void setPixPayment(List<BancoBrasilPixPayment> pixPayment) {
		this.pixPayment = pixPayment;
	}
	
	
	public enum Status {
		DRAFT, REQUEST, PAIDOUT
	}

	public enum ShippingStatus {
		WAITING, PROCESSING, POSTED, SENT, RECEIVED
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "STATUS")
	@NotNull
	private Status status = Status.DRAFT;

	@Enumerated(EnumType.STRING)
	@Column(name = "SHIPPING_STATUS")
	@NotNull
	private ShippingStatus shippingStatus = ShippingStatus.WAITING;

	public String getI18nStatus() {
		return "ecommerce." + status.name();
	}

	@OneToMany(fetch = FetchType.LAZY, cascade = {
			CascadeType.DETACH }, orphanRemoval = true, mappedBy = "shoppingCart")
	private List<ShoppingCartItem> ShoppingCartItens = new ArrayList<ShoppingCartItem>();

	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
	@JoinColumn(name = "USER_ID")
	private Person user;

	@Column(name = "DATETIME")
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
	private LocalDateTime datetime;

	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "SHIPPING_METHOD_ID")
	@JsonIgnore
	private ShippingMethod shippingMethod;

    @ColumnDefault("false")
	@Column(name = "payment_method")
	private Boolean paymentMethod;

	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "COUNTRY_ID")
	private Countries country;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STATE_ID")
	private States state;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CITY_ID")
	private Cities city;

	@Column(name = "ZIPCODE")
	private String zipcode;

	@Column(name = "SHIPPING_VALUE")
	private Double shippingValue = 0D;

	@Column(name = "PAYMENT_ID")
	private Long paymentId;
	
	
	@OneToMany(mappedBy = "shoppingCart", fetch = FetchType.LAZY)
	private List<SafraPayTransaction> transactions;

	public Double getSubTotal() {
		try {
			if (this.ShoppingCartItens == null || this.ShoppingCartItens.isEmpty())
				return 0D;
			Double total = 0D;
			// Sum up the quantities
			for (ShoppingCartItem cartItem : this.ShoppingCartItens) {
				Double subtot = (cartItem.getSubTotal());
				total = total + subtot.floatValue();
			}
			return total;
		} catch (Exception ex) {
			return 0D;
		}
	}
	
	public Double getTotalWithSipping() {
		try {
			
			return this.shippingValue + this.getSubTotal();
		} catch (Exception ex) {
			return 0D;
		}
	}
	
	

	public LocalDateTime getDatetime() {
		return datetime;
	}

	public void setDatetime(LocalDateTime datetime) {
		this.datetime = datetime;
	}

	public Countries getCountry() {
		return country;
	}

	public void setCountry(Countries country) {
		this.country = country;
	}

	public States getState() {
		return state;
	}

	public void setState(States state) {
		this.state = state;
	}

	public Cities getCity() {
		return city;
	}

	public void setCity(Cities city) {
		this.city = city;
	}

	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	public Person getUser() {
		return user;
	}

	public void setUser(Person user) {
		this.user = user;
	}

	public List<ShoppingCartItem> getShoppingCartItens() {
		return ShoppingCartItens;
	}

	public void setShoppingCartItens(List<ShoppingCartItem> shoppingCartItens) {
		ShoppingCartItens = shoppingCartItens;
	}

	public ShippingMethod getShippingMethod() {
		return shippingMethod;
	}

	public void setShippingMethod(ShippingMethod shippingMethod) {
		this.shippingMethod = shippingMethod;
	}

	public String getTopBarFormat() {
		try {
			return "Enviar para: " + this.getCity().getName() + "/" + this.getState().getIso2();
		} catch (Exception ex) {
			return "Enviar para:";
		}
	}

	public Double getShippingValue() {
		return shippingValue;
	}

	public void setShippingValue(Double shippingValue) {
		this.shippingValue = shippingValue;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public ShippingStatus getShippingStatus() {
		return shippingStatus;
	}

	public void setShippingStatus(ShippingStatus shippingStatus) {
		this.shippingStatus = shippingStatus;
	}

	public Long getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(Long paymentId) {
		this.paymentId = paymentId;
	}

	public Boolean getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(Boolean paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public List<SafraPayTransaction> getTransactions() {
		return transactions;
	}

	public void setTransactions(List<SafraPayTransaction> transactions) {
		this.transactions = transactions;
	}



	

}