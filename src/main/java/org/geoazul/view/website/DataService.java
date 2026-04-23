package org.geoazul.view.website;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import org.primefaces.model.FilterMeta;
import org.primefaces.model.SortMeta;

import jakarta.persistence.criteria.Expression;
import com.erp.modules.inventory.entities.Product;

@RequestScoped
public class DataService implements Serializable {
	
	
	
	@Inject
	EntityManager em;

	public List<Product> getProductList(int start, int size, 
			Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
			
			//Map<String, Object> filters) {
		
		
		
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Product> criteriaQuery = cb.createQuery(Product.class);
		Root<Product> root = criteriaQuery.from(Product.class);
		CriteriaQuery<Product> select = criteriaQuery.select(root);

	//	if (filterBy != null && filterBy.size() > 0) {
	//		List<Predicate> predicates = new ArrayList<>();
	//		for (Entry<String, FilterMeta> entry : filterBy.entrySet()) {
	//			String field = entry.getKey();
	//			Object value = entry.getValue();
	//			if (value == null) {
	//				continue;
	//			}
//
	//			Expression<String> expr = root.get(field).as(String.class);
	///			Predicate p = cb.like(cb.lower(expr), "%" + value.toString().toLowerCase() + "%");
	//			predicates.add(p);
	//		}
	//		if (predicates.size() > 0) {
	//			criteriaQuery.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
	//		}
	//	}
		TypedQuery<Product> query = em.createQuery(select);
		//query.setFirstResult(start);
		//query.setMaxResults(size);
		List<Product> list = query.getResultList();
		return list;
	}
	
	
	public int getProductTotalCount() {
	      try {
	      Query query = em.createQuery("Select count(e.id) From Product e");
	      return ((Long) query.getSingleResult()).intValue();
	      }catch(Exception ex) {
	    	  //return 0;
	      }
	      return 0;
	  }

	  public int getFilteredRowCount(
			 
			  Map<String, FilterMeta> filterBy
			  //Map<String, Object> filters
			 
			  
			  ) {
		  
		  
	      
		  CriteriaBuilder cb = em.getCriteriaBuilder();
	      CriteriaQuery<Long> criteriaQuery = cb.createQuery(Long.class);
	      Root<Product> root = criteriaQuery.from(Product.class);
	      CriteriaQuery<Long> select = criteriaQuery.select(cb.count(root));

	     // if (filterBy != null && filterBy.size() > 0) {
	      //     List<Predicate> predicates = new ArrayList<>();
	      //     for (Entry<String, FilterMeta> entry : filterBy.entrySet()) {
	      //         String field = entry.getKey();
	      //         Object value = entry.getValue();
	      //         if (value == null) {
	      //              continue;
	      //          }
	      //
	      //          Expression<String> expr = root.get(field).as(String.class);
	      //          Predicate p = cb.like(cb.lower(expr),
	      //                  "%" + value.toString().toLowerCase() + "%");
	      //        predicates.add(p);
	      //    }
	      //    if (predicates.size() > 0) {
	      //         criteriaQuery.where(cb.and(predicates.toArray
	      //                (new Predicate[predicates.size()])));
	      //    }
	      // }
	      Long count = em.createQuery(select).getSingleResult();
	      return count.intValue();
	  }

}
