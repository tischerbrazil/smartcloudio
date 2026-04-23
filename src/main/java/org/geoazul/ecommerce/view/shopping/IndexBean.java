package org.geoazul.ecommerce.view.shopping;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;



import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;

import jakarta.transaction.Transactional;
import org.geoazul.model.basic.AbstractGeometry;
import org.hibernate.Session;
import org.joda.time.DateTime;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.WKTReader;
import org.primefaces.event.ItemSelectEvent;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import jsonb.JacksonUtil;


@Named
@RequestScoped
public class IndexBean {

    
	
	  
	  public static double round(double value, int places) {
		    if (places < 0) throw new IllegalArgumentException();

		    long factor = (long) Math.pow(10, places);
		    value = value * factor;
		    long tmp = Math.round(value);
		    return (double) tmp / factor;
		}

	  
	  
	  @Inject
		EntityManager entityManager;
	
	  
	  
	  
	  
	  
	  
	  
	  @PostConstruct
	    public void init() {
		 
	  }
	 
	public String getRetorno() {
		
		
		
		 URL obj; 
		 String pathGet = "";
		 String test = null;
		 
		 try {

			
			 DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
			 Date date = new Date();
			 
			
			 String dataFinal = dateFormat.format(date);
			 
			
		  
			 
					 
			// Get current date time
			 LocalDateTime currentDateTime = LocalDateTime.now().minusDays(8);
 
				
			 
			 // Custom format if needed
			 DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
			 // Format LocalDateTime
			 String dataInicio = currentDateTime.format(formatter);
			 // Verify
			
			 
			 
			 
			 
		 	String dinamicPath = "https://olinda.bcb.gov.br/olinda/servico/PTAX/versao/v1/odata/CotacaoDolarPeriodo(dataInicial=@dataInicial,dataFinalCotacao=@dataFinalCotacao)?@dataInicial=%27" + dataInicio + "%27&@dataFinalCotacao=%27" + dataFinal + "%27&$top=100&$format=json&$select=cotacaoCompra,cotacaoVenda" + pathGet ;
		    obj = new URL(dinamicPath);
		    HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		    con.setRequestMethod("GET");
		 

		    BufferedReader in = new BufferedReader(
		            new InputStreamReader(con.getInputStream()));
		    String inputLine;
		    StringBuffer response = new StringBuffer();

		    while ((inputLine = in.readLine()) != null) {
		        response.append(inputLine);
		    }
		    in.close();        
		    
		    test = response.toString();
		
			JsonNode jj = JacksonUtil.toJsonNode(test);
		    
		  //  {"@odata.context":"https://was-p.bcnet.bcb.gov.br/olinda/servico/PTAX/versao/v1/odata$metadata#_CotacaoDolarPeriodo(cotacaoCompra,cotacaoVenda)","value":[{"cotacaoCompra":5.04050,"cotacaoVenda":5.04110},{"cotacaoCompra":4.96600,"cotacaoVenda":4.96660},{"cotacaoCompra":4.92020,"cotacaoVenda":4.92080},{"cotacaoCompra":4.86980,"cotacaoVenda":4.87040}]}
		    
		    
		    
		String json = jj.get("value").toString();
		final ObjectMapper objectMapper = new ObjectMapper();
		Valores[] valores = objectMapper.readValue(json, Valores[].class);
		  List<Number> values = new ArrayList<>();
		for ( Valores valor : valores) {
			  double cotacaoCompra = Double.valueOf(valor.getCotacaoCompra());
			  double cotacaoVenda = Double.valueOf(valor.getCotacaoVenda());
			  
			
		
		
			DecimalFormat df = new DecimalFormat("#.##");      
			cotacaoCompra = Double.valueOf(df.format(cotacaoCompra));
			cotacaoVenda = Double.valueOf(df.format(cotacaoVenda));


		
		      		       
		     
		       values.add(cotacaoCompra);
		}

		
		
		
		
		
		
		
		
		 } catch (Exception ex){
				ex.printStackTrace();
			}
		//return Response.ok(test, MediaType.APPLICATION_JSON).build();
		
		return test;
	}

	private static final DecimalFormat df = new DecimalFormat("0.00");

	
	
	
}