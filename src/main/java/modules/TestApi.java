	package modules;
	
	

	import java.io.BufferedReader;
	import java.io.InputStream;
	import java.io.InputStreamReader;
	import java.net.HttpURLConnection;
	import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.JsonNode;

import jsonb.JacksonUtil;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import java.net.http.HttpClient;
import java.net.http.HttpClient.Version;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.net.http.HttpResponse.PushPromiseHandler;
import java.util.Arrays;

import java.util.List;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;



public class TestApi {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
	
		
		String url = "https://auth.cmegroup.com/as/token.oauth2";
		Map<String, String> parameters = new HashMap<>();
		parameters.put("grant_type", "client_credentials");
		parameters.put("client_id", "api_smartcloudio");
		parameters.put("client_secret", "rJDd96#EyN43D34Ga7dhJnyt");

		String form = parameters.keySet().stream()
		.map(key -> key + "=" + URLEncoder.encode(parameters.get(key), StandardCharsets.UTF_8))
		.collect(Collectors.joining("&"));

		HttpClient client = HttpClient.newHttpClient();

		HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url))
		.headers("Content-Type", "application/x-www-form-urlencoded")
		.POST(BodyPublishers.ofString(form)).build();

		HttpResponse<?> response = null;
		try {
			response = client.send(request, BodyHandlers.ofString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		
		JsonNode tokenFull = JacksonUtil.toJsonNode(response.body().toString());
	    
		  //  {"@odata.context":"https://was-p.bcnet.bcb.gov.br/olinda/servico/PTAX/versao/v1/odata$metadata#_CotacaoDolarPeriodo(cotacaoCompra,cotacaoVenda)","value":[{"cotacaoCompra":5.04050,"cotacaoVenda":5.04110},{"cotacaoCompra":4.96600,"cotacaoVenda":4.96660},{"cotacaoCompra":4.92020,"cotacaoVenda":4.92080},{"cotacaoCompra":4.86980,"cotacaoVenda":4.87040}]}
		    
		    
		    
	
		String finalToken = tokenFull.get("access_token").asText();
		
		
		//https://api.refdata.cmegroup.com/
		
		
		
		try {// Sending get request
			String essa = "https://api.refdata.cmegroup.com/v2/products/67IK2LXKPQFQ/instruments";
								//_/v#/products?globexProductCode=GE																																		
	    URL url2 = new URL(essa);
	    HttpURLConnection conn = (HttpURLConnection) url2.openConnection();

	    conn.setRequestProperty("Authorization","Bearer " + finalToken);
	 
	    conn.setRequestProperty("Content-Type","application/json");
	    conn.setRequestMethod("GET");

	    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	    String output;

	    StringBuffer response1 = new StringBuffer();
	    while ((output = in.readLine()) != null) {
	        response1.append(output);
	    }

	    in.close();
	    
	    
	    
	    
	    
	    // printing result from response

		}catch(Exception ex) {
			ex.printStackTrace();
		}
		
		
		
		
		
		
	}
	
	


}
