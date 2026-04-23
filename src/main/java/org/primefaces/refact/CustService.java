package org.primefaces.refact;


import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import jakarta.ejb.Stateless;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.geoazul.model.security.ClientOAuthEntity;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.MatchMode;
import org.primefaces.model.SortMeta;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import br.bancodobrasil.model.BancoBrasilPixPayment;
import jsonb.JacksonUtil;

@RequestScoped
public class CustService {

	@Inject
	private EntityManager entityManager;

	public List<Customer> findCustEntites(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
		return null;
		
		
	}
	
	 private LocalDate getDate() {
	        LocalDate now = LocalDate.now();
	        long randomDay = ThreadLocalRandom.current().nextLong(now.minusDays(30).toEpochDay(), now.toEpochDay());
	        return LocalDate.ofEpochDay(randomDay);
	    }
	
	
		
	
	 public List<Customer> getCustomers(int number) {
	        List<Customer> customers = new ArrayList<>();
	   
	            customers.add(
	                    new Customer(1, "nome", "comp",  new Country(2, "Brazil", "br") , getDate(),
	                            CustomerStatus.random(), random.nextInt(100), new Representative("Amy Elsner", "amyelsner.png")));
	       
	        return customers;
	    }
	
	 private Random random = new SecureRandom();
	
	protected String buildWhereParams(String sql, Map<String, FilterMeta> filters){ 
		   if ((filters != null) && !filters.isEmpty()) {
		      Iterator<String> fColumns  = filters.keySet().iterator();
		      String column;
		      FilterMeta fMeta;
		      MatchMode mMode;
		      Object fValue;
		      while (fColumns.hasNext()) {
		            column = fColumns.next();
		            fMeta = filters.get(column);
		            mMode = fMeta.getMatchMode();
		            sql += " and ent." + column + mapMatchModeToJPAOperator(mMode) + ":"+column +" ";
		      }
		   }
		   return sql;
		}// of buildWhereParams()
	
	
	protected String mapMatchModeToJPAOperator(MatchMode mMode) {
		   switch (mMode) {
		      case CONTAINS: return " like ";
		      case ENDS_WITH: return " like ";
		      case STARTS_WITH: return " like ";
		      case EQUALS: return " = "; // Checks if column value equals the filter value
		      case EXACT: return " = "; // Checks if string representations of column value and filter value are same
		      case GREATER_THAN : return " > ";
		      case GREATER_THAN_EQUALS: return " >= ";
		      case LESS_THAN: return " < ";
		      case LESS_THAN_EQUALS: return " <= ";
		   }
		   return " = ";
		}
	
	
	protected TypedQuery setWhereParams(TypedQuery query, Map<String, FilterMeta> filters) {
		   if ((filters != null) && !filters.isEmpty()) {
		      Iterator<String> fColumns  = filters.keySet().iterator();
		      String column;
		      FilterMeta fMeta;
		      MatchMode mMode;
		      Object fValue;
		      while (fColumns.hasNext()) {
		            column = fColumns.next();
		            fMeta = filters.get(column);
		            mMode = fMeta.getMatchMode();
		            fValue = fMeta.getFilterValue();
		            query.setParameter(column, buildParamValue(fValue,mMode));
		       }
		    }
		    return query;
		}

	
	protected Object buildParamValue(Object fValue, MatchMode mMode) {
		   if ((fValue != null) && (fValue instanceof String)) {
		      fValue = addPrefixWildcardIfNeed(mMode)  + fValue + addPrefixWildcardIfNeed(mMode);
		   }
		   return fValue;
		}

		private String addPrefixWildcardIfNeed(MatchMode mMode) {
		   switch (mMode) {
		      case CONTAINS: return "%";
		      case STARTS_WITH: return "%";
		    }
		   return "";
		}


		protected String buildOrderBy(String sql, Map<String, SortMeta> sortBy) {
		   if ((sortBy != null) && !sortBy.isEmpty()) {
		      SortMeta sortM = sortBy.entrySet().stream().findFirst().get().getValue();
		      String sortField = sortM.getField().trim();
		      String sortOrder = sortM.getOrder().toString();
		      sql += " order by ent." + sortField + " " + ((sortOrder.equals("ASCENDING") ? "ASC" : "DESC"));
		   }
		   return sql;
		}
	
	/*
	  The main EJB Service method you should use in you overriden LazyDataModel.load() method
	*/  
	public List<?> findEntites(Class<?> c, int first, int pageSize,
	                           Map<String, SortMeta> sortBy,
	                           Map<String, FilterMeta> filterBy) {
	   List<?> result = new ArrayList<>();
	  // String sql = buildWhereParams("from " + c.getName() + " ent where 1=1 ", filterBy);
	  // sql=buildOrderBy(sql,sortBy);
	   
	   
	  
	   
	   
	   
	   
	   TypedQuery query =  entityManager.createQuery("from " + c.getName(), c)
	                                    .setFirstResult(first)
	                                    .setMaxResults(pageSize);
	   
	   // query = setWhereParams(query, filterBy);
	   result = query.getResultList();
	 
	   
	  
	   
	   
	    result = query.getResultList();
	    return result;
	}
	
	

	

	// ------------------------- PIX NEW

	

}
