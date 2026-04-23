package br.safrapay.api.model;

import java.io.Serializable;
import java.util.Objects;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import org.geoazul.ecommerce.model.InstallmentMethod;
import org.geoazul.model.Address;
import org.example.kickoff.model.Person;
import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "gateway_safrapay_card")
@JsonFilter("safraPayCardFilter")
public class PersonCard implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "SMARTID", unique = true, nullable = false)
	private Integer smartid;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "PARTNER_ID", referencedColumnName = "id")
	@JsonIgnore
	private Person partner;
		
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
	@JoinColumn(name = "INSTALLMENT_METHOD_ID")
	@JsonIgnore
	private InstallmentMethod installmentMethod;
	
	@OneToOne(mappedBy = "card")
	@JsonIgnore
	private SafraPayTransaction transaction;
	
	@Column(name = "ID_UUID", nullable = true)
	private String id_uuid; // ": "ffede3ee-37ab-47bb-9981-d4d14697d67a",
		
	@Column(name = "CARD_NUMBER", nullable = true)
	private String cardNumber; // ": "",
		
	@Column(name = "CARD_CVV", nullable = true)
	@Transient
	private String cvv; // ": "",

	@Column(name = "BRAND", nullable = true)
	private Integer brand; // ": "Visa",

	@Column(name = "CARD_HOLDER_NAME", nullable = true)
	@Transient
	private String cardholderName; // ": "Renan Soarez",

	@Column(name = "CARD_HOLDER_DOCUMENT", nullable = true)
	@Transient
	private String cardholderDocument; // ": "01234567891",
	
	@Column(name = "PRIVATE_LABEL", nullable = true)
	@Transient
	private Boolean isPrivateLabel; // ": false,

	@Column(name = "EXPIRATION_MOUNTH", nullable = true)
	@Transient
	private Integer expirationMonth; // ": 12
	
	@Transient
	private String validade; // ": 12

	@Column(name = "EXPIRATION_YEAR", nullable = true)
	@Transient
	private Integer expirationYear; // ": 20126

	@Column(name = "BRAND_NAME", nullable = true)
	private String brandName; // ": "Visa",
		
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "BILLING_ADDRESS_ID", referencedColumnName = "id")
	@Transient
	private Address billingAddress;

	
	
	public Integer getSmartid() {
		return smartid;
	}

	public void setSmartid(Integer smartid) {
		this.smartid = smartid;
	}
	
	public SafraPayTransaction getTransaction() {
		return transaction;
	}

	public void setTransaction(SafraPayTransaction transaction) {
		this.transaction = transaction;
	}

	public String getId_uuid() {
		return id_uuid;
	}

	public void setId_uuid(String id_uuid) {
		this.id_uuid = id_uuid;
	}
	
	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}
	
	public String getCvv() {
		return cvv;
	}

	public void setCvv(String cvv) {
		this.cvv = cvv;
	}

	public Integer getBrand() {
		return brand;
	}

	public void setBrand(Integer brand) {
		this.brand = brand;
	}

	public String getCardholderName() {
		return cardholderName;
	}

	public void setCardholderName(String cardholderName) {
		this.cardholderName = cardholderName;
	}

	public String getCardholderDocument() {
		return cardholderDocument;
	}

	public void setCardholderDocument(String cardholderDocument) {
		this.cardholderDocument = cardholderDocument;
	}

	public Boolean getIsPrivateLabel() {
		return isPrivateLabel;
	}

	public void setIsPrivateLabel(Boolean isPrivateLabel) {
		this.isPrivateLabel = isPrivateLabel;
	}

	public Integer getExpirationMonth() {
		return expirationMonth;
	}

	public void setExpirationMonth(Integer expirationMonth) {
		this.expirationMonth = expirationMonth;
	}

	public Integer getExpirationYear() {
		return expirationYear;
	}

	public void setExpirationYear(Integer expirationYear) {
		this.expirationYear = expirationYear;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	
	public String getValidade() {
		return validade;
	}

	public void setValidade(String validade) {
		this.validade = validade;
	}

	public Person getPartner() {
		return partner;
	}

	public void setPartner(Person partner) {
		this.partner = partner;
	}

	public InstallmentMethod getInstallmentMethod() {
		return installmentMethod;
	}

	public void setInstallmentMethod(InstallmentMethod installmentMethod) {
		this.installmentMethod = installmentMethod;
	}

	@Override
	public int hashCode() {
		return Objects.hash(smartid);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PersonCard other = (PersonCard) obj;
		return Objects.equals(smartid, other.smartid);
	}

	

	

	


	//1	Visa
	//2	MasterCard
	//3	Amex
	//4	Elo
	//5	Aura
	//6	Jcb
	//7	Diners
	//8	Discover
	//9	Hipercard
	//11	Enroute
	//12	Ticket
	//13	Sodexo
	//14	Vr
	//15	Alelo
	//16	Setra
	//18	Vero
	//19	Sorocred
	//20	GreenCard
	//21	Cabal
	//22	Banescard
	//23	VerdeCard
	//24	ValeCard
	//25	UnionPay
	//26	Up
	//27	Tricard
	//28	Bigcard
	//29	Ben
	//30	RedeCompras
	//31	SafrapayDigital
	//32	BamexBenefits
	//33	Cdc
	//34	SindCredit
	//35	IdealCard
	//36	NutriCash
	//37	BiqCard
	//38	PersonalCard
	//39	EuCard
	//40	Planvale
	//41	LiberCard
	//42	MaxxCard
	//43	VsCard
	//44	CooperCard
	//45	Megavalecard
	//46	Abrapetite
	//47	Agiplan
	//48	Credsystem
	//49	Esplanada
	//50	Credz
	//51	Ourocard

}
