package org.geoazul.model.basic.endpoints;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.List;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.transaction.Transactional;
import org.geoazul.model.basic.GeometryJsonModel;
import org.geoazul.model.basic.Layer;
import org.geoazul.model.basic.Linestring;
import org.geoazul.model.basic.Polygon;
import org.geoazul.view.utils.ConvertCoordinate;
import org.geotools.referencing.CRS;
import jsonb.JacksonUtil;
import org.locationtech.jts.geom.Geometry;
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
import org.locationtech.jts.io.WKTReader;
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

@Path("/oauth2")
public class MultiPolygonEndmultipolygon implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	EntityManager entityManager;

	@POST
	@RolesAllowed("gis")
	@Consumes("application/json")
	@Produces("application/json")
	@Path("/multipolygons/{id}")
	@Transactional
	public String create(@PathParam("id") String id, GeometryJsonModel geomodel) {
		
				
		
		Response resposta = null;
	

			try {
			

				TypedQuery<Layer> queryApp = entityManager.createNamedQuery(Layer.LAYER_ID, Layer.class);
				queryApp.setParameter("id",   Long.valueOf(geomodel.getLayerid()));
				Layer layer = queryApp.getSingleResult();
				
							
				int appSRID = layer.getApplicationEntity().getEpsg();
				Integer layerSRID = layer.getEpsg();
			
				WKTReader wktReader = new WKTReader();
				Geometry geometryGeom = null;
				try {
					geometryGeom = wktReader.read(geomodel.getGeometrywkt());
					geometryGeom.setSRID(appSRID);
				} catch (ParseException e) {
				}
				Geometry geometry = geometryGeom;

				Geometry geometryNEW = 	
						ConvertCoordinate.runConvertCoordinateGeometry(geometry, 
								appSRID, layerSRID, entityManager);
				//GeometryFactory geometryFactory = new GeometryFactory();
				//Geometry geometryNEW = geometryFactory.createPoint(converter);
				geometryNEW.setSRID(Integer.valueOf(layer.getEpsg()));
				
				Linestring entity = new Linestring();
				
				entity.setGeometry(geometryNEW);
				entity.setEnabled(true);
				entity.setStrings(JacksonUtil.toJsonNode("{}"));
				entity.setParte(layer.getGeometrias().size() + 1);
				Short sit = 1;
				entity.setSituacao(sit);
				
				entity.setLayer(layer);
				entityManager.persist(entity);
				entityManager.flush();
				
				resposta = Response.created(
						UriBuilder.fromResource(PointEndpoint.class).path(String.valueOf(entity.getId())).build())
						.build();

			
				if (resposta.getStatusInfo().toString().equals("Created")) {
					return "{\"id\":\"" + entity.getId().toString() + "\"}";
				} else {
					return resposta.getStatusInfo().toString();
				}
				
			} catch (Exception e) {
				return null;
			}
			
			

			
			
			

	}

	@GET
	@RolesAllowed("gis")
	@Produces("application/json")
	@Path("/multipolygons/{layer}")
	public String create(
			@PathParam("layer") Long layer,
			@QueryParam("bbox") String bBox, 
			@QueryParam("typename") String typeName,
			@QueryParam("start") Integer startPosition, 
			@QueryParam("max") Integer maxResult)
			throws ParseException, NoSuchAuthorityCodeException, FactoryException, IOException, SchemaException,
			MismatchedDimensionException, TransformException {
	
		Layer layerFind = null;

		try {
			// ===============================================
			TypedQuery<Layer> shoppingCartDraft = entityManager.createNamedQuery(Layer.LAYER_ID, Layer.class);
			shoppingCartDraft.setParameter("id", layer);
			layerFind =shoppingCartDraft.getSingleResult();
			
			Integer appSRID = layerFind.getApplicationEntity().getEpsg();
			Integer layerSRID = layerFind.getEpsg();
			Long layerId = layerFind.getId();
			
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
		        + "abstractge0_.id ," 
		        + "abstractge0_.enabled,"
		        + "abstractge0_.imovel_id,"
		        + "abstractge0_.origin_id,"
		        + "abstractge0_.iconflag,"
		        + "abstractge0_.layer_id,"
		        + "abstractge0_.nome,"
		        + "abstractge0_.parte,"
		        + "abstractge0_.situacao,"
		      //  + "abstractge0_.dtype,"
		        + "abstractge0_.field,"
		        + "public.st_transform(public.st_geomfromtext(public.st_astext(polygongeo1_.geometry), " +  layerSRID + "), " +  appSRID + ")  as geometry,"
		        + "0 as area,"
		        + "0 as perimetro "
		        + "from APP_GEOMETRY abstractge0_ " 
		        + "left outer join "
		        + "APP_MULTIPOLYGON polygongeo1_ "
		        + "on abstractge0_.id=polygongeo1_.id " 
		        + "WHERE polygongeo1_.geometry is not null and abstractge0_.enabled = true  "
		        + "and abstractge0_.situacao > 0 AND abstractge0_.layer_id=" + layerId
			    + " AND public.st_srid(polygongeo1_.geometry) = " + layerSRID; 
			
				
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
					        + "abstractge0_.id ," 
					        + "abstractge0_.enabled,"
					        + "abstractge0_.imovel_id,"
					        + "abstractge0_.origin_id,"
					        + "abstractge0_.iconflag,"
					        + "abstractge0_.layer_id,"
					        + "abstractge0_.nome,"
					        + "abstractge0_.parte,"
					        + "abstractge0_.situacao,"
					       // + "abstractge0_.dtype,"
					        + "abstractge0_.field,"
					        + "public.st_transform(public.st_geomfromtext(public.st_astext(polygongeo1_.geometry), " +  layerSRID + "), " +  appSRID + ")  as geometry,"
					        + "0 as area,"
					        + "0 as perimetro "
					    + "from APP_GEOMETRY abstractge0_ " 
					    + "left outer join "
					        + "APP_MULTIPOLYGON polygongeo1_ "
					            + "on abstractge0_.id=polygongeo1_.id " 
							    + "WHERE polygongeo1_.geometry is not null and abstractge0_.enabled = true  "
							    + "AND  public.ST_Intersects(polygongeo1_.geometry,  "
								+ "public.ST_Transform(public.st_envelope(public.ST_GeomFromText('LINESTRING(" + x1 + " " + y1 + "," + x2 + " " + y2
								+ ")','"  + appSRID.toString() +  "'))," + layerSRID.toString() + ")"
								+ "   )  AND "
								+ "abstractge0_.situacao > 0 AND abstractge0_.layer_id=" + layerId
								+ " AND public.st_srid(polygongeo1_.geometry) = " + layerSRID; 
					}
			
			
			Query findAllQuery33 = entityManager.createNativeQuery(consultax, Polygon.class);
		
			
			List<Polygon> results = findAllQuery33.getResultList();
			
			
			if (results.size() > 0) {

			
				
					// ====================================================
					SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
					tb.setName("MultiPolygon Geometry");
					try {
						tb.add("geometry", Polygon.class, CRS.decode("EPSG:" + appSRID.toString()));
					} catch (FactoryException e) {
					}

					tb.add("nome", String.class);
			
					tb.add("situacao", Integer.class);
			
					SimpleFeatureType schema = tb.buildFeatureType();

					DefaultFeatureCollection fc = new DefaultFeatureCollection();

					SimpleFeatureBuilder fb = null;
					for (Polygon polygon : results) {
						fb = new SimpleFeatureBuilder(schema);
						fb.add(polygon.getGeometry());
						fb.add(polygon.getNome());
						fb.add(polygon.getSituacao().intValue());
						fc.add(fb.buildFeature(polygon.getId().toString()));
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

		} catch (Exception e) {
			return "{\"type\":\"FeatureCollection\",\"crs\":{\"type\":\"name\",\"properties\":{\"name\":\"EPSG:31980\"}},\"features\":[]}";
		}
	}
}