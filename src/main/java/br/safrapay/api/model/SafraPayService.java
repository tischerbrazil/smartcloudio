package br.safrapay.api.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

import org.geoazul.model.Address;
import org.geoazul.model.security.ClientOAuthEntity;
import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.erp.modules.inventory.entities.Product;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.json.JsonWriteFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.safrapay.api.model.SafraPayTransaction.TransactionStatus;
import jsonb.JacksonUtil;

@Stateless
public class SafraPayService {

	@Inject
	private EntityManager entityManager;

	private SafraPayApiToken safraPayApiToken;

	public Date convertToDateViaSqlTimestamp(LocalDateTime dateToConvert) {
		return java.sql.Timestamp.valueOf(dateToConvert);
	}

	Date convertToDateViaInstant(LocalDateTime dateToConvert) {
		return java.util.Date.from(dateToConvert.atZone(ZoneId.systemDefault()).toInstant());
	}

	boolean isJWTExpired(DecodedJWT decodedJWT) {
		Date expiresAt = decodedJWT.getExpiresAt();
		LocalDateTime timeNowSaoPaulo = LocalDateTime.now(ZoneId.of("America/Sao_Paulo"));
		return expiresAt.before(convertToDateViaInstant(timeNowSaoPaulo));
	}

	public SafraPayApiToken getSafraPayApiToken() {

		Optional<SafraPayApiToken> temApiToken = Optional.ofNullable(safraPayApiToken);

		if (temApiToken.isPresent()) {

			Boolean expiredToken = false;
			try {
				DecodedJWT decodedJWT = JWT.decode(safraPayApiToken.getToken());
				expiredToken = isJWTExpired(decodedJWT);
			} catch (JWTDecodeException e) {
			
			}

			if (expiredToken) {
				safraPayTokenRefresh();
			}

			return safraPayApiToken;
		} else {

			processSafraPayToken();

			Boolean expiredToken = false;
			try {
				DecodedJWT decodedJWT = JWT.decode(safraPayApiToken.getToken());
				expiredToken = isJWTExpired(decodedJWT);
			} catch (JWTDecodeException e) {

			
			}
			if (expiredToken)
				safraPayTokenRefresh();
		

			return safraPayApiToken;
		}

	}

	public void setSafraPayApiToken(SafraPayApiToken safraPayApiToken) {
		this.safraPayApiToken = safraPayApiToken;
	}

