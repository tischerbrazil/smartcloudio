package org.geoazul.ecommerce.view.shopping;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ValueChangeEvent;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.servlet.http.HttpServletRequest; 
import jakarta.transaction.Transactional;
import org.example.kickoff.business.exception.EntityNotFoundException;
import org.geoazul.ecommerce.model.InstallmentMethod;
import org.geoazul.ecommerce.view.shopping.ShoppingCart.Status;
import org.geoazul.erp.Cities;
import org.geoazul.erp.Countries;
import org.geoazul.erp.States;
import org.example.kickoff.view.ActiveLocale;
import org.example.kickoff.view.ActiveUser;
import org.geoazul.model.Address;
import org.geoazul.model.security.ClientComponentEntity;
import org.geoazul.model.security.ClientOAuthEntity;
import org.example.kickoff.model.Person;
import org.hibernate.annotations.ColumnDefault;
import org.keycloak.example.oauth.UserData;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Faces;
import org.primefaces.event.SelectEvent;
import com.erp.modules.inventory.entities.Product;
import com.erp.modules.promocode.entities.PromoCode;
import com.erp.modules.shippingmethods.entities.ShippingMethod;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import static modules.LoadInitParameter.save_FILE_PATH;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.imageio.ImageIO;
import br.bancodobrasil.BBService;
import br.bancodobrasil.model.BancoBrasilCalendario;
import br.bancodobrasil.model.BancoBrasilPixPayment;
import br.bancodobrasil.model.BancoBrasilValor;
import br.com.correios.webservice.CorreiosService;
import br.safrapay.api.model.PersonCard;
import br.safrapay.api.model.SafraPayCharge;
import br.safrapay.api.model.SafraPayCustomer;
import br.safrapay.api.model.SafraPayCustomerPhone;
import br.safrapay.api.model.SafraPayTransaction;
import br.safrapay.api.model.SafraPayTransaction.PaymentType;
import br.user.service.UserService;
import br.safrapay.api.model.SafraPayService;
import jsonb.JacksonUtil;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DecimalFormat;

/**
 * @author Laercio Tischer
 * @release: 6.0.5
 */

