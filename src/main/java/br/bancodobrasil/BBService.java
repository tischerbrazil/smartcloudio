package br.bancodobrasil;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
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
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Optional;
import java.util.stream.Collectors;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.geoazul.model.security.ClientOAuthEntity;
import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.bancodobrasil.model.BancoBrasilApiToken;
import br.bancodobrasil.model.BancoBrasilPixPayment;
import br.safrapay.api.model.SafraPayApiToken;
import jsonb.JacksonUtil;

@Stateless
public class BBService {

	@Inject
	private EntityManager entityManager;

		

	public ClientOAuthEntity findClientOAuthEntity() {
		try {
			TypedQuery<ClientOAuthEntity> query = entityManager.createNamedQuery(ClientOAuthEntity.OAUTH_CLIENT_ID,
					ClientOAuthEntity.class);
			query.setParameter("clientId", "BancoBrasil");
			return query.getSingleResult();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	
	
	
	
	

	
	
	
	public String  testeApiBB(BancoBrasilPixPayment bancoBrasilPixPayment  ) {
		ClientOAuthEntity bbPay = findClientOAuthEntity();
		String clientId = bbPay.getPassword();
		String secretId = bbPay.getSecret();
		String chavePix = bbPay.getUsername();
		String gwAppkey = bbPay.getImageUrl();
		String urlProduction = bbPay.getIntroduction();
		String timeOut = bbPay.getDescription();
		bancoBrasilPixPayment.getCalendario().setExpiracao(Integer.parseInt(timeOut));
		String URL_V1 = bbPay.getServerName();
		String keys = clientId + ":" + secretId;
		HashMap<String, String> parameters = new HashMap<>();
		parameters.put("grant_type", "client_credentials");
		String form = parameters.keySet().stream()
				.map(key -> key + "=" + URLEncoder.encode(parameters.get(key), StandardCharsets.UTF_8))
				.collect(Collectors.joining("&"));
		String encoding = Base64.getEncoder().encodeToString(keys.getBytes());
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder().uri(URI.create(URL_V1))
				.headers("Content-Type", "application/x-www-form-urlencoded", "Authorization", "Basic " + encoding)
				.POST(BodyPublishers.ofString(form)).build();
		HttpResponse<?> response = null;
		try {
			response = client.send(request, BodyHandlers.ofString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		JsonNode retornoApi = JacksonUtil.toJsonNode(response.body().toString());
		String accessToken = retornoApi.findValue("access_token").asText();
		bancoBrasilPixPayment.setChave(chavePix);
		return testeApiBBGeneratePix(bancoBrasilPixPayment, accessToken, gwAppkey, urlProduction);
	}		
	
	
	
	
	public String testeApiBBGeneratePix(BancoBrasilPixPayment bancoBrasilPixPayment, 
			String accessToken, String gwAppkey, String urlProduction) {
		
	
		String urlPIX_V2 = urlProduction + "/pix/v1/cob/" + 
				bancoBrasilPixPayment.getTxid().toString().replace("-", "") + "?gw-app-key=" + gwAppkey;
				
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		SimpleFilterProvider filterProvider = new SimpleFilterProvider();
		filterProvider.addFilter("bancoBrasilPixFilter",
				SimpleBeanPropertyFilter.serializeAllExcept("id","status","location","textoImagemQRcode","txid", "revisao", "criacao"));
		
		String jsonCharge = "";
		try {
			jsonCharge = mapper.writer(filterProvider).writeValueAsString(bancoBrasilPixPayment);

			
			
			
			

		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		
		

		HttpResponse<?> responsePix = null;
		HttpClient clientPix = HttpClient.newHttpClient();
		try {
			HttpRequest requestPix = HttpRequest.newBuilder().uri(new URI(urlPIX_V2))
					.header("Authorization", String.format("Bearer %s", accessToken))
					.PUT(HttpRequest.BodyPublishers.ofString(jsonCharge)).build();
				responsePix = clientPix.send(requestPix, BodyHandlers.ofString());
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
		
		
		if (responsePix.statusCode() == 200) {
		}else {
		}
	
     	JsonNode retornoApiPix = JacksonUtil.toJsonNode(responsePix.body().toString());
		String location = retornoApiPix.findValue("location").asText();
		String status = retornoApiPix.findValue("status").asText();
		String textoImagemQRcode = retornoApiPix.findValue("textoImagemQRcode").asText();
		Integer revisao = retornoApiPix.findValue("revisao").asInt();
		
		LocalDateTime aLDT = LocalDateTime.now();

		bancoBrasilPixPayment.setCriacao(aLDT);
		bancoBrasilPixPayment.setTextoImagemQRcode(textoImagemQRcode);
		bancoBrasilPixPayment.setRevisao(revisao);
		bancoBrasilPixPayment.setLocation(location);
		bancoBrasilPixPayment.setStatus(status);
		entityManager.merge(bancoBrasilPixPayment);
		entityManager.flush();
		
		
		
		
			return textoImagemQRcode;
	}
	
	
	
	

	// ------------------------- PIX NEW

	

}
