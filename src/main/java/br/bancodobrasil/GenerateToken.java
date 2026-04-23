package br.bancodobrasil;


import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.stream.Collectors;

public class GenerateToken {

	public static void main(String[] args) {
		

		//Cliend id and client secret
		String keys = "eyJpZCI6IjhiYTY2MmMtOGUwMS00MTcxLWE3YTMtMTFlOWEiLCJjb2RpZ29QdWJsaWNhZG9yIjowLCJjb2RpZ29Tb2Z0d2FyZSI6NTYzODUsInNlcXVlbmNpYWxJbnN0YWxhY2FvIjoxfQ:eyJpZCI6IjY3ZjExOTMtYWUyYS00NTEwLTgxY2YtNTk1MDUiLCJjb2RpZ29QdWJsaWNhZG9yIjowLCJjb2RpZ29Tb2Z0d2FyZSI6NTYzODUsInNlcXVlbmNpYWxJbnN0YWxhY2FvIjoxLCJzZXF1ZW5jaWFsQ3JlZGVuY2lhbCI6MSwiYW1iaWVudGUiOiJob21vbG9nYWNhbyIsImlhdCI6MTY3ODEyNTA3NzQ1OX0";
		String URL = "https://oauth.hm.bb.com.br/oauth/token";

		HashMap<String, String> parameters = new HashMap<>();
		parameters.put("grant_type", "client_credentials");
		String form = parameters.keySet().stream()
		        .map(key -> key + "=" + URLEncoder.encode(parameters.get(key), StandardCharsets.UTF_8))
		        .collect(Collectors.joining("&"));

		//String encoding = Base64.getEncoder().encodeToString(keys.getBytes());
		//HttpClient client = HttpClient.newHttpClient();

		//HttpRequest request = HttpRequest.newBuilder().uri(URI.create(URL))
		//        .headers("Content-Type", "application/x-www-form-urlencoded", "Authorization", "Basic "+encoding)
		//        .POST(BodyPublishers.ofString(form)).build();
		//HttpResponse<?> response = null;
		//try {
		//	response = client.send(request, BodyHandlers.ofString());
		//} catch (IOException e) {
			// TODO Auto-generated catch block
		//	e.printStackTrace();
		//} catch (InterruptedException e) {
			// TODO Auto-generated catch block
		//	e.printStackTrace();
		//}
	}

}
