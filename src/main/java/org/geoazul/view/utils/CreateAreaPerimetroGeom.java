package org.geoazul.view.utils;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.persistence.EntityManager;
import org.geoazul.model.basic.Polygon; //Polygon
import org.geoazul.model.basic.rural.PolygonPoint;  //PolygonPoint
import org.geoazul.view.beta.ProcessAreaCartesian;
import org.geoazul.view.beta.ProcessAreaCartesian2;
import org.geoazul.view.beta.ProcessAzimuth;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import static modules.LoadInitParameter.*;
import org.postgis.Point;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.io.WKTReader;
import org.locationtech.jts.operation.distance.DistanceOp;
import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;

import org.geoazul.model.basic.AbstractGeometry;
import org.geoazul.model.basic.Linestring;
import org.geoazul.model.basic.MultiPolygon;

import org.geoazul.model.basic.endpoints.GenerateSVG;

import org.geoazul.model.website.UrlPostItem;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.FeatureLayer;
import org.geotools.map.MapContent;
import org.geotools.map.MapViewport;


import org.geotools.styling.SLD;


import org.locationtech.jts.geom.Envelope;

import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.filter.expression.PropertyName;
import org.geotools.api.geometry.MismatchedDimensionException;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.NoSuchAuthorityCodeException;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.api.referencing.operation.MathTransform;
import org.geotools.api.referencing.operation.TransformException;
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

public class CreateAreaPerimetroGeom {
	
	GeometryFactory gf = new GeometryFactory(); 
	
	ProcessAreaCartesian processAreaCartesian = new ProcessAreaCartesian();
	ProcessAreaCartesian2 processAreaCartesian2 = new ProcessAreaCartesian2();

	private long count;
	
	private List<PolygonPoint> verticeobraItems; 

	public List<PolygonPoint> getPolygonPointItems()
	{
	   return this.verticeobraItems;
	}

	public long getCount()
	{
	   return this.count;
	}

	
	
