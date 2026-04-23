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



public class TestCorreioApi {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
	

		
		
		try {// Sending get request
			String urlProd = "https://api.correios.com.br/token/v1/autentica";
																																									
	    URL url2 = new URL(urlProd);
	    HttpURLConnection conn = (HttpURLConnection) url2.openConnection();

	    String finalToken = "bngwMzgyNzgxMjVicjolIzRpOGclbHMwM1hXI3A=";
		conn.setRequestProperty("Authorization","Basic " + finalToken );
	 
	    conn.setRequestProperty("Content-Type","application/json");
	    conn.setRequestMethod("POST");

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
