package org.primefaces.refact;

import java.util.List;
import java.util.Map;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import java.util.Iterator;
import org.geoazul.model.website.Modulo;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.MatchMode;
import org.primefaces.model.SortMeta;

@RequestScoped
public class ModuloService {

  

   

    public List<Modulo> getModulos(int number) {
		

		Query queryModFilter = entityManager.createNamedQuery(Modulo.ALL_MODULES);
	
		List modulos = queryModFilter.getResultList();
       return modulos;
    }

    
    @Inject
	private EntityManager entityManager;

	
	
		
	
	
	
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
	public List<Modulo> findEntites(int first, int pageSize,
	                           Map<String, SortMeta> sortBy,
	                           Map<String, FilterMeta> filterBy) {
	 
	  // String sql = buildWhereParams("from " + c.getName() + " ent where 1=1 ", filterBy);
	  // sql=buildOrderBy(sql,sortBy);
	   
	   
	  
	   
	   
	   //Query jpqlQuery = entityManager.createQuery("SELECT u FROM Modulo u");
	   //result = jpqlQuery.getResultList();
	   
	   
	   
	   
	   Query queryModFilter = entityManager.createNamedQuery(Modulo.ALL_MODULES);
		List modulos = queryModFilter.getResultList();
		  
		  
		  
      return modulos;
	   
	   
	//try {
	//   String sql = buildWhereParams("SELECT from Modulo ent ", filterBy);
	//   sql=buildOrderBy(sql,sortBy);
	//   TypedQuery query =  entityManager.createQuery(sql, c);
	   // query = setWhereParams(query, filterBy);
	   
	//}catch (Exception ex){
	//	ex.printStackTrace();
	//}
	   // return result;
	   
	  
	 
	   
	}
	
	

	

	// ------------------------- PIX NEW
    
    
    
    
}