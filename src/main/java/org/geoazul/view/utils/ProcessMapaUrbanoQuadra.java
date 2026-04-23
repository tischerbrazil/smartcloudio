package org.geoazul.view.utils;

import java.awt.Color;
import java.awt.Dimension;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import javax.xml.parsers.ParserConfigurationException;
import org.geoazul.model.basic.AbstractGeometry;
import org.geoazul.model.basic.Linestring;
import org.geoazul.model.basic.MultiPolygon;
import org.geoazul.model.basic.Point;
import org.geoazul.model.basic.Polygon;
import org.geoazul.model.basic.endpoints.GenerateSVG;
import org.geoazul.model.basic.rural.PolygonPoint;
import org.geoazul.model.website.UrlPostItem;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.FeatureLayer;
import org.geotools.map.MapContent;
import org.geotools.map.MapViewport;
import org.geotools.referencing.CRS;

import org.geotools.styling.SLD;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.filter.expression.PropertyName;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.api.style.AnchorPoint;
import org.geotools.api.style.Displacement;
import org.geotools.api.style.FeatureTypeStyle;
import org.geotools.api.style.Fill;
import org.geotools.api.style.Font;
import org.geotools.api.style.Halo;
import org.geotools.api.style.PointPlacement;
import org.geotools.api.style.PolygonSymbolizer;
import org.geotools.api.style.LineSymbolizer;
import org.geotools.api.style.Rule;

import org.geotools.api.style.Stroke;
import org.geotools.api.style.Style;
import org.geotools.api.style.StyleFactory;
import org.geotools.api.style.StyledLayerDescriptor;
import org.geotools.api.style.TextSymbolizer;
import org.geotools.api.style.UserLayer;
import org.keycloak.example.oauth.UserData;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import org.w3c.dom.Node;

public class ProcessMapaUrbanoQuadra {

	private Geometry buffer(Geometry geometry, double distance) {
		// Geometry pGeom = JTS.transform(geom, transform)
		Geometry pBuffer = geometry.buffer(distance);
		return pBuffer;
	}

