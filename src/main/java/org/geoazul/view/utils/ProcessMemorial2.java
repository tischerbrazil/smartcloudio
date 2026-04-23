package org.geoazul.view.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collection;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.logging.Level;
import jakarta.persistence.EntityManager;
import static modules.LoadInitParameter.*;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

import jakarta.transaction.Transactional;
import org.geoazul.model.app.ApplicationAttributeEntity;
import org.geoazul.model.basic.Polygon;
import org.geoazul.model.basic.rural.PolygonPoint;
import org.geoazul.model.website.media.Media;
import org.postgis.Point;
import org.locationtech.jts.geom.GeometryFactory;

public class ProcessMemorial2 {
      
	GeometryFactory gf = new GeometryFactory();
	
	@Transactional
	public String createLineString(String finalFilesUrl, Collection<ApplicationAttributeEntity> appatribs,     Polygon obraCLS, EntityManager entityManager, 
			String reltype, String modelo, String saveFileProcessFilePath_, String saveFileProcessFilePathUrl_,
			String tenant) {
		

		
		String colunaA = "Inicia-se a descrição deste perímetro no vértice " ;
		String colunaB = " de coordenadas ";
		String colunaC = " deste segue confrontando com ";
		
		String colunaH = " com os seguintes azimutes e distâncias: ";
		String colunaI = " até o vértice ";
		
		String colunaK = " com azimute ";
		String colunaL = " e distância ";
		
		String colunaM = " ponto inicial da descrição deste perímetro. Todas as coordenadas aqui descritas estão" +
				" georreferenciadas ao Sistema Geodésico Brasileiro, a partir de coordenadas N m e E m" +
				" e encontram-se representadas no sistema UTM, referenciadas ao Meridiano Central ";
		String colunaN = " , tendo como datum SIRGAS 2000. Todos os azimutes e distâncias," +
				" área e perímetro foram calculados no plano de projeção UTM.";
	
		String colunaX;
		
		String texto = colunaA;

		String doubleLeste;
		String doubleNorte;
		Point FirstPoint;
		Point FirstPointFirst = null;
		Point NextPoint;

		String FirstLesteFirst;
		String FirstNorteFirst;

		String verticeFirstFirst;
		org.geoazul.model.basic.Point verticeToFirstFirst;
		String verticeNext;
		org.geoazul.model.basic.Point verticeTo;
	
		//CAN PROCESS ONLY WITH THREE VERTEXs
		if (obraCLS.getVerticeObrasList().size() < 3) { 
			return null;
		} else {

			
			
			obraCLS = entityManager.find(Polygon.class, obraCLS.getId());
			
			
			// this.abstractGeometry = (Polygon) this.abstractGeometry;
			
			
			//List<PolygonPoint> verticeobraEspecial = obraCLS.getPointriPolygonsList();
			List<PolygonPoint> verticeobraEspecial = obraCLS.getVerticeObrasList();

			Iterator i = verticeobraEspecial.iterator();
			Iterator i2 = verticeobraEspecial.iterator();
			PolygonPoint verticeobraNext = (PolygonPoint) i2.next();
			doubleLeste = stringValueCoord((
					verticeobraNext.getPointri().getGeometry().getCoordinate().x) );
			
			doubleNorte = stringValueCoord((
					verticeobraNext.getPointri().getGeometry().getCoordinate().y) );
	
			verticeFirstFirst = verticeobraNext.getPointri().getNome();
			verticeToFirstFirst = verticeobraNext.getPointri();
			FirstLesteFirst = doubleLeste;
			FirstNorteFirst = doubleNorte;
			if (verticeobraNext.getTipoLimite() == "LA3"){
				colunaX = " a ";
			}else if (verticeobraNext.getTipoLimite() == "LN1"){
				colunaX = " o ";
			}else{
				colunaX = " ";
			}
			texto = texto + verticeFirstFirst + colunaB + " N " + doubleNorte + "" + " e " +
			" E " +  doubleLeste + ";" + colunaC + colunaX + verticeobraNext.getDescritivo() + colunaH + verticeobraNext.getAzimuth() +
			" e " + verticeobraNext.getDistancia() + " m" + colunaI;
			while (i.hasNext()) {
				doubleLeste = stringValueCoord((
						verticeobraNext.getPointri().getGeometry().getCoordinate().x) );
				
				doubleNorte = stringValueCoord((
						verticeobraNext.getPointri().getGeometry().getCoordinate().y) );

				PolygonPoint verticeobra_ = (PolygonPoint) i.next();
				if (i2.hasNext()) {
					verticeobraNext = (PolygonPoint) i2.next();

					// ==============================================================================
					doubleLeste = stringValueCoord((
							verticeobraNext.getPointri().getGeometry().getCoordinate().x) );
					
					doubleNorte = stringValueCoord((
							verticeobraNext.getPointri().getGeometry().getCoordinate().y) );
					
					
					//NextPoint = new org.postgis.Point(doubleLeste, doubleNorte);
					verticeNext = verticeobraNext.getPointri().getNome();
					verticeTo = verticeobraNext.getPointri();
					
					if (verticeobraNext.getTipoLimite() == "LA3"){
						colunaX = " a ";
					}else if (verticeobraNext.getTipoLimite() == "LN1"){
						colunaX = " o ";
					}else{
						colunaX = " ";
					}
					texto = texto + verticeobraNext.getPointri().getNome() + colunaB + " N " + 
					 doubleNorte + " e E " + doubleLeste + ";" +  colunaC + colunaX + verticeobraNext.getDescritivo() +
					 colunaK + verticeobraNext.getAzimuth() +  colunaL + verticeobraNext.getDistancia() + " m"  + colunaI ;
				} else {
					
					NextPoint = FirstPointFirst;
					verticeNext = verticeFirstFirst;
					verticeTo = verticeToFirstFirst;
					

					
					
					texto = texto + " até o vértice " + verticeTo.getNome() + colunaM + obraCLS.getLayer().getApplicationEntity().getEpsg() +
							colunaN;
				}
			}
		}
		
		
		
		
		
		//=================***********************************************
		Double angle;
		
		Set descritivoNorte = new HashSet();;
		Set descritivoLeste = new HashSet();;
		Set descritivoSul = new HashSet();;
		Set descritivoOeste = new HashSet();;

		 String descritivoN = null; 
		 String descritivoL = null; 
		 String descritivoS = null; 
		 String descritivoO = null; 
		if (obraCLS.getVerticeObrasList().size() < 3) { 
			return null;
		} else {
		//*********************************************************************8
			for (PolygonPoint pp : obraCLS.getVerticeObrasList()){
				angle = pp.getAngle();
				//-----------
				if (pp.getDescritivo() != null) {
				if (angle > 45 && angle <= 135  ) {
					descritivoNorte.add(pp.getDescritivo());
				}else if (angle > 135 && angle <= 225  ) {
					descritivoLeste.add(pp.getDescritivo());
				}else if (angle > 225 && angle <= 315  ) {
					descritivoSul.add(pp.getDescritivo());
				}else {
					descritivoOeste.add(pp.getDescritivo());
				}
				}
				//-----------
			}	
			  descritivoN = (String.join(", ", descritivoNorte))  + "."; 
			  descritivoL = (String.join(", ", descritivoLeste))  + "."; 
			  descritivoS = (String.join(", ", descritivoSul))  + "."; 
			  descritivoO = (String.join(", ", descritivoOeste))  + "."; 
		}
		//=================***********************************************
		

		 
		try {
			Media returnMed = runProcessMemorial(finalFilesUrl, appatribs,  obraCLS, entityManager, 
					reltype,  modelo, saveFileProcessFilePath_, texto,	
					descritivoN, descritivoL,descritivoS,  descritivoO, 
					saveFileProcessFilePathUrl_, tenant);
			
				returnMed.setAbstractGeometry(obraCLS);
		entityManager.persist(returnMed);
		entityManager.flush();
	
			
		} catch (Exception e) {
		}




			//persist



		return "ok";
	}
	
	
	