	public String runCreate(Polygon obraCAP, EntityManager entityManager){
		
		
		if (obraCAP.getVerticeObrasList() == null) {
			return null;
		} else {
			Iterator i = obraCAP.getVerticeObrasList().iterator(); //   verticeObraList.iterator();

		

			//-------------------------------------------------------------------
			
			if (obraCAP.getVerticeObrasList().size() > 2) {
				Integer srid_;
				Integer sequencia = 0;
				@SuppressWarnings("unused")
				double doubleLeste;
				@SuppressWarnings("unused")
				double doubleNorte;
				@SuppressWarnings("unused")
				double doubleAltura;
				org.locationtech.jts.geom.Coordinate[] pointsNew   = new Coordinate[obraCAP.getVerticeObrasList().size() + 1];
				org.locationtech.jts.geom.Coordinate[] pointsNew2  = new Coordinate[obraCAP.getVerticeObrasList().size()];
			
				String X4326 = null;
		    	String Y4326 = null;
		    	String utmPoint;
		    	double mediaAltura = 0;
				while (i.hasNext()) {
					PolygonPoint verticeobra_ = (PolygonPoint) i.next();
					mediaAltura = mediaAltura +  verticeobra_.getPointri().getAltura();
					doubleAltura = verticeobra_.getPointri().getAltura();
	
					srid_ = verticeobra_.getPointri().getLayer().getApplicationEntity().getEpsg();

					
					//-------------------------------- NEW POSTGIS CONVERTER --------------------------------------------
			
				    utmPoint = "POINT(" + verticeobra_.getPointri().getGeometry().getCoordinate().x + " " + 
				    verticeobra_.getPointri().getGeometry().getCoordinate().y + ")";
				    String consulta = "SELECT" +
					    	" public.ST_X(public.ST_Transform(public.ST_GeomFromText('" + utmPoint + "'," + srid_ + "),4326)) As X4326," +
					  		" public.ST_Y(public.ST_Transform(public.ST_GeomFromText('" + utmPoint + "'," + srid_ + "),4326)) As Y4326 ";
										 
				    
					
				    Query queryFF = entityManager.createNativeQuery(consulta);
				    List<Object[]> listFF= queryFF.getResultList();
				    if (!listFF.isEmpty()){ 
				        Object stringFF[] =  listFF.get(0);
				        X4326 = stringFF[0].toString();   
				        Y4326 = stringFF[1].toString();    
				    }

				    pointsNew[sequencia] = new Coordinate(Double.valueOf(X4326).doubleValue(), Double.valueOf(Y4326).doubleValue());
				    pointsNew2[sequencia] = new Coordinate(((-1) * Double.valueOf(X4326).doubleValue()), Double.valueOf(Y4326).doubleValue());
				    	
				    	
					sequencia++;
				}
				mediaAltura = mediaAltura  /  obraCAP.getVerticeObrasList().size();
				pointsNew[obraCAP.getVerticeObrasList().size()] = pointsNew[0]; //Close de Polygon 
				Geometry geomet = gf.createPolygon(pointsNew);
				geomet.setSRID(3857);
			  
			    
			  // ------------------------------------ AREA CARTESIAN GET ------------------------------------
			  
			  double areaCartesiana[] = processAreaCartesian.testarPTL(pointsNew2, mediaAltura);
			  
		    
				obraCAP.setArea(areaCartesiana[0]);
				obraCAP.setPerimetro(areaCartesiana[1]);
				
				//------------------ geomet converter
				
				  GeometryFactory geometryFactory = new GeometryFactory();

				 
				   Geometry targetGeometry1 = null;
			 		try {
			 		CoordinateReferenceSystem sourceCRS = CRS.decode("EPSG:4326"); 
			 		CoordinateReferenceSystem targetCRS = CRS.decode("EPSG:" + obraCAP.getLayer().getApplicationEntity().getEpsg() );

			 		MathTransform transform = CRS.findMathTransform(sourceCRS, targetCRS);
			 	 

			 		targetGeometry1 = JTS.transform( geomet , transform);
			 		targetGeometry1.setSRID(obraCAP.getLayer().getApplicationEntity().getEpsg());
			 		
			 		} catch (NoSuchAuthorityCodeException e) {
			 			e.printStackTrace();
			 		} catch (FactoryException e) {
			 			e.printStackTrace();
			 		} catch (MismatchedDimensionException e) {
			 			e.printStackTrace();
			 		} catch (TransformException e) {
			 			e.printStackTrace();
			 		} 
				
				
			 	obraCAP.setGeometry(targetGeometry1);
				
				obraCAP.getGeometry().setSRID(obraCAP.getLayer().getApplicationEntity().getEpsg());
				
		//		obraCAP.getFather().setGeometry(targetGeometry1);
				
		//		obraCAP.getFather().getGeometry().setSRID(obraCAP.getLayer().getApplicationEntity().getEpsg());
				
				
				
				
			
		        
		        
		        
		         entityManager.merge(obraCAP);
					entityManager.merge(obraCAP.getFather());
		    
		    	
		    	

				
				
				createLineString2(obraCAP, entityManager, mediaAltura);
				createLineString(obraCAP, entityManager, mediaAltura); //FIXME
				return null;
			} else {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("ERRO", "1Parcela necessita no minimo 3 vértices!" ));
				return null;
			}
		}   
		   
	   }
	
	
	
	public void createLineString2(Polygon obraCAP, EntityManager entityManager, double mediaAltura) {
		
		double longitudeFirst1X = 0;
		double latitudeFirst1X = 0;
		double altitudeFirstMeters1X = 0;
		
		double longitude1X= 0;
		double latitude1X= 0;
		double altitudeMeters1X = 0;
		
		double longitude2X = 0;
		double latitude2X = 0;
		double altitudeMeters2X = 0;
		
		double longitudeRadians1 = 0;
		double latitudeRadians1 = 0;
		 		
		double longitudeRadians2 = 0;
		double latitudeRadians2 = 0;
		
		
		
		Integer srid_;
		Integer sequencia = 0;
		Coordinate FirstPointGeodetic = null;
		org.locationtech.jts.geom.Coordinate[] pointsNew  = new Coordinate[obraCAP.getVerticeObrasList().size() + 1];
		org.locationtech.jts.geom.Coordinate[] pointsSirgasNew = new Coordinate[obraCAP.getVerticeObrasList().size() + 1];
		  
    	String X4326 = null;
    	String Y4326 = null;
    	String utmPoint;
    	
    	String X4326Next = null;
    	String Y4326Next = null;
    	String utmPointNext;
    	
    	Iterator<?> i = obraCAP.getVerticeObrasList().iterator(); 
    	Iterator<?> iTo = obraCAP.getVerticeObrasList().iterator(); 
    	Integer numeroVertices = obraCAP.getVerticeObrasList().size();
    	org.geoazul.model.basic.Point verticeFirst = null;
    	PolygonPoint verticeobraTo = (PolygonPoint) iTo.next();
    	
    	
		while (i.hasNext()) {
			PolygonPoint verticeobra_ = (PolygonPoint) i.next();

			
			
			srid_ = verticeobra_.getPointri().getLayer().getApplicationEntity().getEpsg();

			//-------------------------------- NEW POSTGIS CONVERTER --------------------------------------------
		    utmPoint = "POINT(" + verticeobra_.getPointri().getGeometry().getCoordinate().x  + " " + 
				        verticeobra_.getPointri().getGeometry().getCoordinate().y  + ")";

		    
		    String consulta = "SELECT" +
			    	" public.ST_X(public.ST_Transform(public.ST_GeomFromText('" + utmPoint + "'," + srid_ + "),4326)) As X4326," +
			  		" public.ST_Y(public.ST_Transform(public.ST_GeomFromText('" + utmPoint + "'," + srid_ + "),4326)) As Y4326 ";

		    Query queryFF = entityManager.createNativeQuery(consulta);
		 
			List<Object[]> listFF= queryFF.getResultList();
		    if (!listFF.isEmpty()){ 
		        Object stringFF[] =  listFF.get(0);
		        X4326 = stringFF[0].toString();   
		        Y4326 = stringFF[1].toString();    
		    }

		    pointsNew[sequencia] = new Coordinate(Double.valueOf(X4326).doubleValue(), Double.valueOf(Y4326).doubleValue());
		    	

		    	
		    pointsSirgasNew[sequencia] =  new Coordinate(verticeobra_.getPointri().getGeometry().getCoordinate().x, 
		    	verticeobra_.getPointri().getGeometry().getCoordinate().y, 
		    	verticeobra_.getPointri().getAltura());
		    	
				

				
				
				if (sequencia == 0)	{
					verticeFirst = verticeobra_.getPointri();
					// =============== FIRST DATA FROM VETEX ===================
					FirstPointGeodetic = new Coordinate(((-1) * Double.valueOf(X4326).doubleValue()), Double.valueOf(Y4326).doubleValue());
					
					longitudeFirst1X = Double.valueOf(X4326).doubleValue();
		    		latitudeFirst1X  = Double.valueOf(Y4326).doubleValue();
		    		altitudeFirstMeters1X = verticeobra_.getPointri().getAltura();
					// =============== FIRST DATA  =============================
				}
				
				// Vamos determinar o atual
				longitude1X = Double.valueOf(X4326).doubleValue();
	    		latitude1X  = Double.valueOf(Y4326).doubleValue();
	    		altitudeMeters1X = verticeobra_.getPointri().getAltura();
				// Fim de Atual
				
				
				if (sequencia + 1 < numeroVertices) {
					verticeobraTo = (PolygonPoint) iTo.next();
					verticeobra_.setPointTo(verticeobraTo.getPointri());

					//-------------------------------- NEW POSTGIS NEXT CONVERTER --------------------------------------------
				    utmPointNext = "POINT(" + verticeobraTo.getPointri().getGeometry().getCoordinate().x + " " + 
						        verticeobraTo.getPointri().getGeometry().getCoordinate().y + ")";

				    
				    String consultaNext = "SELECT" +
					    	" public.ST_X(public.ST_Transform(public.ST_GeomFromText('" + utmPointNext + "'," + srid_ + "),4326)) As X4326," +
					  		" public.ST_Y(public.ST_Transform(public.ST_GeomFromText('" + utmPointNext + "'," + srid_ + "),4326)) As Y4326 ";

				    Query queryFFNext = entityManager.createNativeQuery(consultaNext);
				
					List<Object[]> listFFNext= queryFFNext.getResultList();
				    if (!listFFNext.isEmpty()){ 
				        Object stringFFNext[] =  listFFNext.get(0);
				        X4326Next = stringFFNext[0].toString();   
				        Y4326Next = stringFFNext[1].toString();    
				    }
					
					longitude2X = Double.valueOf(X4326Next).doubleValue();
		    		latitude2X  = Double.valueOf(Y4326Next).doubleValue();
		    		altitudeMeters2X = verticeobra_.getPointri().getAltura();
		    		//--------------------------------------------------------------------
					
		  		  
		    		
		    		String geH= "LINESTRING(" +  pointsNew[sequencia].x + " " +  pointsNew[sequencia].y + ","
							+ X4326Next + " " + Y4326Next + ")";
					
				
					
						 WKTReader fromText = new WKTReader();
						 org.locationtech.jts.geom.Geometry geom55 = null;
					
					      
						 
					            try {
					            	geom55 = fromText.read(geH);
								} catch (org.locationtech.jts.io.ParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
					            geom55.setSRID(4326);

					
					            verticeobra_.setGeometry((LineString) geom55);
					

					
				}else{
					verticeobra_.setPointTo(verticeFirst);
					longitude2X = longitudeFirst1X;
		    		latitude2X  = latitudeFirst1X;
		    		altitudeMeters2X = altitudeFirstMeters1X;
		    		
		    		String geH2= "LINESTRING(" +  pointsNew[sequencia].x + " " +  pointsNew[sequencia].y + ","
							+ longitude2X + " " +  latitude2X  + ")";
					
				
				
					
					
					String geH56= "LINESTRING(" +  pointsNew[sequencia].x + " " +  pointsNew[sequencia].y + ","
							+ X4326Next + " " + Y4326Next + ")";
					
				
					
						 WKTReader fromText = new WKTReader();
						 org.locationtech.jts.geom.Geometry geom56 = null;
					
					     
					            try {
					            	geom56 = fromText.read(geH2);
								} catch (org.locationtech.jts.io.ParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
					            geom56.setSRID(4326);

					
					            verticeobra_.setGeometry((LineString) geom56);
				
			    //
				}
			
				
				
				
				
			    double semiEixoMaior = 6378137.00000; // Semi-eixo maior do elipsÃ³ide de referÃªncia  
				double semiEixoMenor = 6356752.31414; // Semi-eixo menor do elipsÃ³ide de referÃªncia
				double f = 1/298.257223563; 
				
				
				Coordinate pointsNew2 = new Coordinate(((-1) * Double.valueOf(X4326).doubleValue()), Double.valueOf(Y4326).doubleValue());

			    
				Coordinate ptl =  processAreaCartesian2.PTL(FirstPointGeodetic, 
						pointsNew2, semiEixoMaior, semiEixoMenor, f, mediaAltura);
				
			
				
			   		
	    		
	    		
	    		double a1Minus2H = Math.pow(  (altitudeMeters1X - altitudeMeters2X) , 2);
	    		
	    		
	    			    		
	    		

			 
			
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				//---------------------------------------------------------------------
				

				String consultaDescritivo = "SELECT DISTINCT  "
					
						
						
						+ " pointri_id, polygonri_id, point_next, sequencia, tipolimite, geometry, distancia, azimuth, jusantemontante, " 
						+ " esqDir, leste, norte, descritivo, cns, matricula, "
						+ "anchorpointx, anchorpointy, displacementx, displacementy, rotation, "
						+ "polygon_confrontante_id, point_id_to, status, angle, marco_label, print_conf, print_dist, print_marco"
						+ " FROM app_polygonripointri  vo where vo.pointri_id = '" + verticeobra_.getPointTo().getId() 
				 + "' AND vo.point_id_to = '" + verticeobra_.getPointri().getId()  + "' limit (1.1)";
				
				
				   
				   
				   
				   try{
						
					    Query queryNativaDescritivo = entityManager.createNativeQuery(consultaDescritivo, PolygonPoint.class);
						
						PolygonPoint verticeobradescritivo = ((PolygonPoint) queryNativaDescritivo.getSingleResult());
									
						String limiteOld = verticeobra_.getTipoLimite();
		
					//	verticeobradescritivo.setDescritivo(verticeobra_.getPolygonri().getImovel().getDetentor());
			
				
						
				        
				        
						entityManager.merge(verticeobradescritivo);

				        
						
						entityManager.flush();
					

						
				   }catch(NoResultException e){ 
				   }
				   
				   
				
		
				

				
				
			

			        
			    	entityManager.merge(verticeobra_);	

			        
					entityManager.flush();
				
					

				
			
			
				
				sequencia++;
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
			// Inicio criacao das LinesStrings

	
			public void createLineString(Polygon obraCLS, EntityManager entityManager, double mediaAltura) {
				
				double doubleLeste;
				double doubleNorte;

				Point FirstPoint;
				Point FirstPointFirst;
				Point NextPoint;
				
				Coordinate FirstPointGeodetic;
				Coordinate FirstPointFirstGeodetic;
				Coordinate NextPointGeodetic;

				Coordinate FirstdoubleGeodetic;
				
				String verticeFirstFirst;
				org.geoazul.model.basic.Point verticeToFirstFirst;
				String verticeNext;
				org.geoazul.model.basic.Point verticeTo;
				org.geoazul.model.basic.Point utmPoint;
			
				
			
					
			
					
					List<PolygonPoint> verticeobraEspecial = obraCLS.getVerticeObrasList();

					Iterator i = verticeobraEspecial.iterator();
					Iterator i2 = verticeobraEspecial.iterator();
					PolygonPoint verticeobraNext = (PolygonPoint) i2.next();

					// =============== FIRST POINT COORDINATES UTM AND GEODETIC ======
					doubleLeste = verticeobraNext.getPointri().getGeometry().getCoordinate().x;
					doubleNorte = verticeobraNext.getPointri().getGeometry().getCoordinate().y;
					FirstPointFirstGeodetic = verticeobraNext.getPointri().getGeometry().getCoordinate();

					// =============== FIRST POINT NAMES =============================

					FirstPointFirst = new org.postgis.Point(doubleLeste, doubleNorte);
					verticeFirstFirst = verticeobraNext.getPointri().getNome();
					verticeToFirstFirst = verticeobraNext.getPointri();

					Integer contador = 0;
					while (i.hasNext()) {
						
						doubleLeste = verticeobraNext.getPointri().getGeometry().getCoordinate().x;
						
						doubleNorte = verticeobraNext.getPointri().getGeometry().getCoordinate().y;
						
						PolygonPoint verticeobra_ = (PolygonPoint) i.next();

						if (i2.hasNext()) {
							verticeobraNext = (PolygonPoint) i2.next();
							// ==============================================================================
							doubleLeste = verticeobraNext.getPointri().getGeometry().getCoordinate().x;
							doubleNorte = verticeobraNext.getPointri().getGeometry().getCoordinate().y;
							NextPoint = new org.postgis.Point(doubleLeste, doubleNorte);
							NextPointGeodetic = verticeobraNext.getPointri().getGeometry().getCoordinate();
							verticeNext = verticeobraNext.getPointri().getNome();
							verticeTo = verticeobraNext.getPointri();
						} else {
							NextPoint = FirstPointFirst;
							NextPointGeodetic = FirstPointFirstGeodetic;
							verticeNext = verticeFirstFirst;
							verticeTo = verticeToFirstFirst;
						}

						// Aqui o processamento
						doubleLeste = verticeobra_.getPointri().getGeometry().getCoordinate().x;
						doubleNorte = verticeobra_.getPointri().getGeometry().getCoordinate().y;
						

						FirstPoint = new org.postgis.Point(doubleLeste, doubleNorte);
						FirstPointGeodetic = verticeobra_.getPointri().getGeometry().getCoordinate();
						Integer srid_ = obraCLS.getLayer().getApplicationEntity().getEpsg();
						
						FirstPoint.setSrid(srid_);
						NextPoint.setSrid(srid_);

						Coordinate primeiroPonto = new Coordinate( verticeobra_.getPointri().getGeometry().getCoordinate().x,
								 verticeobra_.getPointri().getGeometry().getCoordinate().y);
						Coordinate segundoPonto = new Coordinate(verticeobraNext.getPointri().getGeometry().getCoordinate().x,
								verticeobraNext.getPointri().getGeometry().getCoordinate().y);
						Geometry firstPointX = gf.createPoint(primeiroPonto);
						Geometry secondPointX =  gf.createPoint(segundoPonto);
						DistanceOp distanceOp = new DistanceOp(firstPointX, secondPointX);
	 		
						 
						
						
	ProcessAzimuth processAzimuth = new ProcessAzimuth();
				    	
			    	
			  	 double azimuth = processAzimuth.Azimuth(FirstPointGeodetic, NextPointGeodetic);
							 
							 
				//-----------------------------------------------------------------------------------------		
						
				
						
						Query queryNativa = entityManager
								.createNativeQuery("SELECT public.st_distance('" +  FirstPoint
										+ "" + "','" + NextPoint + "') as distance,"
										+ "degrees(public.st_azimuth1('" + FirstPoint + "','"
										+ NextPoint + "')) as azimute," +

										" public.ST_X(public.ST_Transform(public.ST_GeomFromText('"
										+ FirstPoint + "'," + srid_
										+ "),4326)) As firstX,"
										+ " public.ST_Y(public.ST_Transform(public.ST_GeomFromText('"
										+ FirstPoint + "'," + srid_
										+ "),4326)) As firstY," + 

										" public.ST_X(public.ST_Transform(public.ST_GeomFromText('"
										+ NextPoint + "'," + srid_
										+ "),4326)) As nextX,"
										+ " public.ST_Y(public.ST_Transform(public.ST_GeomFromText('"
										+ NextPoint + "'," + srid_
										+ "),4326)) As nextY ");
						
						
						
						Object saida[] = (Object[]) queryNativa.getSingleResult();

						String dist = saida[0].toString();
						double distancia = Double.valueOf(dist).doubleValue();
			
						int distanciaInt = (int) Math.floor(distancia);
						String inteiro = String.valueOf(distanciaInt);
						String distString = dist.substring(0, inteiro.length() + 3);

						String azim = saida[1].toString();
						double azimute = Double.valueOf(azim).doubleValue();
						String firstX_ = saida[2].toString();
						double firstXD = Double.valueOf(firstX_).doubleValue();

						String firstY_ = saida[3].toString();
						double firstYD = Double.valueOf(firstY_).doubleValue();

						String nextX_ = saida[4].toString();
						double nextXD = Double.valueOf(nextX_).doubleValue();

						String nextY_ = saida[5].toString();
						double nextYD = Double.valueOf(nextY_).doubleValue();

						Point point0 = new org.postgis.Point(firstXD, firstYD);
						Point point1 = new org.postgis.Point(nextXD, nextYD);

						List points2 = new ArrayList();
						points2.add(point0);
						points2.add(point1);

						org.postgis.Geometry lineString = new org.postgis.LineString((Point[]) points2.toArray(new Point[points2.size()]));
						lineString.srid = 4326;

						 WKTReader fromText = new WKTReader();

						 org.locationtech.jts.geom.Geometry geom = null;

						 String teste = "LINESTRING" + lineString.getValue();

					            try {
									geom = fromText.read(teste);
								} catch (org.locationtech.jts.io.ParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

						geom.setSRID(4326);

						Long vertice_id_update = 	 verticeobra_.getId().getPointriId();		
						String testeRR;
						
						Integer azimuthInteger = 10; // (int) azimuth;
						
						
						if (azimuthInteger > 90 && azimuthInteger < 290) {
							
							testeRR = "UPDATE app_polygonripointri SET "
									+ "anchorpointx = "
									+ "0.0"
									+ ", anchorpointy = "
									+ "1.0"
									+ ", rotation = "
									+ "0"
									+ ", displacementx = "
									+ "10.0"
									+ ", displacementy = "
									+ "0"
									+ " where polygonri_id = '"
									+ obraCLS.getId()
									+ "'  AND pointri_id = '" + vertice_id_update + "'";
							
						}else{
							testeRR = "UPDATE app_polygonripointri SET "
									+ "anchorpointx = "
									+ "1.0"
									+ ", anchorpointy = "
									+ "1.0"
									+ ", rotation = "
									+ "0"
									+ ", displacementx = "
									+ "-10.0"
									+ ", displacementy = "
									+ "0"
									+ " where polygonri_id = '"
									+ obraCLS.getId()
									+ "'  AND pointri_id = '" + vertice_id_update + "'" ;
						}
						//if (azimute < 90 && azimute > 0){

						
						
					
						Query queryNativaTeste2 = entityManager
								.createNativeQuery(testeRR);
						
						queryNativaTeste2.executeUpdate();

						verticeobra_.setDistancia(distString);
						
						verticeobra_.setAzimuth(decToDMS(azimute));
						
						verticeobra_.setAngle(azimute);
						
						verticeobra_.setPointNext(verticeNext);
												
						verticeobra_.setPointTo(verticeTo);
						
						
						
						

						 
					    	entityManager.merge(verticeobra_);
						
						   
						  
				       
						

					}
				

				}

					
			public String decToDMS(double coord) {

				double doubleDegres = Math.floor(coord); // 33

				String degrees = String.valueOf((int) doubleDegres); // 33
				double doubleDecimals = coord - doubleDegres; // 0.3333

				double doubleMinutes = doubleDecimals * 60; // 19.998
				double absDoubleMinutes = Math.floor(doubleMinutes); // 19 double
				String Minutes = String.valueOf((int) absDoubleMinutes); // 19 string
				doubleDecimals = doubleMinutes - absDoubleMinutes; // 0.998

				double doubleSeconds = doubleDecimals * 60; // 59.88
				double absDoubleSeconds = Math.floor(doubleSeconds); // 59 double
				String Seconds = String.valueOf((int) absDoubleSeconds); // 19 string

				return degrees + "º" + Minutes + "'" + Seconds + "\"";
			}
			
			public String convertToASCII2(String text) {
				return text.replaceAll("[ãâàáä]", "a").replaceAll("[êèéë]", "e")
						.replaceAll("[îìíï]", "i").replaceAll("[õôòóö]", "o")
						.replaceAll("[ûúùü]", "u").replaceAll("[ÃÂÀÁÄ]", "A")
						.replaceAll("[ÊÈÉË]", "E").replaceAll("[ÎÌÍÏ]", "I")
						.replaceAll("[ÕÔÒÓÖ]", "O").replaceAll("[ÛÙÚÜ]", "U")
						.replace('ç', 'c').replace('Ç', 'C').replace('ñ', 'n')
						.replace('Ñ', 'N');
			}
			
			    	
	
		}