@Named
@ViewScoped
public class ShoppingCartBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Transactional
	public void sendMessage(String messageJ) {
		// String[] splitter = messageJ.split(",");

		String valX = "index.xhtml";
		String valY = "0123456789";

		String retorno = "{\"index\":" + valX + ", \"value\":" + valY + "}";
		// this.sendPushMessage(retorno);
	}

	private boolean aceite;

	public void retrieve() {
		
		
		
		FacesContext context = FacesContext.getCurrentInstance();
		if (context.isPostback()) {
			return;
		}
		
		if (this.uuid == null) {
			throw new EntityNotFoundException();
		}
		
		if (activeUser.isPresent()) {
			this.user = entityManager.find(Person.class, activeUser.get().getId());
			if (this.user.getAddress().getId() != null) {
				this.address = this.user.getAddress();
			} else {
				this.address = this.exaddress;
			}
			
			// ------------------------------------------
		

				List<Address> enderecoEntrega = user.getAddresses().stream().collect(Collectors.toList());
				if (enderecoEntrega.size() > 0) {
					shippingAddress = enderecoEntrega.get(0);
				}
				

		
		} 
		
				
		cartItems = findShoppingItens(uuid, Status.REQUEST);
					
		
		
		
		//-----------
		

		// ------------------------------------------
		try {

			List<Address> enderecoEntrega = user.getAddresses().stream().collect(Collectors.toList());
			if (enderecoEntrega.size() > 0) {
				shippingAddress = enderecoEntrega.get(0);
			}

		} catch (Exception ex5) {
		}


		//INICIO DE PAY E DELIVERY
		
		
			
		
			payInit();
			
			installmentCalculate();
				
			
		
		
		
	}
	
	
	private String viewId;
			
	
	
	
	@PostConstruct
	public void init() {
	
		FacesContext context = FacesContext.getCurrentInstance();
		if (context.isPostback()) {
			return;
		}
		this.viewId = FacesContext.getCurrentInstance().getViewRoot().getViewId();
		String viewId = FacesContext.getCurrentInstance().getViewRoot().getViewId();
		
		
		
		
		if (activeUser.isPresent()) {
			this.user = entityManager.find(Person.class, activeUser.get().getId());
			if (this.user.getAddress().getId() != null) {
				this.address = this.user.getAddress();
			} else {
				this.address = this.exaddress;
			}
			
			// ------------------------------------------
		

				List<Address> enderecoEntrega = user.getAddresses().stream().collect(Collectors.toList());
				if (enderecoEntrega.size() > 0) {
					shippingAddress = enderecoEntrega.get(0);
				}
				

		
		} 
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		//----------------------------------------------------------------------------------------
		
		
		
		if (!viewId.equals("/application/pay.xhtml")) {
		
		Optional<String> cookie_uuid = readCookie("cart_uuid");

		if (!cookie_uuid.isEmpty() && !isFacesEvent(request)) {
			my_cookie_uuid = cookie_uuid.get();
			if (cartItems == null) {
				cartItems = new ArrayList<>();
			}
			try {
				
					cartItems = findShoppingItens(my_cookie_uuid, Status.DRAFT);
				
				

				// set my_cookie_uuid

			} catch (Exception ignore) {

			}
			clientComponent();
             payInit();
			installmentCalculate();
		}
		
		
		
		
		String url = context.getExternalContext().getRequestContextPath();

		if (this.shoppingCartIsEmpty()) {
			try {
				context.getExternalContext().redirect(url + "/ecommerce/empty_cart.xhtml?gcmid=155");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		//-----------
		
		
		

		if (viewId.equals("/application/pay.xhtml")) {
			payInit();
			
			installmentCalculate();
				
			
		} else{
		}

		
		
		
		
		if (viewId.equals("/application/delivery.xhtml")) {
			teste();
		}else {
		}
		
		
		}
		
		
		
		
	}
	
	
	
	
	public void payInit() {
		
		safraPayCard = new PersonCard();
		
	
		TypedQuery<InstallmentMethod> queryCode;

		queryCode = entityManager.createNamedQuery(InstallmentMethod.INSTALLMENT_METHOD_ALL, InstallmentMethod.class);

		InstallmentMethod sInstallment = queryCode.getResultStream().findFirst().get();

		BigDecimal totaVal = getTotalWithShipping();
		BigDecimal fessVal = sInstallment.getFees();

		// =========

		Integer parcelNu = sInstallment.getInstallmentNumber();

		BigDecimal b2 = fessVal.divide(new BigDecimal(100));

		BigDecimal b3 = totaVal.multiply(b2);

		BigDecimal b4 = totaVal.add(b3).setScale(2, RoundingMode.HALF_UP);

		BigDecimal b5 = b4.divide(BigDecimal.valueOf(parcelNu), 2, RoundingMode.HALF_UP);

		// =============

		sInstallment.setValueTotal(b4);

		sInstallment.setParcelTotal(b5);

		safraPayCard.setInstallmentMethod(sInstallment);
		// =============
		
			
		

		Optional<BancoBrasilPixPayment> pixIsRegister = 
				findBBPix(shoppingCart.getId());
		if (pixIsRegister.isPresent()) {

			String fileUrl = "/files/" + userData.getRealmEntity() + "/pix/" + shoppingCart.getUuid().toString()
					+ ".png";

			// timeOut

			LocalDateTime toDateTime = LocalDateTime.now();
			LocalDateTime fromDateTime = pixIsRegister.get().getCriacao();
			Integer timeSeconds = getTime(fromDateTime, toDateTime);
			Integer timeExpMilis = pixIsRegister.get().getCalendario().getExpiracao();
			timeExpMilis = timeExpMilis / 1000;
			
			timeOut = timeExpMilis - timeSeconds;
			

			this.pixImage = fileUrl;
			this.pixCopyPaste = pixIsRegister.get().getTextoImagemQRcode();
			this.isValidPixe = true;
		} else {
			this.isValidPixe = false; 
		}
		
	}

	private static Period getPeriod(LocalDateTime dob, LocalDateTime now) {
		return Period.between(dob.toLocalDate(), now.toLocalDate());
	}

	private static Integer getTime(LocalDateTime dob, LocalDateTime now) {
		LocalDateTime today = LocalDateTime.of(now.getYear(), now.getMonthValue(), now.getDayOfMonth(), dob.getHour(),
				dob.getMinute(), dob.getSecond());
		Duration duration = Duration.between(today, now);

		Integer seconds = (int) duration.getSeconds();

		return seconds;
	}

	private Integer timeOut;

	public Integer getTimeOut() {
		return timeOut;
	}

	public void setTimeOut(Integer timeOut) {
		this.timeOut = timeOut;
	}

	private String promoCode;

	public String getPromoCode() {
		return promoCode;
	}

	public void setPromoCode(String promoCode) {
		this.promoCode = promoCode;
	}

	public boolean isAceite() {
		return aceite;
	}

	public void setAceite(boolean aceite) {
		this.aceite = aceite;
	}


	@Transactional
	public String updateProcess() {
		try {
			TypedQuery<PromoCode> queryCode = entityManager.createNamedQuery(PromoCode.PROMO_CODE, PromoCode.class);
			queryCode.setParameter("code", promoCode);

			long PromoCodeFind = queryCode.getResultStream().count();

			if (PromoCodeFind > 0) {
				// Vamos usar o codigo
				Optional<PromoCode> myPromoCode = Optional
						.ofNullable(queryCode.getResultStream().findFirst().orElse(null));
				PromoCode findCode = myPromoCode.get();
				findCode.setUsed(true);

				entityManager.merge(findCode);
				entityManager.flush();

			} else {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage("Código não é válido", "Verifique se digitou corretamente!"));
				return "delivery?faces-redirect=true";
			}

		} catch (Exception ex) {
			// ex.printStackTrace();
		}

		this.setStepId(4);

		return "checkout3?faces-redirect=true";

	}

	// ------------------------------------------
	private ClientComponentEntity client;

	public void clientComponent() {

		TypedQuery<ClientComponentEntity> queryApp = entityManager
				.createNamedQuery(ClientComponentEntity.COMPONENT_CLIENT_ID, ClientComponentEntity.class);
		queryApp.setParameter("clientId", "mercadopago");
		List<ClientComponentEntity> clientCompEntity = queryApp.getResultList();

		if (clientCompEntity.size() > 0) {

			client = clientCompEntity.get(0);
		}

	}

	@PreDestroy
	public void preDestroy() {
	}

	@Inject
	private ActiveLocale activeLocale;

	@Inject
	private HttpServletRequest request;

	@Inject
	private EntityManager entityManager;

	@Inject
	private CorreiosService correiosService;

	@Inject
	private SafraPayService safraPayService;

	@Inject
	private BBService bBService;

	@Inject
	private UserService userService;

	@Inject
	private UserData userData;

	public Person ActiveUser() {

		

			TypedQuery<Person> queryUser = entityManager.createNamedQuery(Person.USER_UUID_GET,
					Person.class);
			queryUser.setParameter("uuid", UUID.fromString(request.getUserPrincipal().getName()));
			List<Person> usersListRet = queryUser.getResultList();

			if (usersListRet.size() > 0) {

				Optional<List<Address>> enderecos = Optional.ofNullable(usersListRet.get(0).getAddresses());

				if (enderecos.get().size() > 0) {

					try {
						List<Address> enderecoFixo = enderecos.get().stream().collect(Collectors.toList());
						address = enderecoFixo.get(0);

						Optional<Countries> countrySelect = Optional.ofNullable(address.getCountry());

						if (!countrySelect.isEmpty()) {

							TypedQuery<States> query = entityManager.createNamedQuery(States.FIND_BY_COUNTRY,
									States.class);
							query.setParameter("country", countrySelect.get().getId());
							states = query.getResultList();

						}

						Optional<States> stateSelect = Optional.ofNullable(address.getState());

						if (!stateSelect.isEmpty()) {

							TypedQuery<Cities> query = entityManager.createNamedQuery(Cities.FIND_BY_STATE,
									Cities.class);
							query.setParameter("state", stateSelect.get().getId());
							cities = query.getResultList();

						}

					} catch (Exception ex) {

					}

				} else {
					address = new Address();
					if (activeLocale.getLanguageTag().equals("pt")) {
						Optional<Countries> existCountryNative = Optional
								.ofNullable(entityManager.find(Countries.class, 31));
						if (!existCountryNative.isEmpty()) {
							address.setCountry(existCountryNative.get());
						}
					}
				}
				Optional<String> phoneNumber = Optional.ofNullable(usersListRet.get(0).getPhone());

				if (phoneNumber.isEmpty()) {
					usersListRet.get(0).setPhone(" "); // FIXME
				}

				if (!phoneNumber.isPresent()) {
					usersListRet.get(0).setPhone(" "); // FIXME
				}

				return usersListRet.get(0);
			}

		

		return null;
	}

	
	@Inject
	private ActiveUser activeUser;
	
	//public Person getActiveUser() {
		
		

		//try {

		//	TypedQuery<Person> queryUser = entityManager.createNamedQuery(Person.USER_UUID_GET,
		//			Person.class);
		//	queryUser.setParameter("uuid", activeUser.get().getUuid());
		//	Person usersListRet = queryUser.getSingleResult();
		//	return usersListRet;
		//} catch (Exception ex) {

		//}
		//return null;
	//}

	
	

	
	private Address addressSelect;
	private List<Address> addressAll;

	public List<Address> getAddressAll() {
		return addressAll;
	}

	public void setAddressAll(List<Address> addressAll) {
		this.addressAll = addressAll;
	}

	public Address getAddressSelect() {
		return addressSelect;
	}

	public void setAddressSelect(Address addressSelect) {
		this.addressSelect = addressSelect;
	}

	public void onRowSelect(SelectEvent<Address> event) {

		try {
		} catch (Exception ex) {
		}




	}

	private String uuidTemp;

	@Transactional
	public List<ShoppingCartItem> findShoppingItens(String uuid, 
			org.geoazul.ecommerce.view.shopping.ShoppingCart.Status status) {

		

		try {

		

			TypedQuery<ShoppingCart> shoppingCartDraft = entityManager
					.createNamedQuery(ShoppingCart.FIND_BY_UUID_AND_STATUS, ShoppingCart.class);
			shoppingCartDraft.setParameter("uuid", UUID.fromString(uuid));
			shoppingCartDraft.setParameter("status", status);


			List<ShoppingCart> shoppingCarts = shoppingCartDraft.getResultList();
			if (shoppingCarts.size() > 0) {
				this.shoppingCart = shoppingCarts.get(0);
				return this.shoppingCart.getShoppingCartItens();

			} else {
				this.shoppingCart = new ShoppingCart();
				return new ArrayList<ShoppingCartItem>();
			}

			// shoppingCart = shoppingCartDraft.getSingleResult();
			// return shoppingCart.getShoppingCartItens();

		} catch (Exception ex) {

			ex.printStackTrace();

		}
		return null;
	}

	public ShoppingCart findShoppingCart(String uuid, org.geoazul.ecommerce.view.shopping.ShoppingCart.Status status ) {
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


	private Address exaddress = new Address();
	
	
	

	@Transactional
	public void teste() {

		Double pesoDouble = 0D;

		// Sum up the quantities
		for (ShoppingCartItem cartItem : cartItems) {

			Integer volume = cartItem.getItem().getWeight();
			// FP = (CF + CV) MKP
			// volume

			Double fretePeso = 0D;
			Double custoFixo = 0D;
			Double custoVariavel = 0D;

			Float frete44 = 0f;

			try {
				if (!volume.equals(null)) {
					pesoDouble = (pesoDouble + volume) * cartItem.getQuantity();
				}
			} catch (Exception ex) {
				// ex.printStackTrace();
			}


		}

		// entrega minima
		// if (pesoDouble < 20) {
		// pesoDouble = 20f;
		// }
		Integer distancia = 1;

		if (pesoDouble < 100) { // VAMOS ENTREGAR DE CORREIOS

			// Milho Hibrido AG-1051 Seminis 5 kg

			// return 20D;

		} else if (pesoDouble > 20 || pesoDouble < 120) {// VAMOS LEVAR DE CARRO
			// return pesoDouble * 0.5 * distancia;
		} else if (pesoDouble > 120 || pesoDouble < 500) {// VAMOS LEVAR DE CARRO
			// return pesoDouble * 0.4 * distancia;
		} else if (pesoDouble > 500) {// VAMOS LEVAR DE CAMINHAO
			// return pesoDouble * 0.3 * distancia;
		}


		Query allShippingMethod = entityManager.createNamedQuery(ShippingMethod.SHIPING_METHOD_ALL);
		// allShippingMethod.setParameter("client", "correios");
		List<ShippingMethod> sMethods = allShippingMethod.getResultList();


		shippingMethods = new ArrayList<ShippingMethod>();
		for (ShippingMethod sMethod : sMethods) {

			try {


				if (sMethod.getClientEntity().getClientId().equals("correios")) {

					String valorNew = new DecimalFormat("#").format(pesoDouble);
					String myCep;
					if (this.user.getIsSameAddress()) {
						myCep = this.user.getZipcode();
					} else {
						myCep = this.user.getAddress().getZipcode();
					}

					
					String gambiarra = "76900041";

					if (myCep.replace("-", "").equals(gambiarra)) {
						gambiarra = "76908678";
					}

					JsonNode calcShipping = correiosService.calculaPreco(sMethod.getCode(), gambiarra.replace("-", ""),
							myCep.replace("-", ""), valorNew);

					String pcBaseGeral = calcShipping.get("pcBaseGeral").textValue();
					Double xFrete = Double.valueOf(pcBaseGeral.replace(",", "."));

					sMethod.setShippingValue(xFrete);

					// --------------

					String dtEvento = "16-01-2023";
					JsonNode calcPrazo = correiosService.calculaPrazo(sMethod.getCode(), gambiarra.replace("-", ""),
							myCep.replace("-", ""), dtEvento);

					Integer prazoEntrega = calcPrazo.get("prazoEntrega").intValue();

					sMethod.setDeliveryTime(prazoEntrega);

					shippingMethods.add(sMethod);
				} else {
					sMethod.setShippingValue(0D);
					shippingMethods.add(sMethod);
				}
			} catch (Exception ex) {
				//ex.printStackTrace();

			}
			
			for (ShippingMethod dd : shippingMethods) {
			}

			
		}
		// shippingMethods = sMethods;


	}

	@Transactional
	public void installmentCalculate() {

		Query allInstallmentMethods = entityManager.createNamedQuery(InstallmentMethod.INSTALLMENT_METHOD_ALL);

		List<InstallmentMethod> installments = allInstallmentMethods.getResultList();


		installmentMethods = new ArrayList<InstallmentMethod>();

		for (InstallmentMethod sInstallment : installments) {

			BigDecimal totaVal = getTotalWithShipping();
			BigDecimal fessVal = sInstallment.getFees();

			// =========

			Integer parcelNu = sInstallment.getInstallmentNumber();

			BigDecimal b2 = fessVal.divide(new BigDecimal(100));

			BigDecimal b3 = totaVal.multiply(b2);

			BigDecimal b4 = totaVal.add(b3).setScale(2, RoundingMode.HALF_UP);

			
			BigDecimal b5 = b4.divide(BigDecimal.valueOf(parcelNu), 2, RoundingMode.HALF_UP);

			// =============

			sInstallment.setValueTotal(b4);

			sInstallment.setParcelTotal(b5);

			// =============

			installmentMethods.add(sInstallment);
		}


	}

	private Person user;

	public Person getUser() {
			return user;
	}

	public void setUser(Person user) {
		this.user = user;
	}
	

	@Transactional
	public String update() {
		FacesContext context = FacesContext.getCurrentInstance();
		String meuCep = "";
		try {

			meuCep = this.user.getZipcode().replace("-", "");
		} catch (Exception ex) {
		}

		try {

			if (this.user.getIsSameAddress() && !meuCep.isEmpty()) {



				JsonNode responstaCepJson =

						correiosService.cepData(meuCep);

				String sufUfCap = responstaCepJson.get("uf").asText();
				TypedQuery<States> query5 = entityManager.createNamedQuery(States.FIND_BY_COUNTRY_ACTIVES_BY_NAME,
						States.class);
				query5.setParameter("country", 31); // BRAZIL FIXME
				query5.setParameter("iso2", sufUfCap); // STATE FIXME
				long sizeStates = query5.getResultStream().count();
				if (sizeStates < 1) {


					context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "LAMENTAMOS",
							"Municícipio não atendido! Somente Rondônia e Acre!"));

					return "checkout?faces-redirect=true";
				}

			}
		} catch (Exception ex) {
			ex.printStackTrace();

			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERRO", "CEP INVÁLIDO!"));

			return "checkout?faces-redirect=true";

		}

		try {
			entityManager.merge(this.user);

			Optional<Integer> hasAddressId = Optional.ofNullable(user.getAddress().getId());
			if (hasAddressId.isPresent()) {
				this.address.setPerson(user);

				entityManager.merge(this.address);
			} else {
				TypedQuery<Address> query = entityManager.createNamedQuery(Address.ADDRESS_USERID_HOME, Address.class);
				query.setParameter("userid", this.user.getId());
				Optional<Address> hasAddressBD = 
						Optional.ofNullable(query.getResultStream().findFirst().orElse(null));

				if (hasAddressBD.isPresent()) {
					Address updateAddress = hasAddressBD.get();
					updateAddress.setCity(this.address.getCity());
					updateAddress.setCountry(this.address.getCountry());
					updateAddress.setNumber(this.address.getNumber());
					updateAddress.setState(this.address.getState());
					updateAddress.setStreet(this.address.getStreet());
					updateAddress.setNeighborhood(this.address.getNeighborhood());
					updateAddress.setComplement(this.address.getComplement());
					updateAddress.setZipcode(this.address.getZipcode());
					entityManager.merge(updateAddress);
				} else {
					this.address.setPerson(user);

					entityManager.persist(this.address);
				}

			}
			entityManager.flush();
		} catch (Exception ignore) {
			ignore.printStackTrace();
		}

		this.setStepId(1);
		this.shippingAddress = new Address();
		return "delivery?faces-redirect=true";

	}

	@Transactional
	public String updateShipping() {

		FacesContext context = FacesContext.getCurrentInstance();
		String meuCep = "";
		try {

			meuCep = this.shippingAddress.getZipcode().replace("-", "");
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		try {



			JsonNode responstaCepJson = correiosService.cepData(meuCep);

			String sufUfCap = responstaCepJson.get("uf").asText();
			TypedQuery<States> query5 = entityManager.createNamedQuery(States.FIND_BY_COUNTRY_ACTIVES_BY_NAME,
					States.class);
			query5.setParameter("country", 31); // BRAZIL FIXME
			query5.setParameter("iso2", sufUfCap); // STATE FIXME
			long sizeStates = query5.getResultStream().count();
			if (sizeStates < 1) {


				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "LAMENTAMOS",
						"Municícipio não atendido! Somente Rondônia e Acre!"));

				return "checkout_?faces-redirect=true";
			}

		} catch (Exception ex) {
			ex.printStackTrace();

			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERRO", "CEP INVÁLIDO!"));

			return "checkout_?faces-redirect=true";

		}


		Optional<Integer> hasShippingAddressId = null;
		try {
			hasShippingAddressId = Optional.ofNullable(user.getAddress().getId());
		} catch (Exception ex) {

		}

		try {

			if (hasShippingAddressId.isPresent()) {
				// this.shippingAddress.setPerson(user);
				// this.shippingAddress.setTypeaddress(AddressType.SHIPPING);
				entityManager.merge(this.shippingAddress);
			} else {
				TypedQuery<Address> query = entityManager.createNamedQuery(Address.ADDRESS_USERID_HOME, Address.class);
				query.setParameter("userid", this.user.getId());
				Optional<Address> hasShippingAddressBD = Optional
						.ofNullable(query.getResultStream().findFirst().orElse(null));

				if (hasShippingAddressBD.isPresent()) {
					Address updateShippingAddress = hasShippingAddressBD.get();
					// updateAddress.setCity(this.shippingAddress.getCity());

					updateShippingAddress.setNumber(this.shippingAddress.getNumber());
					updateShippingAddress.setState(this.shippingAddress.getState());
					updateShippingAddress.setStreet(this.address.getStreet());
					updateShippingAddress.setNeighborhood(this.address.getNeighborhood());
					updateShippingAddress.setComplement(this.address.getComplement());
					updateShippingAddress.setZipcode(this.shippingAddress.getZipcode());
					entityManager.merge(updateShippingAddress);
				} else {
					this.shippingAddress.setPerson(user);

					entityManager.persist(this.shippingAddress);
				}

			}
			entityManager.flush();
		} catch (Exception ignore) {
			ignore.printStackTrace();
		}

		this.setStepId(1);

		Iterator<FacesMessage> it = context.getMessages();
		while (it.hasNext()) {
			it.next();
			it.remove();
		}

		return "delivery?faces-redirect=true";

	}

	@Transactional
	public void fireSelection(ValueChangeEvent event) {

		this.setTypePerson(event.getNewValue().toString());

		if (event.getNewValue().equals(true)) {
			this.user.setIsCompany(true);
			updateCompanyTrue(this.user.getId());
		} else {
			this.user.setIsCompany(false);
			updateCompanyFalse(this.user.getId());
		}

	}

	@Transactional
	public void paymentChange(ValueChangeEvent event) {
		if (event.getNewValue().equals(true)) {
			this.shoppingCart.setPaymentMethod(true);
			updatePaymentTrue(this.shoppingCart.getId());
		} else {
			this.shoppingCart.setPaymentMethod(false);
			updatePaymentFalse(this.shoppingCart.getId());
		}
	}

	@Transactional
	public void updatePaymentTrue(Long id) {
		Query query = entityManager.createNamedQuery(ShoppingCart.PAYMENT_TRUE);
		query.setParameter("id", id);
		query.executeUpdate();
		entityManager.flush();
	}

	@Transactional
	public void updatePaymentFalse(Long id) {
		Query query = entityManager.createNamedQuery(ShoppingCart.PAYMENT_FALSE);
		query.setParameter("id", id);
		query.executeUpdate();
		entityManager.flush();
	}

	public void addMessage() {
		// String summary = this.user.getIsSameAddress() ? "Mesmo endereço de Entrega "
		// : "Insira Endereço de Entrega";
		// FacesContext.getCurrentInstance().addMessage(null, new
		// FacesMessage(summary));

		if (this.user.getIsSameAddress()) {
			String summary = "Mesmo endereço do cliente ";
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Entrega", summary));
		}
	}

    @ColumnDefault("true")
	private Boolean isLoad;

	public Boolean getIsLoad() {
		return isLoad;
	}

	public void setIsLoad(Boolean isLoad) {
		this.isLoad = isLoad;
	}

	@Transactional
	public void streetSelection(ValueChangeEvent event) {

		this.setStreetType(event.getNewValue().toString());

	}

	@Transactional
	public void updateCompanyTrue(Long userId) {
		Query query = entityManager.createNamedQuery(Person.USER_COMPANY_TRUE);
		query.setParameter("userid", userId);
		query.executeUpdate();
		entityManager.flush();
	}

	@Transactional
	public void updateCompanyFalse(Long userId) {
		Query query = entityManager.createNamedQuery(Person.USER_COMPANY_FALSE);
		query.setParameter("userid", userId);
		query.executeUpdate();
		entityManager.flush();
	}

	// In DEV mode
	@Transactional
	public String update1() {
		

		
		
	


		
		Double valueShip = this.shoppingCart.getShippingMethod().getShippingValue();
		ShippingMethod ShippingMethodChange =
				entityManager.find(ShippingMethod.class, this.shoppingCart.getShippingMethod().getId());

		
		
		this.shoppingCart.setShippingMethod(ShippingMethodChange);
											
		this.shoppingCart.setShippingValue(valueShip);
		shoppingCart.setStatus(Status.REQUEST);
	
		
		Person userLoad =
				entityManager.find(Person.class, this.user.getId());

		
		shoppingCart.setUser(userLoad);
		entityManager.merge(shoppingCart);
		
		entityManager.flush();
		
		
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		
		
		Map<String, Object> properties = new HashMap<>();
	
		properties.put("maxAge", CART_COOKIE_AGE_EXCLUDE);
		properties.put("path", "/");
		try {
			externalContext.addResponseCookie(CART_COOKIE_NAME, URLEncoder.encode(UUID.randomUUID().toString(), "UTF-8"), properties);
		} catch (UnsupportedEncodingException e) {
		}
		
		
		

		this.setStepId(2);

		return "pay?faces-redirect=true&uuid=" + shoppingCart.getUuid().toString();

	}
	
	private void removeCookie(String cookie_name, String value, int cookie_age) {
		  Faces.addResponseCookie("cart_uuid",value, cookie_age);
	        Faces.refresh();
	}	

	public static String apenasNumeros(String s) {
		if (s == null)
			return "";
		String result = "";
		for (char c : s.toCharArray()) {
			if (c >= '0' && c <= '9')
				result += c;
		}
		return result;
	}

	@Transactional
	public void createPix() {
		String qrCodeGIT = "";

		


			BancoBrasilPixPayment bancoBrasilPixPayment = new BancoBrasilPixPayment();

			Map<String, String> devedorValues = new HashMap<>();

			if (this.user.getIsCompany()) {
				devedorValues.put("cnpj", this.user.getCnpj().replaceAll("[^0-9]", ""));
			} else {
				devedorValues.put("cpf", this.user.getCpf().replaceAll("[^0-9]", ""));
			}
			devedorValues.put("nome", this.user.getName());

			bancoBrasilPixPayment.setDevedor(devedorValues);

			BancoBrasilCalendario bancoBrasilCalendario = new BancoBrasilCalendario();

			bancoBrasilCalendario.setExpiracao(180000);
			bancoBrasilPixPayment.setCalendario(bancoBrasilCalendario);

			BancoBrasilValor bancoBrasilValor = new BancoBrasilValor();

			
			bancoBrasilValor.setOriginal(getPixTotalWithShipping().toPlainString());
			bancoBrasilPixPayment.setValor(bancoBrasilValor);


			bancoBrasilPixPayment.setSolicitacaoPagador("Pagamento de Produtos Ecommerce");


			bancoBrasilPixPayment.setShoppingCart(entityManager.find(ShoppingCart.class, this.shoppingCart.getId()));
 
			bancoBrasilPixPayment.setCriacao(LocalDateTime.now());

			//bancoBrasilValor.setOriginal("0.01"); ////////////////////////

			entityManager.persist(bancoBrasilPixPayment);
			
			
			entityManager.flush();
			qrCodeGIT = bBService.testeApiBB(bancoBrasilPixPayment);

			LocalDateTime toDateTime = LocalDateTime.now();
			LocalDateTime fromDateTime = bancoBrasilPixPayment.getCriacao();
			Integer timeSeconds = getTime(fromDateTime, toDateTime);
			Integer timeExpMilis = bancoBrasilPixPayment.getCalendario().getExpiracao();
			timeExpMilis = timeExpMilis / 1000;
			timeOut = timeExpMilis - timeSeconds;
	

		String fileUrl = "/files/" + userData.getRealmEntity() + "/pix/" + shoppingCart.getUuid().toString() + ".png";

		gerarComZXing(save_FILE_PATH + fileUrl, qrCodeGIT);

		pixCopyPaste = qrCodeGIT;
		pixImage = fileUrl;
		this.isValidPixe = true;
		

	}

	private String pixImage;

	public String getPixImage() {
		return pixImage;
	}

	public void setPixImage(String pixImage) {
		this.pixImage = pixImage;
	}

	private String pixCopyPaste;

	public String getPixCopyPaste() {
		return pixCopyPaste;
	}

	public void setPixCopyPaste(String pixCopyPaste) {
		this.pixCopyPaste = pixCopyPaste;
	}

    @ColumnDefault("true")
	private Boolean isValidPixe;

	public Boolean getIsValidPixe() {
		return isValidPixe;
	}

	public void setIsValidPixe(Boolean isValidPixe) {
		this.isValidPixe = isValidPixe;
	}

	private static final String formatoQrCodeGerado = "png";

	private void gerarComZXing(String path, String texto) {
		try {
			File myFile = new File(path);
			Hashtable<EncodeHintType, ErrorCorrectionLevel> hintMap = new Hashtable<EncodeHintType, ErrorCorrectionLevel>();
			hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
			QRCodeWriter qrCodeWriter = new QRCodeWriter();
			BitMatrix byteMatrix = qrCodeWriter.encode(texto, BarcodeFormat.QR_CODE, 200, 200, hintMap);
			int CrunchifyWidth = byteMatrix.getWidth();
			BufferedImage image = new BufferedImage(CrunchifyWidth, CrunchifyWidth, BufferedImage.TYPE_INT_RGB);
			image.createGraphics();
			Graphics2D graphics = (Graphics2D) image.getGraphics();
			graphics.setColor(Color.WHITE);
			graphics.fillRect(0, 0, CrunchifyWidth, CrunchifyWidth);
			graphics.setColor(Color.BLACK);
			for (int i = 0; i < CrunchifyWidth; i++) {
				for (int j = 0; j < CrunchifyWidth; j++) {
					if (byteMatrix.get(i, j)) {
						graphics.fillRect(i, j, 1, 1);
					}
				}
			}
			ImageIO.write(image, formatoQrCodeGerado, myFile);
		} catch (WriterException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Optional<BancoBrasilPixPayment> findBBPix(Long id) {
		TypedQuery<BancoBrasilPixPayment> query = entityManager
				.createNamedQuery(BancoBrasilPixPayment.PIXPAYMENT_SHOPPING_CART_ID, BancoBrasilPixPayment.class);
		query.setParameter("shopping_cart_id", id);
		query.setParameter("datevalidate", LocalDateTime.now().minusMinutes(3));
		

		
		
		return Optional.ofNullable(query.getResultStream().findFirst().orElse(null));
	}

	public Optional<SafraPayCustomer> findCustomerSafra(Long id) {
		TypedQuery<SafraPayCustomer> query = entityManager.createNamedQuery(SafraPayCustomer.CUSTOMER_PARTNER_ID,
				SafraPayCustomer.class);
		query.setParameter("partner_id", id);
		return Optional.ofNullable(query.getResultStream().findFirst().orElse(null));
	}
	
	public BancoBrasilPixPayment findBBPixByTxid(UUID txid_uuid) {
		TypedQuery<BancoBrasilPixPayment> query = entityManager
				.createNamedQuery(BancoBrasilPixPayment.PIXPAYMENT_TXID_GET, BancoBrasilPixPayment.class);
		query.setParameter("txid", txid_uuid);
		return query.getResultStream().findFirst().get();
	}
	
	@Transactional
	public void beanMethodValidate() {
		String shopUUIDCapture = 
				FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap()
				.get("objectid");
		String pixUUIDCapture = 
				FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap()
				.get("numberid");
		
		
		ShoppingCart shoppingCartChange = 
				findShoppingCart(shopUUIDCapture, 
						org.geoazul.ecommerce.view.shopping.ShoppingCart.Status.REQUEST); 
		//FIXME
		if (shoppingCartChange != null) {
			shoppingCartChange.setStatus(org.geoazul.ecommerce.view.shopping.ShoppingCart.Status.PAIDOUT);
			shoppingCartChange.setStatus(Status.PAIDOUT);
			shoppingCartChange.setDatetime(LocalDateTime.now());
			entityManager.merge(shoppingCartChange);
			
		}
		
		BancoBrasilPixPayment bbPixPaymentChange = findBBPixByTxid(UUID.fromString(pixUUIDCapture));
		bbPixPaymentChange.setStatus("PAGO");
		entityManager.merge(bbPixPaymentChange);
		
		entityManager.flush();
		
		FacesContext context = FacesContext.getCurrentInstance();

		try {
			context.getExternalContext().redirect("/application/checkout-complete.xhtml");
		} catch (IOException ex) {
			
		}
		
	}

	@Transactional
	public void onTimeout() {


		this.isValidPixe = false;

		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_ERROR, "Pix Expirado", null));
	}

	@Transactional
	public String createPay() {
		
		TypedQuery<SafraPayCustomer> query = entityManager.createNamedQuery(SafraPayCustomer.CUSTOMER_PARTNER_ID,
				SafraPayCustomer.class);
		query.setParameter("partner_id", this.user.getId());
		SafraPayCustomer safraPayCustomer = new SafraPayCustomer();
		Optional<SafraPayCustomer> customerLoad = Optional.ofNullable(query.getResultStream().findFirst().orElse(null));

		if (customerLoad.isPresent()) {
			 safraPayCustomer = customerLoad.get();
		}else {
			 safraPayCustomer = new SafraPayCustomer();
		}
				
		if (this.user.getIsCompany()) {
			safraPayCustomer.setDocument(this.user.getCnpj().replaceAll("[^0-9]", ""));
			safraPayCustomer.setDocumentType(2);
			safraPayCustomer.setEntityType(2);
		} else {
			safraPayCustomer.setDocument(this.user.getCpf().replaceAll("[^0-9]", ""));
			safraPayCustomer.setDocumentType(1);
			safraPayCustomer.setEntityType(1);
		}

		safraPayCustomer.setEmail(this.user.getEmail());
		safraPayCustomer.setName(this.user.getName());
		
		
		safraPayCustomer.setPartner(this.ActiveUser());

		SafraPayCustomerPhone safraPayCustomerPhone = new SafraPayCustomerPhone();

		if (this.user.getIsCompany()) {
			String phone = apenasNumeros(this.user.getPhone());
			safraPayCustomerPhone.setType(2);
			safraPayCustomerPhone.setNumber(phone.substring(2, 10));
			safraPayCustomerPhone.setAreaCode(phone.substring(0, 2));

		} else {

			String mobile = apenasNumeros(this.user.getMobile());
			safraPayCustomerPhone.setType(5);

			safraPayCustomerPhone.setNumber(mobile.substring(2, 11));
			safraPayCustomerPhone.setAreaCode(mobile.substring(0, 2));
		}


		safraPayCustomerPhone.setCountryCode("55");

		safraPayCustomer.setPhone(safraPayCustomerPhone);
		

		if (customerLoad.isPresent()) {
			entityManager.merge(safraPayCustomer);
			entityManager.flush();
		}else {
			entityManager.persist(safraPayCustomer);
			entityManager.flush();
		}
		
 
		

/////		safraPayService.safraPayUpdateCustomer(safraPayCustomer,
		// safraPayCustomerPhone);

		SafraPayTransaction safraPayTransaction = new SafraPayTransaction();

		String valorString = getTotalWithShipping().toPlainString().replace(".", "");

		Integer valorInteger = Integer.parseInt(valorString);

		// safraPayTransaction.setAmount(valorInteger);
		
		BigDecimal valorTotal = this.safraPayCard.getInstallmentMethod().getValueTotal();
		valorTotal = valorTotal.multiply(new BigDecimal(100));
		safraPayTransaction.setAmount(valorTotal.intValue());

		safraPayTransaction.setPaymentTypeTransient(PaymentType.Credit);
		
		safraPayTransaction.setInstallmentNumber(this.safraPayCard.getInstallmentMethod().getInstallmentNumber());
		safraPayTransaction.setSoftDescriptor(null);

		safraPayTransaction.setShoppingCart(entityManager.find(ShoppingCart.class, this.shoppingCart.getId()));

		String mesString = safraPayCard.getValidade().substring(0, 2);
		String anoString = "20" + safraPayCard.getValidade().substring(3, 5);

		Integer mesInteger = Integer.parseInt(mesString);
		Integer anoInteger = Integer.parseInt(anoString);

		safraPayCard.setExpirationMonth(mesInteger);
		safraPayCard.setExpirationYear(anoInteger);
		safraPayCard.setCardNumber(safraPayCard.getCardNumber().replace(" ", ""));

		if (this.user.getIsCompany()) {
			safraPayCard.setCardholderDocument(this.user.getCnpj().replaceAll("[^0-9]", ""));
		} else {
			safraPayCard.setCardholderDocument(this.user.getCpf().replaceAll("[^0-9]", ""));

		}

		safraPayTransaction.setCard(safraPayCard);
		// -----------------

		SafraPayCharge safraPayCharge = new SafraPayCharge();

		safraPayCharge.setMerchantChargeId(this.shoppingCart.getUuid().toString());

		safraPayCharge.setSafraPayCustomer(safraPayCustomer);
		safraPayCharge.setSource(1);
		List<Map<String, String>> metadata = new ArrayList<Map<String, String>>();
		safraPayCharge.setMetadata(metadata);
		List<SafraPayTransaction> transactions = new ArrayList<SafraPayTransaction>();
		transactions.add(safraPayTransaction);
		safraPayCharge.setTransactions(transactions);
		String retorno = safraPayService.safraPayChargeCreditCard(safraPayCharge);

	
		if (retorno.equals("Aprovada")) {
			this.getShoppingCart().setStatus(Status.PAIDOUT);
			this.getShoppingCart().setDatetime(LocalDateTime.now());
			entityManager.merge(this.getShoppingCart());
			entityManager.flush();
			
			return "checkout-complete?faces-redirect=true";
		}else {
			
			return "checkout-error?faces-redirect=true&codigo=" + retorno;
		}
		
	}

	private PersonCard safraPayCard;

	private int stepId;

	public int getStepId() {
		return stepId;
	}

	public void setStepId(int stepId) {
		this.stepId = stepId;
	}

	private List<ShoppingCartItem> cartItems = new ArrayList<>();
	private Address address;
	private Address shippingAddress;
	private Address companyAddress = new Address();
	private String country = new String();

	@Inject
	private ActiveCookieUuid activeCookieUuid;

	
	private static boolean isFacesEvent(HttpServletRequest request) {
		return request.getParameter("jakarta.faces.behavior.event") != null
				|| request.getParameter("omnifaces.event") != null;
	}

	public Optional<String> readCookie(String key) {
		try {
			return Arrays.stream(request.getCookies()).filter(c -> key.equals(c.getName())).map(jakarta.servlet.http.Cookie::getValue)
					.findAny();
		} catch (Exception ignore) {
		}
		return Optional.empty();
	}

	private String my_cookie_uuid;

	private static final String CART_COOKIE_NAME = "cart_uuid";
	private static final int CART_COOKIE_AGE = -1; // Expires after 30 days
	private static final int CART_COOKIE_AGE_EXCLUDE = 0; // Exclude

	private static final String POSTAL_CODE_COOKIE_NAME = "postal_code";
	private static final int POSTAL_CODE_COOKIE_AGE = -1; // Newer Expires

	
	
	
	

	private ShoppingCart shoppingCart;

	public ShoppingCart getShoppingCart() {
		return shoppingCart;
	}

	public void setShoppingCart(ShoppingCart shoppingCart) {
		this.shoppingCart = shoppingCart;
	}

	private List<InstallmentMethod> installmentMethods;

	
	public List<InstallmentMethod> getInstallmentMethods() {
		return installmentMethods;
	}

	public void setInstallmentMethods(List<InstallmentMethod> installmentMethods) {
		this.installmentMethods = installmentMethods;
	}

	private List<ShippingMethod> shippingMethods;

	
	public List<ShippingMethod> getShippingMethods() {
		return shippingMethods;
	}

	public void setShippingMethods(List<ShippingMethod> shippingMethods) {
		this.shippingMethods = shippingMethods;
	}

	private ShippingMethod shippingMethod;

	public ShippingMethod getShippingMethod() {
		return shippingMethod;
	}

	public void setShippingMethod(ShippingMethod shippingMethod) {
		this.shippingMethod = shippingMethod;
	}
	
	

	@Transactional
	public String addItemToCart() {

	

		Long param11 =  Long.valueOf(FacesContext.getCurrentInstance().getExternalContext().
				getRequestParameterMap().get("id"));
		

		
		Long param22 =  Long.valueOf(FacesContext.getCurrentInstance().getExternalContext()
				.getRequestParameterMap().get("gcmid"));

	
		
		Optional<String> cookie_uuid = readCookie("cart_uuid");

		if (!(!cookie_uuid.isEmpty() && !isFacesEvent(request))) {
			
			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			String name = "cart_uuid";
			
			Map<String, Object> properties = new HashMap<>();
		
			properties.put("maxAge", 31536000);
			properties.put("path", "/");
			
			String cookieValue = UUID.randomUUID().toString();
			
			my_cookie_uuid = cookieValue;
			
			try {
				externalContext.addResponseCookie(name, URLEncoder.encode(cookieValue, "UTF-8"), properties);
			} catch (UnsupportedEncodingException e) {
			}
	
		}
				

		if (!cookie_uuid.isEmpty() && !isFacesEvent(request)) {
			my_cookie_uuid = cookie_uuid.get();
			// init();
			shoppingCart = findShoppingCart(my_cookie_uuid, org.geoazul.ecommerce.view.shopping.ShoppingCart.Status.DRAFT);

			if (shoppingCart == null) {
				shoppingCart = new ShoppingCart();
				shoppingCart.setUuid(UUID.fromString(my_cookie_uuid));
				entityManager.persist(shoppingCart);
				entityManager.flush();
				cartItems = new ArrayList<>();

			} else {
				cartItems = shoppingCart.getShoppingCartItens();
			}

			this.setUuidTemp(my_cookie_uuid);

		}

		// ----------------------

		try {
			// ITEM QUE VAMOS ADICIONAR NO CARRINHO
			
			Query queryModFilter = entityManager.createNamedQuery(Product.FIND_BY_ID);
			queryModFilter.setParameter("id", param11);
			
			Product item =  (Product) queryModFilter.getSingleResult();
			
			


			boolean itemFound = false;
			if (cartItems.size() > 0) {
				for (ShoppingCartItem cartItem : cartItems) {
					// If item already exists in the shopping cart we just change the quantity
					if (cartItem.getItem().getId().equals(item.getId())) {
						cartItem.setQuantity(cartItem.getQuantity() + 1);
						entityManager.merge(cartItem);
						entityManager.flush();
						itemFound = true;
					}
				}
			} 

			if (!itemFound) {
				ShoppingCartItem addShopItem = new ShoppingCartItem(item, 1);
				addShopItem.setShoppingCart(shoppingCart);


				entityManager.persist(addShopItem);

				entityManager.flush();
				cartItems.add(addShopItem);
				try {
					my_cookie_uuid = cookie_uuid.get();
				} catch (Exception dd) {
				}



			}
			return "/ecommerce/index.xhtml?faces-redirect=true&gcmid=" + getParamUuid("gcmid_redirect");

		} catch (Exception e) {
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage("OPS...", "Um erro inesperado ocorrreu! Tente novamente."));
			return "ecommerce/index";
		}
	}

	@Transactional
	public void removeItemFromCart() {
		try {
			ShoppingCartItem item = entityManager.find(ShoppingCartItem.class, getParamId("itemId").longValue());
			cartItems.remove(item);
			entityManager.remove(item);
			entityManager.flush();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("SUCESSO", "ITEM REMOVIDO!"));
		} catch (Exception e) {
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("ERRO", "ERRO INESPERADO 8811!"));
		}
	}

	public String updateQuantity() {
		return null;
	}

	@Transactional
	public void refreshCantidad(ShoppingCartItem item) {
		Query query = entityManager.createNamedQuery(ShoppingCartItem.UPDATE_QUANT);
		query.setParameter("quantity", item.getQuantity());
		query.setParameter("id", item.getId().longValue());
		query.executeUpdate();
		entityManager.flush();
		
		
		
	}

	@Transactional
	public void updateCantidad(ValueChangeEvent e) {
	}

	private String typePerson;

	private Person partner;

	private String streetType;

	public String getStreetType() {
		return streetType;
	}

	public void setStreetType(String streetType) {
		this.streetType = streetType;
	}

	public String getTypePerson() {
		try {
			if (typePerson.equals(null))
				return "person";
		} catch (Exception e) {
			return "person";
		}
		return typePerson;
	}

	public void setTypePerson(String typePerson) {
		this.typePerson = typePerson;
	}


	private String uuid;

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public List<ShoppingCartItem> getCartItems() {
		return cartItems;
	}

	public boolean shoppingCartIsEmpty() {
		return getCartItems() == null || getCartItems().size() == 0;
	}

	public Double getTotal() {
		try {
			if (cartItems == null || cartItems.isEmpty())
				return 0D;
			Double total = 0D;
			// Sum up the quantities
			for (ShoppingCartItem cartItem : cartItems) {
				Double subtot = (cartItem.getSubTotal());
				total = total + subtot.floatValue();
			}
			return total;
		} catch (Exception ex) {
			return 0D;
		}
	}

	private static final DecimalFormat df = new DecimalFormat("0.00");

	public BigDecimal getTotalWithShipping() {
		try {
			Double geralWS = this.getTotal() + this.getShoppingCart().getShippingValue();
			BigDecimal bd = new BigDecimal(geralWS).setScale(2, RoundingMode.HALF_UP);
			return bd;
		} catch (Exception ex) {
			return new BigDecimal(0);
		}
	}
	
	public BigDecimal getPixTotalWithShipping() {
		try {
			Double geralWS = (this.getTotal() + this.getShoppingCart().getShippingValue()) ;   //* .95; 
			BigDecimal bd = new BigDecimal(geralWS).setScale(2, RoundingMode.HALF_UP);
			return bd;
		} catch (Exception ex) {
			return new BigDecimal(0);
		}
	}

	protected Integer getParamId(String param) {
		FacesContext context = FacesContext.getCurrentInstance();
		Map<String, String> map = context.getExternalContext().getRequestParameterMap();
		return Integer.valueOf(map.get(param));
	}

	protected String getParamUuid(String param) {
		FacesContext context = FacesContext.getCurrentInstance();
		Map<String, String> map = context.getExternalContext().getRequestParameterMap();
		return map.get(param);
	}

	// ======================================
	// = Getters and Setters =
	// ======================================

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public Address getCompanyAddress() {
		return companyAddress;
	}

	public void setCompanyAddress(Address companyAddress) {
		this.companyAddress = companyAddress;
	}

	public Address getShippingAddress() {
		return shippingAddress;
	}

	public void setShippingAddress(Address shippingAddress) {
		this.shippingAddress = shippingAddress;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	private States state;

	public States getState() {
		return state;
	}

	public void setState(States state) {
		this.state = state;
	}

	private Cities city;

	public Cities getCity() {
		return city;
	}

	public void setCity(Cities city) {
		this.city = city;
	}

	@Transactional
	public List<Countries> getCountries() {
		try {
			TypedQuery<Countries> query = entityManager.createNamedQuery(Countries.FIND_ALL, Countries.class);
			List<Countries> countries = query.getResultList();
			return countries;
		} catch (Exception ignore) {
		}
		return null;
	}

	private List<States> states;

	public List<States> getStates() {
		Optional<Countries> hasCountry = Optional.ofNullable(this.address.getCountry());
		if (hasCountry.isPresent()) {
			TypedQuery<States> query5 = entityManager.createNamedQuery(States.FIND_BY_COUNTRY, States.class);
			query5.setParameter("country", hasCountry.get().getId());
			return query5.getResultList();
		} else {
			TypedQuery<States> query5 = entityManager.createNamedQuery(States.FIND_BY_COUNTRY, States.class);
			query5.setParameter("country", this.getCountries().get(0).getId());
			states = query5.getResultList();
			return states;
		}
	}

	public void setStates(List<States> states) {
		this.states = states;
	}

	public void onCountryChange() {
		try {

			TypedQuery<States> query5 = 
					entityManager.createNamedQuery(States.FIND_BY_COUNTRY, States.class);


			query5.setParameter("country", this.user.getCountry().getId());


			states = query5.getResultList();


			this.user.setState(null);


			this.user.setCity(null);


		} catch (Exception ignore) {
			ignore.printStackTrace();
		}
	}

	public void onStateChange() {
		try {
			TypedQuery<Cities> query = entityManager.createNamedQuery(Cities.FIND_BY_STATE, Cities.class);
			query.setParameter("state", this.user.getState().getId());
			cities = query.getResultList();
			this.user.setCity(null);
		} catch (Exception ignore) {
		}
	}

	private List<Cities> cities;

	public List<Cities> getCities() {
		Optional<States> hasState = Optional.ofNullable(this.user.getState());
		if (hasState.isPresent()) {
			TypedQuery<Cities> query = entityManager.createNamedQuery(Cities.FIND_BY_STATE, Cities.class);
			query.setParameter("state", this.user.getState().getId());
			return query.getResultList();
		}
		return cities;
	}

	public void setCities(List<Cities> cities) {
		this.cities = cities;
	}

	public String getMy_cookie_uuid() {
		return my_cookie_uuid;
	}

	public void setMy_cookie_uuid(String my_cookie_uuid) {
		this.my_cookie_uuid = my_cookie_uuid;
	}

	public Person getPerson() {
		return partner;
	}

	public void setPerson(Person partner) {
		this.partner = partner;
	}

	// -----------------------------

	// valor entre 0 a 100 que sera usado no progressbar
	private Integer progresso;
	// mensagem de notas sendo processadas ou canceladas
	private String mensagem;
	// quantidade de notas que o usuario informou
	private Integer quantidadeNotas;
	// notas processadas
	private List<String> notas;

	private List<String> processos;

	/**
	 * valores padrao de quantidade e notas
	 */
	/**
	 * cria uma mensagem que sera exibida para usuario atraves do componente
	 * p:messages
	 * 
	 * @param texto mensagem informada
	 */
	private void criarMensagem(String texto) {

		FacesMessage msg = new FacesMessage(texto);
		FacesContext.getCurrentInstance().addMessage("", msg);
	}

	// reseta o progresso e a mensagem do progressbar
	private void resetarProgresso() {
		progresso = 0;
		mensagem = "";
	}

	/**
	 * atualiza o progresso
	 * 
	 * @param i posicao da nota na lista
	 */
	private void atualizarProgresso(int i) {
		// calculo para o percentual do processo em relacao a quantidade de notas
		progresso = (i * 100) / quantidadeNotas;
		// try {
		// Thread.sleep(5000);
		// } catch (Exception ex) {
		// criarMensagem("erro ");
		// }
	}

	

	// @Inject
	// EmailService emailService;

	public ClientOAuthEntity findClientOAuthManager(String realmName) {
		try {
			TypedQuery<ClientOAuthEntity> query = entityManager
					.createNamedQuery(ClientOAuthEntity.OAUTH_CLIENT_ID_REALM, ClientOAuthEntity.class);
			query.setParameter("clientId", "admin-cli");
			query.setParameter("realmId", realmName);
			return query.getSingleResult();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * processa as notas,podendo ser adicionadas ou canceladas
	 * 
	 * @param acao 1 = notas serao processadas 2 = notas serao canceladas
	 */

	
	@Transactional
	public void processarCompra() {
		try {

			

			

		
			// ==========================
			criarMensagem("Sua compra foi criada com sucesso!");


			FacesContext context = FacesContext.getCurrentInstance();

			try {
				context.getExternalContext().redirect("/application/checkout3.xhtml");
			} catch (IOException ex) {
				ex.printStackTrace();
			}

		} catch (Exception e) {

			 e.printStackTrace();
		}
	}

	
	public static String execCmd(String cmd) throws java.io.IOException {
		java.util.Scanner s = new java.util.Scanner(Runtime.getRuntime().exec(cmd).getInputStream())
				.useDelimiter("\\A");
		
		String saidaStr = "";
		while (s.hasNext()) {
			String lineString = s.next().toString();
			saidaStr = saidaStr + lineString;
		}
		return saidaStr;

	
	}

	

	

	public Integer getProgresso() {
		if (progresso == null) {
			progresso = 0;
		}
		return progresso;
	}

	public void setProgresso(Integer progresso) {
		this.progresso = progresso;
	}

	public String getMensagem() {
		if (mensagem == null) {
			mensagem = "";
		}
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	public Integer getQuantidadeNotas() {
		return quantidadeNotas;
	}

	public void setQuantidadeNotas(Integer quantidadeNotas) {
		this.quantidadeNotas = quantidadeNotas;
	}

	public List<String> getNotas() {
		return notas;
	}

	public void setNotas(List<String> notas) {
		this.notas = notas;
	}

	public String getUuidTemp() {
		return uuidTemp;
	}

	public void setUuidTemp(String uuidTemp) {
		this.uuidTemp = uuidTemp;
	}

	public ClientComponentEntity getClient() {
		return client;
	}

	public void setClient(ClientComponentEntity client) {
		this.client = client;
	}

	// ----------------------- POSTAL CODE -------------------------

	private List<States> statesPostalCode;

	public List<States> getStatesPostalCode() {

		try {
			TypedQuery<States> query5 = entityManager.createNamedQuery(States.FIND_BY_COUNTRY_ACTIVES, States.class);
			query5.setParameter("country", 31); // BRAZIL FIXME
			statesPostalCode = query5.getResultList();
			return statesPostalCode;
		} catch (Exception ex) {

		}

		return statesPostalCode;
	}

	public void setStatesPostalCode(List<States> statesPostalCode) {
		this.statesPostalCode = statesPostalCode;
	}

	public void onStatePostalCodeChange() {

		// try {
		TypedQuery<Cities> query = entityManager.createNamedQuery(Cities.FIND_BY_STATE, Cities.class);
		query.setParameter("state", this.shippingAddress.getState().getId());
		citiesPostalCode = query.getResultList();


		// this.shippingAddress.setCity(null);
		this.shippingAddress.setCity(citiesPostalCode.get(0));
		// } catch (Exception ignore) {
		// }

	}

	// =======

	private List<Cities> citiesPostalCode;

	public List<Cities> getCitiesPostalCode() {
		try {
			Optional<States> hasState = Optional.ofNullable(this.shippingAddress.getState());
			if (hasState.isPresent()) {
				TypedQuery<Cities> query = entityManager.createNamedQuery(Cities.FIND_BY_STATE, Cities.class);
				query.setParameter("state", this.shippingAddress.getState().getId());
				return query.getResultList();
			}
		} catch (Exception ex) {

		}
		return citiesPostalCode;
	}

	public void setCitiesPostalCode(List<Cities> citiesPostalCode) {
		this.citiesPostalCode = citiesPostalCode;
	}

	@Transactional
	public void updatePostalCode() {


		Optional<JsonNode> responstaCepJson = Optional.ofNullable(

				correiosService.cepData(this.shoppingCart.getZipcode().replace("-", "")));

		if (responstaCepJson.isPresent()) {

			String sufUfCap = responstaCepJson.get().get("uf").asText();
			TypedQuery<States> query5 = entityManager.createNamedQuery(States.FIND_BY_COUNTRY_ACTIVES_BY_NAME,
					States.class);
			query5.setParameter("country", 31); // BRAZIL FIXME
			query5.setParameter("iso2", sufUfCap); // STATE FIXME
			long sizeStates = query5.getResultStream().count();
			if (sizeStates < 1) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"LAMENTAMOS", "Municícipio não atendido! Somente Rondônia e Acre!"));

				this.shoppingCart.setZipcode(null);
			} else {

				String nomeMunCap = responstaCepJson.get().get("nomeMunicipio").asText();
				TypedQuery<Cities> queryCitie = entityManager.createNamedQuery(Cities.FIND_BY_NAME, Cities.class);
				queryCitie.setParameter("name", nomeMunCap); // BRAZIL FIXME
				shoppingCart.setCity(queryCitie.getSingleResult());
				entityManager.merge(shoppingCart);
				entityManager.flush();

			}
		} else {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"LAMENTAMOS", "Cep não encontrado na base dos correios!"));

			this.shoppingCart.setZipcode(null);
		}

		// ================================================================

	}

	public JsonNode ratreiaObjeto(String codigoObjeto, String finalToken) {

		StringBuffer response = null;
		try {
			String urlProd = "https://api.correios.com.br/srorastro/v1/objetos/" + codigoObjeto + "?resultado=T";
			URL url2 = new URL(urlProd);
			HttpURLConnection conn = (HttpURLConnection) url2.openConnection();
			conn.setRequestProperty("Authorization", "Bearer " + finalToken);
			conn.setRequestProperty("accept", "application/json");
			conn.setRequestMethod("GET");
			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String output;
			response = new StringBuffer();
			while ((output = in.readLine()) != null) {
				response.append(output);
			}
			in.close();
			// printing result from response
			JsonNode retornoCorreiosApiCep = JacksonUtil.toJsonNode(response.toString());

			return retornoCorreiosApiCep;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}

	}

	

	public PersonCard getSafraPayCard() {
		return safraPayCard;
	}

	public void setSafraPayCard(PersonCard safraPayCard) {
		this.safraPayCard = safraPayCard;
	}




	public String getViewId() {
		return viewId;
	}




	public void setViewId(String viewId) {
		this.viewId = viewId;
	}

}