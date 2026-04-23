package org.geoazul.model.basic.endpoints;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.List;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import org.geoazul.model.basic.Layer;
import org.geoazul.model.basic.rural.PolygonPoint;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.geometry.MismatchedDimensionException;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.NoSuchAuthorityCodeException;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.api.referencing.operation.MathTransform;
import org.geotools.api.referencing.operation.TransformException;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geojson.feature.FeatureJSON;
import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;

import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.ParseException;

/**
 * 
 */

@SuppressWarnings("serial")
@Path("/polygons/polygonripoints")
public class PolygonPointEndpolygonRIPoint implements Serializable {

	@Inject 
	EntityManager entityManager;

	@GET
	@RolesAllowed("gis")
	@Produces("application/json")
	@Path("/polygons/{realm}/{layer}")
	public String create(@PathParam("realm") String realm, 
			@PathParam("layer") String layer, 
			@QueryParam("bbox") String bBox, 
			@QueryParam("typename") String typeName,
			@QueryParam("start") Integer startPosition,
			@QueryParam("max") Integer maxResult) throws ParseException, NoSuchAuthorityCodeException, FactoryException, IOException, SchemaException, MismatchedDimensionException, TransformException {
			Layer layerFind = null;

			try {
			
			Integer layerid_int = Integer.parseInt(layer);
			layerFind = entityManager.find(Layer.class, layerid_int ); 
			
			
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
				  consultax = "SELECT surface.* " + "FROM   app_polygonripointri surface, " + "   app_polygon comp "
							+ "WHERE surface.geometry is not null  "
							+ "AND surface.polygonri_id = comp.id " + "AND  comp.layer_id = " + layer;
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
				  consultax = "SELECT surface.* " + "FROM   app_polygonripointri surface, " + "   app_polygon comp "
							+ "WHERE surface.geometry is not null "
							+ "AND surface.polygonri_id = comp.id " + "AND  public.ST_Intersects(surface.geometry,  "
							+ "public.ST_Transform(public.st_envelope(public.ST_GeomFromText('LINESTRING(" + x1 + " " + y1 + "," + x2 + " " + y2
							+ ")','3857'))," + layerFind.getEpsg() + ")"
							+ "   )  AND comp.layer_id = " + layer;
			}
			

			Query findAllQuery = entityManager.createNativeQuery(consultax, PolygonPoint.class);

		
			List<PolygonPoint> results = findAllQuery.getResultList();

		
		
		
		
		if ( layerFind.getApplicationEntity().getEpsg()  == 3857) {
		
	 		//====================================================
	 		
	 		SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
	        try {
				tb.add("geometry", PolygonPoint.class, CRS.decode("EPSG:3857"));
			} catch (FactoryException e) {
			}
	        
	        tb.add("nome", String.class);
	        tb.setName("Laercio");
	        
	        tb.add("name", String.class);
	        tb.setName("xyz");
	        
	        SimpleFeatureType schema = tb.buildFeatureType();

	        DefaultFeatureCollection fc = new DefaultFeatureCollection();

	        SimpleFeatureBuilder fb = null;
			
			for (PolygonPoint polygonRIPoint : results ) {
				   fb = new SimpleFeatureBuilder(schema); 
						fb.add(polygonRIPoint.getGeometry() );
						fb.add(polygonRIPoint.getId());
			      fc.add(fb.buildFeature(polygonRIPoint.getDescritivo()));
			}

			FeatureJSON fj = new FeatureJSON();
		    StringWriter writer88 = new StringWriter();
			fj.writeFeatureCollection(fc, writer88);
			return writer88.toString();

		}else{

		CoordinateReferenceSystem sourceCRS = CRS.decode("EPSG:4326"); 
 		CoordinateReferenceSystem targetCRS = CRS.decode("EPSG:3857");

 		MathTransform transform = CRS.findMathTransform(sourceCRS, targetCRS);
		
 		//====================================================
 		
 		SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
        try {
			tb.add("geometry", PolygonPoint.class, CRS.decode("EPSG:3857"));
		} catch (FactoryException e) {
			
		}
        
        tb.add("nome", String.class);
        tb.setName("Laercio");
        
        tb.add("name", String.class);
        tb.setName("xyz");

        SimpleFeatureType schema = tb.buildFeatureType();

        DefaultFeatureCollection fc = new DefaultFeatureCollection();
 		
 		//====================================================
        SimpleFeatureBuilder fb = null;
		
		for (PolygonPoint polygonRIPoint : results ) {
			
			 Geometry comverterGeometry = JTS.transform( polygonRIPoint.getGeometry() , transform);

			   		fb = new SimpleFeatureBuilder(schema); 
			   
					fb.add(comverterGeometry );
				
					fb.add(polygonRIPoint.getId());
				
					fc.add(fb.buildFeature(polygonRIPoint.getDescritivo()));

		}

	        FeatureJSON fj = new FeatureJSON();
	        StringWriter writer88 = new StringWriter();
			fj.writeFeatureCollection(fc, writer88);

			return writer88.toString();

	    }

	
			
	} catch (Throwable e) {
			return null;
	} 

}
			
			

}