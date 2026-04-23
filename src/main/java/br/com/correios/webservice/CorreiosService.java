package br.com.correios.webservice;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;


import com.fasterxml.jackson.databind.JsonNode;

import br.com.correios.CorreiosApiToken;
import jsonb.JacksonUtil;


@Stateless
public class CorreiosService  {

	@Inject
	private EntityManager entityManager;
		
	private CorreiosApiToken correiosApiToken;





	public CorreiosApiToken getCorreiosApiToken() {
		
		
		Optional<CorreiosApiToken> temApiToken = 
				Optional.ofNullable(correiosApiToken);
		
		LocalDateTime timeNowSaoPaulo = LocalDateTime.now(ZoneId.of("America/Sao_Paulo"));
		
		if (temApiToken.isPresent()) {
			
			
			
			
			
			if (correiosApiToken.getExpiraEm().isBefore(timeNowSaoPaulo)) {
				//expirou vamos gerar e fazer update
				correiosToken(true);
			}else {
			}
			
			return correiosApiToken;
		}else {
			processCorreiosToken();
			
			
			
			
			
			
			
			if (correiosApiToken.getExpiraEm().isBefore(timeNowSaoPaulo)) {
				//expirou vamos gerar e fazer update
				correiosToken(true);
			}else {
			}
			
			
			
			return correiosApiToken;
		}

		
	}



	public void setCorreiosApiToken(CorreiosApiToken correiosApiToken) {
		this.correiosApiToken = correiosApiToken;
	}

	
	@Transactional
	public void processCorreiosToken() {
		
		TypedQuery<CorreiosApiToken> queryCode;
		
		queryCode = entityManager.createNamedQuery(CorreiosApiToken.FIND,
				CorreiosApiToken.class);
	
		long PromoCodeFind = queryCode.getResultStream().count();
		
		if (PromoCodeFind < 1) {
			// create
			correiosToken(false);
		} else {
			correiosApiToken = queryCode.getResultStream().findFirst().get();
		}

	}
	
	
	
	
	
	public void correiosToken(Boolean updateToken) {

		// String urlProd = "https://api.correios.com.br/token/v1/autentica";
		String urlProd = "https://api.correios.com.br/token/v1/autentica/contrato";
		// String urlProd =
		// "https://api.correios.com.br/token/v1/autentica//v1/autentica/cartaopostagem";
		String finalToken = "bngwMzgyNzgxMjVicjolIzRpOGclbHMwM1hXI3A=";
		String contratoCorreios = "9912588736";
		StringBuffer response = null;

		correiosApiToken = new CorreiosApiToken();

		try {

			URL url2 = new URL(urlProd);
			HttpURLConnection conn = (HttpURLConnection) url2.openConnection();

			conn.setRequestProperty("Authorization", "Basic " + finalToken);

			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Accept", "application/json");
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);
			String datajson = "{\"numero\": \"" + contratoCorreios + "\"}";
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

			// printing result from response

			JsonNode retornoCorreiosApi = JacksonUtil.toJsonNode(response.toString());

			correiosApiToken.setAmbiente(retornoCorreiosApi.findValue("ambiente").asText());
			correiosApiToken.setUserId(retornoCorreiosApi.findValue("id").asText());
			correiosApiToken.setIp(retornoCorreiosApi.findValue("ip").asText());
			correiosApiToken.setPerfil(retornoCorreiosApi.findValue("perfil").asText());
			correiosApiToken.setCpf_cnpj(retornoCorreiosApi.findValue("cnpj").asText());

			String emissaoDate = retornoCorreiosApi.findValue("emissao").asText();
			String expirateDate = retornoCorreiosApi.findValue("expiraEm").asText();

			LocalDateTime emissaoDateIso8601 = LocalDateTime.parse(emissaoDate);
			correiosApiToken.setEmissao(emissaoDateIso8601);

			LocalDateTime expiraEmDateIso8601 = LocalDateTime.parse(expirateDate);
			correiosApiToken.setExpiraEm(expiraEmDateIso8601);

			correiosApiToken.setZoneOffset(retornoCorreiosApi.findValue("zoneOffset").asText());
			correiosApiToken.setToken(retornoCorreiosApi.findValue("token").asText());
			correiosApiToken.setId(1);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		if (updateToken) {
			entityManager.merge(correiosApiToken);
			
		}else{
			entityManager.persist(correiosApiToken);
			
		}
		entityManager.flush();
		
	
	}
	
	
	public JsonNode calculaPrazo(String codEnvio, String cepOrigem, String cepDestino, String dtEvento
			) {

		cepOrigem = cepOrigem.replace("-", "");
		cepDestino = cepDestino.replace("-", "");
		
		StringBuffer response = null;
		try {

			String urlProd = "https://api.correios.com.br/prazo/v1/nacional/" + codEnvio + "/?cepDestino=" + cepDestino
					+ "&cepOrigem=" + cepOrigem + "&dtEvento=" + dtEvento;
			
			URL url2 = new URL(urlProd);
			HttpURLConnection conn = (HttpURLConnection) url2.openConnection();
			conn.setRequestProperty("Authorization", "Bearer " + this.getCorreiosApiToken().getToken());
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

	
public JsonNode cepData(String cep) {
		
		StringBuffer response = null;
		try {
			String urlProd = "https://api.correios.com.br/cep/v1/enderecos/" + cep;
			URL url2 = new URL(urlProd);
			HttpURLConnection conn = (HttpURLConnection) url2.openConnection();
			conn.setRequestProperty("Authorization", "Bearer " + this.getCorreiosApiToken().getToken());
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
						
		}
		
		return null;

	}



public JsonNode calculaPreco(String codEnvio, String cepOrigem, String cepDestino, String psObjeto
		) {

	cepOrigem = cepOrigem.replace("-", "");
	cepDestino = cepDestino.replace("-", "");
	
	StringBuffer response = null;
	try {
		String urlProd = "https://api.correios.com.br/preco/v1/nacional/" + codEnvio + "/?cepDestino=" + cepDestino
				+ "&cepOrigem=" + cepOrigem + "&psObjeto=" + psObjeto;
		
		URL url2 = new URL(urlProd);
		HttpURLConnection conn = (HttpURLConnection) url2.openConnection();
		conn.setRequestProperty("Authorization", "Bearer " + this.getCorreiosApiToken().getToken());
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

	
}