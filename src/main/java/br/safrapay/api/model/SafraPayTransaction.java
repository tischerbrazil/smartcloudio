package br.safrapay.api.model;

import java.util.Objects;
import java.util.UUID;
import jakarta.persistence.Basic;
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
import jakarta.persistence.PostLoad;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

import org.geoazul.ecommerce.view.shopping.ShoppingCart;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Type;
import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

import br.bancodobrasil.model.JsonJsonNode;

import java.util.stream.Stream;

@Entity
@Table(name = "gateway_safrapay_transaction")
@JsonFilter("safraPayTransactionFilter")
public class SafraPayTransaction {

	private static final long serialVersionUID = 1L;

	public enum TransactionStatus {
		Undefined(0), PreAuthorized(1), Captured(2), Denied(3), Pending(4), Canceled(5), PendingCancel(6),
		PendingPayment(7), Paid(8), ErrorCreation(9), Expired(10), PendingNewDeadline(11), Timeout(12),
		PendingReferral(13);

		private int transactionStatus;

		private TransactionStatus(int transactionStatus) {
			this.transactionStatus = transactionStatus;
		}

		public int getTransactionStatus() {
			return transactionStatus;
		}

		public static TransactionStatus of(int transactionStatus) {
			return Stream.of(TransactionStatus.values()).filter(p -> p.getTransactionStatus() == transactionStatus)
					.findFirst().orElseThrow(IllegalArgumentException::new);
		}
	}

	@Basic
	private int transactionStatusValue;
	
	@Transient
	private TransactionStatus transactionStatusTransient;
	
	@Transient
	private int transactionStatus;

	@PostLoad
	void fillTransient() {
		if (transactionStatusValue > -1) {
			this.transactionStatusTransient = TransactionStatus.of(transactionStatusValue);
		}
		
		if (paymentTypeValue > -1) {
			this.paymentTypeTransient = PaymentType.of(paymentTypeValue);
		}
		
		if (installmentTypeValue > -1) {
			this.installmentType = InstallmentType.of(installmentTypeValue);
		}
	}

	@PrePersist
	void fillPersistent() {
		if (transactionStatusTransient != null) {
			this.transactionStatusValue = transactionStatusTransient.getTransactionStatus();
		}

		if (installmentType != null) {
			this.installmentTypeValue = installmentType.getInstallmentType();
		}

		if (paymentTypeTransient != null) {
			this.paymentTypeValue = paymentTypeTransient.getPaymentType();
		}

	}

	public enum PaymentType {
		Undefined(0), Debit(1), Credit(2), Voucher(3), Boleto(4), Doc(5), SafetyPay(6), Pix(7), Wallet(8);

		private int paymentType;

		private PaymentType(int paymentType) {
			this.paymentType = paymentType;
		}

		public int getPaymentType() {
			return paymentType;
		}

		public static PaymentType of(int paymentType) {
			return Stream.of(PaymentType.values()).filter(p -> p.getPaymentType() == paymentType).findFirst()
					.orElseThrow(IllegalArgumentException::new);
		}
	}

	@Basic
	private int paymentTypeValue;

	
	@Transient
	private PaymentType paymentTypeTransient;