	public ClientOAuthEntity findClientOAuthEntity() {
		try {
			TypedQuery<ClientOAuthEntity> query = entityManager.createNamedQuery(ClientOAuthEntity.OAUTH_CLIENT_ID,
					ClientOAuthEntity.class);
			query.setParameter("clientId", "SafraPay");
			return query.getSingleResult();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Transactional
	public void processSafraPayToken() {
		TypedQuery<SafraPayApiToken> queryCode;
		queryCode = entityManager.createNamedQuery(SafraPayApiToken.FIND, SafraPayApiToken.class);
		long codeFind = queryCode.getResultStream().count();
		if (codeFind < 1) {
			safraPayToken();
		} else {
			safraPayApiToken = queryCode.getResultStream().findFirst().get();
		}

	}

	@Transactional
	public void safraPayToken() {
		ClientOAuthEntity clientSafraPay = findClientOAuthEntity();
		StringBuffer response = null;
		String merchantToken = clientSafraPay.getPassword();
		String cnpj = clientSafraPay.getUsername();
		String prodUrl = clientSafraPay.getServerName();

		String password = cnpj + merchantToken;
		String bcryptHashString = BCrypt.withDefaults().hashToString(12, password.toCharArray());

		String pathString = "/v2/merchant/auth";

		safraPayApiToken = new SafraPayApiToken();

		String urlFinal = prodUrl + pathString;

		try {

			URL url2 = new URL(urlFinal);
			HttpURLConnection conn = (HttpURLConnection) url2.openConnection();
			conn.setRequestProperty("Authorization", bcryptHashString);
			conn.setRequestProperty("merchantCredential", cnpj);
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Accept", "application/json");
			conn.setRequestProperty("Content-Length", "0");
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);
			String datajson = "{}";
			OutputStream os = conn.getOutputStream();
			os.write(datajson.getBytes("UTF-8"));
			os.close();
			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String output;
			response = new StringBuffer();
			while ((output = in.readLine()) != null) {
				response.append(output);
			}
			in.close();
			JsonNode retornoSafraPayApi = JacksonUtil.toJsonNode(response.toString());
			safraPayApiToken.setToken(retornoSafraPayApi.findValue("accessToken").asText());
			safraPayApiToken.setRefreshToken(retornoSafraPayApi.findValue("refreshToken").asText());
			safraPayApiToken.setSuccess(retornoSafraPayApi.findValue("success").asBoolean());
			safraPayApiToken.setTraceKey(retornoSafraPayApi.findValue("traceKey").asText());
			safraPayApiToken.setServerName(prodUrl);
			safraPayApiToken.setId(1);
			JsonNode valor = JacksonUtil.toJsonNode(retornoSafraPayApi.findValue("errors").asText());
			safraPayApiToken.setErrors(valor);
		} catch (Exception ex) {
		
			ex.printStackTrace();
		}
		entityManager.persist(safraPayApiToken);
		entityManager.flush();

	}

	@Transactional
	public void safraPayTokenRefresh() {

		String accessToken = safraPayApiToken.getToken();
		String refreshToken = safraPayApiToken.getRefreshToken();
		String pathString = "/v2/refreshtoken";
		String urlFinal = safraPayApiToken.getServerName() + pathString;
		StringBuffer responseRefresh = null;
		try {
			URL url2 = new URL(urlFinal);
			HttpURLConnection conn = (HttpURLConnection) url2.openConnection();
			conn.setRequestProperty("Authorization", "Bearer " + accessToken);
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Accept", "application/json");
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);
			String datajson = "{" + "\"accessToken\": \"" + accessToken + "\"," + "\"refreshToken\": \"" + refreshToken
					+ "\"" + "}";
		
			OutputStream os = conn.getOutputStream();
			os.write(datajson.getBytes("UTF-8"));
			os.close();
			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String output;
			responseRefresh = new StringBuffer();
			while ((output = in.readLine()) != null) {
				responseRefresh.append(output);
			}
			in.close();
			JsonNode retornoSafraPayApi = JacksonUtil.toJsonNode(responseRefresh.toString());
			safraPayApiToken.setToken(retornoSafraPayApi.findValue("accessToken").asText());
			safraPayApiToken.setRefreshToken(retornoSafraPayApi.findValue("refreshToken").asText());
			safraPayApiToken.setSuccess(retornoSafraPayApi.findValue("success").asBoolean());
			safraPayApiToken.setTraceKey(retornoSafraPayApi.findValue("traceKey").asText());
			JsonNode valor = JacksonUtil.toJsonNode(retornoSafraPayApi.findValue("errors").asText());
			safraPayApiToken.setErrors(valor);
		} catch (Exception ex) {
		
			ex.printStackTrace();
		}
		entityManager.merge(safraPayApiToken);
		entityManager.flush();
	}

	public JsonNode testToken() {
		try {
			String tokEN = this.getSafraPayApiToken().getToken();
		} catch (Exception ex) {
		
			 ex.printStackTrace();
		}

		return null;

	}

