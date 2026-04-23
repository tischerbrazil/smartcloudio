package org.geoazul.geoserver;


import org.apache.http.HttpEntity;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.geoazul.model.security.ClientComponentEntity;
import org.geoazul.view.LayerWizard;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;
import java.io.Serializable;

public class CreateProjectWorkspace implements Serializable {

	private static final long serialVersionUID = 1L;

	public static void createLayerGeoTIFF(String name,
			ClientComponentEntity clientGeoserver, String workspace)
					throws ClientProtocolException, IOException {
		
		CloseableHttpClient client = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(
				clientGeoserver.getServerName() + "/rest/workspaces/" + workspace + "/coveragestores/" + name
						+ "/coverages");

		ObjectMapper mapper = new ObjectMapper();
		ObjectNode node = mapper.createObjectNode();
		node.put("name", name);
		node.put("title", name);
		node.put("nativeCRS", "EPSG:32720");
		node.put("srs", "EPSG:32720");

		ObjectNode node1 = mapper.createObjectNode();
		node1.set("coverage", node); // repeat as needed


		String JSON_STRING = node1.toString();
		httpPost.setEntity(new StringEntity(JSON_STRING, ContentType.APPLICATION_JSON));

		UsernamePasswordCredentials creds = new 
				UsernamePasswordCredentials(clientGeoserver.getUsername(),
						clientGeoserver.getPassword());
		try {
			httpPost.addHeader(new BasicScheme().authenticate(creds, httpPost, null));
		} catch (org.apache.http.auth.AuthenticationException e) {

			// e.printStackTrace();
		}


		
		CloseableHttpResponse response = client.execute(httpPost);

		client.close();
	}