	@Transient
	private int paymentType;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "smartid", unique = true, nullable = false)
	private Integer smartid;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "charge_id", updatable = true, nullable = true)
	@JsonIgnore
	private SafraPayCharge charge;


	@JsonProperty("id")
	@Column(name = "TRANSACTION_ID")
	private String transactionId;

	@JsonProperty("aditumNumber")
	private String aditumNumber;

	@JsonProperty("qrCode")
	private String qrCode;

	@JsonProperty("qrCodeBase64")
	private String qrCodeBase64;

	@JsonProperty("amount")
	private Integer amount;

	@JsonProperty("acquirer")
	private String acquirer;

	@JsonProperty("creationDateTime")
	private String creationDateTime;

	@JsonProperty("bankIssuerId")
	private String bankIssuerId;

	// CREDIT CARD ADD FIELDS
	// 0 Transação a vista.
	// 1 Transação parcelada pelo lojista, ou seja, sem juros.
	// 2 Transação parcelada pelo emissor do cartão, ou seja, com juros.

	public enum InstallmentType {

			
		None(0), Merchant(1), Issuer(2);

		private int installmentType;

		private InstallmentType(int installmentType) {
			this.installmentType = installmentType;
		}

		public int getInstallmentType() {
			return installmentType;
		}

		public static InstallmentType of(int installmentType) {
			return Stream.of(InstallmentType.values()).filter(p -> p.getInstallmentType() == installmentType)
					.findFirst().orElseThrow(IllegalArgumentException::new);
		}
	}

	@Basic
	private int installmentTypeValue;

	@Transient
	private InstallmentType installmentType;


	


	@JsonProperty("isApproved")
	private Boolean isApproved;

	@JsonProperty("installmentNumber")
	private Integer installmentNumber;

	@JsonProperty("softDescriptor")
	private String softDescriptor;

	@JsonProperty("isRecurrency")
	private Boolean isRecurrency;

	@JsonProperty("isCanceled")
	private Boolean isCanceled;

	@JsonProperty("merchantTransactionId")
	@Column(name = "MERCHANT_TRANSACTION_ID")
	private String merchantTransactionId;

	@JsonProperty("errorCode")
	private String errorCode;

	@JsonProperty("acquirerErrorCode")
	private String acquirerErrorCode;

	@JsonProperty("acquirerErrorMessage")
	private String acquirerErrorMessage;

	@JsonProperty("authorizationResponseCode")
	private String authorizationResponseCode;

	@JsonProperty("acquirerTransactionId")
	private String acquirerTransactionId;

	@JsonProperty("bankSlipStatus")
	private Integer bankSlipStatus; // boleto

	@JsonProperty("cancellations")
	@Type(JsonJsonNode.class)
	@Column(name = "cancellations")
	@ColumnDefault("'{}'") 
	private JsonNode cancellations; // fora do help

	
	@JsonProperty("issuerInstallment")
	@Type(JsonJsonNode.class)
	@Column(name = "issuerInstallment")
	@ColumnDefault("'{}'") 
	private JsonNode issuerInstallment; // fora do help

	// ----------------------------------------

	
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "CARD_ID", referencedColumnName = "smartid")
	private PersonCard card;
	
	
	
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
	@JoinColumn(name = "SHOPPING_CART_UUID")
	@JsonIgnore
	private ShoppingCart shoppingCart;
	//-----------------------------------------
	

	public int getTransactionStatusValue() {
		return transactionStatusValue;
	}

	public void setTransactionStatusValue(int transactionStatusValue) {
		this.transactionStatusValue = transactionStatusValue;
	}

	public TransactionStatus getTransactionStatusTransient() {
		return transactionStatusTransient;
	}

	public void setTransactionStatusTransient(TransactionStatus transactionStatusTransient) {
		this.transactionStatusTransient = transactionStatusTransient;
	}

	public int getPaymentTypeValue() {
		return paymentTypeValue;
	}

	public void setPaymentTypeValue(int paymentTypeValue) {
		this.paymentTypeValue = paymentTypeValue;
	}

	public PaymentType getPaymentTypeTransient() {
		return paymentTypeTransient;
	}

	public void setPaymentTypeTransient(PaymentType paymentTypeTransient) {
		this.paymentTypeTransient = paymentTypeTransient;
	}

	public Integer getSmartid() {
		return smartid;
	}

	public void setSmartid(Integer smartid) {
		this.smartid = smartid;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getAditumNumber() {
		return aditumNumber;
	}

	public void setAditumNumber(String aditumNumber) {
		this.aditumNumber = aditumNumber;
	}

	public String getQrCode() {
		return qrCode;
	}

	public void setQrCode(String qrCode) {
		this.qrCode = qrCode;
	}

	public String getQrCodeBase64() {
		return qrCodeBase64;
	}

	public void setQrCodeBase64(String qrCodeBase64) {
		this.qrCodeBase64 = qrCodeBase64;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public String getAcquirer() {
		return acquirer;
	}

	public void setAcquirer(String acquirer) {
		this.acquirer = acquirer;
	}

	public String getCreationDateTime() {
		return creationDateTime;
	}

	public void setCreationDateTime(String creationDateTime) {
		this.creationDateTime = creationDateTime;
	}

	public String getBankIssuerId() {
		return bankIssuerId;
	}

	public void setBankIssuerId(String bankIssuerId) {
		this.bankIssuerId = bankIssuerId;
	}

	public int getInstallmentTypeValue() {
		return installmentTypeValue;
	}

	public void setInstallmentTypeValue(int installmentTypeValue) {
		this.installmentTypeValue = installmentTypeValue;
	}

		
	public Boolean getIsApproved() {
		return isApproved;
	}

	public void setIsApproved(Boolean isApproved) {
		this.isApproved = isApproved;
	}

	public Integer getInstallmentNumber() {
		return installmentNumber;
	}

	public void setInstallmentNumber(Integer installmentNumber) {
		this.installmentNumber = installmentNumber;
	}

	public String getSoftDescriptor() {
		return softDescriptor;
	}

	public void setSoftDescriptor(String softDescriptor) {
		this.softDescriptor = softDescriptor;
	}

	public Boolean getIsRecurrency() {
		return isRecurrency;
	}

	public void setIsRecurrency(Boolean isRecurrency) {
		this.isRecurrency = isRecurrency;
	}

	public Boolean getIsCanceled() {
		return isCanceled;
	}

	public void setIsCanceled(Boolean isCanceled) {
		this.isCanceled = isCanceled;
	}

	public String getMerchantTransactionId() {
		return merchantTransactionId;
	}

	public void setMerchantTransactionId(String merchantTransactionId) {
		this.merchantTransactionId = merchantTransactionId;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getAcquirerErrorCode() {
		return acquirerErrorCode;
	}

	public void setAcquirerErrorCode(String acquirerErrorCode) {
		this.acquirerErrorCode = acquirerErrorCode;
	}

	public String getAcquirerErrorMessage() {
		return acquirerErrorMessage;
	}

	public void setAcquirerErrorMessage(String acquirerErrorMessage) {
		this.acquirerErrorMessage = acquirerErrorMessage;
	}

	public String getAuthorizationResponseCode() {
		return authorizationResponseCode;
	}

	public void setAuthorizationResponseCode(String authorizationResponseCode) {
		this.authorizationResponseCode = authorizationResponseCode;
	}

	public String getAcquirerTransactionId() {
		return acquirerTransactionId;
	}

	public void setAcquirerTransactionId(String acquirerTransactionId) {
		this.acquirerTransactionId = acquirerTransactionId;
	}

	public Integer getBankSlipStatus() {
		return bankSlipStatus;
	}

	public void setBankSlipStatus(Integer bankSlipStatus) {
		this.bankSlipStatus = bankSlipStatus;
	}

	public JsonNode getCancellations() {
		return cancellations;
	}

	public void setCancellations(JsonNode cancellations) {
		this.cancellations = cancellations;
	}

	public JsonNode getIssuerInstallment() {
		return issuerInstallment;
	}

	public void setIssuerInstallment(JsonNode issuerInstallment) {
		this.issuerInstallment = issuerInstallment;
	}
	
	public PersonCard getCard() {
		return card;
	}

	public void setCard(PersonCard card) {
		this.card = card;
	}
	
	public int getTransactionStatus() {
		return transactionStatusTransient.transactionStatus;
	}

	public int getPaymentType() {
		return paymentTypeTransient.paymentType;
	}

	public SafraPayCharge getCharge() {
		return charge;
	}

	public void setCharge(SafraPayCharge charge) {
		this.charge = charge;
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
		SafraPayTransaction other = (SafraPayTransaction) obj;
		return Objects.equals(smartid, other.smartid);
	}

	public ShoppingCart getShoppingCart() {
		return shoppingCart;
	}

	public void setShoppingCart(ShoppingCart shoppingCart) {
		this.shoppingCart = shoppingCart;
	}



}