package org.geoazul.model.basic.endpoints.ctm;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.ws.rs.GET;
import org.geoazul.model.basic.Layer;
import org.geoazul.model.basic.Polygon;
import org.hibernate.Session;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;

@Path("/bearer/surface")
public class SurfaceBearerEndpoint implements Serializable {

	
	private static final long serialVersionUID = 1L;
	
	@Inject 
	EntityManager entityManager;
	
	@GET
	@RolesAllowed("gis")
	@Produces("application/json")
	@Path("/{layer}")
	public Response listAll(
			@PathParam("layer") Long layer,
			@QueryParam("start") Integer startPosition, 
			@QueryParam("max") Integer maxResult) {
		
		
		Layer layerFind = null;
		
		try {
			
			TypedQuery<Layer> shoppingCartDraft = entityManager.createNamedQuery(Layer.LAYER_ID, Layer.class);
			shoppingCartDraft.setParameter("id", layer);
			layerFind =shoppingCartDraft.getSingleResult();
			
			
			
			TypedQuery<Polygon> querySurfaceItens = 
					entityManager.createNamedQuery(Polygon.SURFACE_ITEMS,
					Polygon.class);
			querySurfaceItens.setParameter("layerId", layerFind);

			if (startPosition != null && maxResult != null) {
				querySurfaceItens.setFirstResult(startPosition * maxResult);
				querySurfaceItens.setMaxResults(maxResult);
			} else {
				querySurfaceItens.setFirstResult(1 * 1);
				querySurfaceItens.setMaxResults(1);
			}

			List<Polygon> itens = querySurfaceItens.getResultList();


			
			
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
			
			
			 SimpleFilterProvider filterProvider = new SimpleFilterProvider();
			 //filterProvider.addFilter("parcelFilter",
			 //        SimpleBeanPropertyFilter.serializeAll());
		      filterProvider.addFilter("abstractGeometryFilter",
		              SimpleBeanPropertyFilter.serializeAllExcept("enabled","enabled","iconflag","medias","strings"));
		      //filterProvider.addFilter("surfaceFilter",
		      //        SimpleBeanPropertyFilter.serializeAllExcept("enabled","iconflag","medias","strings","area","parcels","maxX","maxY","minX","minY","perimetro"));

		      filterProvider.addFilter("layerFilter",
		              SimpleBeanPropertyFilter.serializeAllExcept("enabled","enabled","image",
		            		  "dtype","name","description","icon","fields","selected",
		            		  "clientComponentEntity","geometrias","layerCat","childrensCategories",
		            		  "layerCategories","iconcategory","epsg","containers","strings",
		            		  "centroidwkt","zoom","minres","maxres","editable",
		            		  "scriptStyle","orderlayer","geometryStyle","labelStyle",
		            		  "hibernateLazyInitializer"));
			
			
			
			String dtoAsString = mapper.writer(filterProvider).writeValueAsString(itens);

			return Response.ok(dtoAsString, MediaType.APPLICATION_JSON).build();
			

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

}