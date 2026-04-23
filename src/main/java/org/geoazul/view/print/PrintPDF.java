package org.geoazul.view.print;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.time.LocalDate;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import static modules.LoadInitParameter.*;

import jakarta.transaction.Transactional;
import org.geoazul.model.app.ApplicationAttributeEntity;
import org.geoazul.model.app.ApplicationEntity;
import org.geoazul.model.basic.Polygon;
import org.geoazul.model.website.media.Media;
import org.geoazul.view.utils.ConvertCoordinate;
import org.geoazul.view.utils.ProcessMapaUrbano;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.io.ParseException;

@Named
@RequestScoped
public class PrintPDF {



	@Inject
	EntityManager entityManager;

	// ------------------------------------------------------- PRINT PROCESS IMOVEL
	public String convertToASCII2(String text) {
		return text.replaceAll("[ãâàáä]", "a").replaceAll("[êèéë]", "e").replaceAll("[îìíï]", "i")
				.replaceAll("[õôòóö]", "o").replaceAll("[ûúùü]", "u").replaceAll("[ÃÂÀÁÄ]", "A")
				.replaceAll("[ÊÈÉË]", "E").replaceAll("[ÎÌÍÏ]", "I").replaceAll("[ÕÔÒÓÖ]", "O")
				.replaceAll("[ÛÙÚÜ]", "U").replace('ç', 'c').replace('Ç', 'C').replace('ñ', 'n').replace('Ñ', 'N');
	}

	private String inputFile;

	public String getInputFile() {
		return this.inputFile;
	}

	public void setInputFile(String inputFile) {
		this.inputFile = inputFile;
	}

