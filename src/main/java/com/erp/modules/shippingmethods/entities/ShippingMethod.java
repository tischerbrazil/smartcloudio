package com.erp.modules.shippingmethods.entities;

import java.security.SecureRandom;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import org.geoazul.ecommerce.view.shopping.ShoppingCart;
import org.geoazul.model.security.ClientEntity;
import org.hibernate.annotations.NaturalId;
import org.omnifaces.optimusfaces.test.model.LocalGeneratedIdEntity;

import com.erp.modules.commonClasses.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "erp_shipping_method")
@NamedQueries({
		@NamedQuery(name = "ShippingMethod.findByCode", query = "SELECT p FROM ShippingMethod p WHERE p.id = :id"),
		@NamedQuery(name = "ShippingMethod.findAllActives", query = "SELECT p FROM ShippingMethod p WHERE p.enabled = true"),
		@NamedQuery(name = "ShippingMethod.findByClientActives", query = "SELECT p FROM ShippingMethod p WHERE p.enabled = true AND p.clientEntity.id = :client"),
		 })
public class ShippingMethod extends BaseEntity {

	private static final long serialVersionUID = 1L;
	public static final String SHIPING_METHOD = "ShippingMethod.findByCode";
	public static final String SHIPING_METHOD_ALL = "ShippingMethod.findAllActives";
	public static final String SHIPING_METHOD_ALL_BY_CLIENT = "ShippingMethod.findByClientActives";


	@Column(name = "name")
	private String name;

	@Column(name = "enabled")
	private boolean enabled;
	
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
	@JoinColumn(name = "client_id")
	@JsonIgnore

	private ClientEntity clientEntity;

	@Column(name = "minweight")
	private Double minweight = 0d;

	@Column(name = "maxweight")
	private Double maxweight = 0d;

	@Column(name = "index")
	private Double index = 1d;
	
	@Column(name = "CODE")
	private String code ; // 04510 PAX
	
	@Transient
	private Double shippingValue = 0d;
	
	@Transient
	private Integer deliveryTime = 1;
	
	@OneToMany(mappedBy = "shippingMethod", fetch = FetchType.LAZY)
	@JsonIgnore
	private List<ShoppingCart> ShoppingCarts;

	public ShippingMethod() {

	}

	public String createRandomCode(int codeLength) {
		char[] chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890".toCharArray();
		StringBuilder sb = new StringBuilder();
		Random random = new SecureRandom();
		for (int i = 0; i < codeLength; i++) {
			char c = chars[random.nextInt(chars.length)];
			sb.append(c);
		}
		String output = sb.toString();
		return output;
	}

	@Override
	public String toString() {
		return super.getId().toString();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public Double getMinweight() {
		return minweight;
	}

	public void setMinweight(Double minweight) {
		this.minweight = minweight;
	}

	public Double getMaxweight() {
		return maxweight;
	}

	public void setMaxweight(Double maxweight) {
		this.maxweight = maxweight;
	}

	public Double getShippingValue() {
		return shippingValue;
	}

	public void setShippingValue(Double shippingValue) {
		this.shippingValue = shippingValue;
	}

	public List<ShoppingCart> getShoppingCarts() {
		return ShoppingCarts;
	}

	public void setShoppingCarts(List<ShoppingCart> shoppingCarts) {
		ShoppingCarts = shoppingCarts;
	}

	public ClientEntity getClientEntity() {
		return clientEntity;
	}

	public void setClientEntity(ClientEntity clientEntity) {
		this.clientEntity = clientEntity;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Integer getDeliveryTime() {
		return deliveryTime;
	}

	public void setDeliveryTime(Integer deliveryTime) {
		this.deliveryTime = deliveryTime;
	}
	


	

}