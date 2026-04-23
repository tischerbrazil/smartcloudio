package org.geoazul.view.beta;

import org.locationtech.jts.geom.Coordinate;


public class ProcessAzimuth {

	
public double Azimuth(Coordinate origem, Coordinate destino){
    	
	
	double e2 = 0.00669438002290;
	double a = 6378137;
	double seno_1segundo = 0.00000484813681108;
	

	double lat_media = ((origem.y/180.0*Math.PI) + destino.y/180.0*Math.PI) / 2;
	
	double seno_lat_media = Math.sin(lat_media);
	double cos_lat_media = Math.cos(lat_media);
	double pow_seno_20 = Math.pow(seno_lat_media, 2);
	double nm = a / (Math.pow(1 - (e2 * pow_seno_20), 0.5));
	double  delta_lat = (destino.y - origem.y) * 3600;
	double delta_lon = (destino.x - origem.x) * 3600;

	double mm = (a * (1 - e2)) / Math.pow(1 - (e2 * pow_seno_20), 1.5);

	double bm = 1 / (mm * seno_1segundo);

	double xw = delta_lon * cos_lat_media * nm * seno_1segundo;
	
	double yw = delta_lat * Math.cos( Math.toRadians(  (delta_lon / 7200))) * mm * seno_1segundo + 0.0000000001;

	double Fw= (1 / 12) * seno_lat_media * cos_lat_media * cos_lat_media * seno_1segundo * seno_1segundo;
	
	double gamma = (delta_lon * seno_lat_media * (1 / Math.cos((delta_lat/180.0*Math.PI) / 7200)) + (
    Fw * delta_lon * delta_lon * delta_lon));
	
	double sinal_x;
	double sinal_y;
	
	 if (xw < 0){
         sinal_x = -1;
	 }else if (xw == 0){
         sinal_x = 0;
	 }else{
         sinal_x = 1;
	 }
	 
     if (yw < 0){
         sinal_y = -1;
     }else if (yw == 0){
         sinal_y = 0;
     }else{
         sinal_y = 1;
     }

	double  atan =   Math.atan(xw / yw);
	double  degresatan =   Math.toDegrees( atan);
	double  gama7200 =   gamma / 7200;
	double  az =   degresatan - gama7200;
	
	double  azimute = 180 * (1 - (0.5 * sinal_x) - (0.5 * sinal_x * sinal_y)) + az;
	
	
		return azimute;
		
		

		
	    

    }


   


	
	
}
