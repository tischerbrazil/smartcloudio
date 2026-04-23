package br.bancodobrasil.model;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import org.hibernate.annotations.Type;
import org.locationtech.jts.geom.Geometry;
import org.geoazul.ecommerce.view.shopping.ShoppingCart;
import org.geolatte.geom.G2D;
import org.geolatte.geom.LineString;
import org.geolatte.geom.Point;
import com.erp.modules.commonClasses.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;

@Entity
@Table(name = "gateway_bancobrasil_pix")
@JsonFilter("bancoBrasilPixFilter")
@NamedQueries({
		@NamedQuery(name = "BancoBrasilPixPayment.getPixById", query = "select c from BancoBrasilPixPayment c where c.id = :id"),
		@NamedQuery(name = "BancoBrasilPixPayment.getPixByTxid", query = "select c from BancoBrasilPixPayment c where c.txid = :txid"),
		@NamedQuery(name = "BancoBrasilPixPayment.getPixByShoppingCartId", query = "select c from BancoBrasilPixPayment c where c.shoppingCart.id = :shopping_cart_id AND c.criacao > :datevalidate"),
 })
public class BancoBrasilPixPayment extends BaseEntity {
	
	
	
	private static final long serialVersionUID = 1L;

	public static final String PIXPAYMENT_ID_GET = "BancoBrasilPixPayment.getPixById";
	public static final String PIXPAYMENT_TXID_GET = "BancoBrasilPixPayment.getPixByTxid";
	public static final String PIXPAYMENT_SHOPPING_CART_ID = "BancoBrasilPixPayment.getPixByShoppingCartId";
	
	public BancoBrasilPixPayment() {
		this.txid = UUID.randomUUID();
	}

	public BancoBrasilPixPayment(BancoBrasilCalendario calendario, ShoppingCart shoppingCart,
			Map<String, String> devedor, BancoBrasilValor valor, String chave, String solicitacaoPagador) {
		super();
		this.txid = UUID.randomUUID();
		this.calendario = calendario;
		this.setShoppingCart(shoppingCart);
		this.devedor = devedor;
		this.valor = valor;
		this.chave = chave;
		this.solicitacaoPagador = solicitacaoPagador;
	}
	

	
	
	
	
	

	@JoinColumn(name = "UUID")
	@JsonIgnore
	private UUID txid;

	@Type(JsonBancoBrasilCalendario.class)
	@Column(name = "CALENDARIO")
	private BancoBrasilCalendario calendario;

	@Column(name = "STATUS")
	private String status;

	@Column(name = "LOCATION")
	private String location;

	@Column(name = "TEXTO_IMAGEM_QRCODE")
	private String textoImagemQRcode;

	@Type(JsonMapStringString.class)
	@Column(name = "DEVEDOR")
	private Map<String, String> devedor = new HashMap<>();

	@Type(JsonBancoBrasilValor.class)
	@Column(name = "VALOR")
	private BancoBrasilValor valor;

	@Column(name = "REVISAO")
	private Integer revisao;

	@Column(name = "CHAVE")
	private String chave;

	@Column(name = "SOLICITACAO_PAGADOR")
	private String solicitacaoPagador;

	@Column(name = "CRIACAO")
	private LocalDateTime criacao;

	public UUID getTxid() {
		return txid;
	}
	
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
	@JoinColumn(name = "SHOPPING_CART_ID")
	@JsonIgnore
	private ShoppingCart shoppingCart;
	
	
 
	
	public void setTxid(UUID txid) {
		this.txid = txid;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getTextoImagemQRcode() {
		return textoImagemQRcode;
	}

	public void setTextoImagemQRcode(String textoImagemQRcode) {
		this.textoImagemQRcode = textoImagemQRcode;
	}

	public Integer getRevisao() {
		return revisao;
	}

	public void setRevisao(Integer revisao) {
		this.revisao = revisao;
	}

	public String getChave() {
		return chave;
	}

	public void setChave(String chave) {
		this.chave = chave;
	}

	public String getSolicitacaoPagador() {
		return solicitacaoPagador;
	}

	public void setSolicitacaoPagador(String solicitacaoPagador) {
		this.solicitacaoPagador = solicitacaoPagador;
	}

	public BancoBrasilCalendario getCalendario() {
		return calendario;
	}

	public void setCalendario(BancoBrasilCalendario calendario) {
		this.calendario = calendario;
	}
	
	public Map<String, String> getDevedor() {
		return devedor;
	}

	public void setDevedor(Map<String, String> devedor) {
		this.devedor = devedor;
	}

	public BancoBrasilValor getValor() {
		return valor;
	}

	public void setValor(BancoBrasilValor valor) {
		this.valor = valor;
	}

	public LocalDateTime getCriacao() {
		return criacao;
	}

	public void setCriacao(LocalDateTime criacao) {
		this.criacao = criacao;
	}

	

	public ShoppingCart getShoppingCart() {
		return shoppingCart;
	}

	public void setShoppingCart(ShoppingCart shoppingCart) {
		this.shoppingCart = shoppingCart;
	}

	
	

}