	 public String stringValueCoord(Double doubleValue) {
		 String doubleL = String.valueOf(doubleValue);
		 doubleL = doubleL.replace(".", ",");
		 String[] parts = doubleL.split(",");
		 String x1 = parts[0];
		 String x2 = parts[1];
		 String mil3 = "";
		 String completa = "";
		 String centes = "000";
	if (x2.length() == 0 ) {
		centes = "000";
	}else if (x2.length() == 1 ) {
		centes = x2 + "00";
	}if (x2.length() == 2 ) {
		centes = x2 + "0";
	} else if (x2.length() == 3 ) {
			centes = x2;
	}
	if (x1.length() > 3) {
		String mil1 = x1.substring(x1.length() -3 , x1.length()  );
		String mil2 = x1.substring(x1.length() -6 , x1.length() -3);
		 completa = mil2 + "." + mil1 + "," + centes;
		if (x1.length() > 6) {
			 mil3 = x1.substring(0 , x1.length() -6);
			 completa = mil3 + "." + completa;
		}
	}
		return completa;
	}
	
	
		   	   
	   public Media runProcessMemorial(String finalFilesUrl, Collection<ApplicationAttributeEntity> appatribs,
			   Polygon obraCAP, 
			   EntityManager entityManager, String reltype, String modelo,
			   String saveFileProcessFilePath_, 
			   String texto,
			   String descritivoN, String descritivoL,String descritivoS,  
			   String descritivoO, String saveFileProcessFilePathUrl_, 
			   String tenant
			   ) {

		   
		   
			
			try {

				String reportFileUser;
				
				 
				String rootFilePath_ = save_FILE_PATH;

				//reportFileUser = save_FILE_PATH + "/birt/" + reltype + "_MD_" + modelo + ".j";
		
				reportFileUser = save_FILE_PATH + "/birt/MemorialUA4_Lote.jasper";
				
			
				
				// ----------------------
				// ------------------------------NAME
				
				String nomeArquivo = "";
				String nomeArquivoPath = "";
				String nomeArquivoPNG = "";

				Random random = new Random();
				int randomInt = random.nextInt(1000000000);
				nomeArquivoPNG = "QRCODE_" + String.valueOf(randomInt) + ".png";

			
				nomeArquivo = "PLANTA_DESCRITIVA" + "_A" + "_" + String.valueOf(randomInt) + ".pdf";
				

				nomeArquivoPath = saveFileProcessFilePath_ + "/" + nomeArquivo;

				String destinationFileQRCODE = saveFileProcessFilePath_ + "/" + nomeArquivoPNG;
						
				
					
				
				 HashMap<String,Object> param_map = new HashMap<String,Object>();
				
				 param_map.put("descritivoT", texto);
				 
				 param_map.put("descritivoN", descritivoN);
				 param_map.put("descritivoL", descritivoL);
				 param_map.put("descritivoS", descritivoS);
				 param_map.put("descritivoO", descritivoO);
				 
				
				param_map.put("obraId", obraCAP.getId());
				
				
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
						
						
						String  mediaFilePath  =  saveFileProcessFilePathUrl_  +  "/";

		// saveFileProcessFilePath_, 
		// saveFileProcessFilePathUrl_, 

		saveFileProcessFilePathUrl_ = save_FILE_PATH + saveFileProcessFilePathUrl_ ;
						 Connection con;
						  try {
					            Class.forName("org.postgresql.Driver");
					            con = DriverManager.getConnection(
					            		"jdbc:postgresql://" + pg_HOST + ":" + pg_PORT + "/" + 
					            				pg_DAT + "?currentSchema=" + tenant, 
					            				pg_USER, 
					            				pg_PASS
					            		);
					            JasperPrint jp;
					            jp = JasperFillManager.fillReport(reportFileUser, 
					            		param_map, con);
					            JasperExportManager.exportReportToPdfFile(jp, nomeArquivoPath);
					            con.close();
					        } catch (ClassNotFoundException | SQLException | JRException ex) {
					            ex.printStackTrace();
					        }
						

						  Media media = new Media("application/pdf", 
									nomeArquivo, mediaFilePath ); 
						
								
								

								return media;

			} catch (Exception e) {
				e.printStackTrace();
				return null;
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
	
}