	public static void createDataStoreGeoTIFF(String name, 
			String workspace, String filePath,
			ClientComponentEntity clientGeoserver)
			throws ClientProtocolException, IOException {
		
		
		CloseableHttpClient client = HttpClients.createDefault();
		
		
		HttpPost httpPost = new HttpPost(
				clientGeoserver.getServerName() + "/rest/workspaces/" + workspace + "/coveragestores");
		
		
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode node = mapper.createObjectNode();
		node.put("name", name);
		node.put("workspace", workspace);
		node.put("enabled", true);
		node.put("type", "GeoTIFF");
		node.put("url", filePath);

		ObjectNode node1 = mapper.createObjectNode();
		node1.set("coverageStore", node); // repeat as needed


		String JSON_STRING = node1.toString();
		httpPost.setEntity(new StringEntity(JSON_STRING, ContentType.APPLICATION_JSON));

		UsernamePasswordCredentials creds = new 
				UsernamePasswordCredentials(clientGeoserver.getUsername(),
						clientGeoserver.getPassword());
		
		try {
			httpPost.addHeader(new BasicScheme().authenticate(creds, httpPost, null));
		} catch (org.apache.http.auth.AuthenticationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		CloseableHttpResponse response = client.execute(httpPost);

		client.close();
	} 
	
	public static void createLayerSHP( String storeName, String dataSet, String layerName, 
			String workspace, 
			ClientComponentEntity clientGeoserver)
			throws ClientProtocolException, IOException {
		
		CloseableHttpClient client = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(
				clientGeoserver.getServerName() + "/rest/workspaces/" 
		+ workspace + "/datastores/" + storeName + "/featuretypes");
		
		ObjectMapper mapper = new ObjectMapper();

		ObjectNode nodeName = mapper.createObjectNode();
		nodeName.put("title", layerName);
		nodeName.put("name", layerName);
		nodeName.put("nativeName", dataSet);

		ObjectNode nodeFinal = mapper.createObjectNode();
		nodeFinal.set("featureType", nodeName); // repeat as needed


		String JSON_STRING = nodeFinal.toString();
		httpPost.setEntity(new StringEntity(JSON_STRING, ContentType.APPLICATION_JSON));

		UsernamePasswordCredentials creds = new 
				UsernamePasswordCredentials(clientGeoserver.getUsername(),
						clientGeoserver.getPassword());

		try {
			httpPost.addHeader(new BasicScheme().authenticate(creds, httpPost, null));
		} catch (org.apache.http.auth.AuthenticationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		CloseableHttpResponse response = client.execute(httpPost);


		client.close();
	}
	
	public static void createStyleSLD(String nameSld, String fileNameSld, 
			String workspace,
			ClientComponentEntity clientGeoserver)
			throws ClientProtocolException, IOException {
		
		CloseableHttpClient client = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(
				clientGeoserver.getServerName() + "/rest/workspaces/" 
		+ workspace + "/styles");
		
		
		
		ObjectMapper mapper = new ObjectMapper();

		ObjectNode nodeName = mapper.createObjectNode();
	
		nodeName.put("name", nameSld);
		nodeName.put("filename", fileNameSld);

		ObjectNode nodeFinal = mapper.createObjectNode();
		nodeFinal.set("style", nodeName); // repeat as needed


		String JSON_STRING = nodeFinal.toString();
		httpPost.setEntity(new StringEntity(JSON_STRING, ContentType.APPLICATION_JSON));

		UsernamePasswordCredentials creds = new 
				UsernamePasswordCredentials(clientGeoserver.getUsername(),
						clientGeoserver.getPassword());

		try {
			httpPost.addHeader(new BasicScheme().authenticate(creds, httpPost, null));
		} catch (org.apache.http.auth.AuthenticationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		CloseableHttpResponse response = client.execute(httpPost);


		client.close();
	}
	
	
	public static void updateLayerSLD(String nameLayer, String nameSldNew, 
			String workspace, 
			ClientComponentEntity clientGeoserver)
			throws ClientProtocolException, IOException {
	
		CloseableHttpClient client = HttpClients.createDefault();
		HttpPut httpPut = new HttpPut(
				clientGeoserver.getServerName() + "/rest/workspaces/" 
		+ workspace + "/layers/" + nameLayer);
		

	//	CloseableHttpClient client = HttpClients.createDefault();
	//	String serverName = "http://localhost:8080/geoserver";
	//	HttpPut httpPut = new HttpPut(serverName + 
	//"/rest/workspaces/geoazul_127_0_0_1/layers/l815576863341550939-wmsgeoserver");

		
	//	String x = "http://localhost:8080/geoserver/rest/workspaces/geoazul_127_0_0_1/layers/l815579049437961591-wmsgeoserver";
		
	//	HttpPut httpPut = new HttpPut(x); 
		//l815575327949463171-wmsgeoserver
		
		ObjectMapper mapper = new ObjectMapper();

		ObjectNode nodeName = mapper.createObjectNode();
		nodeName.put("name", nameSldNew);

		ObjectNode nodeDS = mapper.createObjectNode();
		nodeDS.set("defaultStyle", nodeName); // repeat as needed

		ObjectNode nodeFinal = mapper.createObjectNode();
		nodeFinal.set("layer", nodeDS); // repeat as needed


		String JSON_STRING = nodeFinal.toString();
		httpPut.setEntity(new StringEntity(JSON_STRING, ContentType.APPLICATION_JSON));

		
		UsernamePasswordCredentials creds = new UsernamePasswordCredentials("admin", "geoserver");

		try {
			httpPut.addHeader(new BasicScheme().authenticate(creds, httpPut, null));
		} catch (org.apache.http.auth.AuthenticationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		CloseableHttpResponse response = client.execute(httpPut);


		client.close();
		
	}
	
	
	public static void createDataStoreSHP(LayerWizard layerWizard, 
			String workspace, String filePath,
			ClientComponentEntity clientGeoserver)
			throws ClientProtocolException, IOException {
		
		CloseableHttpClient client = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(
				clientGeoserver.getServerName() + "/rest/workspaces/" + workspace + "/datastores");
	
		ObjectMapper mapper = new ObjectMapper();

		ObjectNode nodeName = mapper.createObjectNode();
		nodeName.put("name", layerWizard.storeLayerWizardString());

		ObjectNode nodeEntry1 = mapper.createObjectNode();
		nodeEntry1.put("@key", "url"); 
		String urlLayer = "file:" + layerWizard.getUrlLayer();
		nodeEntry1.put("$", urlLayer);

		// nodeEntry1.put("$", "file:///data/shapefiles/states.shp");

		ObjectNode nodeFinal = mapper.createObjectNode();
		nodeFinal.set("dataStore", nodeName); // repeat as needed

		ObjectNode nodeCP = mapper.createObjectNode();

		ArrayNode itemsArray = nodeCP.putArray("entry");
		itemsArray.add(nodeEntry1);

		nodeName.set("connectionParameters", nodeCP); // repeat as needed


		String JSON_STRING = nodeFinal.toString();
		httpPost.setEntity(new StringEntity(JSON_STRING, ContentType.APPLICATION_JSON));

		UsernamePasswordCredentials creds = new 
				UsernamePasswordCredentials(clientGeoserver.getUsername(),
						clientGeoserver.getPassword());

		try {
			httpPost.addHeader(new BasicScheme().authenticate(creds, httpPost, null));
		} catch (org.apache.http.auth.AuthenticationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		CloseableHttpResponse response = client.execute(httpPost);


		client.close();
		
	}

	public static void createWorkspace(String workspace,
			ClientComponentEntity clientGeoserver)
			throws ClientProtocolException, IOException {
		CloseableHttpClient client = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(
				clientGeoserver.getServerName() + "/rest/workspaces");

		
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode node = mapper.createObjectNode();
		node.put("name", workspace); // repeat as needed

		ObjectNode node1 = mapper.createObjectNode();
		node1.set("workspace", node); // repeat as needed


		String JSON_STRING = node1.toString();
		httpPost.setEntity(new StringEntity(JSON_STRING, ContentType.APPLICATION_JSON));

		
		UsernamePasswordCredentials creds = new 
				UsernamePasswordCredentials(clientGeoserver.getUsername(),
						clientGeoserver.getPassword());
		
		try {
			httpPost.addHeader(new BasicScheme().authenticate(creds, httpPost, null));
		} catch (org.apache.http.auth.AuthenticationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		CloseableHttpResponse response = client.execute(httpPost);

		client.close();
	}
	
	
	
	

	public static void listWorkspaces(String name,
			ClientComponentEntity clientGeoserver)
			throws ClientProtocolException, IOException {

		HttpGet request = new HttpGet(clientGeoserver + "/rest/workspaces");

		CredentialsProvider provider = new BasicCredentialsProvider();
		provider.setCredentials(AuthScope.ANY, new 
				UsernamePasswordCredentials(clientGeoserver.getUsername(), 
						clientGeoserver.getPassword()));

		try (CloseableHttpClient httpClient = HttpClientBuilder.create().setDefaultCredentialsProvider(provider)
				.build(); CloseableHttpResponse response = httpClient.execute(request)) {

			// 401 if wrong user/password

			HttpEntity entity = response.getEntity();
			if (entity != null) {
				// return it as a String
				String result = EntityUtils.toString(entity);
			}

		}
	}

}