package org.geoazul.view.utils;

import java.awt.Color;
import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import javax.xml.parsers.ParserConfigurationException;
import static modules.LoadInitParameter.*;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperRunManager;

import org.apache.batik.anim.dom.SAXSVGDocumentFactory;
import org.apache.batik.anim.dom.SVGDOMImplementation;
import org.apache.batik.dom.util.DOMUtilities;
import org.apache.batik.util.XMLResourceDescriptor;
import org.geoazul.model.app.ApplicationAttributeEntity;
import org.geoazul.model.basic.Linestring;
import org.geoazul.model.basic.endpoints.GenerateSVG;
import org.geoazul.model.basic.Polygon;
import org.geoazul.model.basic.rural.PolygonPoint;
import org.geoazul.model.website.media.Media;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.FeatureLayer;
import org.geotools.map.MapContent;
import org.geotools.map.MapViewport;
import org.geotools.referencing.CRS;
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
import org.geotools.styling.SLD;
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
import org.locationtech.jts.geom.Geometry;
import org.w3c.dom.Node;
import org.geotools.api.feature.simple.SimpleFeatureType;

public class ProcessMapaUrbano {

	@Inject
	private UserData userData;

	

	private Geometry buffer(Geometry geometry, double distance) {
		// Geometry pGeom = JTS.transform(geom, transform)
		Geometry pBuffer = geometry.buffer(distance);
		return pBuffer;
	}

	public String convertToASCII2(String text) {
		return text.replaceAll("[ãâàáä]", "a").replaceAll("[êèéë]", "e").replaceAll("[îìíï]", "i")
				.replaceAll("[õôòóö]", "o").replaceAll("[ûúùü]", "u").replaceAll("[ÃÂÀÁÄ]", "A")
				.replaceAll("[ÊÈÉË]", "E").replaceAll("[ÎÌÍÏ]", "I").replaceAll("[ÕÔÒÓÖ]", "O")
				.replaceAll("[ÛÙÚÜ]", "U").replace('ç', 'c').replace('Ç', 'C').replace('ñ', 'n').replace('Ñ', 'N');
	}