	public String startMapEngineUrbanoQuadra(String logoUrl, String scale, String modelo, String colorPopup,
			Integer grouplayer, boolean fundomap, boolean automatico, String paper, Polygon obraSIGEF,
			EntityManager entityManager, String urlBBOX, String bBOX, String saveFileProcessFilePath_,
			String saveFileProcessFilePathUrl_, String Center, Integer widthModelPage, Integer heightModelPage,
			boolean secondTable, String geoazulURL) throws FileNotFoundException, IOException {

		colorPopup = "#FFFFFF";


		CoordinateReferenceSystem sourceCRS = null;
		try {
			sourceCRS = CRS.decode("EPSG:" + obraSIGEF.getLayer().getEpsg());
		} catch (FactoryException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		Polygon father = entityManager.find(Polygon.class, obraSIGEF.getFather().getId());

		// Envelope father_env_buffer = father.getGeometry().getEnvelopeInternal();

			Envelope father_env_buffer = buffer(father.getGeometry(), 16).getEnvelopeInternal();
		Coordinate centro = father_env_buffer.centre();
		// ----------

		double x1 = father_env_buffer.getMinX();
		double y1 = father_env_buffer.getMinY();
		double x2 = father_env_buffer.getMaxX();
		double y2 = father_env_buffer.getMaxY();


		int dimen = 0;
		if ((x2 - x1) / (y2 - y1) < 1.5) {
			dimen = (int) ((y2 - y1) / 4);

			x1 = centro.x - (dimen * 3);
			x2 = centro.x + (dimen * 3);

			y1 = centro.y - dimen * 2;
			y2 = centro.y + dimen * 2;

		} else {
		}




		
		String destinationFile = saveFileProcessFilePath_ + "/backgroundUTMQuadra.svg";
		// ==========================================XXXXXXXXXXXXXXXXXXXXXXXXX

		// -------------------------------------------------------------------------------------------------------

		// ====================================================

		SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
		try {
			tb.add("geometry", Point.class, CRS.decode("EPSG:" + obraSIGEF.getLayer().getEpsg()));
		} catch (FactoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		tb.add("nome", String.class);
		tb.setName("Laercio");

		tb.add("name", String.class);
		tb.setName("xyz");

		tb.add("anchorpointx", Double.class);
		tb.setName("0");
		tb.add("anchorpointy", Double.class);
		tb.setName("0");
		tb.add("displacementx", Integer.class);
		tb.setName("0");
		tb.add("displacementy", Integer.class);
		tb.setName("0");
		tb.add("rotation", Integer.class);
		tb.setName("0");

		SimpleFeatureType schema = tb.buildFeatureType();

		// ====================================================

		DefaultFeatureCollection fc = new DefaultFeatureCollection();

		DefaultFeatureCollection fc4 = new DefaultFeatureCollection();

		DefaultFeatureCollection fc7 = new DefaultFeatureCollection();

		DefaultFeatureCollection fc9 = new DefaultFeatureCollection();

		SimpleFeatureBuilder fb = null;

		SimpleFeatureBuilder fb4 = null;

		SimpleFeatureBuilder fb7 = null;

		SimpleFeatureBuilder fb9 = null;

		fb = new SimpleFeatureBuilder(schema);

		fb.add(obraSIGEF.getGeometry()); // comverterGeometry

		fb.add(obraSIGEF.getNome());

		fc.add(fb.buildFeature(obraSIGEF.getId().toString()));

		if (father instanceof Polygon) {

			Polygon polygonRI = (Polygon) father;

			List<PolygonPoint> ddd = polygonRI.getVerticeObrasList();

			for (PolygonPoint p : ddd) {

				fb4 = new SimpleFeatureBuilder(schema);

				fb7 = new SimpleFeatureBuilder(schema);

				fb4.add(p.getGeometry()); // comverterGeometry
				fb4.add(p.getDescritivo());
				fb4.add(p.getDescritivo());

				fb4.add(p.getAnchorpointx() / 10);
				fb4.add(p.getAnchorpointy() / 10);
				fb4.add(p.getDisplacementx() / 10);
				fb4.add(p.getDisplacementy() / 10);
				fb4.add(p.getRotation() / 10);

				fc4.add(fb4.buildFeature(p.getPointri().getId().toString()));
				fb4.reset();

				if (p.getPrintConf()) {

					fb7.add(p.getGeometry()); // comverterGeometry
					fb7.add(p.getDescritivo());
					fb7.add(p.getDescritivo());

					fb7.add(0.5);
					fb7.add(0);

					if (p.getAngle() <= 90) {
						fb7.add(0);
						fb7.add(10);
						fb7.add(p.getAngle() - 90);
					} else if (p.getAngle() > 90 && p.getAngle() <= 180) {
						fb7.add(0);
						fb7.add(10);
						fb7.add(p.getAngle() - 90);
					} else if (p.getAngle() > 180 && p.getAngle() <= 270) {
						fb7.add(0);
						fb7.add(-10);
						fb7.add(p.getAngle() + 90);
					} else {
						fb7.add(0);
						fb7.add(-10);
						fb7.add(p.getAngle() - 270);
					}
					fc7.add(fb7.buildFeature(p.getPointri().getId().toString() + p.getPolygonri().getId()));
					fb7.reset();
				}

			}
		}

		// ---------------
		String consultax = "SELECT * " + "FROM   app_polygon surface " + "WHERE public.ST_Intersects(surface.geometry,  "
				+ "public.ST_Transform(public.st_envelope(public.ST_GeomFromText('LINESTRING(" + x1 + " " + y1 + ","
				+ x2 + " " + y2 + ")','" + obraSIGEF.getLayer().getEpsg() + "')), " + obraSIGEF.getLayer().getEpsg()
				+ ")" + "   )  AND surface.layer_id = " + obraSIGEF.getLayer().getId() + " AND surface.id <> '"
				+ obraSIGEF.getId() + "'";

		// ---------------
		Query findAllQuery = entityManager.createNativeQuery(consultax, Polygon.class);
		final List<AbstractGeometry> resultsPOLY = father.getChildrens();
		DefaultFeatureCollection fc8 = new DefaultFeatureCollection();
		SimpleFeatureBuilder fb8 = null;
		for (AbstractGeometry poly : resultsPOLY) {
			
			if (poly instanceof Polygon) {
			}else if (poly instanceof Point ) {
}else if (poly instanceof Linestring ) {
}else if (poly instanceof MultiPolygon ) {
}else if (poly instanceof UrlPostItem ) {
}
			
			
			

			
			
			Polygon poli = (Polygon) poly;

			if (poli.getLayer().getId() == obraSIGEF.getLayer().getId()) {

				fb8 = new SimpleFeatureBuilder(schema);
				fb8.add(poli.getGeometry()); // comverterGeometry
				fb8.add(poli.getNome());
				fb8.add(poli.getNome());
				fb8.add(0);
				fb8.add(0);
				fb8.add(0);
				fb8.add(0);
				fb8.add(0);
				fc8.add(fb8.buildFeature(poly.getId().toString()));
				fb8.reset();

				fb9 = new SimpleFeatureBuilder(schema);
				fb9.add(poli.getGeometry()); // comverterGeometry
				fb9.add(poli.getNome());
				fb9.add(poli.getNome());
				fb9.add(0.5);
				fb9.add(0);
				fb9.add(0);
				fb9.add(-7);
				fb9.add(0);
				fc9.add(fb9.buildFeature(poly.getId().toString()));
				fb9.reset();

			}

		}

		// ------------


		// Step 1: Create map
		MapContent map = new MapContent();
		map.setTitle("MAPA UTM");

		// Step 2: Set projection
		// CoordinateReferenceSystem crs = CRS.decode("EPSG:3857"); //Conic projection
		// over US
		MapViewport vp = map.getViewport();
		vp.setCoordinateReferenceSystem(sourceCRS);


		ReferencedEnvelope dataArea = new ReferencedEnvelope(x1, x2, y1, y2, sourceCRS);
		// --------------------

		vp.setBounds(dataArea);
		// Step 3: Add layers to map

		Style stylePolygon = SLD.createPolygonStyle(Color.GRAY, Color.LIGHT_GRAY, 1.0f);

		Style stylePoint = SLD.createPointStyle("circle", Color.GRAY, Color.WHITE, 1.0f, 15.1f);

		Style styleLine = SLD.createLineStyle(Color.BLACK, 0.4f);

		// ---------------------------USER STYLE
		StyleFactory sf = CommonFactoryFinder.getStyleFactory();

		FilterFactory ff = CommonFactoryFinder.getFilterFactory();
		Stroke strocado = sf.createStroke(ff.literal(Color.LIGHT_GRAY), ff.literal(0.1));
		// strocado.setDashArray(Arrays.asList((Expression) ff.literal("3 3")) );

		// Fill filado = sf.createFill(ff.literal(Color.BLUE), ff.literal(1.0));

		PolygonSymbolizer featureTypeStyle33 = sf.createPolygonSymbolizer();
		featureTypeStyle33.setStroke(strocado);
		// featureTypeStyle33.setFill(filado);

		FeatureTypeStyle featureTypeStyle333 = sf.createFeatureTypeStyle();

		Rule rulado = sf.createRule();
		rulado.symbolizers().add(featureTypeStyle33);

		featureTypeStyle333.rules().add(rulado);

		Style style33 = sf.createStyle();
		style33.setName("style33");
		style33.featureTypeStyles().add(featureTypeStyle333);


		StyledLayerDescriptor sld = sf.createStyledLayerDescriptor();
		sld.setName("mapa_vertice_marco_label");
		sld.setTitle("azure square point style");
		sld.setAbstract("Label Style Layer Descriptor");

		UserLayer layer = sf.createUserLayer();
		layer.setName("layer");

		// -------------------------------------------------------------------------------------------------
		// RULE

		FeatureTypeStyle featureTypeStyle = sf.createFeatureTypeStyle();

		// RULE 1
		// first rule to draw cities
		Rule rule1 = sf.createRule();
		rule1.setName("rule1");

		TextSymbolizer textSymbolizer = sf.createTextSymbolizer();

		PropertyName propNameOgc = ff.property("nome");

		PropertyName anchorpointxOgc = ff.property("anchorpointx");
		PropertyName anchorpointyOgc = ff.property("anchorpointy");
		PropertyName displacementxOgc = ff.property("displacementx");
		PropertyName displacementyOgc = ff.property("displacementy");
		PropertyName rotationOgc = ff.property("rotation");

		textSymbolizer.setLabel(propNameOgc);

		Expression fontStyle = ff.literal("normal");
		Expression fontSize = ff.literal(9);
		Expression fontFamily = ff.literal("DejaVu Sans");
		Expression fontWeight = ff.literal("normal");
		Font font = sf.createFont(fontFamily, fontStyle, fontWeight, fontSize);

		textSymbolizer.setFont(font);

		Expression rotation = rotationOgc; // use default

		Expression anchor_x = anchorpointxOgc;
		Expression anchor_y = anchorpointyOgc;
		AnchorPoint anchorPoint = sf.createAnchorPoint(anchor_x, anchor_y);

		Expression displ_x = displacementxOgc;
		Expression disp_y = displacementyOgc;
		Displacement displacementPoint = sf.createDisplacement(displ_x, disp_y);

		PointPlacement pointPlacement = sf.createPointPlacement(anchorPoint, displacementPoint, rotation);

		textSymbolizer.setLabelPlacement(pointPlacement);

		Fill fill = sf.fill(null, ff.literal(Color.WHITE), ff.literal(1.0));
		Expression radius = ff.literal(1);
		Halo halo = sf.createHalo(fill, radius);
		textSymbolizer.setHalo(halo);

		Fill fill2 = sf.fill(null, ff.literal(Color.BLACK), ff.literal(1.0));
		textSymbolizer.setFill(fill2);

		rule1.symbolizers().add(textSymbolizer);
		featureTypeStyle.rules().add(rule1);

		Style style8 = sf.createStyle();
		style8.setName("style");
		style8.featureTypeStyles().add(featureTypeStyle);
		
		
		
		//--------------------------------------------------------
		//-------------------------------------------------------
		FeatureTypeStyle featureTypeStylePoly = sf.createFeatureTypeStyle();

		// RULE 1
		// first rule to draw cities
		Rule rulePoly = sf.createRule();
		rulePoly.setName("rulePoly");

		TextSymbolizer textSymbolizerPoly = sf.createTextSymbolizer();

		PropertyName propNameOgcPoly = ff.property("nome");

	
		textSymbolizerPoly.setLabel(propNameOgcPoly);

		Expression fontStylePoly = ff.literal("normal");
		Expression fontSizePoly = ff.literal(6);
		Expression fontFamilyPoly = ff.literal("DejaVu Sans");
		Expression fontWeightPoly = ff.literal("normal");
		Font fontPoly = sf.createFont(fontFamilyPoly, fontStylePoly, fontWeightPoly, fontSizePoly);


		textSymbolizerPoly.setFont(fontPoly);

		Expression rotationPoly = ff.literal(0.0f);
		
		Expression anchor_xPoly = ff.literal(0.5f);
		Expression anchor_yPoly = ff.literal(0.5f);;
		AnchorPoint anchorPointPoly = sf.createAnchorPoint(anchor_xPoly, anchor_yPoly);
		Expression displ_xPoly = ff.literal(0.0f);
		Expression disp_yPoly = ff.literal(0.0f);
		Displacement displacementPointPoly = sf.createDisplacement(displ_xPoly, disp_yPoly);
		PointPlacement pointPlacementPoly = sf.createPointPlacement(anchorPointPoly, displacementPointPoly, rotationPoly);

		textSymbolizerPoly.setLabelPlacement(pointPlacementPoly);

		Fill fillPoly = sf.fill(null, ff.literal(Color.WHITE), ff.literal(1.0));
		Expression radiusPoly = ff.literal(1);
		Halo haloPoly = sf.createHalo(fillPoly, radiusPoly);
		textSymbolizerPoly.setHalo(haloPoly);

		Fill fill2Poly = sf.fill(null, ff.literal(Color.BLACK), ff.literal(1.0));
		textSymbolizerPoly.setFill(fill2Poly);

		rulePoly.symbolizers().add(textSymbolizerPoly);
		featureTypeStylePoly.rules().add(rulePoly);



Style stylePontoCirculoPolygon = sf.createStyle();
stylePontoCirculoPolygon.setName("style");
stylePontoCirculoPolygon.featureTypeStyles().add(featureTypeStylePoly);
		
		

		/*
		 * GeoEJB 3
		 * 
		 * Copyright 2018 GeoEJB, Inc. and/or its affiliates Orders for print layers in
		 * the pdf map file!
		 */

		if (fc.size() > 0) {
			FeatureLayer layerPolygon = new FeatureLayer(fc, stylePolygon);
			layerPolygon.setTitle("LAYER_POLYGON");
			map.addLayer(layerPolygon);
		}

		// -------
		if (fc4.size() > 0) {
			FeatureLayer layerLinestring = new FeatureLayer(fc4, styleLine);
			layerLinestring.setTitle("LAYER_LINESTRING");
			map.addLayer(layerLinestring);
		}

		if (fc7.size() > 0) {
			FeatureLayer layerLinestringLabel7 = new FeatureLayer(fc7, style8);
			layerLinestringLabel7.setTitle("LAYER_LINESTRING_LABEL");
			map.addLayer(layerLinestringLabel7);
		}

		if (fc8.size() > 0) {
			FeatureLayer layerPolygonGround = new FeatureLayer(fc8, style33); // style33
			layerPolygonGround.setTitle("LAYER_POLYGON_GROUND");
			map.addLayer(layerPolygonGround);
		}

		if (fc9.size() > 0) {
			FeatureLayer layerCirclePolygon = new FeatureLayer(fc9, stylePoint);
			layerCirclePolygon.setTitle("LAYER_POLYGON_LABEL");
			map.addLayer(layerCirclePolygon);

			FeatureLayer layerPolygonLabel = new FeatureLayer(fc9, stylePontoCirculoPolygon);
			layerPolygonLabel.setTitle("LAYER_POLYGON_LABEL");
			map.addLayer(layerPolygonLabel);
		}

		GenerateSVG generateSVG = new GenerateSVG();

		Dimension canvasSize = new Dimension(330, 220);
		OutputStream out = null;

		File f1 = new File(destinationFile);
		OutputStream out8 = new FileOutputStream(f1);

		try {
			GenerateSVG.exportSVG(map, dataArea, out8, canvasSize);
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block

			e.printStackTrace();
		}

		return "";
	}

}