	@Transactional
	public void safraPayAddCustomer(SafraPayCustomer safraPayCustomer, SafraPayCustomerPhone safraPayCustomerPhone) {

		String pathString = "/v2/customer";
		String accessTokenNew = "";

		try {
			accessTokenNew = this.getSafraPayApiToken().getToken();
		} catch (Exception ex) {

		}

		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

		SimpleFilterProvider filterProvider = new SimpleFilterProvider();

		filterProvider.addFilter("safraPayCustomerFilter",
				SimpleBeanPropertyFilter.serializeAllExcept("smartid", "id"));

		String jsonCustomer = null;

		try {
			jsonCustomer = mapper.writeValueAsString(safraPayCustomer);
		
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		String datajson = "{\"customer\": " + jsonCustomer + "}";
	

		if (!accessTokenNew.isBlank()) {

			

			String accessToken = this.safraPayApiToken.getToken();
			String urlFinal = this.safraPayApiToken.getServerName() + pathString;

		

			// =================
			StringBuffer responseRefresh = null;
			try {
				URL url2 = new URL(urlFinal);
				HttpURLConnection conn = (HttpURLConnection) url2.openConnection();
				conn.setRequestProperty("Authorization", "Bearer " + accessToken);
				conn.setRequestProperty("Content-Type", "application/json");
				conn.setRequestProperty("Accept", "application/json");
				conn.setRequestMethod("POST");
				conn.setDoOutput(true);

				OutputStream os = conn.getOutputStream();
				os.write(datajson.getBytes("UTF-8"));
				os.close();
				BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				String output;
				responseRefresh = new StringBuffer();
				while ((output = in.readLine()) != null) {
					responseRefresh.append(output);
				}
				in.close();

		
				JsonNode retornoSafraPayApi = JacksonUtil.toJsonNode(responseRefresh.toString());
				String a1w = retornoSafraPayApi.findValue("id").asText();
				String a2w = retornoSafraPayApi.findValue("/id").asText();

				

			} catch (Exception ex) {
				
				ex.printStackTrace();
			}
			// ***********************

			// ***********************

//===================

			// safraPayCustomer.setAddress(findAddressById(safraPayCustomer.getAddress().getId()));

			entityManager.persist(safraPayCustomer);
			entityManager.flush();

		}
	}

	@Transactional
	public void safraPayUpdateCustomer(SafraPayCustomer safraPayCustomer, SafraPayCustomerPhone safraPayCustomerPhone) {

		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

		SimpleFilterProvider filterProvider = new SimpleFilterProvider();

		filterProvider.addFilter("safraPayCustomerFilter", SimpleBeanPropertyFilter.serializeAllExcept("smartid"));

		String jsonCustomer = "";
		try {
			jsonCustomer = mapper.writer(filterProvider).writeValueAsString(safraPayCustomer);
		

		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String pathString = "/v2/customer";
		String accessTokenNew = "";

		try {
			accessTokenNew = this.getSafraPayApiToken().getToken();
		} catch (Exception ex) {

		}

		String datajson = "{\"customer\": " + jsonCustomer + "}";
	

		if (!accessTokenNew.isBlank()) {

		

			String accessToken = this.safraPayApiToken.getToken();
			String urlFinal = this.safraPayApiToken.getServerName() + pathString;

		

			// =================
			StringBuffer responseRefresh = null;
			try {
				URL url2 = new URL(urlFinal);
				HttpURLConnection conn = (HttpURLConnection) url2.openConnection();
				conn.setRequestProperty("Authorization", "Bearer " + accessToken);
				conn.setRequestProperty("Content-Type", "application/json");
				conn.setRequestProperty("Accept", "application/json");
				conn.setRequestMethod("PUT");
				conn.setDoOutput(true);

				OutputStream os = conn.getOutputStream();
				os.write(datajson.getBytes("UTF-8"));
				os.close();
				BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				String output;
				responseRefresh = new StringBuffer();
				while ((output = in.readLine()) != null) {
					responseRefresh.append(output);
				}
				in.close();

			
			} catch (Exception ex) {
			
				ex.printStackTrace();
			}
//===================
			// safraPayCustomer.setCustomer_id(UUID.fromString("68d41d64-b9b8-483d-9c71-ca53c50724cb"));

		}

	}

	public Address findAddressById(Integer addressId) {
		try {
			return entityManager.find(Address.class, addressId);
		} catch (Exception ex) {
			return null;
		}
	}

	@Transactional
	public void safraPayChargeCreditCard2(SafraPayCharge safraPayCharge) {

		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

		SimpleFilterProvider filterProvider = new SimpleFilterProvider();

		filterProvider.addFilter("safraPayCustomerFilter",
				SimpleBeanPropertyFilter.serializeAllExcept("smartid", "address"));

		filterProvider.addFilter("safraPayChargeFilter", SimpleBeanPropertyFilter.serializeAllExcept("smartid"));

		filterProvider.addFilter("safraPayTransactionFilter",
				SimpleBeanPropertyFilter.serializeAllExcept("smartid", "transactionId", "aditumNumber", "qrCode",
						"qrCodeBase64", "acquirer", "creationDateTime", "bankIssuerId", "creationDateTime",
						"installmentTypeValue", "transactionStatusValue", "installmentType", "transactionStatus",
						"transactionStatusTransient", "bankIssuerId", "paymentTypeValue", "paymentTypeTransient",
						"installmentTypeTransient", "isApproved", "brand", "transactionId", "isRecurrency",
						"isCanceled", "errorCode", "acquirerErrorCode", "id", "acquirerErrorMessage",
						"authorizationResponseCode", "acquirerTransactionId", "bankSlipStatus", "cancellations",
						"issuerInstallment"));

		filterProvider.addFilter("safraPayCardFilter", SimpleBeanPropertyFilter.serializeAllExcept("smartid",
				"transaction", "id_uuid", "isPrivateLabel", "brandName", "billingAddress", "validade"));

		String jsonCharge = "";
		try {
			jsonCharge = mapper.writer(filterProvider).writeValueAsString(safraPayCharge);

		
			jsonCharge = "{\"charge\": " + jsonCharge + "}";

			

		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// ===================

		String pathString = "/v2/charge/authorization";
		String accessTokenNew = "";

		try {
			accessTokenNew = this.getSafraPayApiToken().getToken();
		} catch (Exception ex) {
			ex.printStackTrace();
			
		}

		try {
			String accessToken = this.safraPayApiToken.getToken();

		
		} catch (Exception ex) {
			ex.printStackTrace();
		
		}

		
		List<Map<String, String>> metadata = new ArrayList<Map<String, String>>();

		safraPayCharge.setMetadata(metadata);

		for (SafraPayTransaction transaction : safraPayCharge.getTransactions()) {

			PersonCard card = transaction.getCard();

			transaction.setTransactionStatusTransient(TransactionStatus.Paid);
			transaction.setTransactionId("123456");
			entityManager.persist(transaction);

			card.setTransaction(transaction);

			card.setCardNumber("null");

			entityManager.persist(card);
		}
	

		entityManager.flush();

		

	}

	@Transactional
	public String safraPayChargeCreditCard(SafraPayCharge safraPayCharge) {
	
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
	
		SimpleFilterProvider filterProvider = new SimpleFilterProvider();
		
		filterProvider.addFilter("safraPayCustomerFilter",
				SimpleBeanPropertyFilter.serializeAllExcept("smartid", "address"));
		
		filterProvider.addFilter("safraPayChargeFilter", SimpleBeanPropertyFilter.serializeAllExcept("smartid"));
		
		filterProvider.addFilter("safraPayTransactionFilter",
				SimpleBeanPropertyFilter.serializeAllExcept("smartid", "transactionId", "aditumNumber", "qrCode",
						"qrCodeBase64", "acquirer", "creationDateTime", "bankIssuerId", "creationDateTime",
						"installmentTypeValue", "transactionStatusValue", "installmentType", "transactionStatus",
						"transactionStatusTransient", "bankIssuerId", "paymentTypeValue", "paymentTypeTransient",
						"installmentTypeTransient", "isApproved", "brand", "transactionId", "isRecurrency",
						"isCanceled", "errorCode", "acquirerErrorCode", "id", "acquirerErrorMessage",
						"authorizationResponseCode", "acquirerTransactionId", "bankSlipStatus", "cancellations",
						"issuerInstallment"));
	
		filterProvider.addFilter("safraPayCardFilter", SimpleBeanPropertyFilter.serializeAllExcept("smartid",
				"transaction", "id_uuid", "isPrivateLabel", "brandName", "billingAddress", "validade"));
	
		String jsonCharge = "";
		try {
			jsonCharge = mapper.writer(filterProvider).writeValueAsString(safraPayCharge);

		
			jsonCharge = "{\"charge\": " + jsonCharge + "}";

			

		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// ===================

		String pathString = "/v2/charge/authorization";
		String accessTokenNew = "";

		try {
			accessTokenNew = this.getSafraPayApiToken().getToken();
		} catch (Exception ex) {

		}

		if (!accessTokenNew.isBlank()) {

	

			String accessToken = this.safraPayApiToken.getToken();
			String urlFinal = this.safraPayApiToken.getServerName() + pathString;

		

			String body = "";

			HttpResponse<?> responseRefresh = null;
			HttpClient clientPix = HttpClient.newHttpClient();
			try {
				HttpRequest requestPix = HttpRequest.newBuilder().uri(new URI(urlFinal))
						.header("Authorization", String.format("Bearer %s", accessToken))
						.header("Content-Type", "application/json")
						.POST(HttpRequest.BodyPublishers.ofString(jsonCharge)).build();
				responseRefresh = clientPix.send(requestPix, BodyHandlers.ofString());

				body = responseRefresh.body().toString();

	

				JsonNode retornoApiCard = JacksonUtil.toJsonNode(body);

				Boolean success = retornoApiCard.findValue("success").asBoolean();

				if (success) {
					String merchantChargeId = retornoApiCard.findValue("merchantChargeId").asText();
					String transactionId = retornoApiCard.findValue("transactionId").asText();
					String id = retornoApiCard.findValue("id").asText();
					String errorMessage = retornoApiCard.findValue("errorMessage").asText();
					Boolean isApproved = retornoApiCard.findValue("isApproved").asBoolean();

					List<Map<String, String>> metadata = new ArrayList<Map<String, String>>();

					safraPayCharge.setMetadata(metadata);

					for (SafraPayTransaction transaction : safraPayCharge.getTransactions()) {
						PersonCard card = transaction.getCard();
						transaction.setTransactionStatusTransient(TransactionStatus.Paid);
						transaction.setTransactionId(transactionId);
						transaction.setMerchantTransactionId(merchantChargeId);
						entityManager.persist(transaction);
						card.setTransaction(transaction);
						card.setCardNumber("null");
						entityManager.persist(card);
					}
				
					entityManager.flush();
				
					return errorMessage;

				} else {
					String message = retornoApiCard.findValue("message").asText();
					return message;
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

//===================

		}
		return "ERROR";

	}

	@Transactional
	public void safraPayChargePix(SafraPayCharge safraPayCharge) {

		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

		SimpleFilterProvider filterProvider = new SimpleFilterProvider();

		filterProvider.addFilter("safraPayCustomerFilter",
				SimpleBeanPropertyFilter.serializeAllExcept("smartid", "address"));

		filterProvider.addFilter("safraPayChargeFilter", SimpleBeanPropertyFilter.serializeAllExcept("smartid"));

		filterProvider.addFilter("safraPayTransactionFilter",
				SimpleBeanPropertyFilter.serializeAllExcept("smartid", "transactionId", "aditumNumber", "qrCode",
						"qrCodeBase64", "acquirer", "creationDateTime", "bankIssuerId", "creationDateTime",
						"installmentTypeValue", "transactionStatusValue", "installmentType", "transactionStatus",
						"transactionStatusTransient", "bankIssuerId", "paymentTypeValue", "paymentTypeTransient",
						"installmentTypeTransient", "isApproved", "brand", "transactionId", "isRecurrency",
						"isCanceled", "errorCode", "acquirerErrorCode", "id", "acquirerErrorMessage",
						"authorizationResponseCode", "acquirerTransactionId", "bankSlipStatus", "cancellations",
						"issuerInstallment", "paymentType", "card", "installmentNumber", "softDescriptor",
						"merchantTransactionId", "shoppingCart"));

		filterProvider.addFilter("safraPayCardFilter", SimpleBeanPropertyFilter.serializeAllExcept("smartid",
				"transaction", "id_uuid", "isPrivateLabel", "brandName", "billingAddress"));

		String jsonCharge = "";
		try {
			jsonCharge = mapper.writer(filterProvider).writeValueAsString(safraPayCharge);

		
			jsonCharge = "{\"charge\": " + jsonCharge + "}";

			

		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// ===================

		String pathString = "/v2/charge/pix";
		String accessTokenNew = "";

		try {
			accessTokenNew = this.getSafraPayApiToken().getToken();
		} catch (Exception ex) {

		}

		if (!accessTokenNew.isBlank()) {

		

			String accessToken = this.safraPayApiToken.getToken();
			String urlFinal = this.safraPayApiToken.getServerName() + pathString;

		

			// =================
			StringBuffer responseRefresh = null;
			try {
				URL url2 = new URL(urlFinal);
				HttpURLConnection conn = (HttpURLConnection) url2.openConnection();
				conn.setRequestProperty("Authorization", "Bearer " + accessToken);
				conn.setRequestProperty("Content-Type", "application/json");
				conn.setRequestProperty("Accept", "application/json");
				conn.setRequestMethod("POST");
				conn.setDoOutput(true);

				OutputStream os = conn.getOutputStream();
				os.write(jsonCharge.getBytes("UTF-8"));
				os.close();
				BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				String output;
				responseRefresh = new StringBuffer();
				while ((output = in.readLine()) != null) {
					responseRefresh.append(output);
				}
				in.close();

				

			} catch (Exception ex) {
				
				ex.printStackTrace();
			}
//===================

		}

		

	}

}