	public Media startMapEngineUrbano(String finalFilesUrl, Collection<ApplicationAttributeEntity> appatribs, String logoUrl, String scale, String reltype, String modelo, String colorPopup,
			Integer grouplayer, boolean fundomap, boolean automatico, String paper, Polygon obraSIGEF,
			EntityManager entityManager, String urlBBOX, String bBOX, String saveFileProcessFilePath_,
			String saveFileProcessFilePathUrl_, String Center, Integer widthModelPage, Integer heightModelPage,
			boolean secondTable, String geoazulPath, Integer widthSize, Integer heightSize,
			String geoserverLayer, String tenant)
			throws FileNotFoundException, IOException {

		
		
		String  mediaFilePath  =  saveFileProcessFilePathUrl_  +  "/";
		

		saveFileProcessFilePathUrl_ = geoazulPath + saveFileProcessFilePathUrl_ ;
		
		
		
		
		
		String returnStringUTM;
		boolean process = true;
		if (obraSIGEF.getFather() != null) {
			ProcessMapaUrbanoQuadra quadraGeoMapUrbano = new ProcessMapaUrbanoQuadra();
			
			returnStringUTM = quadraGeoMapUrbano.startMapEngineUrbanoQuadra(
				logoUrl, scale, modelo, colorPopup, 1,
				fundomap, false, paper, obraSIGEF, entityManager, urlBBOX, bBOX, saveFileProcessFilePath_,
				saveFileProcessFilePathUrl_, Center, widthModelPage, heightModelPage, secondTable, geoazulPath);
		
		
			
		
		}else {
			 process = false;
		}
		
		
		 

		

		if (process) {
			colorPopup = "#FFFFFF";

			// ----------

			CoordinateReferenceSystem sourceCRS = null;
			try {
				sourceCRS = CRS.decode("EPSG:" + obraSIGEF.getLayer().getEpsg());
			} catch (FactoryException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}

			// ----------


			String[] parts = bBOX.split(",");
			String part1 = parts[0];
			String part2 = parts[1];
			String part3 = parts[2];
			String part4 = parts[3];

			double x1 = Double.valueOf(part1).doubleValue();
			double y1 = Double.valueOf(part2).doubleValue();
			double x2 = Double.valueOf(part3).doubleValue();
			double y2 = Double.valueOf(part4).doubleValue();

			fundomap = false;
			
			if (fundomap) {

				// ----------------------------------------------------------------------------------------------------------------
				// 711363.0499955507 8508526.201675259 711549.0499955507 8508650.201675259

				String inFile = save_FILE_PATH + "/gis/lib/geo.map";
				String sReturn = "";
				try (BufferedReader br = new BufferedReader(new FileReader(inFile))) {
					String sCurrentLine;
					while ((sCurrentLine = br.readLine()) != null) {
						sReturn = sReturn + sCurrentLine + "\n";
					}
				} catch (IOException e) {
					e.printStackTrace();
				}



				String imageUrlGoogleGeo = geoazulPath + "/map.js" + "?id1=" + x1 + "&id2=" + y1 + "&id3=" + x2 + "&id4="
						+ y2 + "&width=" + widthModelPage + "&height=" + heightModelPage;

				String destinationFileGoogleGeo = "/ms4w/apps/tutorial/htdocs/geo.map";


				URL urlGoogle = null;

				try {
					urlGoogle = new URL(imageUrlGoogleGeo);
				} catch (MalformedURLException e1) {
					e1.printStackTrace();
				}
				InputStream isGoogle = null;
				try {
					isGoogle = urlGoogle.openStream();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				OutputStream osGoogle = null;

				try {


					osGoogle = new FileOutputStream(destinationFileGoogleGeo);
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}


				byte[] bGoogle = new byte[2048];
				int lengthGoogle;

				try {
					while ((lengthGoogle = isGoogle.read(bGoogle)) != -1) {
						osGoogle.write(bGoogle, 0, lengthGoogle);
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}

				try {
					isGoogle.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				try {
					osGoogle.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}

				// END IMAGEM DE REFERENCIA LOCALIZACAO
				// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

			} // end fundomap here

			// --------------------------------------------------------------------------------------------
			

			String urlFilePathQuadra = saveFileProcessFilePath_ +  "/backgroundUTMQuadra.svg";

			String destinationFile = saveFileProcessFilePath_ + "/backgroundUTM.svg";
			String destinationFile2 = saveFileProcessFilePath_ + "/backgroundUTMGrid.svg";
			String destinationFileHorizontal = saveFileProcessFilePath_ + "/backgroundUTM_H.svg";
			String destinationFileVertical = saveFileProcessFilePath_ + "/backgroundUTM_V.svg";

			// ==========================================XXXXXXXXXXXXXXXXXXXXXXXXX
			String nomarquivo = "";
			String newdestinationFileGoogleF = "";

			String destinationFileGoogleF = "";
			String newurldestinationFileGoogleF = "";

			try {

				// START PNG REFERENCE LOCATION IMAGE

				String newScale = "";
				if (scale.equals("445")) {
					newScale = "20";
				} else if (scale.equals("890")) {
					newScale = "19";
				} else if (scale.equals("1781")) {
					newScale = "18";
				} else if (scale.equals("3562")) {
					newScale = "17";
				} else if (scale.equals("7125")) {
					newScale = "16";
				} else if (scale.equals("14250")) {
					newScale = "15";
				} else if (scale.equals("28500")) {
					newScale = "14";
				} else if (scale.equals("57000")) {
					newScale = "13";
				} else if (scale.equals("114000")) {
					newScale = "12";
				} else if (scale.equals("228000")) {
					newScale = "11";
				}

				if (fundomap) {

					String[] partsx = Center.split(",");
					String latX = partsx[0];
					String longY = partsx[1];

					String destinationFileGoogle = saveFileProcessFilePath_ + "/backgroundUTM.png";



					Random random = new Random();
					int randomInt = random.nextInt(1000000000);
					
				
					String imageUrlGoogleF = userData.getGeoserverURL()  
							+ userData.getRealmEntity()
							+ "/wms?service="
							+ "WMS&version=1.1.0&request=GetMap&layers="
							+ userData.getRealmEntity() + "%3A" + 
							geoserverLayer
							+ "&bbox="
							+ x1 + "%2C" + y1 + "%2C" + x2 + "%2C" + y2 
							+ "&width=" + widthModelPage + "&height=" + heightModelPage 
							+ 	"&srs=EPSG%3A32720&format=image%2Fpng";

					
					// LINK
					nomarquivo = "/backgroundUTMF_" + String.valueOf(randomInt) + ".png";
					destinationFileGoogleF = saveFileProcessFilePath_ + nomarquivo;
					newdestinationFileGoogleF = saveFileProcessFilePath_ + nomarquivo;




					newurldestinationFileGoogleF = saveFileProcessFilePathUrl_ + "/" + nomarquivo;

					URL urlGoogleF = null;
					try {
						urlGoogleF = new URL(imageUrlGoogleF);
					} catch (MalformedURLException e1) {
						e1.printStackTrace();
					}
					InputStream isGoogleF = null;
					try {
						isGoogleF = urlGoogleF.openStream();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					OutputStream osGoogleF = null;
					try {
						osGoogleF = new FileOutputStream(destinationFileGoogleF);
					} catch (FileNotFoundException e1) {
						e1.printStackTrace();
					}


					byte[] bGoogleF = new byte[2048];
					int lengthGoogleF;

					try {
						while ((lengthGoogleF = isGoogleF.read(bGoogleF)) != -1) {
							osGoogleF.write(bGoogleF, 0, lengthGoogleF);
						}
					} catch (IOException e1) {
						e1.printStackTrace();
					}

					try {
						isGoogleF.close();
					} catch (IOException e1) {
					}
					try {
						osGoogleF.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}

				} // end fundomap2

			} catch (Exception e1) {
				e1.printStackTrace();
			}

	
			// ===========================================XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX

			// -------------------------------------------------------------------------------------------------------

			// -------------------------------------------------------------------------------------------------------

			// ====================================================

			SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
			try {
				tb.add("geometry", Geometry.class, CRS.decode("EPSG:" + obraSIGEF.getLayer().getEpsg()));
			} catch (FactoryException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			tb.add("nome", String.class);
			tb.setName("nome");

			tb.add("name", String.class);
			tb.setName("name");

			tb.add("anchorpointx", double.class);
			tb.setName("anchorpointx");
			tb.add("anchorpointy", double.class);
			tb.setName("anchorpointy");
			tb.add("displacementx", Integer.class);
			tb.setName("displacementx");
			tb.add("displacementy", Integer.class);
			tb.setName("displacementy");
			tb.add("rotation", Integer.class);
			tb.setName("rotation");

			SimpleFeatureType schema = tb.buildFeatureType();

			DefaultFeatureCollection fc = new DefaultFeatureCollection();

			// ====================================================
			SimpleFeatureBuilder fb = null;

			fb = new SimpleFeatureBuilder(schema);

			fb.add(obraSIGEF.getGeometry()); // comverterGeometry

			fb.add(obraSIGEF.getNome());
			fb.add(obraSIGEF.getNome());

			fb.add(0.5);
			fb.add(0);
			fb.add(0);
			fb.add(-7);
			fb.add(0);

			fc.add(fb.buildFeature(obraSIGEF.getId().toString()));

			DefaultFeatureCollection fc2 = new DefaultFeatureCollection();
			DefaultFeatureCollection fc3 = new DefaultFeatureCollection();

			DefaultFeatureCollection fc4 = new DefaultFeatureCollection();
			DefaultFeatureCollection fc5 = new DefaultFeatureCollection();

			DefaultFeatureCollection fc7 = new DefaultFeatureCollection();

			SimpleFeatureBuilder fb2 = null;
			SimpleFeatureBuilder fb3 = null;

			SimpleFeatureBuilder fb4 = null;
			SimpleFeatureBuilder fb5 = null;

			SimpleFeatureBuilder fb7 = null;

			if (obraSIGEF instanceof Polygon) {

				List<PolygonPoint> ddd = obraSIGEF.getVerticeObrasList();

				for (PolygonPoint p : ddd) {

					fb2 = new SimpleFeatureBuilder(schema);
					fb3 = new SimpleFeatureBuilder(schema);

					fb4 = new SimpleFeatureBuilder(schema);
					fb5 = new SimpleFeatureBuilder(schema);

					fb7 = new SimpleFeatureBuilder(schema);

					fb2.add(p.getPointri().getGeometry()); // comverterGeometry
					fb2.add(p.getPointri().getNome());
					fb2.add(p.getPointri().getNome());

					fc2.add(fb2.buildFeature(p.getPointri().getId().toString()));
					fb2.reset();

					if (p.getPrintMarco()) {

						fb3.add(p.getPointri().getGeometry()); // comverterGeometry
						fb3.add(p.getMarcoLabel());
						fb3.add(p.getMarcoLabel());
						if (p.getAngle() <= 90) {
							fb3.add(0); // ->
							fb3.add(0);
							fb3.add(10);
							fb3.add(-5); // -20
						} else if (p.getAngle() > 90 && p.getAngle() <= 180) {
							fb3.add(0);
							fb3.add(0);
							fb3.add(10);
							fb3.add(-5);
						} else if (p.getAngle() > 180 && p.getAngle() <= 270) {
							fb3.add(0);
							fb3.add(0);
							fb3.add(10);
							fb3.add(-5);
						} else {
							fb3.add(0);
							fb3.add(0);
							fb3.add(10);
							fb3.add(-5);
						}
						fb3.add(0);
						fc3.add(fb3.buildFeature(p.getPointri().getId().toString()));
						fb3.reset();
					}
					fb4.add(p.getGeometry()); // comverterGeometry
					fb4.add(p.getDescritivo());
					fb4.add(p.getDescritivo());
					fc4.add(fb4.buildFeature(p.getPointri().getId().toString()));
					fb4.reset();
					if (p.getPrintDist()) {
						fb5.add(p.getGeometry()); // comverterGeometry
						fb5.add(p.getDistancia() + " m");
						fb5.add(p.getDistancia() + " m");
						// UDA QUI 33
						if (p.getAngle() <= 90) {
							fb5.add(0.5);
							fb5.add(0);
							fb5.add(0);
							fb5.add(-30);
							fb5.add(p.getAngle() - 90);
						} else if (p.getAngle() > 90 && p.getAngle() <= 180) {
							fb5.add(0.5);
							fb5.add(0);
							fb5.add(0);
							fb5.add(-30);
							fb5.add(p.getAngle() - 90);
						} else if (p.getAngle() > 180 && p.getAngle() <= 270) {
							fb5.add(0.5);
							fb5.add(0);
							fb5.add(0);
							fb5.add(20);
							fb5.add(p.getAngle() + 90);
						} else {
							fb5.add(0.5);
							fb5.add(0);
							fb5.add(0);
							fb5.add(20);
							fb5.add(p.getAngle() - 270);
						}

						fc5.add(fb5.buildFeature(p.getPointri().getId().toString()));
						fb5.reset();

					}

					if (p.getPrintConf()) {

						fb7.add(p.getGeometry()); // comverterGeometry
						fb7.add(p.getDescritivo());
						fb7.add(p.getDescritivo());

						if (p.getAngle() <= 90) {
							fb7.add(0.5);
							fb7.add(0);
							fb7.add(0);
							fb7.add(45);
							fb7.add(p.getAngle() - 90);
						} else if (p.getAngle() > 90 && p.getAngle() <= 180) {
							fb7.add(0.5);
							fb7.add(0);
							fb7.add(0);
							fb7.add(45);
							fb7.add(p.getAngle() - 90);
						} else if (p.getAngle() > 180 && p.getAngle() <= 270) {
							fb7.add(0.5);
							fb7.add(0);
							fb7.add(0);
							fb7.add(-45);
							fb7.add(p.getAngle() + 90);
						} else {
							fb7.add(0.5);
							fb7.add(0);
							fb7.add(0);
							fb7.add(-45);
							fb7.add(p.getAngle() - 270);
						}

						fc7.add(fb7.buildFeature(p.getPointri().getId().toString() + p.getPolygonri().getId()));
						fb7.reset();
					}
				}
			}

			// ---------------

			String consultax = "SELECT "
					        + "abstractge0_.id ," 
					        + "abstractge0_.enabled,"
					        + "abstractge0_.imovel_id,"
					        + "abstractge0_.origin_id,"
					        + "abstractge0_.iconflag,"
					        + "abstractge0_.layer_id,"
					        + "abstractge0_.nome,"
					        + "abstractge0_.parte,"
					        + "abstractge0_.situacao,"
					     //   + "abstractge0_.dtype,"
					        + "abstractge0_.field,"
					        + "public.st_transform(public.st_geomfromtext(public.st_astext(polygongeo1_.geometry), " +  obraSIGEF.getLayer().getEpsg() + "), " +  obraSIGEF.getLayer().getEpsg() + ")  as geometry,"
					        + "polygongeo1_.area,"
					        + "polygongeo1_.perimetro "
					        + "from APP_GEOMETRY abstractge0_ " 
					        + "left outer join "
					        + "APP_POLYGON polygongeo1_ "
					        + "on abstractge0_.id=polygongeo1_.id  ,"
					        + "   APP_COMPONENT comp " 
					        + "WHERE abstractge0_.layer_id = comp.id "
					        + "AND polygongeo1_.geometry is not null AND "
					        + "abstractge0_.enabled = true  "
					        + " AND public.st_srid(polygongeo1_.geometry) = " +   obraSIGEF.getLayer().getEpsg()
					        + " AND abstractge0_.situacao > 0 "
					        
					        
					        + "AND  public.ST_Intersects(polygongeo1_.geometry,  "
							+ "public.ST_Transform(public.st_envelope(public.ST_GeomFromText('LINESTRING(" + x1 + " " + y1 + "," + x2 + " " + y2
							+ ")','" + obraSIGEF.getLayer().getEpsg() + "'))," + obraSIGEF.getLayer().getEpsg() + ")"
							+ "   )  AND comp.selected = true " + "AND comp.application_id = '"
							+ obraSIGEF.getLayer().getApplicationEntity().getId() + "'  AND abstractge0_.id <> '" + obraSIGEF.getId()
							+ "'";

					        
					        
					        //+ "abstractge0_.layer_id=" + layerId;
						
					
					
				
				
			// ---------------


			Query findAllQuery = entityManager.createNativeQuery(consultax, Polygon.class);

			final List<Polygon> resultsPOLY = findAllQuery.getResultList();

			DefaultFeatureCollection fc8 = new DefaultFeatureCollection();
			SimpleFeatureBuilder fb8 = null;

			fb.add(obraSIGEF.getGeometry()); // comverterGeometry

			fb.add(obraSIGEF.getNome());

			fc.add(fb.buildFeature(obraSIGEF.getId().toString()));

			for (Polygon poly : resultsPOLY) {

				fb8 = new SimpleFeatureBuilder(schema);

				fb8.add(poly.getGeometry()); // comverterGeometry
				fb8.add(poly.getNome());
				fb8.add(poly.getId());

				fc8.add(fb8.buildFeature(poly.getId().toString()));
				fb8.reset();
			}

			// ------------

			// ---------------

			String consultaLine = "SELECT "
				
					
 + "abstractge0_.id ," 
 + "abstractge0_.enabled,"
 + "abstractge0_.imovel_id,"
 + "abstractge0_.origin_id,"
 + "abstractge0_.iconflag,"
 + "abstractge0_.layer_id,"
 + "abstractge0_.nome,"
 + "abstractge0_.parte,"
 + "abstractge0_.situacao,"
 //+ "abstractge0_.dtype,"
 + "abstractge0_.field,"
 + "line.geometry,"
 + "line.valoroffset "
					
					+ "FROM   app_linestring line,  app_geometry abstractge0_,  app_component comp "
					+ "WHERE  line.id = abstractge0_.id AND  abstractge0_.layer_id = comp.id " + "AND public.ST_Intersects(line.geometry,  "
					+ "public.ST_Transform(public.st_envelope(public.ST_GeomFromText('LINESTRING(" + x1 + " " + y1 + "," + x2 + " " + y2
					+ ")','" + obraSIGEF.getLayer().getEpsg() + "')),   " + obraSIGEF.getLayer().getEpsg() + ")"
					+ "   )  AND comp.selected = true " + "AND comp.application_id = '"
					+ obraSIGEF.getLayer().getApplicationEntity().getId()
					+ "'   AND public.geometrytype(geometry)  = 'LINESTRING'"
					+ " AND public.st_srid(geometry) = " + obraSIGEF.getLayer().getEpsg();
					


			Query findAllQueryLine = entityManager.createNativeQuery(consultaLine, Linestring.class);

			final List<Linestring> resultsLines = findAllQueryLine.getResultList();


			DefaultFeatureCollection fc81 = new DefaultFeatureCollection();

			SimpleFeatureBuilder fb81 = null;


			for (Linestring line1 : resultsLines) {

				fb81 = new SimpleFeatureBuilder(schema);

				fb81.add(line1.getGeometry()); // comverterGeometry
				fb81.add(line1.getNome());
				fb81.add(line1.getNome());
				fb81.add(0);
				fb81.add(0);
				fb81.add(0);
				fb81.add(0);
				fb81.add(0);
				fc81.add(fb81.buildFeature(line1.getId().toString()));
				fb81.reset();
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

			Style stylePolygon = SLD.createPolygonStyle(Color.LIGHT_GRAY, null, 0.1f);

			Style stylePoint = SLD.createPointStyle("Circle", Color.BLACK, Color.WHITE, 0.3f, 4.0f);

			Style stylePoint2 = SLD.createPointStyle("Circle", Color.GRAY, Color.WHITE, 1.0f, 20.0f);

			// Style stylePointLabel = SLD.createPointStyle("Circle", Color.BLACK,
			// Color.WHITE, 0.3f, 5);

			   Style styleLine;
				if (fundomap) {
					    styleLine =    SLD.createLineStyle(Color.WHITE, 1.4f);
				}else{
					    styleLine =    SLD.createLineStyle(Color.BLACK, 1.4f);
				}

			Style styleLineCalcada = SLD.createLineStyle(Color.GRAY, 0.4f);

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
			
			//========================================
			//========================================
			
			// RULE

						FeatureTypeStyle featureTypeStylePoly = sf.createFeatureTypeStyle();

						// RULE 1
						// first rule to draw cities
						Rule rulePoly = sf.createRule();
						rulePoly.setName("rulePoly");

						TextSymbolizer textSymbolizerPoly = sf.createTextSymbolizer();

						PropertyName propNameOgcPoly = ff.property("nome");

					
						textSymbolizerPoly.setLabel(propNameOgcPoly);

						Expression fontStylePoly = ff.literal("normal");
						Expression fontSizePoly = ff.literal(9);
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

			
			
			// ------------
			Stroke strocado81;
			if (fundomap) {
				 strocado81 = sf.createStroke(ff.literal(Color.WHITE), ff.literal(1.5));
				strocado81.setDashArray(Arrays.asList((Expression) ff.literal("4 4")));
			}else{
				strocado81 = sf.createStroke(ff.literal(Color.GRAY), ff.literal(1.5));
				strocado81.setDashArray(Arrays.asList((Expression) ff.literal("4 4")));
			}

			

			LineSymbolizer lineSymbolizer81 = sf.createLineSymbolizer();
			lineSymbolizer81.setStroke(strocado81);

			FeatureTypeStyle featureTypeStyle81 = sf.createFeatureTypeStyle();

			Rule rulado81 = sf.createRule();
			rulado81.symbolizers().add(lineSymbolizer81);

			featureTypeStyle81.rules().add(rulado81);

			Style style81 = sf.createStyle();
			style81.setName("style81");
			style81.featureTypeStyles().add(featureTypeStyle81);

			/*
			 * GeoEJB 3
			 * 
			 * Copyright 2018 GeoEJB, Inc. and/or its affiliates Orders for print layers in
			 * the pdf map file!
			 */
			
			if (fc8.size() > 0) {
				FeatureLayer layerPolygonGround = new FeatureLayer(fc8, style33); // style33
				layerPolygonGround.setTitle("LAYER_POLYGON_GROUND");
				map.addLayer(layerPolygonGround);
			}

			
			
			if (fc4.size() > 0) {
				FeatureLayer layerLinestring = new FeatureLayer(fc4, styleLine);
				layerLinestring.setTitle("LAYER_LINESTRING");
				map.addLayer(layerLinestring);
			}		
			
			if (fc5.size() > 0) {
				FeatureLayer layerLinestringLabel = new FeatureLayer(fc5, style8);
				layerLinestringLabel.setTitle("LAYER_LINESTRING1_LABEL");
				map.addLayer(layerLinestringLabel);
			}
			
			if (fc7.size() > 0) {
				FeatureLayer layerLinestringLabel7 = new FeatureLayer(fc7, style8);
				layerLinestringLabel7.setTitle("LAYER_LINESTRING2_LABEL");
				map.addLayer(layerLinestringLabel7);
			}
			
		
			
			if (fc.size() > 0) {
				
				
				FeatureLayer layerPolygonLabel = new FeatureLayer(fc, stylePontoCirculoPolygon);
				layerPolygonLabel.setTitle("LAYER_POLYGON_LABEL");
				map.addLayer(layerPolygonLabel);
				
				FeatureLayer layerCirclePolygon = new FeatureLayer(fc, stylePoint2);
				layerCirclePolygon.setTitle("LAYER_CIRCLE POLYGON");
				map.addLayer(layerCirclePolygon);
			}
			
			

			// fc ---->>> POLYGON ABSTRACT GEOMETRY
			if (fc.size() > 0) {
			FeatureLayer layerPolygon = new FeatureLayer(fc, stylePolygon);
			layerPolygon.setTitle("LAYER_POLYGON");
			map.addLayer(layerPolygon);
			}
			
			
			
			if (fc3.size() > 0) {
				FeatureLayer layerPointLabel = new FeatureLayer(fc3, style8);
				layerPointLabel.setTitle("LAYER_POINT_LABEL");
				map.addLayer(layerPointLabel);
			}
			
			if (fc2.size() > 0) {
				FeatureLayer layerPoint = new FeatureLayer(fc2, stylePoint);
				layerPoint.setTitle("LAYER_POINT");
				map.addLayer(layerPoint);
				}
		

//			FeatureLayer layerLinestringCurve = new FeatureLayer(fc81, style81);
//			layerLinestringCurve.setTitle("LAYER_LINESTRING_CURVE");

//			map.addLayer(layerLinestringCurve);

			GenerateSVG generateSVG = new GenerateSVG();
			Dimension canvasSize = new Dimension(widthSize, heightSize);
			OutputStream out = null;

			File f1 = new File(destinationFile);
			OutputStream out8 = new FileOutputStream(f1);

			try {
				GenerateSVG.exportSVG(map, dataArea, out8, canvasSize);
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block

				e.printStackTrace();
			}

			// --------------------------------------------------------------------------------------------------------

			// ===========================================XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX

			/**
			 * <p/>
			 * This class generate the backgrounds image vector to compose the PDF Map</tt>
			 * This is a implementation from the <tt>GeoEJB Map Engine Generator</tt>.
			 */

			DOMImplementation impl = SVGDOMImplementation.getDOMImplementation();
			String svgNSNew = SVGDOMImplementation.SVG_NAMESPACE_URI;
			Document docHorizontal = impl.createDocument(svgNSNew, "svg", null);
			Element svgRootHorizontal = docHorizontal.getDocumentElement();

			/**
			 * <p/>
			 * Incomplete Code! The attribute 'height' need decrease or crease based on
			 * image map resolution. Need more code analyzes!
			 */

			svgRootHorizontal.setAttributeNS(null, "width", widthModelPage.toString());
			svgRootHorizontal.setAttributeNS(null, "height", "10");

			Document docVertical = impl.createDocument(svgNSNew, "svg", null);
			Element svgRootVertical = docVertical.getDocumentElement();

			svgRootVertical.setAttributeNS(null, "width", "10");
			svgRootVertical.setAttributeNS(null, "height", heightModelPage.toString());

			URI source = null;

			/**
			 * <p/>
			 * This paragraph must be remove in future. See more information from the
			 * URISyntaxException! (<tt>"file:" + destinationFile</tt>)
			 */

			try {
				source = new URI("file:" + destinationFile);
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			String parser = XMLResourceDescriptor.getXMLParserClassName();
			SAXSVGDocumentFactory f = new SAXSVGDocumentFactory(parser);
			String sourceUri = source.toString();
			Document document = f.createSVGDocument(sourceUri);

			Element svgRoot = document.getDocumentElement();

			int grid = 1;
			double larguraUTM = x2 - x1;

			double totalMetrosEntreLinhasGrid = larguraUTM / 5;
			Integer totalMetrosEntreLinhasGridInt = (int) totalMetrosEntreLinhasGrid;
			Integer larguraMELI = totalMetrosEntreLinhasGridInt.toString().length();
			if (larguraMELI == 1) {
				grid = totalMetrosEntreLinhasGridInt;
			} else if (larguraMELI == 2) {
				grid = (totalMetrosEntreLinhasGridInt / 10) * 10;
			} else if (larguraMELI == 3) {
				grid = (totalMetrosEntreLinhasGridInt / 100) * 100;
			} else if (larguraMELI == 4) {
				grid = (totalMetrosEntreLinhasGridInt / 1000) * 1000;
			} else if (larguraMELI == 5) {
				grid = (totalMetrosEntreLinhasGridInt / 10000) * 10000;
			} else if (larguraMELI == 6) {
				grid = (totalMetrosEntreLinhasGridInt / 100000) * 100000;
			} else if (larguraMELI == 7) {
				grid = (totalMetrosEntreLinhasGridInt / 1000000) * 1000000;
			} else if (larguraMELI == 8) {
				grid = (totalMetrosEntreLinhasGridInt / 10000000) * 10000000;
			}

			double escala = larguraUTM / 0.20;
			Integer escalaInteger = (int) escala;
			// String escalaFormat = "1:" + escalaInteger.toString();

			String escalaFormat = "1:" + scale;

			/**
			 * <p/>
			 * Start the second process on SVG Map Engine
			 */

			String svgNS = "http://www.w3.org/2000/svg";

			Integer first1 = (int) x1;
			String start1String = first1.toString();
			Integer lengthStart1 = start1String.length();
			String tresUltimos = null;
			if (grid <= 10) {
				tresUltimos = start1String.substring(lengthStart1 - 1);
			} else if (grid <= 100) {
				tresUltimos = start1String.substring(lengthStart1 - 2);
			} else if (grid <= 1000) {
				tresUltimos = start1String.substring(lengthStart1 - 3);
			} else if (grid <= 10000) {
				tresUltimos = start1String.substring(lengthStart1 - 4);
			} else if (grid <= 100000) {
				tresUltimos = start1String.substring(lengthStart1 - 5);
			} else {
				tresUltimos = start1String.substring(lengthStart1 - 6);
			}
			Integer tresUltimosInt = Integer.parseInt(tresUltimos);

			if (tresUltimosInt <= grid) {
				first1 = (first1 - tresUltimosInt) + grid;
			} else {
				first1 = (first1 - tresUltimosInt) + grid + grid;
			}

			double larguraPagina = widthModelPage;

			String label1;

			Node primeiroNode;
			Node ultimoNode;

			Element line1;
			Element text1;

			double porcentagem1;
			double firstPAGE_CORD;

			Integer firstPAGE_CORD_INT;
			Text data;

			while (first1 < x2) {

				double distancia1 = first1 - x1;
				porcentagem1 = (distancia1 * 100) / larguraUTM;
				firstPAGE_CORD = (larguraPagina * porcentagem1) / 100;

				firstPAGE_CORD_INT = (int) firstPAGE_CORD;

				line1 = document.createElementNS(svgNS, "line");
				line1.setAttributeNS(null, "x1", firstPAGE_CORD_INT.toString());
				line1.setAttributeNS(null, "y1", "0");
				line1.setAttributeNS(null, "x2", firstPAGE_CORD_INT.toString());
				line1.setAttributeNS(null, "y2", heightModelPage.toString());
				line1.setAttributeNS(null, "stroke", "#EAEAEA");
				line1.setAttributeNS(null, "stroke-width", "0.3");

				ultimoNode = line1;
				primeiroNode = svgRoot.getFirstChild();
				svgRoot.insertBefore(ultimoNode, primeiroNode);

				label1 = String.valueOf(first1);

				data = docHorizontal.createTextNode("E-" + label1);

				text1 = docHorizontal.createElementNS(svgNS, "text");
				text1.setAttributeNS(null, "x", firstPAGE_CORD_INT.toString());
				text1.setAttributeNS(null, "y", "6");
				if (firstPAGE_CORD_INT < 30) {
					text1.setAttributeNS(null, "text-anchor", "start");
				} else if (firstPAGE_CORD_INT > 570) {
					text1.setAttributeNS(null, "text-anchor", "end");
				} else {
					text1.setAttributeNS(null, "text-anchor", "middle");
				}

				text1.setAttributeNS(null, "font-family", "DejaVu Sans");
				text1.setAttributeNS(null, "font-size", "8");
				text1.setAttributeNS(null, "stroke-width", "0");
				text1.setAttributeNS(null, "stroke", "#FFFFFF");
				text1.setAttributeNS(null, "fill", "#3D3D3D");

				text1.appendChild(data);

				svgRootHorizontal.appendChild(text1);

				first1 = first1 + grid;

			}

			Integer first2 = (int) y1;
			String start2String = first2.toString();
			Integer lengthStart2 = start2String.length();
			String tres2Ultimos = null;
			if (grid <= 10) {
				tres2Ultimos = start2String.substring(lengthStart2 - 1);
			} else if (grid <= 100) {
				tres2Ultimos = start2String.substring(lengthStart2 - 2);
			} else if (grid <= 1000) {
				tres2Ultimos = start2String.substring(lengthStart2 - 3);
			} else if (grid <= 10000) {
				tres2Ultimos = start2String.substring(lengthStart2 - 4);
			} else if (grid <= 100000) {
				tres2Ultimos = start2String.substring(lengthStart2 - 5);
			} else {
				tres2Ultimos = start2String.substring(lengthStart2 - 6);
			}
			Integer tres2UltimosInt = Integer.parseInt(tres2Ultimos);

			if (tres2UltimosInt <= grid) {
				first2 = (first2 - tres2UltimosInt) + grid;
			} else {
				first2 = (first2 - tres2UltimosInt) + grid + grid;
			}

			String label2;
			double alturaPagina = heightModelPage;

			double alturaUTM = y2 - y1;

			Node primeiro2Node;
			Node ultimo2Node;

			Element line2;
			Element text2;

			double porcentagem2;
			double first2PAGE_CORD;

			Integer first2PAGE_CORD_INT;
			Text data2;

			while (first2 < y2) {

				double distancia2 = first2 - y1;
				porcentagem2 = (distancia2 * 100) / alturaUTM;
				first2PAGE_CORD = (alturaPagina * porcentagem2) / 100;

				first2PAGE_CORD_INT = (int) first2PAGE_CORD;

				// Create the line
				line2 = document.createElementNS(svgNS, "line");
				line2.setAttributeNS(null, "x1", "0");
				line2.setAttributeNS(null, "y1", first2PAGE_CORD_INT.toString());
				line2.setAttributeNS(null, "x2", widthModelPage.toString());
				line2.setAttributeNS(null, "y2", first2PAGE_CORD_INT.toString());
				line2.setAttributeNS(null, "stroke", "#F0F0F0");
				line2.setAttributeNS(null, "stroke-width", "0.3");

				ultimo2Node = line2;
				primeiro2Node = svgRoot.getFirstChild();
				svgRoot.insertBefore(ultimo2Node, primeiro2Node);

				label2 = String.valueOf(first2);

				data2 = docVertical.createTextNode("N-" + label2);

				text2 = docVertical.createElementNS(svgNS, "text");
				// text1.setAttributeNS(svgNS, "style",
				// "font-family:Arial;font-size:8;stroke:#000000;#fill:#00ff00;");
				text2.setAttributeNS(null, "x", "6");
				text2.setAttributeNS(null, "y", first2PAGE_CORD_INT.toString());
				text2.setAttributeNS(null, "xml:space", "preserve");
				if (first2PAGE_CORD_INT < 30) {
					text2.setAttributeNS(null, "text-anchor", "end");
				} else if (first2PAGE_CORD_INT > 370) {
					text2.setAttributeNS(null, "text-anchor", "start");
				} else {
					text2.setAttributeNS(null, "text-anchor", "middle");
				}

				text2.setAttributeNS(null, "font-family", "DejaVu Sans");
				text2.setAttributeNS(null, "font-size", "8");
				text2.setAttributeNS(null, "transform", "rotate(-90,6," + first2PAGE_CORD_INT.toString() + ")");
				text2.setAttributeNS(null, "stroke-width", "0");
				text2.setAttributeNS(null, "stroke", "#FFFFFF");
				text2.setAttributeNS(null, "fill", "#3D3D3D");

				text2.appendChild(data2);

				svgRootVertical.appendChild(text2);

				first2 = first2 + grid;

			}

			if (fundomap) {

				String varBoxWidth = null;
				String varBoxHeight = null;

				if (paper.equals("0")) {

					varBoxWidth = "3000";
					varBoxHeight = "2000";
				} else if (paper.equals("1")) {

					varBoxWidth = "2250";
					varBoxHeight = "1500";
				} else if (paper.equals("2")) {

					varBoxWidth = "1500"; // 420 × 594
					varBoxHeight = "1000";
				} else if (paper.equals("3")) {

					varBoxWidth = "1000";
					varBoxHeight = "667";
				} else if (paper.equals("4")) {

					varBoxWidth = "660";
					varBoxHeight = "440";
				} 

				// -----------

				// <pattern id="image" patternUnits="userSpaceOnUse" height="300" width="600">
				// <image x="0" y="0" height="300" width="600"
				// xlink:href="http://IP/arquivos/background.png"></image>
				// </pattern>

				// =====================================================
				Element pattern = document.createElementNS(svgNS, "pattern");
				pattern.setAttributeNS(null, "id", "image");
				pattern.setAttributeNS(null, "patternUnits", "userSpaceOnUse");
				pattern.setAttributeNS(null, "height", varBoxHeight);
				pattern.setAttributeNS(null, "width", varBoxWidth);
				Element image = document.createElementNS(svgNS, "image");
				image.setAttributeNS(null, "x", "0");
				image.setAttributeNS(null, "u", "0");
				image.setAttributeNS(null, "height", varBoxHeight);
				image.setAttributeNS(null, "width", varBoxWidth);

				image.setAttributeNS(null, "xlink:href", newurldestinationFileGoogleF);

				pattern.appendChild(image);

				svgRoot.appendChild(pattern);

				Element rect2 = document.createElementNS(svgNS, "rect");
				rect2.setAttributeNS(null, "fill", "url(#image)");
				rect2.setAttributeNS(null, "x", "0");
				rect2.setAttributeNS(null, "y", "0");
				rect2.setAttributeNS(null, "width", widthModelPage.toString());
				rect2.setAttributeNS(null, "height", heightModelPage.toString());
				// rect2.setAttributeNS(null, "style", "fill:none");
				rect2.setAttributeNS(null, "stroke", "#3D3D3D");
				rect2.setAttributeNS(null, "stroke-width", "1");

				// attach the rectangle to the svg root element
				// svgRoot.appendChild(rect2);

				primeiroNode = svgRoot.getFirstChild();
				svgRoot.insertBefore(rect2, primeiroNode);
			} else {

				Element rect2 = document.createElementNS(svgNS, "rect");
				rect2.setAttributeNS(null, "fill", colorPopup);
				rect2.setAttributeNS(null, "x", "0");
				rect2.setAttributeNS(null, "y", "0");
				rect2.setAttributeNS(null, "width", widthModelPage.toString());
				rect2.setAttributeNS(null, "height", heightModelPage.toString());
				rect2.setAttributeNS(null, "stroke", "#3D3D3D");
				rect2.setAttributeNS(null, "stroke-width", "1");

				primeiroNode = svgRoot.getFirstChild();
				svgRoot.insertBefore(rect2, primeiroNode);

			}

			OutputStream fileOutputStream = new FileOutputStream(destinationFile2);
			Writer svgWriter = new OutputStreamWriter(fileOutputStream, "UTF-8");

			DOMUtilities.writeDocument(document, svgWriter);
			svgWriter.flush();
			svgWriter.close();

			OutputStream fileOutputStreamHorizontal = new FileOutputStream(destinationFileHorizontal);
			Writer svgWriterHorizontal = new OutputStreamWriter(fileOutputStreamHorizontal, "UTF-8");

			DOMUtilities.writeDocument(docHorizontal, svgWriterHorizontal);
			svgWriterHorizontal.flush();
			svgWriterHorizontal.close();

			OutputStream fileOutputStreamVertical = new FileOutputStream(destinationFileVertical);
			Writer svgWriterVertical = new OutputStreamWriter(fileOutputStreamVertical, "UTF-8");

			DOMUtilities.writeDocument(docVertical, svgWriterVertical);
			svgWriterVertical.flush();
			svgWriterVertical.close();

			String reportFileUser = "";
			if (paper.equals("0")) {
				reportFileUser = save_FILE_PATH + "/birt/" + reltype + "UA0_" + modelo + ".jasper";
			} else if (paper.equals("1")) {
				reportFileUser = save_FILE_PATH + "/birt/" + reltype + "UA1_" + modelo + ".jasper";
			} else if (paper.equals("2")) {
				reportFileUser = save_FILE_PATH + "/birt/" + reltype + "UA2_" + modelo + ".jasper";
			} else if (paper.equals("3")) {
				reportFileUser = save_FILE_PATH + "/birt/" + reltype + "UA3_" + modelo + ".jasper";
			} else if (paper.equals("4")) {
				reportFileUser = save_FILE_PATH + "/birt/Relatorio.jasper";
			} else {
				reportFileUser = save_FILE_PATH  + "/birt/" + "/ERROR.rptdesign";
			}

			// ----------------------
			// ------------------------------NAME
			
			String nomeArquivo = "";
			String nomeArquivoPath = "";
			String nomeArquivoPNG = "";

			Random random = new Random();
			int randomInt = random.nextInt(1000000000);
			nomeArquivoPNG = "QRCODE_" + String.valueOf(randomInt) + ".png";

			if (automatico) {
				nomeArquivo = "PLANTA_DESCRITIVA.pdf";
			} else {
				nomeArquivo = "PLANTA_UTM" + "_A" + paper + "_" + String.valueOf(randomInt) + ".pdf";
			}

			nomeArquivoPath = saveFileProcessFilePath_ + "/" + nomeArquivo;

			String destinationFileQRCODE = saveFileProcessFilePath_ + "/" + nomeArquivoPNG;
					
			
			
			
			 HashMap<String,Object> param_map = new HashMap<String,Object>();
			

			 Long teste =  obraSIGEF.getId();
		
			param_map.put("obraId", teste);
			param_map.put("svgUrl", destinationFile2);
			param_map.put("svgUrlV", destinationFileVertical);
			param_map.put("svgUrlH", destinationFileHorizontal);
		
			param_map.put("escala", escalaFormat);
		
			param_map.put("svgUrlQuadra", urlFilePathQuadra);
		

			param_map.put("logoUrl", logoUrl);
			param_map.put("arrowNorthUrl", "https://smartcloudio.com.br/jakarta.faces.resource/images/mc.png.xhtml");
			
			param_map.put("medCentralUrl", "https://smartcloudio.com.br/jakarta.faces.resource/images/north.png.xhtml");

			
			
			
			
			for (ApplicationAttributeEntity atrib : appatribs) {
				
				switch (atrib.getName()) {
				
				case "mun_uf":
					param_map.put("mun_uf", atrib.getValue());
				break;
				
				case "comarca":
					param_map.put("comarca", atrib.getValue());
				break;
				
				case "numero_rt":
					param_map.put("numero_rt", atrib.getValue());
				break;
				
				case "nome_rt":
					param_map.put("nome_rt", atrib.getValue());
				break;
				
				case "referencia_rt":
					param_map.put("referencia_rt", atrib.getValue());
				break;
				
				case "header1":
					param_map.put("header1", atrib.getValue());
				break;
				
				case "header2":
					param_map.put("header2", atrib.getValue());
				break;
				
				case "header3":
					param_map.put("header3", atrib.getValue());
				break;
				
				case "header4":
					param_map.put("header4", atrib.getValue());
				break;
				
				case "header_img":
					param_map.put("header_img", finalFilesUrl + atrib.getValue());
				break;
				
				case "exec_img":
					param_map.put("exec_img", finalFilesUrl + atrib.getValue());
				break;
				
				case "attrib_data":
					param_map.put("attrib_data", atrib.getValue());
				break;
								 
				case "cm_projecao":
					param_map.put("cm_projecao", atrib.getValue());
				break;
				
				case "cm_datum":
					param_map.put("cm_datum", atrib.getValue());
				break;
				
				case "cm_mc":
					param_map.put("cm_mc", atrib.getValue());
				break;
				
				case "cm_vertice":
					param_map.put("cm_vertice", atrib.getValue());
				break;
				
				case "cm_latitude":
					param_map.put("cm_latitude", atrib.getValue());
				break;
				
				case "cm_longitude":
					param_map.put("cm_longitude", atrib.getValue());
				break;
				
				case "cm_ce":
					param_map.put("cm_ce", atrib.getValue());
				break;
				
				case "cm_cm":
					param_map.put("cm_cm", atrib.getValue());
				break;
				
				case "cm_dm":
					param_map.put("cm_dm", atrib.getValue());
				break;
				
				case "cm_va":
					param_map.put("cm_va", atrib.getValue());
				break;
				
				case "cm_data":
					param_map.put("cm_data", atrib.getValue());
				break;
				
				}
			}



			
			
			
			  Connection con;
			  try {
		            Class.forName("org.postgresql.Driver");
		            con = DriverManager.getConnection(
		            		"jdbc:postgresql://" + pg_HOST + ":" + pg_PORT + "/" + 
		            				pg_DAT + "?currentSchema=" + tenant, 
		            				pg_USER, 
		            				pg_PASS
		            		);
		            
		            
		           param_map.put("REPORT_CONNECTION", con);
		            JasperPrint jp;
		            jp = JasperFillManager.fillReport(reportFileUser, 
		            		param_map, con);

		            JasperExportManager.exportReportToPdfFile(jp, nomeArquivoPath);

		            con.close();
		        } catch (ClassNotFoundException | SQLException | JRException ex) {
		            ex.printStackTrace();
		        }
						
			  
			
			  
			 
		Media media = new Media("application/pdf", 
				nomeArquivo , mediaFilePath ); 
	
			
			

			return media;
		}

		return null;
	}

}