package org.geoazul.model.basic.endpoints;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.List;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.transaction.Transactional;
import org.geoazul.model.Contador;
import org.geotools.referencing.CRS;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geojson.feature.FeatureJSON;
import org.geotools.geojson.geom.GeometryJSON;
import org.locationtech.jts.io.ParseException;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.SchemaException;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.geometry.MismatchedDimensionException;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.NoSuchAuthorityCodeException;
import org.geotools.api.referencing.operation.TransformException;

/** 
 * 
 */

@SuppressWarnings("serial")
@Path("/oauth2")
public class VisitorsEndpoint implements Serializable {

	@Inject 
	EntityManager entityManager;

	@POST
	@RolesAllowed("administrator")
	@Consumes("application/json")
	@Produces("application/json")
	@Path("/visitors")
	@Transactional
	public String create(Contador entity) {
		Response resposta = null;
		try {
			entityManager.persist(entity);
			entityManager.flush();
			resposta = Response.created(
					UriBuilder.fromResource(VisitorsEndpoint.class).path(String.valueOf(entity.getId())).build())
					.build();
		} catch (Exception e) {
		} 
		
		if (resposta.getStatusInfo().toString().equals("Created")) {
			return "{\"id\":\"" + entity.getId().toString() + "\"}";
		} else {
			return resposta.getStatusInfo().toString();
		}

	}


	@GET
	@RolesAllowed("administrator")
	@Produces("application/json")
	@Path("/visitors/{realm}")
	public String create(
			@PathParam("realm") String realm,
			@QueryParam("bbox") String bBox, 
			@QueryParam("typename") String typeName,
			@QueryParam("start") Integer startPosition, 
			@QueryParam("max") Integer maxResult)
			throws ParseException, NoSuchAuthorityCodeException, FactoryException, IOException, SchemaException,
			MismatchedDimensionException, TransformException {
	
		

		try {
			
			Integer appSRID = 3857;
			Integer layerSRID = 4326;
	
			double x1 = 0.0;
			double y1 =  0.0;
			double x2 =  0.0;
			double y2 =  0.0;
						
			String[] parts = null;
			String part1 = null;
			String part2 = null;
			String part3 = null;
			String part4 = null;
			String consultax = null;
			if (bBox == null){
			consultax = "select " 
		        + "id as id," 
					
	+ "realm_id as realm_id," 
	+ "session as session," 
	+ "datetime as datetime," 
	+ "ip as ip," 
	+ "gcmid as gcmid," 
	+ "country as country," 
	+ "country_iso as country_iso,"
	+ "subdivision as subdivision," 
	+ "subdivision_iso as subdivision_iso," 
	+ "city as city," 
	+ "postal as postal," 
	+ "public.st_transform(public.st_geomfromtext(public.st_astext(geometry), " +  layerSRID + "), " +  appSRID + ")  as geometry "
		        + "from IN_CONTADOR " 
		        + "WHERE geometry is not null "
		        //+ "AND realm=" + realm  
		        + "  LIMIT 5000 ";
			}else {
				 parts = bBox.split(",");
				 part1 = parts[0];
				 part2 = parts[1];
				 part3 = parts[2];
				 part4 = parts[3];
				 consultax = null;
				 x1 = Double.valueOf(part1).doubleValue();
				 y1 = Double.valueOf(part2).doubleValue();
				 x2 = Double.valueOf(part3).doubleValue();
				 y2 = Double.valueOf(part4).doubleValue();
				
					consultax = "select " 
 						+ "id as id," 
							
+ "realm_id as realm_id," 
+ "session as session," 
+ "datetime as datetime," 
+ "ip as ip," 
+ "gcmid as gcmid," 
+ "country as country," 
+ "country_iso as country_iso,"
+ "subdivision as subdivision," 
+ "subdivision_iso as subdivision_iso," 
+ "city as city," 
+ "postal as postal," 		
+ "public.st_transform(public.st_geomfromtext(public.st_astext(geometry), " +  layerSRID + "), " +  appSRID + ")  as geometry "
 						+ "from IN_CONTADOR " 
 						+ "WHERE geometry is not null "
 						//+ "AND realm=" + realm
 
+ "AND  public.ST_Intersects(geometry,  "
+ "public.ST_Transform(public.st_envelope(public.ST_GeomFromText('LINESTRING(" + x1 + " " + y1 + "," + x2 + " " + y2
+ ")','"  + "4326" +  "'))," + "4326" + ")"
+ "   )   ";
						
						
						//+ " AND  public.ST_Intersects(geometry,  "
						//+ "public.st_envelope(public.ST_GeomFromText('LINESTRING(" + x1 + " " + y1 + "," + x2 + " " + y2
						//+ ")') ) )";
					}
			Query findAllQuery33 = entityManager.createNativeQuery(consultax, Contador.class);
		
			List<Contador> results = findAllQuery33.getResultList();
			
			if (results.size() > 0) {

				
				
					// ====================================================
					SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
					
					try {
						tb.add("geometry", Contador.class, CRS.decode("EPSG:4326"));
					} catch (FactoryException e) {
					}

					tb.add("nome", String.class);
					tb.setName("Laercio");
					
					tb.add("iconflag", String.class);
					tb.setName("icon");

					tb.add("name", String.class);
					tb.setName("xyz");

					SimpleFeatureType schema = tb.buildFeatureType();

					DefaultFeatureCollection fc = new DefaultFeatureCollection();

					SimpleFeatureBuilder fb = null;
					for (Contador contador : results) {
						fb = new SimpleFeatureBuilder(schema);
						fb.add(contador.getGeometry());
						fb.add(contador.getCity());
						fb.add("maki-star");
						fc.add(fb.buildFeature(contador.getId().toString()));
					}
					
					int decimals = 14;
					GeometryJSON gjson = new GeometryJSON(decimals);
						
					FeatureJSON fj = new FeatureJSON(gjson);
					StringWriter writer88 = new StringWriter();
					fj.writeFeatureCollection(fc, writer88);

					return writer88.toString();

			} else {
				return "{\"type\":\"FeatureCollection\",\"crs\":{\"type\":\"name\",\"properties\":{\"name\":\"EPSG:31980\"}},\"features\":[]}";
			}

		} catch (Throwable e) {
	e.printStackTrace();
				return "{\"type\":\"FeatureCollection\",\"crs\":{\"type\":\"name\",\"properties\":{\"name\":\"EPSG:31980\"}},\"features\":[]}";
		
		} 

	}

}