package org.geoazul.ecommerce.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Digits;
import org.geoazul.model.security.ClientEntity;
import org.omnifaces.optimusfaces.test.model.LocalGeneratedIdEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import br.safrapay.api.model.PersonCard;

@Entity
@Table(name = "erp_installment_method")
@NamedQueries({
		@NamedQuery(name = "InstallmentMethod.findByCode", query = "SELECT p FROM InstallmentMethod p WHERE p.id = :id"),
		@NamedQuery(name = "InstallmentMethod.findAllActives", query = "SELECT p FROM InstallmentMethod p WHERE p.enabled = true order by installmentNumber"),
		@NamedQuery(name = "InstallmentMethod.findByClientActives", query = "SELECT p FROM InstallmentMethod p WHERE p.enabled = true AND p.clientEntity.id = :client"),
		@NamedQuery(name = "InstallmentMethod.findById", query = "SELECT p FROM InstallmentMethod p WHERE p.id = :id") })

public class InstallmentMethod extends LocalGeneratedIdEntity {

	private static final long serialVersionUID = 1L;
	
	public static final String INSTALLMENT_METHOD = "InstallmentMethod.findByCode";
	public static final String INSTALLMENT_METHOD_ALL = "InstallmentMethod.findAllActives";
	public static final String INSTALLMENT_METHOD_ALL_BY_CLIENT = "InstallmentMethod.findByClientActives";
	public static final String INSTALLMENT_METHOD_ID = "InstallmentMethod.findById";

	@Column(name = "name")
	private String name;

	@Column(name = "enabled")
	private boolean enabled;
	
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
	@JoinColumn(name = "client_id")
	@JsonIgnore

	private ClientEntity clientEntity;
					
	@OneToMany(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH }, 
			orphanRemoval = false, mappedBy = "installmentMethod")
	@JsonIgnore

	@OrderBy("id DESC")
	private List<PersonCard> cardList = new ArrayList<PersonCard>();

	@Column(name = "INSTALLMENT_NUMBER")
	private Integer installmentNumber;
	
	@Digits(integer=2, fraction=2)
	@Column(name = "FESS")
	private BigDecimal fees;
	
	@Transient
	private BigDecimal valueTotal;
	
	@Transient
	private BigDecimal parcelTotal;
	
	
	public InstallmentMethod() {
	
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

	public ClientEntity getClientEntity() {
		return clientEntity;
	}

	public void setClientEntity(ClientEntity clientEntity) {
		this.clientEntity = clientEntity;
	}
	
	public List<PersonCard> getCardList() {
		return cardList;
	}

	public void setCardList(List<PersonCard> cardList) {
		this.cardList = cardList;
	}

	public BigDecimal getFees() {
		return fees;
	}

	public void setFees(BigDecimal fees) {
		this.fees = fees;
	}

	public BigDecimal getValueTotal() {
		return valueTotal;
	}

	public void setValueTotal(BigDecimal valueTotal) {
		this.valueTotal = valueTotal;
	}

	public BigDecimal getParcelTotal() {
		return parcelTotal;
	}

	public Integer getInstallmentNumber() {
		return installmentNumber;
	}

	public void setInstallmentNumber(Integer installmentNumber) {
		this.installmentNumber = installmentNumber;
	}

	public void setParcelTotal(BigDecimal parcelTotal) {
		this.parcelTotal = parcelTotal;
	}



	

	

	

}