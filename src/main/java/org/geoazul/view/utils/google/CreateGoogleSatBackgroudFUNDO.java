package org.geoazul.view.utils.google;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;

import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import org.geoazul.model.basic.AbstractGeometry;
import org.keycloak.example.oauth.UserData;

import static modules.LoadInitParameter.*;

public class CreateGoogleSatBackgroudFUNDO {

	@Inject
	private UserData userData;

	

	public String runProcessImgage(String geoazulURL, String scale, String Center, String saveFileProcessFilePath_,
			AbstractGeometry obraSIGEF, EntityManager entityManager, String identity) {

		String destinationFileGoogleF = "";
		String newdestinationFileGoogleF = "";
		String nomarquivo = "";
		String newurldestinationFileGoogleF = "";

		try {

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

			Random random = new Random();
			int randomInt = random.nextInt(1000000000);

			String imageUrlGoogleF = "http://maps.googleapis.com/maps/api/staticmap?center=" + Center + "&zoom="
					+ newScale + "&scale=false&size=640x427&maptype=satellite&format=png&visual_refresh=true";

			nomarquivo = "backgroundUTMF_" + String.valueOf(randomInt) + ".png";
			destinationFileGoogleF = saveFileProcessFilePath_ + nomarquivo;
			newdestinationFileGoogleF = saveFileProcessFilePath_ + nomarquivo;



			newurldestinationFileGoogleF = geoazulURL + "/files/" + userData.getRealmEntity() + "/"
					+ obraSIGEF.getLayer().getApplicationEntity().getId() + "/" + obraSIGEF.getId() + "/" + nomarquivo;


			URL urlGoogleF = null;
			try {
				urlGoogleF = new URL(imageUrlGoogleF);
			} catch (MalformedURLException e1) {
			}
			InputStream isGoogleF = null;
			try {
				isGoogleF = urlGoogleF.openStream();
			} catch (IOException e1) {
			}
			OutputStream osGoogleF = null;
			try {
				osGoogleF = new FileOutputStream(destinationFileGoogleF);
			} catch (FileNotFoundException e1) {
			}


			byte[] bGoogleF = new byte[2048];
			int lengthGoogleF;

			try {
				while ((lengthGoogleF = isGoogleF.read(bGoogleF)) != -1) {
					osGoogleF.write(bGoogleF, 0, lengthGoogleF);
				}
			} catch (IOException e1) {
			}

			try {
				isGoogleF.close();
			} catch (IOException e1) {
			}
			try {
				osGoogleF.close();
			} catch (IOException e1) {
			}

		} catch (Exception e1) {
			e1.printStackTrace();
		}


		return "success";

	}

}