	@Transactional
	public void printRun33(String finalFilesUrl, Collection<ApplicationAttributeEntity> appatribs, String logoUrl, EntityManager session, ApplicationEntity gleba, Polygon obraCAP, String bBox,
			String bBoxUTM, String sistcord, String scale, String reltype, String modelo, String colorPopup,
			String paper, String background, String geoserverUrl, String geoazulPath, Boolean automatico,
			String saveFileProcessFilePath_, String saveFileProcessFilePathUrl_, String geoserverLayer, String tenant) throws IOException, ParseException {

		ConvertCoordinate convertCoordinate = null;
		String outputSRID;
		String utmPoint;
		String stringX;
		// convertCoordinate.runConvertCoordinate(utmPoint, inputSRID, outputSRID,
		// entityManager)

		// .runConvertCoordinate(utmPoint, "EPSG:" + obraCAP.getLayer().getEpsg(),,
		// outputSRID, entityManager););

		boolean secondTable = true;
		// if (getNumberVerticesObra(obraCAP, entityManager) > 20){
		// secondTable = false;
		// }

		String urlBBOX = null;
		String bBOX = null;
		String Leste = null;
		String Norte = null;
		String X1 = null;
		String X2 = null;
		String X3 = null;
		String X4 = null;

		double[] boundsRet;

		String urlBBOXUTM = null;
		String bBOXUTM = null;

		// boolean baseImovelDirStart = (new
		// File(saveFileProcessImovelFilePathStartBase_ )).mkdir();
		// boolean baseImovelAppDirStart = (new File(saveFileProcessAppPath_)).mkdir();

		// boolean baseObraDir = (new File(saveFileProcessFilePath_ )).mkdir();

		// // try{

		String CenterNewUTM = null;

		if (bBox.equals("")) {
			bBOXUTM = bBoxUTM;
		} else {

			String[] parts = bBox.split(",");
			String part1 = parts[0];
			String part2 = parts[1];
			String part3 = parts[2];
			String part4 = parts[3];

			double x1 = Double.valueOf(part1).doubleValue();
			double y1 = Double.valueOf(part2).doubleValue();
			double x2 = Double.valueOf(part3).doubleValue();
			double y2 = Double.valueOf(part4).doubleValue();

			double CenterNewUTM_X = x1 + ((x2 - x1) / 2);
			double CenterNewUTM_Y = y1 + ((y2 - y1) / 2);

			CenterNewUTM = "" + CenterNewUTM_Y + "," + CenterNewUTM_X;

			// converter to UTM
			GeometryFactory geometryFactory = new GeometryFactory();

			Coordinate coordenada1 = new Coordinate(x1, y1);
			Coordinate coordenada2 = new Coordinate(x2, y2);

			Geometry pointGeometry1 = geometryFactory.createPoint(coordenada1);
			Geometry pointGeometry2 = geometryFactory.createPoint(coordenada2);

			Coordinate targetGeometry1 = ConvertCoordinate.runConvertCoordinate(
					"POINT(" + pointGeometry1.getCoordinate().x + " " + pointGeometry1.getCoordinate().y + ")", obraCAP.getLayer().getApplicationEntity().getEpsg(),
					obraCAP.getLayer().getEpsg(), session);
			Coordinate targetGeometry2 = ConvertCoordinate.runConvertCoordinate(
					"POINT(" + pointGeometry2.getCoordinate().x + " " + pointGeometry2.getCoordinate().y + ")", obraCAP.getLayer().getApplicationEntity().getEpsg(),
					obraCAP.getLayer().getEpsg(), session);

			bBOXUTM = "" + targetGeometry1.x + "," + targetGeometry1.y + "," + targetGeometry2.x + ","
					+ targetGeometry2.y;

		}
		
		
		
		
	//	bBOXUTM = "711388.1272688023,8508535.064524189,711595.1075467397,8508669.821215518";
	//	CenterNewUTM = "13.483060466973335,-61.04620585673676";
		
		
		Integer varBoxWidth = null;
		Integer varBoxHeight = null;

		if (paper.equals("0")) {
			varBoxWidth = 3000;
			varBoxHeight = 2000;
		} else if (paper.equals("1")) {
			varBoxWidth = 2250;
			varBoxHeight = 1500;
		} else if (paper.equals("2")) {
			varBoxWidth = 1500; // 420 × 594
			varBoxHeight = 1000;
		} else if (paper.equals("3")) {
			varBoxWidth = 1000;
			varBoxHeight = 667;
		} else if (paper.equals("4")) {
			varBoxWidth = 660;
			varBoxHeight = 440;
		} else {
		}

		Integer sridProjeto = gleba.getEpsg();
		String keyMaping = sridProjeto.toString();

		boolean fundomap = false;
		if (background.equals("2")) {
			fundomap = true;
		}

		ProcessMapaUrbano sessionGeoMapUrbano = new ProcessMapaUrbano();
		Media returnStringUTM = null;

		
		if (sistcord.equals("1")) {
			returnStringUTM = sessionGeoMapUrbano.startMapEngineUrbano(
					 finalFilesUrl, appatribs,
					logoUrl, scale, reltype, modelo,
					"#" + colorPopup, 1, fundomap, automatico, paper, obraCAP, session, urlBBOX, bBOXUTM,
					saveFileProcessFilePath_, saveFileProcessFilePathUrl_, CenterNewUTM, varBoxWidth, varBoxHeight,
					secondTable, geoazulPath, varBoxWidth, varBoxHeight, geoserverLayer, tenant
					);

		} else if (sistcord.equals("2")) { // UTM
			returnStringUTM = sessionGeoMapUrbano.startMapEngineUrbano(
					 finalFilesUrl,  appatribs,
					logoUrl, scale, reltype, modelo,
					"#" + colorPopup, 1, fundomap, automatico, paper, obraCAP, session, urlBBOXUTM, bBOXUTM,
					saveFileProcessFilePath_, saveFileProcessFilePathUrl_, CenterNewUTM, varBoxWidth, varBoxHeight,
					secondTable, geoazulPath, varBoxWidth, varBoxHeight, geoserverLayer, tenant);

		}

		//if (!automatico) {
		
		
		try {

		}catch (Exception e){
			e.printStackTrace();
			
		}
		
		//obraCAP
		
		returnStringUTM.setAbstractGeometry(obraCAP);
		session.persist(returnStringUTM);
		session.flush();
	
		
		

			
		//}
	}
}